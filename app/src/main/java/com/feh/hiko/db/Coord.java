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


    public Coord(long id,long hikeId,float latitude,float longitude,String picture,String comment)
    {
        this.id = id;
        this.hikeId = hikeId;
        this.longitude = longitude;
        this.latitude = latitude;
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
    public float getLongitude() {
        return this.longitude;
    }

    public float getLatitude() {
        return this.latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
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
