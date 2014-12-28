package com.feh.hiko.db;

/**
 * Created by theoz on 12/23/14.
 */
public class Hike {

    private long id;
    private String hikeName;
    private int totalDistance;
    private int totalTime;

    private Location locations;

    public Hike(long p_id,String p_HikeName,int p_totalDistance,int p_totalTime,Location locationsId)
    {
        id = p_id;
        hikeName = p_HikeName;
        totalDistance = p_totalDistance;
        totalTime = p_totalTime;
        this.locations = locationsId;
    }

    public Hike(){}

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }



    public Location getLocations()
    {
        return locations;
    }

    public void setLocations(Location locations)
    {
        this.locations = locations;
    }
    public String getHikeName(){
        return hikeName;
    }

    public int getTotalDistance(){
        return totalDistance;
    }

    public int getTotalTime(){
        return totalTime;
    }

    public void setHikeName(String pHikeName){
        hikeName = pHikeName;

    }

    public void setTotalDistance(int pTotalDistance){
        totalDistance = pTotalDistance;
    }

    public void setTotalTime(int pTotalTime){
        totalTime = pTotalTime;
    }

    public String toString()
    {
        return id + " " + hikeName + " " + totalDistance + " " + totalTime + " " ;// locations.getlCoords().size();
    }

}
