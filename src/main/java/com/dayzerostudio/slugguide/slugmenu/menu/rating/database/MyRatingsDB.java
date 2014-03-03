package com.dayzerostudio.slugguide.slugmenu.menu.rating.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyRatingsDB {

    private static String TAG = MyRatingsDB.class.toString();

    private static SQLiteDatabase database;
    private static MyRatingsDatabaseHelper dbHelper; //this being static is key to not locking sqliteDB

    public final static String DB_TABLE ="MyRatings";

    public final static String DB_ID ="_id";
    public final static String DB_RATING ="rating";

    public MyRatingsDB() {}

    public MyRatingsDB(Context context) {
        if (dbHelper == null) {dbHelper = new MyRatingsDatabaseHelper(context);}
        SQLiteDatabase sqlDB;
        try{
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
        return database.insertWithOnConflict(DB_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public Float getRatingFor(String id) {
        Cursor c = database.query(DB_TABLE, new String[] {DB_ID, DB_RATING},
                DB_ID + " = ?", new String[]{id},
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

    /*
    public List<MenuItem> getAllRatings() {
        Cursor c = database.query(true, DB_TABLE, new String[] {DB_ID, DB_RATING},
                null, null, null, null, null, null);

        if (c != null && c.getColumnCount() > 2) {
            c.moveToFirst();
        } else {
            Log.e(TAG+"#getAllRatings", "Cursor was null!!!");
            return null;
        }
        List<MenuItem> menuObjs = new ArrayList<MenuItem>();
        for (int i = 0; i < c.getCount(); c.moveToNext()) {
            menuObjs.add(new MenuItem(c.getString(0), c.getFloat(1)));
        }
        c.close();
        return menuObjs;
    }

    public int getRatingsCount() {
        return getAllRatings().size();
    }

    public void deleteRating(String id) {
        database.delete(DB_TABLE, DB_ID + " = ?", new String[]{id});
    }*/

    public void closeDB() {
        database.close();
    }
}
