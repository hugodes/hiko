package com.feh.hiko.db;

import java.util.Vector;

/**
 * Created by theoz on 12/25/14.
 */
public class Location {



    private  long hikeId = 0;

    Vector<Coord> lCoords = new Vector<Coord>();

    public Location()
    {
    }

    public void addCoord(Coord p_coord)
    {
        lCoords.add(p_coord);
    }

    public long getHikeId() {
        return hikeId;
    }

    public void setHikeId(long hikeId){
        this.hikeId = hikeId;
    }


    public Vector<Coord> getlCoords()
    {
        return lCoords;
    }

    public String toString()
    {
         return String.valueOf(lCoords.size());
    }


}
