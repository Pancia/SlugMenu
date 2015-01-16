package com.dayzerostudio.slugguide.slugmenu.application;

import android.app.Application;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.dayzerostudio.slugguide.slugmenu.menu.storage.MenuStorage;
import com.dayzerostudio.utils.network.MyConnectivity;
import com.dayzerostudio.utils.storage.shrdprfs.MyShrdPrfs;

public class SlugMenu extends Application {

    private static final String TAG = SlugMenu.class.getSimpleName();
    public static final Boolean isNewAPI = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;

    @Override
    public void onCreate() {
        super.onCreate();
        MyShrdPrfs.init(this);
        MenuStorage.init(this);
        MyConnectivity.init(this);

        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.e(TAG + "#android_id", android_id);
    }

}
