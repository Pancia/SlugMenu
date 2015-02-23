package com.dayzerostudio.slugguide.slugmenu.menu.rating.notifs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.RatingBar;

import com.dayzerostudio.slugguide.slugmenu.R;
import com.dayzerostudio.slugguide.slugmenu.activities.DiningHallsListAdapter;
import com.dayzerostudio.slugguide.slugmenu.activities.DiningHallsListFragment;
import com.dayzerostudio.slugguide.slugmenu.activities.DisplayMenuActivity;
import com.dayzerostudio.slugguide.slugmenu.activities.DisplayMenuFragment;
import com.dayzerostudio.slugguide.slugmenu.menu.menuobjects.MenuItem;
import com.dayzerostudio.slugguide.slugmenu.menu.rating.RatingsManager;
import com.dayzerostudio.slugguide.slugmenu.menu.viewpager.displaymenu.BaseMenuFragment;
import com.dayzerostudio.slugguide.slugmenu.menu.viewpager.displaymenu.DisplayMenuPagerAdapter;
import com.dayzerostudio.slugguide.slugmenu.menu.viewpager.displaymenu.MenuListAdapter;
import com.dayzerostudio.utils.storage.shrdprfs.MyShrdPrfs;
import com.tjerkw_slideexpandable_library.MySlideExpandableListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RatingsDialog extends DialogFragment {

    private final static String TAG = RatingsDialog.class.getSimpleName();

    private RatingBar myRatingBar;

    private List<MenuItem> selectedMenuItems = new ArrayList<MenuItem>();

    public RatingsDialog() {} //is actually used on rotation

    public RatingsDialog init(ListAdapter adapter, List<MenuItem> objs) {
        Log.d(TAG, adapter.toString());
        Log.d(TAG, objs.toString());
        this.selectedMenuItems.addAll(objs);
        return this;
    }

    @Override
    public void setArguments(Bundle args) {
        Log.e(TAG, "setArguments");
        selectedMenuItems = args.getParcelableArrayList("SELECTED_MENU_ITEMS");
        super.setArguments(args);
    }

    public void show(FragmentManager manager) {
        super.show(manager, TAG);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view = getActivity().getLayoutInflater().inflate(R.layout.displaymenu_dialog_fragment, null);
        if (view != null) {
            myRatingBar = (RatingBar) view.findViewById(R.id.displaymenu_dialog_rating_ratingbar);
        }

        if (savedInstanceState != null) {
            super.onViewStateRestored(savedInstanceState);
            Log.d(TAG+"bundle", savedInstanceState.toString());
            if (savedInstanceState.containsKey(TAG+"#rating")) {
                myRatingBar.setRating(savedInstanceState.getFloat(TAG+"#rating"));
            }
            selectedMenuItems.clear();
            ArrayList<MenuItem> list = savedInstanceState.getParcelableArrayList(TAG+"#selectedMenuItems");
            selectedMenuItems.addAll(list);
        }

        builder.setView(view)
                .setTitle(selectedMenuItems.toString())
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ViewPager viewPager = ((DisplayMenuActivity) getActivity()).getDisplayMenuViewPager();
                        BaseMenuFragment bmf = ((DisplayMenuPagerAdapter) viewPager.getAdapter()).fragments.get(viewPager.getCurrentItem());
                        MenuListAdapter adapter = (MenuListAdapter) ((MySlideExpandableListAdapter) bmf.getListAdapter()).getWrappedAdapter();
                        RatingsManager rm = new RatingsManager(getActivity(), adapter.myBmf.getDh());
                        rm.storeRatingsFor(selectedMenuItems, myRatingBar.getRating());
                        rm.closeDB();
                        adapter.notifyDataSetChanged();
                        adapter.resetSelection();
                        ((DiningHallsListAdapter)
                                ((DiningHallsListFragment)
                                        getActivity().getSupportFragmentManager()
                                                .findFragmentById(R.id.displaymenu_masterfragment))
                                        .getListView().getAdapter())
                                .notifyDataSetChanged();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putFloat(TAG + "#rating", myRatingBar.getRating());

        ArrayList<MenuItem> selected = new ArrayList<MenuItem>(selectedMenuItems);
        outState.putParcelableArrayList(TAG+"#selectedMenuItems", selected);
        super.onSaveInstanceState(outState);
    }

}
