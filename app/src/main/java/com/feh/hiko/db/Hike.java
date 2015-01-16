package com.feh.hiko.db;

/**
 * Created by theoz on 12/23/14.
 */
public class Hike {

    private long id;
    private String hikeName;
    private float totalDistance;
    private float totalTime;


    public Hike(long p_id,String p_HikeName,float p_totalDistance,float p_totalTime)
    {
        id = p_id;
        hikeName = p_HikeName;
        totalDistance = p_totalDistance;
        totalTime = p_totalTime;
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



    public String getHikeName(){
        return hikeName;
    }

    public float getTotalDistance(){
        return totalDistance;
    }

    public float getTotalTime(){
        return totalTime;
    }

    public void setHikeName(String pHikeName){
        hikeName = pHikeName;

    }

    public void setTotalDistance(float pTotalDistance){
        totalDistance = pTotalDistance;
    }

    public void setTotalTime(float pTotalTime){
        totalTime = pTotalTime;
    }

    public String toString()
    {
        return id + " " + hikeName + " " + totalDistance + " " + totalTime + " " ;
    }

}
