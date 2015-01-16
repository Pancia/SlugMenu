package com.dayzerostudio.slugguide.slugmenu.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.dayzerostudio.slugguide.slugmenu.MySlidingPaneLayout;
import com.dayzerostudio.slugguide.slugmenu.R;
import com.dayzerostudio.slugguide.slugmenu.application.SlugMenu;
import com.dayzerostudio.slugguide.slugmenu.menu.menuobjects.Meal;
import com.dayzerostudio.slugguide.slugmenu.menu.rating.notifs.RatingsDialog;
import com.dayzerostudio.slugguide.slugmenu.menu.viewpager.displaymenu.BaseMenuFragment;
import com.dayzerostudio.slugguide.slugmenu.menu.viewpager.displaymenu.DisplayMenuPagerAdapter;
import com.dayzerostudio.utils.dialogs.FeedBackDialog;
import com.dayzerostudio.utils.dialogs.GetInternetDialog;
import com.dayzerostudio.utils.storage.shrdprfs.MyShrdPrfs;

public class DisplayMenuActivity extends ActionBarActivity {

    private final static String TAG = DisplayMenuActivity.class.getSimpleName();

    private ActionBar myActionBar;
    private MySlidingPaneLayout myPaneLayout;
    private boolean isShowingDiningHalls = false;

    public DiningHallsListAdapter getDiningHallsListAdapter() {
        return ((DiningHallsListAdapter)((DiningHallsListFragment)getSupportFragmentManager()
                .findFragmentById(R.id.displaymenu_masterfragment)).getListView().getAdapter());
    }

    private ViewPager getDisplayMenuViewPager() {
        if (getSupportFragmentManager()
                .findFragmentById(R.id.displaymenu_detailfragment).getTag()
                .equals("DisplayMenuFragment")) {
            return ((DisplayMenuFragment)getSupportFragmentManager()
                    .findFragmentById(R.id.displaymenu_detailfragment)).myViewPager;
        } else {
            return null;
        }
    }

