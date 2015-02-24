package com.dayzerostudio.slugguide.slugmenu.activities;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dayzerostudio.slugguide.slugmenu.R;
import com.dayzerostudio.slugguide.slugmenu.menu.menuobjects.DiningHallItem;
import com.dayzerostudio.slugguide.slugmenu.menu.menuobjects.JsonMenuObject;
import com.dayzerostudio.slugguide.slugmenu.menu.menuobjects.Menu;
import com.dayzerostudio.slugguide.slugmenu.menu.storage.MenuStorage;
import com.dayzerostudio.slugguide.slugmenu.menu.viewpager.ViewHolder;
import com.dayzerostudio.slugguide.slugmenu.server.AsyncHttpRequestManager;
import com.dayzerostudio.slugguide.slugmenu.server.MyFutureTask;
import com.dayzerostudio.utils.network.MyConnectivity;

import java.util.ArrayList;
import java.util.List;

public class DiningHallsListAdapter extends ArrayAdapter<DiningHallItem> {
    private static final String TAG = DiningHallsListAdapter.class.getSimpleName();
    private final DisplayMenuActivity myActivity;

    public static DiningHallsListAdapter newInstance(Activity activity,
                                                     int resource, int textViewResourceId) {
        List<DiningHallItem> dhs = new ArrayList<DiningHallItem>();
        for (int i = 0; i < 5; i++) {
            dhs.add(DiningHallItem.newInstance(activity, i));
        }
        return new DiningHallsListAdapter(activity, resource, textViewResourceId, dhs);
    }

    private DiningHallsListAdapter(Activity a, int res, int txtViewRes, List<DiningHallItem> dhs) {
        super(a, res, txtViewRes, dhs);
        this.myActivity = (DisplayMenuActivity) a;
    }

    private class DiningHallViewHolder implements ViewHolder {
        LinearLayout outer_layout;
        TextView dh_name;
        TextView avg_rating;

        public View getExpBtn() {return null;}
        public View getExpView() {return null;}
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final DiningHallItem dhItem = getItem(position);
        final String dhName = dhItem.getName()
                .substring(0, dhItem.getName().indexOf("and") - 1).toLowerCase();
        final DiningHallViewHolder vh;
        final Menu menu = new Menu(dhName);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) myActivity
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dininghalls_row, null);
            if (convertView == null) {throw new InstantiationError("Error inflating tag_row");}

            vh = getNewViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (DiningHallViewHolder) convertView.getTag();
        }

        if (dhItem != null) {
            vh.dh_name.setText(dhItem.getName());

            vh.avg_rating.setText("Loading...");
            vh.outer_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!MyConnectivity.isOnline()) {
                        myActivity.showGetInternetDialog();
                    } else {
                        Toast.makeText(myActivity, "Re-Loading...", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }
                }
            });
            new AsyncHttpRequestManager(this.myActivity)
                    .setCallback(new MyFutureTask() {
                        @Override
                        public void onTaskCompleted(final JsonMenuObject jmo, Boolean success) {
                            if (success) {
                                Float avgRating = dhItem.getAvgMealRating(jmo, menu.getClosestMeal());
                                vh.avg_rating.setText( "Avg: " +
                                        (avgRating >= 0
                                                ? Float.toString(avgRating).substring(0, 3)
                                                : "-") );
                                vh.outer_layout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        myActivity.onDisplayMenu(dhName);
                                    }
                                });
                            } else {
                                vh.avg_rating.setText("Avg: n/a");
                                vh.outer_layout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (!MenuStorage.hasMenu(jmo.dh, 0)) {
                                            Toast.makeText(getContext(), myActivity.getString(R.string.dzs_sm_dhnotavailable), Toast.LENGTH_LONG).show();
                                            notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        }
                    })
                    .selectDiningHall(dhName)
                    .execute();
        }

        return convertView;
    }

    private DiningHallViewHolder getNewViewHolder(View convertView) {
        DiningHallViewHolder vh = new DiningHallViewHolder();
        vh.outer_layout = (LinearLayout) convertView.findViewById(R.id.dininghalls_primaryrow_layout);
        vh.dh_name = (TextView) convertView.findViewById(R.id.dininghalls_primaryrow_dhname);
        vh.avg_rating = (TextView) convertView.findViewById(R.id.dininghalls_primaryrow_avgrating);
        return vh;
    }

}
