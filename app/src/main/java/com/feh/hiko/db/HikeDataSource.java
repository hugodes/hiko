package com.feh.hiko.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by theoz on 12/23/14.
 */
public class HikeDataSource {

    private HikeStorage dbHelper;
    private SQLiteDatabase db;
  //  private String[] allColumns = {dbHelper.getColumnName(),dbHelper.getColumnDistance(),dbHelper.getColumnTime()};
    private String[] allColumns = {HikeStorage.COLUMN_NAME,HikeStorage.COLUMN_DISTANCE,HikeStorage.COLUMN_TIME};

    public HikeDataSource(Context context){
        dbHelper = new HikeStorage(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close()
    {
        dbHelper.close();
    }

    public void createHike(Hike hikeToAdd)
    {
        ContentValues values = new ContentValues();
        values.put(HikeStorage.COLUMN_NAME,hikeToAdd.getHikeName());
        values.put(HikeStorage.COLUMN_DISTANCE,hikeToAdd.getTotalDistance());
        values.put(HikeStorage.COLUMN_TIME,hikeToAdd.getTotalTime());

      //  Log.w("test", values.toString() + HikeStorage.HIKE_TABLE);
        long iId = db.insert(HikeStorage.HIKE_TABLE, null, values);
    }

    public List<Hike> getAllHike(){

        List<Hike> hikes = new ArrayList<Hike>();
        Cursor cursor = db.query(dbHelper.getHikeTable(),allColumns,null,null,null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            Hike hike = cursorToHike(cursor);
            hikes.add(hike);
            cursor.moveToNext();
        }
        return hikes;
    }

    private Hike cursorToHike(Cursor cursor)
    {
        Hike hike = new Hike();
        hike.setHikeName(cursor.getString(0));
        hike.setTotalDistance(cursor.getInt(1));
        hike.setTotalTime(cursor.getInt(2));
        return hike;
    }
}
