package com.dayzerostudio.slugguide.slugmenu.server;

import android.accounts.NetworkErrorException;
import android.util.Log;

import com.dayzerostudio.slugguide.slugmenu.menu.menuobjects.JsonMenuObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuParser {

    private static final String TAG = MenuParser.class.getSimpleName();

    private static final String base_url =
            "http://nutrition.sa.ucsc.edu/menuSamp.asp?myaction=read&locationNum=";
    private static final String dt_date = "&dtdate="; //Month%2FDay%2FYear;

    //valid dhs
    public static final List<String> dhs =
            new ArrayList<String>(Arrays.asList("nine", "eight", "cowell", "porter", "crown"));

    //codes for each dining hall's ?locationNum=
    private static final Map<String, String> dh_loc_nums =
            new HashMap<String, String>() {};
    static {
        dh_loc_nums.put("nine", "40");
        dh_loc_nums.put("eight", "30");
        dh_loc_nums.put("cowell", "05");
        dh_loc_nums.put("porter", "25");
        dh_loc_nums.put("crown", "20");
    }

    private static String getFormattedDateUrl(int dtdate) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, dtdate);
        return dt_date + (cal.get(Calendar.MONTH)+1)
                + "%2F" + cal.get(Calendar.DAY_OF_MONTH)
                + "%2F" + cal.get(Calendar.YEAR);
    }

    public static String getUrl(String dh, int dttime) {
        return base_url + dh_loc_nums.get(dh) + getFormattedDateUrl(dttime);
    }

    public static JsonMenuObject parseString(String html, String dh, int dtdate) throws NetworkErrorException {
        Document document = Jsoup.parse(html);
        if (!document.getElementsByTag("title").html().contains("UC Santa Cruz Dining Menus")) {
            //if we didnt get the meal page
            //we probably are connected to cruznet
            //and need authentication!
            throw new NetworkErrorException("Must authenticate first!");
        }

        List<Element> s = new ArrayList<Element>();
        Elements items = document.getElementsByTag("td");
        for (Element item : items) {
            if ( item.hasAttr("valign") && item.hasAttr("width")
                    && item.attr("valign").equals("top")
                    && (item.attr("width").equals("30%")
                        || item.attr("width").equals("50%")
                        || item.attr("width").equals("100%")) ) {
                s.add(item);
            }
        }

        List<String> menu = new ArrayList<String>();
        for (Element ele : s) {
            List<String> mealList = new ArrayList<String>();
            menu.add(ele.getElementsByClass("menusampmeals").html());//meal name (eg breakfast)
            Elements menuitems = ele.getElementsByClass("menusamprecipes");//all meal items
            for (Element e : menuitems) {
                String menuitem = e.getElementsByTag("span").html();
                if (!"".equals(menuitem) && !mealList.contains(menuitem) && !menuitem.contains("CHEF'S SPECIAL"))
                    mealList.add(menuitem);
            }
            menu.addAll(mealList);
        }
        List<String> itemsToRemove = new ArrayList<String>();
        for (String str : menu) {
            if ("".equals(str))
                itemsToRemove.add(str);
        }
        menu.removeAll(itemsToRemove);
        Log.i(TAG, menu.toString());

        String title = document.getElementsByClass("menusamptitle").html();//title

        JsonMenuObject json = new JsonMenuObject();
        json.response.setTitle(title);
        json.menu.setDh(dh);

        int warning = 0;
        if (!menu.contains("Breakfast"))
            warning += 1;
        if (!menu.contains("Lunch"))
            warning += 2;
        if (!menu.contains("Dinner"))
            warning += 4;
        json.server.setWarning(warning);

        Log.i(TAG, json.toString());

        if (warning == 7) {
            json.request.setSuccess(0);
            json.server.setError(1);
            json.response.setMessage("Couldn\'t get the menu, the dining hall might be closed for the day.");
            Log.w(TAG, json.toString());
            return json;
        }
        json.request.setSuccess(1);
        json.server.setError(0);
        json.response.setMessage("Got the menu!");

        String meal = null; ArrayList<String> mealsMenu = new ArrayList();
        for (String str : menu) {
            if (str.equals("Breakfast") || str.equals("Lunch") || str.equals("Dinner")) {
                if (meal != null) {
                    json.menu.setMealWith(meal, (ArrayList<String>)mealsMenu.clone());
                    mealsMenu.clear();
                }
                meal = str;
            } else {
                mealsMenu.add(str.replace("&amp;", "&"));
            }
        }
        json.menu.setMealWith(meal, mealsMenu);
        Log.i(TAG, json.toString());
        return json;
    }

}