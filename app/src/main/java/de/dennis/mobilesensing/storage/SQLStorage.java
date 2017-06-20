package de.dennis.mobilesensing.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dennis on 28.02.2017.
 */
public class SQLStorage extends SQLiteOpenHelper {

    public static final String TABLE_LOCATION_HISTORY = "locHistory";
    public static final String TABLE_ACTIVITY_HISTORY = "actHistory";
    public static final String TABLE_CALL_HISTORY = "callHistory";
    public static final String TABLE_DEVICE_POSITION_HISTORY = "devPosHistory";
    public static final String TABLE_RUNNING_APPLICATION_HISTORY ="appHistory";
    public static final String TABLE_NETWORK_HISTORY = "netHistory";
    public static final String TABLE_TRACK_HISTORY = "trackHistory";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LNG = "lng";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_PROBABILITY ="probability";
    public static final String COLUMN_VEL = "velocity";
    public static final String COLUMN_ENDTIMESTAMP = "endtimestamp";
    public static final String COLUMN_KILOMETER = "kilometer";
    public static final String COLUMN_ALT = "altitude";
    public static final String COLUMN_ACC = "accuracy";


    private static final String DATABASE_NAME = "MobileSensing.db";
    private static final int DATABASE_VERSION = 1;


    private static final String DATABASE_CREATE_LOCATION_HISTORY = "create table "
            + TABLE_LOCATION_HISTORY + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_LAT + " real , "
            + COLUMN_LNG + " real , "
            + COLUMN_VEL + " real , "
            + COLUMN_ALT +" real , "
            + COLUMN_ACC +" real , "
            + COLUMN_TIMESTAMP +" integer not null"
            + ") ;";

    private static final String DATABASE_CREATE_ACTIVITY_HISTORY = "create table "
            + TABLE_ACTIVITY_HISTORY + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TIMESTAMP +" integer not null , "
            + COLUMN_TYPE +" text , "
            + COLUMN_PROBABILITY +" integer"
            + ") ;";

    private static final String DATABASE_CREATE_RUNNING_APPLICATION_HISTORY = "create table "
            + TABLE_RUNNING_APPLICATION_HISTORY + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TYPE + " text , "
            + COLUMN_TIMESTAMP +" integer not null"
            + ") ;";

    private static final String DATABASE_CREATE_CALL_HISTORY = "create table "
            + TABLE_CALL_HISTORY + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TYPE + " text , "
            + COLUMN_TIMESTAMP +" integer not null"
            + ") ;";

    private static final String DATABASE_CREATE_NETWORK_HISTORY = "create table "
            + TABLE_NETWORK_HISTORY + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TYPE + " text , "
            + COLUMN_TIMESTAMP +" integer not null"
            + ") ;";

    private static final String DATABASE_CREATE_DEVICE_POSITION_HISTORY = "create table "
            + TABLE_DEVICE_POSITION_HISTORY + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TYPE+ " text , "
            + COLUMN_TIMESTAMP +" integer not null"
            + ") ;";

    private static final String DATABASE_CREATE_TRACK_HISTORY = "create table "
            + TABLE_TRACK_HISTORY + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TIMESTAMP +" Integer, "
            + COLUMN_ENDTIMESTAMP +" Integer, "
            + COLUMN_TYPE +" text, "
            + COLUMN_KILOMETER + " real"
            + ") ;";







    public SQLStorage(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_LOCATION_HISTORY);
        database.execSQL(DATABASE_CREATE_ACTIVITY_HISTORY);
        database.execSQL(DATABASE_CREATE_CALL_HISTORY);
        database.execSQL(DATABASE_CREATE_DEVICE_POSITION_HISTORY);
        database.execSQL(DATABASE_CREATE_NETWORK_HISTORY);
        database.execSQL(DATABASE_CREATE_RUNNING_APPLICATION_HISTORY);
        database.execSQL(DATABASE_CREATE_TRACK_HISTORY);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
