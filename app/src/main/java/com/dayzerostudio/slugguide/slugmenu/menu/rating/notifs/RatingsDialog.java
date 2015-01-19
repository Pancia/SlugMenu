package com.dayzerostudio.slugguide.slugmenu.menu.rating.notifs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.RatingBar;

import com.dayzerostudio.slugguide.slugmenu.R;
import com.dayzerostudio.slugguide.slugmenu.activities.DiningHallsListAdapter;
import com.dayzerostudio.slugguide.slugmenu.activities.DiningHallsListFragment;
import com.dayzerostudio.slugguide.slugmenu.menu.menuobjects.MenuItem;
import com.dayzerostudio.slugguide.slugmenu.menu.rating.RatingsManager;
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

    private MySlideExpandableListAdapter myAdapter;
    private List<MenuItem> selectedMenuItems = new ArrayList<MenuItem>();

    public RatingsDialog() {} //is actually used on rotation

    public RatingsDialog(ListAdapter adapter, List<MenuItem> objs) {
        Log.d(TAG, adapter.toString());
        Log.d(TAG, objs.toString());
        selectedMenuItems.addAll(objs);
        this.myAdapter = (MySlideExpandableListAdapter) adapter;
    }

    public void show(FragmentManager manager) {
        super.show(manager, TAG);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.displaymenu_dialog_fragment, null);
        if (view != null) {
            myRatingBar = (RatingBar) view.findViewById(R.id.displaymenu_dialog_rating_ratingbar);
        }

        if (savedInstanceState != null) {
            super.onViewStateRestored(savedInstanceState);
            Log.d(TAG, savedInstanceState.toString());
            Log.d(TAG, ((Boolean)savedInstanceState.containsKey(TAG+"#rating")).toString());
            if (savedInstanceState.containsKey(TAG+"#rating")) {
                myRatingBar.setRating(savedInstanceState.getFloat(TAG + "#rating"));
            }
            if (savedInstanceState.containsKey(TAG+"#selectedMenuItems")) {
                List<MenuItem> list = new ArrayList<MenuItem>();

                JSONObject json = new JSONObject();
                try {
                    json = new JSONObject(MyShrdPrfs.myShrdPrfs.getString(TAG+"#selectedMenuItems", ""));
                } catch (JSONException e) {e.printStackTrace();}

                Iterator iter = json.keys();
                for (String key; iter.hasNext();) {
                    key = (String) iter.next();
                    try {
                        list.add(new MenuItem(key, (float) json.getDouble(key)));
                    } catch (JSONException e) {e.printStackTrace();}
                }

                selectedMenuItems.clear();
                selectedMenuItems.addAll(list);
            }
        }

        builder.setView(view)
                .setTitle(selectedMenuItems.toString())
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RatingsManager rm = new RatingsManager(getActivity(), ((MenuListAdapter)RatingsDialog.this.myAdapter.getWrappedAdapter()).myBmf.getDh());
                        rm.storeRatingsFor(selectedMenuItems, myRatingBar.getRating());
                        rm.closeDB();
                        ((MenuListAdapter) myAdapter.getWrappedAdapter()).notifyDataSetChanged();
                        ((MenuListAdapter) myAdapter.getWrappedAdapter()).resetSelection();
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

        String tmp = "{";
        for (int i = 0; i < selectedMenuItems.size(); i++) {
            MenuItem obj = selectedMenuItems.get(i);
            tmp+="\"" + obj.getName() +"\":" + Float.toString(obj.getRating());
            if (i == selectedMenuItems.size()-1) break;
            tmp+=", ";

        }
        tmp+="}";
        JSONObject jsonSelectedMenuObjs = new JSONObject();
        try {
            jsonSelectedMenuObjs = new JSONObject(tmp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyShrdPrfs.saveObject(TAG+"#selectedMenuItems", jsonSelectedMenuObjs.toString());
        outState.putBoolean(TAG+"#selectedMenuItems", true);
        super.onSaveInstanceState(outState);
    }

}
