package com.smu.cse7350;

import java.io.*;
import java.util.*;


public class Part2Main {
    static Session sessionMapRO[];
    static Session sessionMapIO[];
    static Session sessionMapSLV[];
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

        sessionMapRO = new Session[N+1];
        sessionMapIO = new Session[N+1];
        sessionMapSLV = new Session[N+1];
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
                sessionMapRO[i] = new Session(conflictSessions, i);
                sessionMapIO[i] = new Session(conflictSessions, i);
                sessionMapSLV[i] = new Session(conflictSessions, i);

            }
            else {
                sessionMapRO[i] = null;
                sessionMapIO[i] = null;
                sessionMapSLV[i] = null;
            }



        }




        // This is where Vertex Colouring Happens
        long start = System.nanoTime();
        randomOrderColour();
        long finishRO = System.nanoTime();

        inOrderColor();
        long finishIO = System.nanoTime();
        smallestLastVertex();
        long finishSLV = System.nanoTime();

//      Final Printing of the Sessions, their colour, and what they conflict with.
        int sessionIOColorMax = -1;
        int sessionROColorMax = -1;
        for(Session session : sessionMapIO)
        {
            if (session != null && session.getColor() > sessionIOColorMax)
                sessionIOColorMax = session.getColor();
        }
        for(Session session : sessionMapRO)
        {
            if (session != null && session.getColor() > sessionROColorMax) {
                sessionROColorMax = session.getColor();
            }
        }
//        for(Session session : sessionMapRO)
//        {
//            if (session != null)
//                System.out.println(session.toString());
//        }

        System.out.println("In Order Color Max: " + sessionIOColorMax);
        System.out.println("Random Order Colour Max: " + sessionROColorMax);
        System.out.println("Time for In Order Colouring:             " + (finishIO - finishRO)/1000000000.0 + " Seconds.");
        System.out.println("Time for Random Order Colouring:         " + (finishRO - start)/1000000000.0 + " Seconds.");
        System.out.println("Time for Smallest Last Vertex Colouring: " + (finishSLV - finishIO)/1000000000.0 + " Seconds.");


    }


    static void inOrderColor()
    {
        for (int i = 1; i  < sessionMapIO.length; i++)
        {
//            System.out.println(i + " " + sessionMap.length);
            int minColour = 0;
//            System.out.println(sessionMap[i].toString());
            if (sessionMapIO[i] != null) {
                int conflits[] = sessionMapIO[i].getConflictArray();
                for (int conflict : conflits) {
                    int conflictColor = sessionMapIO[conflict].getColor();
                    if (conflictColor > minColour) {
                        minColour = conflictColor;
                    }
                }
                sessionMapIO[i].setColour(minColour + 1);
            }
        }
    }

    static void randomOrderColour()
    {
        int[] sessionList = new int[sessionMapRO.length-1]; // Set the Randomizer to be the size of the SessionMap.
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
            if (sessionMapRO[sessionList[i]] != null) {
                int conflits[] = sessionMapRO[sessionList[i]].getConflictArray();
                for (int conflict : conflits) {
                    int conflictColor = sessionMapRO[conflict].getColor();
                    if (conflictColor > minColour) {
                        minColour = conflictColor;
                    }
                }
                sessionMapRO[sessionList[i]].setColour(minColour + 1);
            }
        }
    }

    static void smallestLastVertex()
    {
//        int[][] fa

    }
}
