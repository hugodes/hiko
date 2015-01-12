package com.feh.hiko.db;

/**
 * Created by theoz on 12/25/14.
 */
public class Coord {
    private float point1; //longitutde
    private float point2; //latitude

    private long hikeId;

    public long id;
    public Coord(float p_point1,float p_point2)
    {
        point1 = p_point1;
        point2 = p_point2;
    }

    public Coord(long id,long hikeId,float p_point1,float p_point2)
    {
        this.id = id;
        this.hikeId = hikeId;
        point1 = p_point1;
        point2 = p_point2;
    }

    public Coord(){

    }

    public void setId(long id){
        this.id = id;
    }

    public long getId(){
        return id;
    }
    public void setHikeId(long hikeId){
        this.hikeId = hikeId;
    }

    public long getHikeId(){
        return this.hikeId;
    }
    public float getPoint1() {
        return point1;
    }

    public float getPoint2() {
        return point2;
    }

    public void setPoint1(float point1) {
        this.point1 = point1;
    }

    public void setPoint2(float point2) {
        this.point2 = point2;
    }

    public String toString()
    {
        return id + " " + hikeId + " " + point1 + " " + point2;
    }
}
