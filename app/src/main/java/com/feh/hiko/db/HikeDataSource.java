package com.feh.hiko.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by theoz on 12/23/14.
 */
public class HikeDataSource {


    private HikeStorage dbHelper;
    private LocationStorage dbStorageHelper;

    private SQLiteDatabase dbLocations;
    private SQLiteDatabase db;

    private String[] allColumnsLocation = {LocationStorage.COLUMN_ID,LocationStorage.COLUMN_HIKEID,LocationStorage.COLUMN_POINT1,LocationStorage.COLUMN_POINT2,LocationStorage.COLUMN_PICTURE,LocationStorage.COLUMN_COMMENT};
    private String[] allColumns = {HikeStorage.COLUMN_ID,HikeStorage.COLUMN_NAME,HikeStorage.COLUMN_DISTANCE,
            HikeStorage.COLUMN_TIME};

    public HikeDataSource(Context context){
        dbHelper = new HikeStorage(context);
        dbStorageHelper = new LocationStorage(context);

    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        dbLocations = dbStorageHelper.getWritableDatabase();

    }



    public void close()
    {

        dbHelper.close();
        dbStorageHelper.close();
    }


    public void addHikePhoneDb(Hike hikeToAdd){
        //On créer les locations
        ContentValues values = new ContentValues();
        values.put(HikeStorage.COLUMN_ID,hikeToAdd.getId());
        values.put(HikeStorage.COLUMN_NAME,hikeToAdd.getHikeName());
        values.put(HikeStorage.COLUMN_DISTANCE,hikeToAdd.getTotalDistance());
        values.put(HikeStorage.COLUMN_TIME,hikeToAdd.getTotalTime());
        long iId = db.insert(HikeStorage.HIKE_TABLE, null, values);


    }

    public void addLocationPhoneDb(Coord locationToAdd){
        ContentValues valuesLocation = new ContentValues();
        valuesLocation.put(LocationStorage.COLUMN_ID,locationToAdd.getId());
        valuesLocation.put(LocationStorage.COLUMN_HIKEID,locationToAdd.getHikeId());
        valuesLocation.put(LocationStorage.COLUMN_POINT1,locationToAdd.getPoint1());
        valuesLocation.put(LocationStorage.COLUMN_POINT2,locationToAdd.getPoint2());
        valuesLocation.put(LocationStorage.COLUMN_PICTURE,locationToAdd.getPicture());
        valuesLocation.put(LocationStorage.COLUMN_COMMENT,locationToAdd.getComment());
        long idLoc = dbLocations.insert(LocationStorage.LOCATION_TABLE,null,valuesLocation);
    }



    public int getNbLocations(){

        Cursor cursor = dbLocations.query(LocationStorage.LOCATION_TABLE,allColumnsLocation,null,null,null,null,null);
        return cursor.getCount();
    }
    public int getNbHike()
    {
        int cpt = 0;
        Cursor cursor = db.query(HikeStorage.HIKE_TABLE,allColumns,null,null,null,null,null);
        cpt = cursor.getCount();
        return cpt;
    }

    public boolean isLocationsExist(long id){
        Cursor cursor = dbLocations.query(LocationStorage.LOCATION_TABLE,allColumnsLocation,LocationStorage.COLUMN_ID + "=" + id,
                null, null, null, null);
        if (cursor.getCount()> 0) {
            return true;
        }
        else
            return false;

    }

    public boolean isHikeExist(long id){
        Cursor cursor = db.query(HikeStorage.HIKE_TABLE,allColumns,HikeStorage.COLUMN_ID + "=" + id,
                null, null, null, null);
        if (cursor.getCount() == 0) {
            System.out.println("HERE ");
            return false;
        }
        else {
            return true;
        }

    }
    public List<Hike> getAllHike(){

        List<Hike> hikes = new ArrayList<Hike>();
        Cursor cursor = db.query(HikeStorage.HIKE_TABLE,allColumns,null,null,null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            Hike hike = cursorToHike(cursor);
            hikes.add(hike);
            cursor.moveToNext();
        }
        return hikes;
    }

    public Coord getSingleCoordForId(long coordId){

        Cursor cursor = dbLocations.query(LocationStorage.LOCATION_TABLE,allColumnsLocation,LocationStorage.COLUMN_ID + "=" + coordId, null, null, null, null);
        cursor.moveToFirst();
        return new Coord(cursor.getLong(0),cursor.getLong(1),cursor.getFloat(2),cursor.getFloat(3),cursor.getString(4),cursor.getString(5));
    }

    public Vector<Coord> getLocationForId(long hikeId)
    {


        Vector<Coord> vCoord = new Vector<Coord>();

        Cursor cursor = dbLocations.query(LocationStorage.LOCATION_TABLE,allColumnsLocation,LocationStorage.COLUMN_HIKEID + "=" + Long.toString(hikeId),
                 null, null, null, null);
   //     Cursor cursor = dbLocations.query(LocationStorage.LOCATION_TABLE,allColumnsLocation,null,null,null,null,null);

        Log.w("CURSOR COUNT",Integer.toString(cursor.getCount()));
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            Coord nCoord = new Coord(cursor.getLong(0),cursor.getLong(1),cursor.getFloat(2),cursor.getFloat(3),cursor.getString(4),cursor.getString(5));
            vCoord.add(nCoord);
            cursor.moveToNext();
        }
        return vCoord;
    }


    private Hike cursorToHike(Cursor cursor)
    {
        Hike hike = new Hike();
        hike.setId(cursor.getLong(0));
        hike.setHikeName(cursor.getString(1));
        hike.setTotalDistance(cursor.getFloat(2));
        hike.setTotalTime(cursor.getFloat(3));
        //hike.(cursor.getLong(4));
        return hike;
    }
}
