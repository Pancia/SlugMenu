package com.dayzerostudio.slugguide.slugmenu.menu.storage;

import android.content.Context;

import com.dayzerostudio.slugguide.slugmenu.menu.menuobjects.JsonMenuObject;
import com.dayzerostudio.slugguide.slugmenu.menu.menuobjects.MenuItem;
import com.dayzerostudio.slugguide.slugmenu.menu.rating.RatingsManager;
import com.dayzerostudio.utils.storage.shrdprfs.MyShrdPrfs;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MenuStorage {

    private static String TAG = MenuStorage.class.getSimpleName();

    private static Context myContext;

    //maybe make MenuStorage non-static?
    public static void init(Context context) {
        myContext = context;
    }

    public static boolean hasMenu(String dh, int dtdate) {
        Calendar myCal = Calendar.getInstance();
        myCal.add(Calendar.DAY_OF_MONTH, dtdate);
        return MyShrdPrfs.myShrdPrfs.contains(dh+":"+myCal.get(Calendar.DAY_OF_MONTH)+":"+myCal.get(Calendar.MONTH)+":"+myCal.get(Calendar.YEAR));
    }

    public static void saveMenu(String dh, JsonMenuObject json, int dtdate) {
        Calendar myCal = Calendar.getInstance();
        myCal.add(Calendar.DAY_OF_MONTH, dtdate);
        MyShrdPrfs.saveObject(dh+":"+myCal.get(Calendar.DAY_OF_MONTH)+":"+myCal.get(Calendar.MONTH)+":"+myCal.get(Calendar.YEAR), json.toString());
    }

    public static JsonMenuObject getJsonMenuObject(String dh, int dtdate) {
        Calendar myCal = Calendar.getInstance();
        myCal.add(Calendar.DAY_OF_MONTH, dtdate);
        String menu = MyShrdPrfs.myShrdPrfs.getString(dh+":"+myCal.get(Calendar.DAY_OF_MONTH)+":"+myCal.get(Calendar.MONTH)+":"+myCal.get(Calendar.YEAR), null);
        if (menu == null)
            return null;
        JsonReader reader = new JsonReader(new StringReader(menu));
        reader.setLenient(true);
        return new Gson().fromJson(reader, JsonMenuObject.class);
    }

    public static List<MenuItem> getMeal(String dh, String meal, int dtdate) {
        List<MenuItem> listOfMenuItems = new ArrayList<MenuItem>();

        JsonMenuObject jmo = getJsonMenuObject(dh, dtdate);

        listOfMenuItems = jmo.menu.getMeal(
                (meal.equals("breakfast")?JsonMenuObject.BREAKFAST
                        :meal.equals("lunch")?JsonMenuObject.LUNCH
                        :meal.equals("dinner")?JsonMenuObject.DINNER
                        :0)
        );

        RatingsManager rm = new RatingsManager(MenuStorage.myContext);
        for (MenuItem item : listOfMenuItems) {
            item.setRating(rm.getRatingFor(item).getRating());
        }
        rm.closeDB();

        return listOfMenuItems;
    }

    public static boolean hasMeal(String dh, String meal, int dtdate) {
        if (!hasMenu(dh, dtdate))
            return false;
        if (getMeal(dh, meal, dtdate) == null)
            return false;
        JsonMenuObject jmo = getJsonMenuObject(dh, dtdate);

        List<MenuItem> listOfMenuItems = jmo.menu.getMeal(
                (meal.equals("breakfast")?JsonMenuObject.BREAKFAST
                        :meal.equals("lunch")?JsonMenuObject.LUNCH
                        :meal.equals("dinner")?JsonMenuObject.DINNER
                        :0)
        );
        return listOfMenuItems.size() != 0;
    }

}