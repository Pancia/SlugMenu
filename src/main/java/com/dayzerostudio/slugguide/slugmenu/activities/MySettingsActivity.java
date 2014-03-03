package com.dayzerostudio.slugguide.slugmenu.activities;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.dayzerostudio.slugguide.slugmenu.R;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;

public class MySettingsActivity extends PreferenceActivity {

    private final static String TAG = MySettingsActivity.class.getSimpleName();
    private final static String KEY_USERNAME = "username";
    private final static String KEY_OPTOUT = "GA-TrackingPreference";

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT<11) {
            Log.d(TAG, "addPreferencesFromResource()");
            addPreferencesFromResource(R.xml.settings_preferencefragment_layout);
        } else {
            getFragmentManager().beginTransaction().replace(android.R.id.content,
                    new PrefsFragment()).commit();
        }
    }

    @TargetApi(11)
    public static class PrefsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.d(TAG, "in PrefsFragment()");
            addPreferencesFromResource(R.xml.settings_preferencefragment_layout);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }

    private SharedPreferences.OnSharedPreferenceChangeListener myPrefsListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    if (key.equals(KEY_OPTOUT)) {
                        GoogleAnalytics.getInstance(getApplicationContext()).setAppOptOut(sharedPreferences.getBoolean(key, false));
                    }
                }
            };

    @Override
    protected void onResume() {
        super.onResume();
        //noinspection ConstantConditions,deprecation
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this.myPrefsListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //noinspection ConstantConditions,deprecation
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this.myPrefsListener);
    }

}
