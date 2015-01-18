package com.feh.hiko.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by theoz on 12/25/14.
 */
public class LocationStorage extends SQLiteOpenHelper
{
    public static final String LOCATION_TABLE = "locations";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_HIKEID = "HIKE_ID";
    public static final String COLUMN_LONGITUDE = "LONGITUDE";
    public static final String COLUMN_LATITUDE = "LATITUDE";
    public static final String COLUMN_PICTURE = "PICTURE";
    public static final String COLUMN_COMMENT = "COMMENT";

    private static final int DB_VERSION = 49;

    private static final String LOCATION_TABLE_CREATE =
            "CREATE TABLE " + LOCATION_TABLE  +
                    "(_id integer, " +
                    "HIKE_ID INTEGER, " +
                    "LONGITUDE REAL, " +
                    "LATITUDE     REAL, " +
                    "PICTURE TEXT, " +
                    "COMMENT TEXT);";

    public LocationStorage(Context context){
        super(context,"locations.db",null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(LOCATION_TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        Log.w(HikeStorage.class.getName(),
                "Upgrading database from version " + i + " to "
                        + i2 + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LOCATION_TABLE);
        onCreate(sqLiteDatabase);

    }

}
