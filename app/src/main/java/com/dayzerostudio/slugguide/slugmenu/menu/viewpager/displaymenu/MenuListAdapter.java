package com.dayzerostudio.slugguide.slugmenu.menu.viewpager.displaymenu;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dayzerostudio.slugguide.slugmenu.R;
import com.dayzerostudio.slugguide.slugmenu.activities.DisplayMenuActivity;
import com.dayzerostudio.slugguide.slugmenu.application.SlugMenu;
import com.dayzerostudio.slugguide.slugmenu.menu.menuobjects.MenuItem;
import com.dayzerostudio.slugguide.slugmenu.menu.rating.RatingsManager;
import com.dayzerostudio.slugguide.slugmenu.menu.viewpager.ViewHolder;
import com.dayzerostudio.utils.storage.shrdprfs.MyShrdPrfs;
import com.tjerkw_slideexpandable_library.MySlideExpandableListAdapter;

import java.util.Comparator;
import java.util.List;

public class MenuListAdapter extends ArrayAdapter<MenuItem> implements Comparator {

    private String TAG = MenuListAdapter.class.getSimpleName();

    private Activity myActivity;
    private BaseMenuFragment myBmf;

    public boolean sortAlphabetically = true;//todo change to shrdprfs field

    //Vodoo Magix
    @Override
    public int getViewTypeCount() {return getCount();}
    @Override
    public int getItemViewType(int position) {return position;}

    @Override
    public int compare(Object i, Object j) {
        if (sortAlphabetically)
            return i.toString().compareToIgnoreCase(j.toString());
        else {
            int ratingCompare = ((MenuItem)j).getRating().compareTo(((MenuItem)i).getRating());
            return (ratingCompare==0 ? i.toString().compareToIgnoreCase(j.toString()) : ratingCompare);
        }
    }

    public void swapSortingMethod() {
        Log.d(TAG, "Swapping sorting method.");
        sortAlphabetically = !sortAlphabetically;
        sort(this);
    }

    public class MenuItemViewHolder implements ViewHolder {
        LinearLayout linLayout;
        CheckBox checkBox;
        TextView menuItem;
        TextView textRating;
        RatingBar ratingBar;
        LinearLayout expLayout;
        Button expComment;
        RatingBar expRatingBar;
        public View getExpBtn() {return linLayout;}
        public View getExpView() {return expLayout;}
    }

    public MenuListAdapter(Activity activity, int resId, int txtViewResId, List<MenuItem> data, BaseMenuFragment bmf) {
        super(activity, resId, txtViewResId, data);
        try {
            if (!this.isEmpty())
                sort(this);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        this.myActivity = activity;
        this.myBmf = bmf;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MenuItem menuObj = getItem(position);
        final MenuItemViewHolder vh;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) myActivity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.displaymenu_listview_row, null);
            if (convertView == null) {throw new InstantiationError("Error inflating display_listview_row");}

            vh = getNewViewHolder(convertView);
            vh.expComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyShrdPrfs.saveObject("hasExpandedAListViewItem", true);
                    Toast.makeText(getContext(), R.string.dzs_sm_unimplemented, Toast.LENGTH_SHORT).show();
                }
            });
            if (SlugMenu.isNewAPI) {
                vh.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myBmf.onListItemClick(myBmf.getListView(), v, position, R.id.displaymenu_row_checkbox);
                    }
                });
            } else {
                vh.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<MenuItem> selected = myBmf.getSelectedMenuItems();
                        if (!selected.contains(menuObj)) {
                            selected.add(menuObj);
                        } else {
                            selected.remove(menuObj);
                        }
                    }
                });

            }
            vh.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        vh.linLayout.setBackgroundResource(R.color.dzs_sm_pressed);
                    } else {
                        vh.linLayout.setBackgroundResource(R.color.dzs_sm_transparent);
                    }
                }
            });

            convertView.setTag(vh);
            if (position == 0) {toggleFirstMenuItem(convertView);}
        } else {
            vh = (MenuItemViewHolder) convertView.getTag();
            //verify rating, and update if old
            RatingsManager rm = new RatingsManager(myActivity);
            Float rating = rm.getRatingFor(menuObj).getRating();
            if (!rating.equals(menuObj.getRating())) {
                menuObj.setRating(rating);
                notifyDataSetChanged();
            }
            rm.closeDB();
        }
        vh.expRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    updateRating(rating);
                    MyShrdPrfs.saveObject("hasExpandedAListViewItem", true);
                }
            }

            private void updateRating(float rating) {
                RatingsManager rm = new RatingsManager(myActivity);
                rm.storeRatingsFor(menuObj, rating);
                menuObj.setRating(rating);
                rm.closeDB();
                sort(MenuListAdapter.this);
                ((DisplayMenuActivity) myActivity).getDiningHallsListAdapter().notifyDataSetChanged();
                //todo close expanded view
            }
        });

        vh.checkBox.setChecked(myBmf.getSelectedMenuItems().contains(menuObj));
        vh.menuItem.setText(getItem(position).getName());
        vh.textRating.setText(menuObj.getRating("-"));
        vh.expRatingBar.setRating(menuObj.getRating());

        return convertView;
    }

    private MenuItemViewHolder getNewViewHolder(View convertView) {
        MenuItemViewHolder vh = new MenuItemViewHolder();

        vh.linLayout = (LinearLayout) convertView.findViewById(R.id.displaymenu_row_visible_layout);
        vh.checkBox = (CheckBox) convertView.findViewById(R.id.displaymenu_row_checkbox);
        vh.menuItem = (TextView) convertView.findViewById(R.id.displaymenu_row_textview_menuitem);
        vh.textRating = (TextView) convertView.findViewById(R.id.displaymenu_row_textview_rating);
        vh.ratingBar = (RatingBar) convertView.findViewById(R.id.displaymenu_row_ratingbar);

        vh.expLayout = (LinearLayout) convertView.findViewById(R.id.displaymenu_row_expandable_layout);
        vh.expComment = (Button) convertView.findViewById(R.id.displaymenu_row_button_comment);
        vh.expRatingBar = (RatingBar) convertView.findViewById(R.id.displaymenu_expanded_row_ratingbar);

        return vh;
    }

    public void toggleFirstMenuItem(View firstViewInList) {
        /**simulates onclick to expand first item*/
        if (MyShrdPrfs.myShrdPrfs.getBoolean("isFirstTimeUser", true) ||
                !MyShrdPrfs.myShrdPrfs.getBoolean("hasExpandedAListViewItem", false)) {
            ((MySlideExpandableListAdapter)myBmf.getListAdapter()).enableFor(firstViewInList, 0);
            ((MySlideExpandableListAdapter)myBmf.getListAdapter()).getExpandToggleButton(firstViewInList).performClick();
        }
    }

    public void resetSelection() {
        myBmf.getSelectedMenuItems().clear();
        ListView lv = myBmf.getListView();
        for (int i = 0; i < lv.getChildCount(); i++) {
            View v = lv.getChildAt(i);
            if (v == null) continue;
            CheckBox cb = (CheckBox) v.findViewById(R.id.displaymenu_row_checkbox);
            if (cb.isChecked()) cb.setChecked(false);
        }
    }

}