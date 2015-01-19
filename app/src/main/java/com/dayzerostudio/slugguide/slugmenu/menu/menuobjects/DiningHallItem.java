package com.dayzerostudio.slugguide.slugmenu.menu.menuobjects;

import android.app.Activity;

import com.dayzerostudio.slugguide.slugmenu.R;
import com.dayzerostudio.slugguide.slugmenu.menu.rating.RatingsManager;

import java.util.ArrayList;
import java.util.List;

public class DiningHallItem {

    private final static String TAG = DiningHallItem.class.getSimpleName();

    private final static int DH_COWELL = 0;
    private final static int DH_CROWN = 1;
    private final static int DH_EIGHT = 2;
    private final static int DH_NINE = 3;
    private final static int DH_PORTER = 4;

    private String myName;
    private Activity myActivity;

    public DiningHallItem(Activity activity, String dhName) {
        this.myName = dhName;
        this.myActivity = activity;
    }

    public static DiningHallItem newInstance(Activity activity, int dh) {
        switch (dh) {
            case DH_COWELL:
                return new DiningHallItem(activity, activity.getResources().getString(R.string.dzs_sm_dh_cowell));
            case DH_CROWN:
                return new DiningHallItem(activity, activity.getResources().getString(R.string.dzs_sm_dh_crown));
            case DH_EIGHT:
                return new DiningHallItem(activity, activity.getResources().getString(R.string.dzs_sm_dh_eight));
            case DH_NINE:
                return new DiningHallItem(activity, activity.getResources().getString(R.string.dzs_sm_dh_nine));
            case DH_PORTER:
                return new DiningHallItem(activity, activity.getResources().getString(R.string.dzs_sm_dh_porter));
            default:
                return null;
        }
    }

    public float getAvgMealRating(JsonMenuObject jmo, int meal) {
        ArrayList<MenuItem> items = jmo.menu.getMeal(meal);
        if (items == null)
            return -1;
        else if (items.isEmpty())
            return -2;
        RatingsManager rm = new RatingsManager(this.myActivity, jmo.menu.getDh().replace("\"", ""));
        List<MenuItem> menuItems = rm.getRatingsFor(items);
        rm.closeDB();
        if (menuItems == null)
            return -3;
        else if (menuItems.isEmpty())
            return -4;

        float totRating = 0; int size = 0;
        for (MenuItem menuItem : menuItems) {
            float rating = menuItem.getRating();
            if (rating != -1) {
                totRating += rating;
                size++;
            }
        }
        return totRating/size;
    }

    public String getName() {
        return this.myName;
    }

    @Override
    public String toString() {
        return this.myName;
    }

}
