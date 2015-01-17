package com.feh.hiko.db;

/**
 * Created by theoz on 1/17/15.
 */
public class Picture {
    long coordId;
    long hikeId;
    String picture;

    public Picture(long coordId,long hikeId,String picture){
        this.coordId = coordId;
        this.hikeId = hikeId;
        this.picture = picture;
    }

    public String getPicture(){
        return picture;
    }

    public void setPicture(String picture){
        this.picture = picture;
    }

    public long getCoordId(){
        return coordId;
    }

    public long getHikeId(){
        return hikeId;
    }
}
