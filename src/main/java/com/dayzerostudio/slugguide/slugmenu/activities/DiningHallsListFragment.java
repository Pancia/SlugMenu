package com.dayzerostudio.slugguide.slugmenu.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dayzerostudio.slugguide.slugmenu.R;

public class DiningHallsListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dininghalls_fragment, container, false);
        assert v != null;
        ListView myLv = (ListView) v.findViewById(R.id.dininghalls_fragment_listview);
        myLv.setAdapter(DiningHallsListAdapter.newInstance(getActivity(),
                R.id.dininghalls_primaryrow_layout, R.id.dininghalls_primaryrow_dhname));
        return v;
    }

    public ListView getListView() {
        return (ListView) getView().findViewById(R.id.dininghalls_fragment_listview);
    }

}
