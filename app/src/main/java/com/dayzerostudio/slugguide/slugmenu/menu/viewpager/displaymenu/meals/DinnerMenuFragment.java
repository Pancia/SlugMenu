package com.dayzerostudio.slugguide.slugmenu.menu.viewpager.displaymenu.meals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dayzerostudio.slugguide.slugmenu.menu.menuobjects.MenuItem;
import com.dayzerostudio.slugguide.slugmenu.menu.storage.MenuStorage;
import com.dayzerostudio.slugguide.slugmenu.menu.viewpager.displaymenu.BaseMenuFragment;

import java.util.List;

public class DinnerMenuFragment extends BaseMenuFragment {

    private String TAG = DinnerMenuFragment.class.getSimpleName();

    private String myDh;
    public String dh = "dinner";

    public DinnerMenuFragment() {}

    public DinnerMenuFragment(String dh, int dtdate) {
        this.myDh = dh;
        this.myDtDate = dtdate;
    }

    @Override
    public List<MenuItem> getMenuData() {
        return MenuStorage.getMeal(myDh, "dinner", this.myDtDate);
    }

    @Override
    public String getDh() {
        return this.dh;
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
