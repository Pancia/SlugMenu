package com.dayzerostudio.utils.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class MyConnectivity {

    private final static String TAG = MyConnectivity.class.getSimpleName();
    private static Context myContext;

    public static void init(Context context) {
        myContext = context;
    }

    public static boolean isOnline() {
        Boolean isOnline = false;
        ConnectivityManager cm = (ConnectivityManager) myContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo[] infos = cm.getAllNetworkInfo();
            if (infos != null) {
                for (NetworkInfo info : infos) {
                    if (info.getState() == NetworkInfo.State.CONNECTED)
                        isOnline = true;
                }
            } else {
                Log.e(TAG, "", new Exception("Failed to getSystemService(CONNECTIVITY_SERVICE)"));
                //todo log with GA
            }
        } else {
            Log.e(TAG, "", new Exception("Failed to getAllNetworkInfo()"));
            //todo log with GA
        }
        return isOnline;
    }



}
