package com.dayzerostudio.slugguide.slugmenu.menu.viewpager.displaymenu.meals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dayzerostudio.slugguide.slugmenu.menu.menuobjects.MenuItem;
import com.dayzerostudio.slugguide.slugmenu.menu.storage.MenuStorage;
import com.dayzerostudio.slugguide.slugmenu.menu.viewpager.displaymenu.BaseMenuFragment;

import java.util.List;

public class BreakfastMenuFragment extends BaseMenuFragment {

    private String TAG = BreakfastMenuFragment.class.getSimpleName();

    private String myDh;
    public String dh = "breakfast";

    public BreakfastMenuFragment() {}

    public BreakfastMenuFragment(String menu, int dtdate) {
        myDh = menu;
        myDtDate = dtdate;
    }

    @Override
    public List<MenuItem> getMenuData() {
        return MenuStorage.getMeal(myDh, dh, this.myDtDate);
    }

    @Override
    public String getDh() {
        return myDh;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            myDh = savedInstanceState.getString("MY_MENU");
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("MY_MENU", myDh);
        super.onSaveInstanceState(outState);
    }

}
