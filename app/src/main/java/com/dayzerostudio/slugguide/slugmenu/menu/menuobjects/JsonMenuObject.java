package com.dayzerostudio.slugguide.slugmenu.menu.menuobjects;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class JsonMenuObject {

    public final static int BREAKFAST = 0;
    public final static int LUNCH = 1;
    public final static int DINNER = 2;

    @SerializedName("title")
    public String title;
    @SerializedName("Breakfast")
    public ArrayList<String> breakfast;
    @SerializedName("Lunch")
    public ArrayList<String> lunch;
    @SerializedName("Dinner")
    public ArrayList<String> dinner;
    @SerializedName("dh")
    public String dh;

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public Boolean wasSuccessful() {
        return title != null;
    }

    public ArrayList<MenuItem> getMeal(int i) {
        switch (i) {
            case BREAKFAST:
                if (!breakfast.isEmpty())
                    return toMenuItemList(breakfast);
            case LUNCH:
                if (!lunch.isEmpty())
                    return toMenuItemList(lunch);
            case DINNER:
                if (!dinner.isEmpty())
                    return toMenuItemList(dinner);
            default:
                return !breakfast.isEmpty()?toMenuItemList(breakfast)
                        :!lunch.isEmpty()?toMenuItemList(lunch)
                        :!dinner.isEmpty()?toMenuItemList(dinner)
                        :null;
        }
    }

    private ArrayList<MenuItem> toMenuItemList(ArrayList<String> items) {
        if (items == null || items.isEmpty())
            return null;
        ArrayList<MenuItem> itemList = new ArrayList<MenuItem>();
        for (String item : items) {
            itemList.add(new MenuItem(item));
        }
        return itemList;
    }

}
