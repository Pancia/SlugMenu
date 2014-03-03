package com.dayzerostudio.slugguide.slugmenu.application;

import android.app.Application;
import android.os.Build;

import com.dayzerostudio.slugguide.slugmenu.menu.storage.MenuStorage;
import com.dayzerostudio.utils.network.MyConnectivity;
import com.dayzerostudio.utils.storage.shrdprfs.MyShrdPrfs;

public class SlugMenu extends Application {

    public static final Boolean isNewAPI = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;

    @Override
    public void onCreate() {
        super.onCreate();
        MyShrdPrfs.init(this);
        MyGoogleAnalytics.initGA(this);
        MenuStorage.init(this);
        MyConnectivity.init(this);
    }

}
