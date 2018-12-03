package com.smu.cse7350;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.File;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Please Enter the Number of Sessions, the Number of Attendees, How many Sessions everyone is"
                    + " attending, and How they are distributed%n");
            System.exit(1);
        }
        if ((Integer.parseInt(args[0]) > 10000 || Integer.parseInt(args[0]) < 0)) {
            System.out.println("Please set the number of Sessions to a  positive integer less than 10,000.");
            System.exit(1);
        }

        if ((Integer.parseInt(args[1]) > 100000 || Integer.parseInt(args[1]) < 0)) {
            System.out.println("Please set the number of attendees to a  positive integer less than 100,000.");
            System.exit(1);
        }
        int distribution;
        switch (args[3]) {
            case "UNIFORM":
                if (Integer.parseInt(args[2]) > Integer.parseInt(args[0])) {
                    System.out.println("Please set the nnumber of Sessions per attnendee to less than the number of Total Sessions");
                    System.exit(1);
                }
                distribution = 0;
                break;
            case "TIERED":
                if (Integer.parseInt(args[2]) > Integer.parseInt(args[0])) {
                    System.out.println("Please set the nnumber of Sessions per attnendee to less than the number of Total Sessions");
                    System.exit(1);
                }
                distribution = 1;
                break;
            case "SKEWED":
                if (Integer.parseInt(args[2]) > (Integer.parseInt(args[0]) / 10)) {
                    System.out.println("Please set the nnumber of Sessions per attnendee to less than the number of Total Sessions divided by 10");
                    System.exit(1);
                }
                distribution = 2;
                break;
            default:
                if (Integer.parseInt(args[2]) > (Integer.parseInt(args[0]) / 10)) {
                    System.out.println("Please set the nnumber of Sessions per attnendee to less than the number of Total Sessions divided by 10");
                    System.exit(1);
                }
                distribution = 3;
                break;
        }

        int N = Integer.parseInt(args[0]);
        int S = Integer.parseInt(args[1]);
        int K = Integer.parseInt(args[2]);

        System.out.println(N + " Sessions, " + S + " Attendees, " + K + " sessions per attendee, " + distribution);

        long start = System.nanoTime();

        Attendee[] conferenceAttendees = new Attendee[S];
        for (int i = 0; i < S; i++) {
            Attendee temp = new Attendee(K);
            temp.populateSessions(N, distribution);

            conferenceAttendees[i] = temp;
        }
        long stop = System.nanoTime();
        long generatorTime = stop - start;
        System.out.println("Generated the Attendees: " + generatorTime/1000000000.0 + " Seconds.");

        System.out.println("Finished Generating the Attendees");
        try {
            PrintWriter histogramOutput = new PrintWriter(new File("histogram_" + args[3] + ".csv"));
            StringBuilder histogramString = new StringBuilder();
            int counter = 0;
            for (Attendee person : conferenceAttendees) {
                if (counter < 100) {
                    for (Object session : person.mySessionsArray) {
                        histogramString.append(session.toString());
                        histogramString.append(",");
                    }
                    histogramString.append('\n');
                    counter++;
                }
            }
            histogramOutput.write(histogramString.toString());
            histogramOutput.close();
        } catch (Exception e) {

        }
        System.out.println("Created the Histogram CSV file");

        int attendeeSessionCount = K;
        //SortedSet<Conflict> SessionTree = new TreeSet<>();
        Set<Conflict> SessionTree = new HashSet<>();
        start = System.nanoTime();
