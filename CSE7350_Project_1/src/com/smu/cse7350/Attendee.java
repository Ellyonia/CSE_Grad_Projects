package com.smu.cse7350;

import java.util.SortedSet;
import java.util.TreeSet;

public class Attendee {
    int sessionCount;
    Object[] mySessionsArray;
    SortedSet<Conflict> myConflicts;

    public Attendee() {
        new Attendee(20);
    }

    public Attendee(int sessionCount) {
        this.sessionCount = sessionCount;
        mySessionsArray = new Object[sessionCount];
        myConflicts = new TreeSet<Conflict>();
    }

    void populateSessions(int totalSessions, int distributionType) {
        switch (distributionType) {
            case 0:
                populateUniform(totalSessions);
                break;
            case 1:
                populateTiered(totalSessions);
                break;
            case 2:
                populateSkewed(totalSessions);
                break;
            case 3:

                populatePersonal(totalSessions);
                break;
        }

//
    }


    void populateUniform(int totalSessions){
        int[] sessions = new int[totalSessions];
        for (int i = 0; i <  totalSessions; i++)
            sessions[i] = i+1;
        for (int i = 0; i < totalSessions; i++)
        {
            int rand = (int) (Math.random() * (totalSessions - 1) + 1);
            int temp1 = sessions[i];
            int temp2 = sessions[rand];
            sessions[rand] = temp1;
            sessions[i] = temp2;
        }

        for (int i = 0; i < sessionCount; i++)
        {
            mySessionsArray[i] = sessions[i];
        }

    }

    void populateSkewed(int totalSessions){
        SortedSet<Integer> mySessions = new TreeSet<Integer>();

        for (int i = 0; i < sessionCount; i++) {
            int session1 = (int) (Math.random() * (totalSessions -1) + 1);
            int session2 = (int) (Math.random() * (totalSessions-1) + 1);
            int session = (session1 < session2) ? session1 : session2;
            if (mySessions.contains(session)) {
                i--;
            } else {
                mySessions.add(session);
            }
        }

        mySessionsArray = mySessions.toArray();

    }

    void populateTiered(int totalSessions){

        int[] sessions = new int[totalSessions];
        for (int i = 0; i <  totalSessions; i++)
            sessions[i] = i+1;

        for (int i = 0; i < totalSessions/10; i++)
        {
            int rand = (int) (Math.random() * (totalSessions/10-1) + 1);
            int temp1 = sessions[i];
            int temp2 = sessions[rand];
            sessions[rand] = temp1;
            sessions[i] = temp2;
        }

        for (int i = totalSessions/10; i < totalSessions; i++)
        {
            int rand = (int) (Math.random() * (totalSessions - totalSessions/10) + totalSessions/10);
            int temp1 = sessions[i];
            int temp2 = sessions[rand];
            sessions[rand] = temp1;
            sessions[i] = temp2;
        }

        int firstHalfCounter = 0;
        int secondHalfCounter = totalSessions/10;

        for (int i = 0; i < sessionCount; i++)
        {
            int coinFlip = (int) (Math.random()*2);
            if (coinFlip == 1 && firstHalfCounter < totalSessions/10)
            {
                mySessionsArray[i] = sessions[firstHalfCounter];
                firstHalfCounter++;

            }else
            {
                mySessionsArray[i] = sessions[secondHalfCounter];
                secondHalfCounter++;
            }

        }

    }

    void populatePersonal(int totalSessions){
        SortedSet<Integer> mySessions = new TreeSet<Integer>();

        for (int i = 0; i < sessionCount; i++) {
            int session1 = (int) (Math.random() * (totalSessions - 1) + 1);
            int session2 = (int) (Math.random() * (totalSessions - 1) + 1);
            int session3 = (int) (Math.random() * (totalSessions - 1) + 1);
            int session = (session1 + session2 + session3) / 3; //(session1 > session2) ? session1 : session2;
            if (mySessions.contains(session)) {
                i--;
            } else {
                mySessions.add(session);
            }

        }
        mySessionsArray = mySessions.toArray();

    }
}
