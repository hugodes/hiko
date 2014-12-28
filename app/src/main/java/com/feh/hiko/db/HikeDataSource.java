package com.feh.hiko.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

    private String[] allColumnsLocation = {LocationStorage.COLUMN_HIKEID,LocationStorage.COLUMN_POINT1,LocationStorage.COLUMN_POINT2};
    private String[] allColumns = {HikeStorage.COLUMN_ID,HikeStorage.COLUMN_NAME,HikeStorage.COLUMN_DISTANCE,
            HikeStorage.COLUMN_TIME,HikeStorage.COLUMN_LOCATION};

    public HikeDataSource(Context context){
        dbHelper = new HikeStorage(context);
        dbStorageHelper = new LocationStorage(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        dbLocations = dbStorageHelper.getWritableDatabase();
        Log.w("succes","HERE");
    }

    public void close()
    {

        dbHelper.close();
        dbStorageHelper.close();
    }

    public void createHike(Hike hikeToAdd)
    {
        //On créer les locations
        ContentValues valuesLocation = new ContentValues();


        Vector<Coord> lCoords = hikeToAdd.getLocations().getlCoords();

        for(int i=0;i<lCoords.size();i++){
            valuesLocation.put(LocationStorage.COLUMN_HIKEID,hikeToAdd.getId());
            valuesLocation.put(LocationStorage.COLUMN_POINT1,lCoords.get(i).getPoint1());
            valuesLocation.put(LocationStorage.COLUMN_POINT2,lCoords.get(i).getPoint2());
            long idLoc = dbLocations.insert(LocationStorage.LOCATION_TABLE,null,valuesLocation);
            Log.w("hikeId",Long.toString(hikeToAdd.getId()));
        }

        ContentValues values = new ContentValues();
        values.put(HikeStorage.COLUMN_NAME,hikeToAdd.getHikeName());
        values.put(HikeStorage.COLUMN_DISTANCE,hikeToAdd.getTotalDistance());
        values.put(HikeStorage.COLUMN_TIME,hikeToAdd.getTotalTime());
        values.put(HikeStorage.COLUMN_LOCATION,hikeToAdd.getLocations().getId());

      //  Log.w("test", values.toString() + HikeStorage.HIKE_TABLE);
        long iId = db.insert(HikeStorage.HIKE_TABLE, null, values);
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

    public Vector<Coord> getLocationForId(long hikeId)
    {
        Location locations = new Location();

        Vector<Coord> vCoord = new Vector<Coord>();

        Cursor cursor = dbLocations.query(LocationStorage.LOCATION_TABLE,allColumnsLocation,LocationStorage.COLUMN_HIKEID + "=" + Long.toString(hikeId),
                 null, null, null, null);
   //     Cursor cursor = dbLocations.query(LocationStorage.LOCATION_TABLE,allColumnsLocation,null,null,null,null,null);

        Log.w("CURSOR COUNT",Integer.toString(cursor.getCount()));
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            Coord nCoord = new Coord(cursor.getFloat(1),cursor.getFloat(2));
            vCoord.add(nCoord);
            String test = Float.toString(cursor.getFloat(1));
            Log.w("Cursor",test);
            cursor.moveToNext();
        }
        return vCoord;
    }

  /*  private Location cursorToLocation(Cursor cursor)
    {
        Location location = new Location();
        location.set
    }*/

    private Hike cursorToHike(Cursor cursor)
    {
        Hike hike = new Hike();
        hike.setId(cursor.getLong(0));
        hike.setHikeName(cursor.getString(1));
        hike.setTotalDistance(cursor.getInt(2));
        hike.setTotalTime(cursor.getInt(3));
        //hike.(cursor.getLong(4));
        return hike;
    }
}
