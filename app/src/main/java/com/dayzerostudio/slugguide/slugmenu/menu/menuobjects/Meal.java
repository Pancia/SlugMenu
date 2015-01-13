package com.dayzerostudio.slugguide.slugmenu.menu.menuobjects;

import com.dayzerostudio.slugguide.slugmenu.R;

import java.util.Calendar;

public class Meal {

    public final static int MENU_ALL_MEALS         = 0;
    public final static int MENU_NO_BREAKFAST      = 1;
    public final static int MENU_NO_LUNCH          = 2;
    public final static int MENU_NO_DINNER         = 4;
    public final static int MENU_ONLY_BREAKFAST    = 6;
    public final static int MENU_ONLY_LUNCH        = 5;
    public final static int MENU_ONLY_DINNER       = 3;
    public final static int MENU_NO_MEALS          = 7;

    public Boolean hasBreakfast = false;
    public Boolean hasLunch = false;
    public Boolean hasDinner = false;
    public String myDh;

    public final static int BREAKFAST = 0;
    public final static int LUNCH = 1;
    public final static int DINNER = 2;

    public Meal() {}

    public Meal(String dh) {
        this.myDh = dh;
    }

    public int getNumOfMeals() {
        int numOfPages = 0;
        if (hasBreakfast) numOfPages++;
        if (hasLunch) numOfPages++;
        if (hasDinner) numOfPages++;
        return numOfPages;
    }

    public int getOptCode() {
        int optCode = 0;
        if (!hasBreakfast) optCode+=1;
        if (!hasLunch) optCode+=2;
        if (!hasDinner) optCode+=4;
        return optCode;
    }

    @Override
    public String toString() {
        return "Meal@" + super.toString() + " " +
                "DiningHall: " + myDh + " " +
                "hasBreakfast: " + hasBreakfast.toString() +
                ", hasLunch: " + hasLunch.toString() +
                ", hasDinner: " + hasDinner.toString();
    }

    public void putMeal(int meal) {
        switch (meal){
            case BREAKFAST:
                hasBreakfast = true;
                break;
            case LUNCH:
                hasLunch = true;
                break;
            case DINNER:
                hasDinner = true;
            default:
                break;
        }
    }

    public void clearAllMeals() {
        hasBreakfast = false;
        hasLunch = false;
        hasDinner = false;
    }

    public int getOffScreenPageLimit() {
        if (getNumOfMeals() > 2) return 2;
        else return 1;
    }

    public int getClosestMeal() {
        int intTab = 0;
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        boolean weekend = (dayOfWeek == 1 || dayOfWeek == 7);
        if (weekend) {
            if (hour < 14) {//brunch
                intTab = 0;
            } else {
                intTab = 1;
            }
        } else {
            if (0 <= hour || hour < 11) {//breakfast
                intTab = 0;//redundant
            } else if (11 <= hour || hour < 14) {//lunch
                if (hasBreakfast && hasLunch) intTab = 1;
            } else if (14 <= hour || hour < 24) {//dinner
                if (hasBreakfast && hasLunch) intTab = 2;
                else if (hasBreakfast || hasLunch) intTab = 1;
            }
//            else {//past dinner
//                intTab = 0;//todo go to next day
//            }
        }
        return intTab;
    }

//    //maybe just return getClosestIntTab();?
//    public int getClosestMeal() {
//        int closest = getClosestIntTab();
//        if (closest == 0) {
//            if (hasBreakfast) return BREAKFAST;
//            else if (hasLunch) return LUNCH;
//            else return DINNER;
//        } else if (closest == 1) {
//            if (hasBreakfast) {
//                if (hasLunch) return LUNCH;
//                else return DINNER;
//            } else {
//                return DINNER;
//            }
//        } else {
//            return DINNER;
//        }
//    }

    public static int getDhName(String dh) {
        if (dh.equals("nine"))
            return R.string.dzs_sm_dh_nine;
        else if (dh.equals("porter"))
            return R.string.dzs_sm_dh_porter;
        else if (dh.equals("eight"))
            return R.string.dzs_sm_dh_eight;
        else if (dh.equals("cowell"))
            return R.string.dzs_sm_dh_cowell;
        else if (dh.equals("crown"))
            return R.string.dzs_sm_dh_crown;
        else
            return 0;
    }

    public boolean isEmpty() {
        return !hasBreakfast && !hasLunch && !hasDinner;
    }

}
