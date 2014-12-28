package com.feh.hiko.db;

import java.util.Vector;

/**
 * Created by theoz on 12/25/14.
 */
public class Location {

    private static long id = 0;

    Vector<Coord> lCoords = new Vector<Coord>();

    public Location()
    {
    }

    public void addCoord(Coord p_coord)
    {
        lCoords.add(p_coord);
        id++;
    }

    public long getId() {
        return id;
    }


    public Vector<Coord> getlCoords()
    {
        return lCoords;
    }

    public String toString()
    {
        return id + " " + lCoords.size();
    }


}
