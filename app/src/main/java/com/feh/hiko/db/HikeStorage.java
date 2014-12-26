package com.feh.hiko.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by theoz on 12/23/14.
 */
public class HikeStorage extends SQLiteOpenHelper {

    public static final String HIKE_TABLE = "hikes";
    public static final String COLUMN_NAME = "HIKE_NAME";
    public static final String COLUMN_DISTANCE = "HIKE_DISTANCE";
    public static final String COLUMN_TIME = "HIKE_TIME";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LOCATION = "HIKE_LOCATION";

    private static final String HIKE_TABLE_CREATE =
            "CREATE TABLE " + HIKE_TABLE  +
             "( _id integer primary key autoincrement, " +
            "HIKE_NAME TEXT, " +
            "HIKE_DISTANCE  INTEGER, " +
            "HIKE_TIME INTEGER, " +
            "HIKE_LOCATION INTEGER);";

    private static final int DB_VERSION = 10;

    public HikeStorage(Context context) {
        super(context,"hikes.db",null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(HIKE_TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        Log.w(HikeStorage.class.getName(),
                "Upgrading database from version " + i + " to "
                        + i2 + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HIKE_TABLE);
        onCreate(sqLiteDatabase);

    }

}
