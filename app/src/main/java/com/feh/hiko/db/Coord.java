package com.feh.hiko.db;

/**
 * Created by theoz on 12/25/14.
 */
public class Coord {
    private float longitude; //longitutde
    private float latitude; //latitude

    private long hikeId;

    public long id;
    public String picture;

    public String comment;


    public Coord(long id,long hikeId,float p_point1,float p_point2,String picture,String comment)
    {
        this.id = id;
        this.hikeId = hikeId;
        this.longitude = p_point1;
        this.latitude = p_point2;
        this.picture = picture;
        this.comment = comment;
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
        return this.longitude;
    }

    public float getPoint2() {
        return this.latitude;
    }

    public void setPoint1(float point1) {
        this.longitude = point1;
    }

    public void setPoint2(float point2) {
        this.latitude = point2;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    public String getComment(){
        return comment;
    }

    public void setPicture(String picture){
        this.picture = picture;
    }

    public String getPicture(){
        return this.picture;
    }

    public String toString()
    {
        return id + " " + hikeId + " " + longitude + " " + latitude;
    }
}
