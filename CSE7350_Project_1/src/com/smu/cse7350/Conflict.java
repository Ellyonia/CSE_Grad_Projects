package com.smu.cse7350;

import java.util.Objects;

public class Conflict implements Comparable<Conflict>{
    int conflict1;
    int conflict2;

    public Conflict(){
        new Conflict( 1, 2);
    }

    public Conflict(int conflictOne, int conflictTwo)
    {
        conflict1 = conflictOne;
        conflict2 = conflictTwo;
    }

    @Override
    public boolean equals(Object other)
    {
        if (null == other) return false;

        if (! (other instanceof Conflict)) return false;

        if (conflict1 != ((Conflict) other).conflict1) return false;

        if (conflict2 != ((Conflict) other).conflict2) return false;
        return true;
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(conflict1, conflict2);
    }


    @Override
    public int compareTo(Conflict other)
    {
        if (this.conflict1 < other.conflict1)
        {
            return this.conflict1 - other.conflict1;
        }else if (conflict1 == other.conflict1)
        {
            return 0;
//            if (this.conflict2 < other.conflict2)
//            {
//                return this.conflict2 - other.conflict2;
//            }else if (conflict2 > other.conflict2)
//            {
//                return this.conflict2 - other.conflict2;
//            }else if (conflict2 == other.conflict2)
//            {
//                return 0;
//            }
        } else if (conflict1 > other.conflict1)
        {
            return this.conflict1 - other.conflict1;
        }
        return -3;
    }
    
    @Override
    public String toString()
    {
        final String s = "[" + conflict1 + ", " + conflict2 + "]";
        return s;
    }


}
