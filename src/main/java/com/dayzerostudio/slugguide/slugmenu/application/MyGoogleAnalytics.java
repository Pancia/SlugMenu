package com.dayzerostudio.slugguide.slugmenu.application;

import android.content.Context;
import android.util.Log;

import com.dayzerostudio.utils.storage.shrdprfs.MyShrdPrfs;
import com.google.analytics.tracking.android.GoogleAnalytics;

public class MyGoogleAnalytics {

    private final static String TAG = MyGoogleAnalytics.class.getSimpleName();
    private static Context myContext;

    public static final String TRACKING_PREF_KEY = "GA-TrackingPreference";
    public static void initGA(final Context context) {
        myContext = context;
        setOptOutPreference(MyShrdPrfs.myShrdPrfs.getBoolean(TRACKING_PREF_KEY, true));
    }
    public static void setOptOutPreference(Boolean isoptout) {
        Log.i(TAG, "setOptOut: " + isoptout);
        MyShrdPrfs.saveObject(TRACKING_PREF_KEY, isoptout);
    }
    public static boolean isOptOut() {
        Log.i(TAG, "isOptOut: "+GoogleAnalytics.getInstance(myContext).getAppOptOut());
        return GoogleAnalytics.getInstance(myContext).getAppOptOut();
    }

}
