package com.dayzerostudio.slugguide.slugmenu.menu.rating;

import android.content.Context;

import com.dayzerostudio.slugguide.slugmenu.menu.menuobjects.MenuItem;
import com.dayzerostudio.slugguide.slugmenu.menu.rating.database.MyRatingsDB;

import java.util.ArrayList;
import java.util.List;

public class RatingsManager {

    private static String TAG = RatingsManager.class.toString();
    private Context myContext;
    private MyRatingsDB mydb;

    public RatingsManager(Context context) {
        this.myContext = context;
        this.mydb = new MyRatingsDB(this.myContext);
    }

    public void storeRatingsFor(MenuItem menuObj, float rating) {
        this.mydb.addRating(menuObj.getName(), rating);
    }

    public void storeRatingsFor(List<MenuItem> selectedMenuItems, float rating) {
        for (MenuItem item : selectedMenuItems) {
            storeRatingsFor(item, rating);
        }
    }

    public Float getRatingFor(String id) {
        return this.mydb.getRatingFor(id);
    }

    public MenuItem getRatingFor(MenuItem menuObj) {
        return new MenuItem( menuObj.getName(), this.mydb.getRatingFor(menuObj.getName()) );
    }

    public List<MenuItem> getRatingsFor(List<MenuItem> selectedMenuItems) {
        if (selectedMenuItems == null)
                return null;
        List<MenuItem> myRatingsList = new ArrayList<MenuItem>();
        for (MenuItem menuObj : selectedMenuItems) {
            myRatingsList.add(getRatingFor(menuObj));
        }
        return myRatingsList;
    }

    /*public List<MenuItem> getAllRatings() {
        return this.mydb.getAllRatings();
    }*/

    public void closeDB() {
        this.mydb.closeDB();
    }

}
