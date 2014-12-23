package com.feh.hiko.db;

/**
 * Created by theoz on 12/23/14.
 */
public class Hike {

    private String hikeName;
    private int totalDistance;
    private int totalTime;

    public Hike(String p_HikeName,int p_totalDistance,int p_totalTime)
    {
        hikeName = p_HikeName;
        totalDistance = p_totalDistance;
        totalTime = p_totalTime;
    }

    public Hike(){}

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
        return hikeName + " " + totalDistance + " " + totalTime;
    }

}
