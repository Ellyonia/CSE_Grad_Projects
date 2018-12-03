package com.smu.cse7350;

import java.io.*;
import java.util.*;


public class Part2Main {
    public static void main(String[] args) {

        int N = -1;
        int S = -1;
        int K = -1;
        int M = -1;
        int T = -1;
        int P[] = null;
        int E[] = null;
        File p1Input = new File("output.txt");
        if(!p1Input.exists())
        {
            System.out.println("Please Run Part 1 First. (Main.java)");
            System.exit(1);
        }
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader("output.txt"));
            String line = reader.readLine();
            while (line != null)
            {
                System.out.println(line);
                char switchCase = line.charAt(0);
                if (line.length() > 1)
                {
                    switchCase = 'Z';
                }
                switch(switchCase) {
                    case 'N':
                        line = reader.readLine();
                        N = Integer.parseInt(line);
                        P = new int[N+1];
                        break;
                    case 'S':
                        line = reader.readLine();
                        S = Integer.parseInt(line);
                        break;
                    case 'K':
                        line = reader.readLine();
                        K = Integer.parseInt(line);
                        break;
                    case 'M':
                        line = reader.readLine();
                        M = Integer.parseInt(line);
                        E = new int[M];
                        break;
                    case 'T':
                        line = reader.readLine();
                        T = Integer.parseInt(line);
                        break;
                    case 'P':
                        int previousP;
                        for (int i = 0; i < N+1; i++)
                        {
                            line = reader.readLine();
                            P[i] = Integer.parseInt(line);
                            if(P[i] == 0 && i != 0)
                            {
                                P[i] = P[i-1];
                            }
                        }
                        break;
                    case 'E':
                        for (int i = 0; i < M; i++)
                        {
                            line = reader.readLine();
                            E[i] = Integer.parseInt(line);
                        }
                    default:
                        break;
                }
                line = reader.readLine();
            }
        } catch( Exception e)
        {
            System.out.println(e);
        }

        System.out.println("N: " + N);
        System.out.println("S: " + S);
        System.out.println("K: " + K);
        System.out.println("M: " + M);
        System.out.println("T: " + T);

        Session sessionMap[] = new Session[N+1];
        int eCounter = 0;


        for (int i =0; i <= N; i++)
        {
            int currentSessionStart = P[i];
            int nextSessionStart ;


            if (i == N) {
                nextSessionStart = E.length;
            }
            else
                nextSessionStart = P[i + 1];



            if (currentSessionStart != nextSessionStart)
            {
                int conflictSessions[] = new int[nextSessionStart - currentSessionStart];
                for (int j = 0; j < conflictSessions.length; j++)
                {
                    conflictSessions[j] = E[eCounter];
                    eCounter ++;
                }
                sessionMap[i] = new Session(conflictSessions);


            }
            else
                sessionMap[i] = null;


        }




        // This is where Vertex Colouring Happens

        //Random Colouring
        int[] sessionList = new int[sessionMap.length-1]; // Set the Randomizer to be the size of the SessionMap.
        for (int i = 0; i <  sessionList.length; i++)
            sessionList[i] = i+1;
        for (int i = 0; i < sessionList.length; i++)
        {
            int rand = (int) (Math.random() * (sessionList.length - 1) + 1);
            int temp1 = sessionList[i];
            int temp2 = sessionList[rand];
            sessionList[rand] = temp1;
            sessionList[i] = temp2;
        }

        for (int i = 0; i  < sessionList.length; i++)
        {
            int minColour = 0;
            int conflits[] = sessionMap[sessionList[i]].getConflictArray();
//            System.out.println(sessionList[i]);
            for(int conflict : conflits)
            {
                int conflictColor = sessionMap[conflict].getColor();
//                System.out.println(conflictColor);
                if (conflictColor > minColour)
                {
                    minColour = conflictColor;
                }
            }
            sessionMap[sessionList[i]].setColour(minColour+1);
        }





        //In Order Colouring

//        for (int i = 1; i  < sessionMap.length; i++)
//        {
////            System.out.println(i + " " + sessionMap.length);
//            int minColour = 0;
////            System.out.println(sessionMap[i].toString());
//            int conflits[] = sessionMap[i].getConflictArray();
//            for(int conflict : conflits)
//            {
////                System.out.println(conflict);
//                int conflictColor = sessionMap[conflict].getColor();
//                if (conflictColor > minColour)
//                {
//                    minColour = conflictColor;
//                }
//            }
//            sessionMap[i].setColour(minColour+1);
//        }


        for(Session session : sessionMap)
        {
            if (session != null)
                System.out.println(session.toString());
        }




    }
}
