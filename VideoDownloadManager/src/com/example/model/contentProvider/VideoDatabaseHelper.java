package com.example.model.contentProvider;

import java.io.File;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The database helper used by the Video Content Provider to create
 * and manage its underling database.
 */
public class VideoDatabaseHelper extends SQLiteOpenHelper {
    /**
     * Database name.
     */
    private static String DATABASE_NAME =
        "com_example_assignment3_db";

    /**
     * Database version number, which is updated with each schema
     * change.
     */
    private static int DATABASE_VERSION = 1;

    /*
     * SQL create table statements.
     */

    /**
     * SQL statement used to create the Hobbit table.
     */
    final String SQL_CREATE_VIDEO_TABLE =
        "CREATE TABLE "
        + VideoContract.VideoEntry.TABLE_NAME + " (" 
        + VideoContract.VideoEntry._ID + " INTEGER PRIMARY KEY, " 
        + VideoContract.VideoEntry.COLUMN_VIDEO_ID + " INTEGER NOT NULL ,"
        + VideoContract.VideoEntry.COLUMN_TITLE + " TEXT NOT NULL, " 
        + VideoContract.VideoEntry.COLUMN_DURATION + " INTEGER NOT NULL, "
        + VideoContract.VideoEntry.COLUMN_CONTENT_TYPE + " TEXT NOT NULL, "
        + VideoContract.VideoEntry.COLUMN_DATA_URL + " TEXT NOT NULL, "
        + VideoContract.VideoEntry.COLUMN_RATING_SUM + " REAL NOT NULL, "
        + VideoContract.VideoEntry.COLUMN_RATING_COUNT + " REAL NOT NULL "
        + " );";

     /**
     * Constructor - initialize database name and version, but don't
     * actually construct the database (which is done in the
     * onCreate() hook method). It places the database in the
     * application's cache directory, which will be automatically
     * cleaned up by Android if the device runs low on storage space.
     * 
     * @param context
     */
    public VideoDatabaseHelper(Context context) {
        super(context, 
              context.getCacheDir()
              + File.separator 
              + DATABASE_NAME, 
              null,
              DATABASE_VERSION);
    }

    /**
     * Hook method called when the database is created.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the table.
        db.execSQL(SQL_CREATE_VIDEO_TABLE);
    }

    /**
     * Hook method called when the database is upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {
        // Delete the existing tables.
        db.execSQL("DROP TABLE IF EXISTS "
                   + VideoContract.VideoEntry.TABLE_NAME);
        // Create the new tables.
        onCreate(db);
    }
}
