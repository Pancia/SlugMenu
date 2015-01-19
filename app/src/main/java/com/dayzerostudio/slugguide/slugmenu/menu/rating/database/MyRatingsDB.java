package com.dayzerostudio.slugguide.slugmenu.menu.rating.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyRatingsDB {

    private static String TAG = MyRatingsDB.class.toString();

    private static SQLiteDatabase database;
    private static MyRatingsDatabaseHelper dbHelper; //static == not lock sqliteDB

    public final static String DB_TABLE ="MyRatings";

    public final static String DB_ID ="_id";
    public final static String DB_RATING ="rating";
    public final static String DB_DH = "dh";
    private String mydh = null;

    public MyRatingsDB() {}

    public MyRatingsDB(Context context, String dh) {
        this.mydh = dh;
        if (dbHelper == null) {
            dbHelper = new MyRatingsDatabaseHelper(context);
        }
        SQLiteDatabase sqlDB;
        try {
            sqlDB = dbHelper.getWritableDatabase();
            database = sqlDB;
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public long addRating(String id, Float rating) {
        ContentValues values = new ContentValues();
        values.put(DB_RATING, rating);
        values.put(DB_ID, id);
        values.put(DB_DH, mydh);
        return database.insertWithOnConflict(DB_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public Float getRatingFor(String id) {
        Cursor c = database.query(DB_TABLE, new String[] {DB_ID, DB_RATING, DB_DH},
                DB_ID + " = ? AND " + DB_DH + " = ?", new String[]{id, mydh},
                null, null, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
        } else {
            return (float) -1;
        }
        Float ret = c.getFloat(1);
        c.close();
        return ret;
    }

    public void closeDB() {
        database.close();
    }
}