//
        int T = 0;
        int counter = 0;

        int sessionMatrix[][] = new int[N+1][N+1];
        for (Object obj : conferenceAttendees) {
//            if (counter % 50 == 0) {
//                System.out.println(counter);
//            }
            Attendee attendee = (Attendee) obj;
            for (int index = 0; index < attendeeSessionCount - 1; index++)
            {
                int session1 = (int) attendee.mySessionsArray[index];
                for (int index2 = index + 1; index2 < attendeeSessionCount; index2++)
                {
                    int session2 = (int) attendee.mySessionsArray[index2];
                    sessionMatrix[session1][session2] = 1;
                    sessionMatrix[session2][session1] = 1;
                    T = T + 2;


                }
            }
            counter++;
        }
        System.out.println("N^2 Matrix filled, reducing to M size now.");
        DoubleLinkList<Conflict> reducedMatrix = new DoubleLinkList<>();
        for (int i = 0; i < sessionMatrix.length; i++)
        {
            for (int j = 0; j< sessionMatrix[i].length; j++)
            {
                if (sessionMatrix[i][j] == 1)
                {
                    Conflict temp = new Conflict(i,j);
                    reducedMatrix.addLast(temp);
                }

            }
//            if (i%100 == 0)
//            {
//                System.out.println(i);
//            }
        }

        System.out.println("Reduced Matrix Length: " + reducedMatrix.length);

        stop = System.nanoTime();

        long timeToPopulateAndReduceNsqr = stop - start;
        start = System.nanoTime();

        counter = 0;
        for (Object obj : conferenceAttendees) {
//            if (counter % 50 == 0) {
//                System.out.println(counter);
//            }
            Attendee attendee = (Attendee) obj;
            for (int index = 0; index < attendeeSessionCount - 1; index++)
            {
                int session1 = (int) attendee.mySessionsArray[index];
                for (int index2 = index + 1; index2 < attendeeSessionCount; index2++)
                {
                    int session2 = (int) attendee.mySessionsArray[index2];
                    Conflict temp = new Conflict(session1, session2);
                    Conflict temp2 = new Conflict(session2, session1);

                    SessionTree.add(temp);
                    SessionTree.add(temp2);
                }
            }
            counter++;
        }
        stop = System.nanoTime();

        long timeToPopulateM = stop - start;

        start = System.nanoTime();

        int M = SessionTree.size();
        System.out.println("T: " + T);
        System.out.println("M: " + M);
        int P[] = new int[N+1];
        int E[] = new int[M];
        int previousP = -1;
        int pCounter = 1;
//
        int whileCounter = 0;
        DoubleLinkList.Node head = reducedMatrix.head;
        while (head.next != null)
        {
            int p = ((Conflict) head.data).conflict1;
            if (p> previousP)
            {
                P[pCounter] = whileCounter;
                previousP = p;
                pCounter ++;
            }
            E[whileCounter] = ((Conflict) head.data).conflict2;
            whileCounter ++;
            head = head.next;
        }
//
        stop = System.nanoTime();

        long timeForEPFromNsqr = stop - start;

//
        System.out.println("Time to Populate and Reduce N^2: " + (timeToPopulateAndReduceNsqr/1000000000.0) + " Seconds. \nTime to Populate and reduce M:   " + (timeToPopulateM/1000000000.0)+" Seconds.");





        Object[] sessionArray = SessionTree.toArray();
        int P2[] = new int[N+1];
        int E2[] = new int[M];
        previousP = -1;
        pCounter = 1;
//        System.out.println("SessionArray Length: " + sessionArray.length);
//        for (Object a : sessionArray)
//        {
//            System.out.print(a + " ");
//        }
//        System.out.println("");
//        head = reducedMatrix.head;
//        while (head.next != null)
//        {
//            System.out.print(head.data + " ");
//            head = head.next;
//        }
//        System.out.println("");


        start = System.nanoTime();

        for (int i = 0; i < sessionArray.length; i++)
        {
            int p =((Conflict) sessionArray[i]).conflict1;
            if (p > previousP)
            {
                P2[pCounter] = i;
                previousP = p;
                pCounter ++;
            }
            E2[i] = ((Conflict) sessionArray[i]).conflict2;

        }

        stop = System.nanoTime();

        long timeForEPFromM = stop - start;


//        for (int a : P)
//        {
//            System.out.print(a + " ");
//        }
//        System.out.println("");
//        for (int a : E)
//        {
//            System.out.print(a + " ");
//        }
//        System.out.println("");
//        for (int a : P2)
//        {
//            System.out.print(a + " ");
//        }
//        System.out.println("");
//        for (int a : E2)
//        {
//            System.out.print(a + " ");
//        }
//        System.out.println("");

        System.out.println("Time to Generate E + P from N^2: " + (timeForEPFromNsqr/1000000000.0) + " Seconds.\nTime to Generate E + P from M:   " + (timeForEPFromM/1000000000.0) + " Seconds");
        File output = new File("output.txt");
        try {
            PrintWriter pw = new PrintWriter(output);
            StringBuilder outputLine = new StringBuilder();
            outputLine.append("N\n");
            outputLine.append(N + "\n");
            outputLine.append("S\n");
            outputLine.append(S + "\n");
            outputLine.append("K\n");
            outputLine.append(K + "\n");
            outputLine.append("M\n");
            outputLine.append(M + "\n");
            outputLine.append("T\n");
            outputLine.append(T + "\n");
            outputLine.append("DIST\n");
            outputLine.append(args[3] + "\n");
            outputLine.append("P\n");
            for (int a : P)
            {
                outputLine.append(a + "\n");
            }
            outputLine.append("E\n");
            for (int a : E)
            {
                outputLine.append(a + "\n");
            }

            pw.write(outputLine.toString());

            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