    private boolean isShowingGetInternetDialog;
    public void setIsShowingGetInternetDialog(boolean isShowingGetInternetDialog) {
        this.isShowingGetInternetDialog = isShowingGetInternetDialog;
    }
    public void showGetInternetDialog() {
        if (!this.isShowingGetInternetDialog) {
            setIsShowingGetInternetDialog(true);
            GetInternetDialog.newInstance().show(getSupportFragmentManager());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_displaymenu);

        myActionBar = getSupportActionBar();

        myPaneLayout = (MySlidingPaneLayout) findViewById(R.id.displaymenu_container);
        myPaneLayout.setSliderFadeColor(Color.TRANSPARENT);

        if (savedInstanceState == null) {
            myPaneLayout.openPane();
            isShowingDiningHalls = true;
        } else {
            isShowingDiningHalls = savedInstanceState.getBoolean("IS_SHOWING_DININGHALLS");
            if (isShowingDiningHalls) {
                myPaneLayout.openPane();
                myActionBar.setHomeButtonEnabled(false);
                myActionBar.setDisplayHomeAsUpEnabled(false);
            } else {
                myPaneLayout.closePane();
                myActionBar.setHomeButtonEnabled(true);
                myActionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        myPaneLayout.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {}

            @Override
            public void onPanelOpened(View view) {
                getSupportFragmentManager().findFragmentById(R.id.displaymenu_masterfragment).setHasOptionsMenu(true);
                getSupportFragmentManager().findFragmentById(R.id.displaymenu_detailfragment).setHasOptionsMenu(false);
                myActionBar.setHomeButtonEnabled(false);
                myActionBar.setDisplayHomeAsUpEnabled(false);
                isShowingDiningHalls = true;
            }

            @Override
            public void onPanelClosed(View view) {
                getSupportFragmentManager().findFragmentById(R.id.displaymenu_masterfragment).setHasOptionsMenu(false);
                getSupportFragmentManager().findFragmentById(R.id.displaymenu_detailfragment).setHasOptionsMenu(true);
                myActionBar.setHomeButtonEnabled(true);
                myActionBar.setDisplayHomeAsUpEnabled(true);
                isShowingDiningHalls = false;
            }
        });

        if (savedInstanceState == null) {
            //todo change/upgrade to most used
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.displaymenu_detailfragment,
                            new DisplayMenuFragment(), "DisplayMenuFragment")
                    .commit();
        } else {
            myActionBar.setSubtitle(savedInstanceState.getString("DH_NAME"));
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager()
                .findFragmentById(R.id.displaymenu_detailfragment).getTag()
                .equals("MyWebViewFragment")) {
            MyWebViewFragment webFragment = ((MyWebViewFragment)getSupportFragmentManager().findFragmentByTag("MyWebViewFragment"));
            WebView wv = webFragment.myWebView;
            assert wv != null;
            if (wv.canGoBack()) {
                wv.goBack();
            } else {
                onDisplayMenu(webFragment.getDhName().split("-")[0]);
            }
        }
        else if ( !myPaneLayout.isOpen() )
            myPaneLayout.openPane();
        else
            super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (MyShrdPrfs.myShrdPrfs.getBoolean("isFirstTimeUser", true)) {
            MyShrdPrfs.saveObject("isFirstTimeUser", false);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (SlugMenu.isNewAPI)
            inflater.inflate(R.menu.activity_dininghalls, menu);
        else
            inflater.inflate(R.menu.activity_dininghalls_pre11, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!SlugMenu.isNewAPI) {
            if (item.getItemId() == R.id.action_rate) {
                if (!getSupportFragmentManager()
                        .findFragmentById(R.id.displaymenu_detailfragment).getTag()
                        .equals("DisplayMenuFragment")) {
                    BaseMenuFragment bmf =
                            ((DisplayMenuPagerAdapter) getDisplayMenuViewPager().getAdapter())
                                    .fragments.get(getDisplayMenuViewPager().getCurrentItem());
                    new RatingsDialog(bmf.getListAdapter(), bmf.getSelectedMenuItems())
                            .show(getSupportFragmentManager());
                }
            }
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                myPaneLayout.openPane();
                return true;
            case R.id.action_webview:
                if (myActionBar.getSubtitle() == null)
                    return false; //todo: either ask to select dh or show "all"
                if (!getSupportFragmentManager()
                        .findFragmentById(R.id.displaymenu_detailfragment).getTag()
                        .equals("MyWebViewFragment")) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.displaymenu_detailfragment,
                                    new MyWebViewFragment(myActionBar.getSubtitle().toString()),
                                    "MyWebViewFragment")
                            .commit();
                }
                return true;
            case R.id.action_feedback:
                FeedBackDialog.newInstance().show(getSupportFragmentManager());
                return true;
            case R.id.action_settings:
                startActivity(new Intent(DisplayMenuActivity.this, MySettingsActivity.class));
                return true;
            case R.id.action_reset:
                MyShrdPrfs.reset();
                ((DiningHallsListAdapter)((DiningHallsListFragment)getSupportFragmentManager()
                        .findFragmentById(R.id.displaymenu_masterfragment))
                        .getListView().getAdapter()).notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onDisplayMenu(String dhName) {
        if (dhName != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.displaymenu_detailfragment,
                            new DisplayMenuFragment(dhName), "DisplayMenuFragment")
                    .commit();
            myActionBar.setSubtitle(Meal.getDhName(dhName));
            myPaneLayout.closePane();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (myActionBar.getSubtitle()!=null) {
            outState.putString("DH_NAME", myActionBar.getSubtitle().toString());
        }
        outState.putBoolean("IS_SHOWING_DININGHALLS", isShowingDiningHalls);
        super.onSaveInstanceState(outState);
    }

}