package com.dayzerostudio.slugguide.slugmenu.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.dayzerostudio.slugguide.slugmenu.R;
import com.dayzerostudio.slugguide.slugmenu.menu.menuobjects.Meal;
import com.dayzerostudio.slugguide.slugmenu.menu.storage.MenuStorage;
import com.dayzerostudio.slugguide.slugmenu.menu.viewpager.displaymenu.DisplayMenuPagerAdapter;
import com.viewpagerindicator.TitlePageIndicator;

public class DisplayMenuFragment extends Fragment {

    private static final String TAG = DisplayMenuFragment.class.getSimpleName();
    private String dhName;
    public ViewPager myViewPager;

    public DisplayMenuFragment() {}

    public DisplayMenuFragment(String dhName) {
        this.dhName = dhName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.displaymenu_fragment, container, false);
        assert v != null;
        myViewPager = (ViewPager) v.findViewById(R.id.displaymenu_fragment_viewpager);
        if (savedInstanceState != null)
            dhName = savedInstanceState.getString("DH_NAME");
        displayMenu(v, dhName);
        return v;
    }

    private void displayMenu(View v, String dhName) {
        if (dhName == null) {
            return;
        }
        final Meal meal = new Meal(dhName);
        if (MenuStorage.hasMeal(dhName, "breakfast", 0)) {
            meal.putMeal(Meal.BREAKFAST);
        }
        if (MenuStorage.hasMeal(dhName, "lunch", 0)) {
            meal.putMeal(Meal.LUNCH);
        }
        if (MenuStorage.hasMeal(dhName, "dinner", 0)) {
            meal.putMeal(Meal.DINNER);
        }

        if (meal.isEmpty()) {
            //todo: either make background have message,
            //todo: or put "it's empty" item into some adapter
            Log.w(TAG, "meal.isEmpty()!");
            myViewPager.setAdapter(null);
        } else {
            myViewPager.setOffscreenPageLimit(meal.getOffScreenPageLimit());
            DisplayMenuPagerAdapter vpa =
                    new DisplayMenuPagerAdapter(
                            getActivity().getSupportFragmentManager(), meal, 0
                    );
            myViewPager.setAdapter(vpa);
            TitlePageIndicator titleIndicator = (TitlePageIndicator) v.findViewById(R.id.displaymenu_fragment_titles);
            titleIndicator.setViewPager(myViewPager);
            titleIndicator.setCurrentItem(meal.getClosestMeal());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                //todo should actually modify all fragments, and future fragments
                // #see menulistadapter:sortAlphabetically
                Log.d(TAG, "Swapping sorting method.");
                ((DisplayMenuPagerAdapter)myViewPager.getAdapter())
                        .fragments.get(myViewPager.getCurrentItem())
                        .getMenuListAdapter().swapSortingMethod();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("DH_NAME", dhName);
        super.onSaveInstanceState(outState);
    }

}
