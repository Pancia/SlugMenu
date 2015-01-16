package com.dayzerostudio.slugguide.slugmenu.activities;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.dayzerostudio.slugguide.slugmenu.R;

public class MySettingsActivity extends PreferenceActivity {

    private final static String TAG = MySettingsActivity.class.getSimpleName();

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
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
