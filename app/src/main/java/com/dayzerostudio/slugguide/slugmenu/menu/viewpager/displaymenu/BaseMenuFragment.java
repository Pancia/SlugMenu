package com.dayzerostudio.slugguide.slugmenu.menu.viewpager.displaymenu;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.dayzerostudio.slugguide.slugmenu.R;
import com.dayzerostudio.slugguide.slugmenu.application.SlugMenu;
import com.dayzerostudio.slugguide.slugmenu.menu.menuobjects.MenuItem;
import com.dayzerostudio.slugguide.slugmenu.menu.rating.RatingsManager;
import com.dayzerostudio.slugguide.slugmenu.menu.rating.notifs.RatingsDialog;
import com.tjerkw_slideexpandable_library.MySlideExpandableListAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMenuFragment extends ListFragment {

    private static String TAG = BaseMenuFragment.class.toString();

    private ActionMode myAM;
    private List<MenuItem> selectedMenuItems = new ArrayList<MenuItem>();
    public int myDtDate;

    public List<MenuItem> getSelectedMenuItems() {return selectedMenuItems;}

    public abstract List<MenuItem> getMenuData();
    public abstract String getDh();

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ActionMode.Callback getAMCallback() {
        return new ActionMode.Callback() {
            private String TAG = "ActionMode.Callback";

            private void setAlerts() {
                //TODO
            }

            private void setRatings() {
                new RatingsDialog().init(getListView().getAdapter(), selectedMenuItems)
                        .show(getFragmentManager(), TAG);
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, android.view.MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.displaymenu_contextual_actionbar_alerts:
                        setAlerts();
                        //mode.finish();
                        Toast.makeText(getActivity(), R.string.dzs_sm_unimplemented, Toast.LENGTH_SHORT)
                                .show();
                        return true;
                    case R.id.displaymenu_contextual_actionbar_rate:
                        setRatings();
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                if (inflater != null) {
                    inflater.inflate(R.menu.displaymenu_contextual_actionbar, menu);
                }
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                selectedMenuItems.clear();
                myAM = null;
                //need to unselect everything!
                ListView lv = getListView();
                for (int i = 0; i < lv.getChildCount(); i++) {
                    View v = lv.getChildAt(i);
                    if (v == null) continue;
                    CheckBox cb = (CheckBox) v.findViewById(R.id.displaymenu_row_checkbox);
                    if (cb.isChecked()) cb.setChecked(false);
                }
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }
        };
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void startMyAM() {
        myAM = getActivity().startActionMode(getAMCallback());
        myAM.setTitle(selectedMenuItems.get(0).getName());
        myAM.setSubtitle(Integer.toString(selectedMenuItems.size()));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (SlugMenu.isNewAPI)
            doNewAPIListItemClick(l, v, position, id);
    }

    @TargetApi(11)
    private void doNewAPIListItemClick(ListView l, View v, int position, long id) {
        if (myAM != null) { //Handle click event
            MenuItem menuObj = (MenuItem) l.getItemAtPosition(position);
            if (!selectedMenuItems.contains(menuObj)) {
                selectedMenuItems.add(menuObj);
                myAM.setSubtitle(Integer.toString(selectedMenuItems.size()));
                //v.setSelected(true);
                if (v instanceof CheckBox)
                    ((CheckBox)v).setChecked(true);
            } else {
                selectedMenuItems.remove(menuObj);
                if (selectedMenuItems.size() == 0)
                    myAM.finish();
                else
                    myAM.setSubtitle(Integer.toString(selectedMenuItems.size()));

                //v.setSelected(false);
                if (v instanceof CheckBox)
                    ((CheckBox)v).setChecked(false);
            }
        } else { //Create new ActionModeBar
            selectedMenuItems.add((MenuItem) l.getItemAtPosition(position));
            startMyAM();
            //v.setSelected(true);
            if (v instanceof CheckBox)
                ((CheckBox)v).setChecked(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        List<MenuItem> menuData = getMenuData();
        if (savedInstanceState != null) {
            myDtDate = savedInstanceState.getInt("MY_DT_DATE");
            selectedMenuItems.clear();
            RatingsManager rm = new RatingsManager(getActivity(), getDh());
            //noinspection ConstantConditions
            for (String i : savedInstanceState.getStringArrayList("SELECTED_MENU_OBJECTS")) {
                Float rating = rm.getRatingFor(i);
                selectedMenuItems.add(menuData.get(menuData.indexOf(new MenuItem(i, rating))));
            }
            rm.closeDB();
            if (!selectedMenuItems.isEmpty() && SlugMenu.isNewAPI)
                startMyAM();
        }
        setListAdapter(new MySlideExpandableListAdapter(
                new MenuListAdapter(getActivity(),
                        R.layout.displaymenu_listview_row, R.id.displaymenu_row_textview_menuitem,
                        menuData, this))
        );
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**call to notify menulistadapter to update data*/
    public MenuListAdapter getMenuListAdapter() {
        return (MenuListAdapter)((MySlideExpandableListAdapter)getListAdapter()).getWrappedAdapter();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("MY_DT_DATE", myDtDate);

        ArrayList<String> selectedItems = new ArrayList<String>();
        for (MenuItem item : selectedMenuItems) {
            selectedItems.add(item.getName());
        }
        if (myAM != null && SlugMenu.isNewAPI)
            finishMyAM();
        else
            selectedMenuItems.clear();

        outState.putStringArrayList("SELECTED_MENU_OBJECTS", selectedItems);
        super.onSaveInstanceState(outState);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void finishMyAM() {
        myAM.finish();
    }

}
