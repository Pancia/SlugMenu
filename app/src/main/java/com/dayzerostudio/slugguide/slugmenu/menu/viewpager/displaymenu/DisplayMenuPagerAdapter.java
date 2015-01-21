package com.dayzerostudio.slugguide.slugmenu.menu.viewpager.displaymenu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.dayzerostudio.slugguide.slugmenu.menu.menuobjects.Menu;
import com.dayzerostudio.slugguide.slugmenu.menu.viewpager.displaymenu.meals.BreakfastMenuFragment;
import com.dayzerostudio.slugguide.slugmenu.menu.viewpager.displaymenu.meals.DinnerMenuFragment;
import com.dayzerostudio.slugguide.slugmenu.menu.viewpager.displaymenu.meals.LunchMenuFragment;

public class DisplayMenuPagerAdapter extends FragmentStatePagerAdapter {

     private final static String TAG = DisplayMenuPagerAdapter.class.getSimpleName();
     private final int dtdate;

     public SparseArray<BaseMenuFragment> fragments = new SparseArray<BaseMenuFragment>();

     private Menu menu;

     public DisplayMenuPagerAdapter(FragmentManager fm, Menu menu, int dtdate) {
         super(fm);
         this.menu = menu;
         this.dtdate  = dtdate;
     }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (menu.getOptCode()) {
            case Menu.MENU_ALL_MEALS:
                switch (position) {
                    case 0:
                        return "Breakfast";
                    case 1:
                        return "Lunch";
                    case 2:
                        return "Dinner";
                    default:
                        return null;
                }
            case Menu.MENU_NO_BREAKFAST:
                switch (position) {
                    case 0:
                        return "Lunch";
                    case 1:
                        return "Dinner";
                    default:
                        return null;
                }
            case Menu.MENU_NO_LUNCH:
                switch (position) {
                    case 0:
                        return "Breakfast";
                    case 1:
                        return "Dinner";
                    default:
                        return null;
                }
            case Menu.MENU_NO_DINNER:
                switch (position) {
                    case 0:
                        return "Breakfast";
                    case 1:
                        return "Lunch";
                    default:
                        return null;
                }
            case Menu.MENU_ONLY_DINNER:
                return "Dinner";
            case Menu.MENU_ONLY_LUNCH:
                return "Lunch";
            case Menu.MENU_ONLY_BREAKFAST:
                return "Breakfast";
            case Menu.MENU_NO_MEALS:
                return null;
            default:
                return null;
        }
    }

    //where/when the fragment-meals are created/destroyed
     @Override
     public Object instantiateItem(ViewGroup container, int position) {
         BaseMenuFragment obj = (BaseMenuFragment)super.instantiateItem(container, position);
         fragments.put(position, obj);
         return obj;
     }
     @Override
     public void destroyItem(ViewGroup container, int position, Object object) {
         fragments.remove(position);
         super.destroyItem(container, position, object);
     }

     //notifies every fragment to refresh data
     @Override
     public void notifyDataSetChanged() {
         int key;
         for (int i = 0; i < fragments.size(); i++) {
             key = fragments.keyAt(i);
             BaseMenuFragment bmf = fragments.get(key);
             bmf.getMenuListAdapter().notifyDataSetChanged();
         }
         super.notifyDataSetChanged();
     }

     @Override
     public Fragment getItem(int position) {
         switch (menu.getOptCode()) {
             case Menu.MENU_ALL_MEALS:
                 switch (position) {
                     case 0:
                         return new BreakfastMenuFragment(this.menu.myDh, this.dtdate);
                     case 1:
                         return new LunchMenuFragment(this.menu.myDh, this.dtdate);
                     case 2:
                         return new DinnerMenuFragment(this.menu.myDh, this.dtdate);
                     default:
                         return null;
                 }
             case Menu.MENU_NO_BREAKFAST:
                 switch (position) {
                     case 0:
                         return new LunchMenuFragment(this.menu.myDh, this.dtdate);
                     case 1:
                         return new DinnerMenuFragment(this.menu.myDh, this.dtdate);
                     default:
                         return null;
                 }
             case Menu.MENU_NO_LUNCH:
                 switch (position) {
                     case 0:
                         return new BreakfastMenuFragment(this.menu.myDh, this.dtdate);
                     case 1:
                         return new DinnerMenuFragment(this.menu.myDh, this.dtdate);
                     default:
                         return null;
                 }
             case Menu.MENU_NO_DINNER:
                 switch (position) {
                     case 0:
                         return new BreakfastMenuFragment(this.menu.myDh, this.dtdate);
                     case 1:
                         return new LunchMenuFragment(this.menu.myDh, this.dtdate);
                     default:
                         return null;
                 }
             case Menu.MENU_ONLY_DINNER:
                 return new DinnerMenuFragment(this.menu.myDh, this.dtdate);
             case Menu.MENU_ONLY_LUNCH:
                 return new LunchMenuFragment(this.menu.myDh, this.dtdate);
             case Menu.MENU_ONLY_BREAKFAST:
                 return new BreakfastMenuFragment(this.menu.myDh, this.dtdate);
             case Menu.MENU_NO_MEALS:
                 return null;
             default:
                 return null;
         }
     }

     @Override
     public int getCount() {
         return this.menu.getNumOfMeals();
     }

 }