package com.tjerkw_slideexpandable_library;

import android.view.View;
import android.widget.ListAdapter;

import com.dayzerostudio.slugguide.slugmenu.menu.viewpager.ViewHolder;

/**
 * ListAdapter that adds sliding functionality to a list.
 * Modified to use (abstract) ViewHolder pattern (class).
 *
 * @author Pancia
 * #date 11/3/13
 */
public class MySlideExpandableListAdapter extends AbstractSlideExpandableListAdapter {

	public MySlideExpandableListAdapter(ListAdapter wrapped) {
		super(wrapped);
	}

	@Override
	public View getExpandToggleButton(View parent) {
        ViewHolder vh = (ViewHolder) parent.getTag();
		return vh.getExpBtn();
	}

	@Override
	public View getExpandableView(View parent) {
        ViewHolder vh = (ViewHolder) parent.getTag();
		return vh.getExpView();
	}

}
