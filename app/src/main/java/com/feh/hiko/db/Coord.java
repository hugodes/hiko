package com.feh.hiko.db;

/**
 * Created by theoz on 12/25/14.
 */
public class Coord {
    private float point1;
    private float point2;

    public Coord(float p_point1,float p_point2)
    {
        point1 = p_point1;
        point2 = p_point2;
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
        return point1 + " " + point2;
    }
}
