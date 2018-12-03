package com.smu.cse7350;

public class Session
{
    int sessionName;
    int conflictArray[];
    int colour;
    int conflictCount;

    public Session()
    {
        this.colour = -1;
    }

    public Session(int conflictArray[], int sessionName)
    {
        this.sessionName = sessionName;
        this.colour = -1;
        this.conflictArray = conflictArray;
        StringBuilder output = new StringBuilder();
        output.append("[");
        for(int i = 0; i < conflictArray.length; i++)
        {
            output.append(conflictArray[i] + ", ");

        }
        output.append("]");
    }

    public String toString()
    {
        StringBuilder output = new StringBuilder();
        output.append("Session: " + this.sessionName + " Colour: " + colour + " [");
        for(int i = 0; i < conflictArray.length; i++)
        {
            output.append(conflictArray[i] + ", ");

        }
        output.append("]");
        return output.toString();
    }

    public int getColor()
    {
        return colour;
    }

    public void setColour(int color)
    {
        this.colour = color;
    }

    public int[] getConflictArray()
    {
        return conflictArray;
    }





}
