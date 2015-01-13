package com.dayzerostudio.slugguide.slugmenu.menu.rating.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyRatingsDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserPreferences";
    private static final String TABLE_NAME = "MyRatings";

    //change this to implement any changes
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE = "create table "+TABLE_NAME+"( _id text primary key, rating real);";

    public MyRatingsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        Log.w(MyRatingsDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");

        database.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(database);
    }

}