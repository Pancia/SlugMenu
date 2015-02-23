package com.dayzerostudio.slugguide.slugmenu.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dayzerostudio.slugguide.slugmenu.R;

/**
* Created by Pancia on 2/21/14.
*/
public class MyWebViewFragment extends Fragment {

    private static final String TAG = MyWebViewFragment.class.getSimpleName();

    String dhName;

    WebView myWebView;

    public MyWebViewFragment() {}

    @Override
    public void setArguments(Bundle args) {
        dhName = args.getString("DH_NAME");
        super.setArguments(args);
    }

    public String getDhName() {
        String name = dhName;
        if (getString(R.string.dzs_sm_dh_cowell).equals(name)) {
            return "cowell-stevenson";
        } else if (getString(R.string.dzs_sm_dh_crown).equals(name)) {
            return "crown-merrill";
        } else if (getString(R.string.dzs_sm_dh_eight).equals(name)) {
            return "oakes-eight";
        } else if (getString(R.string.dzs_sm_dh_nine).equals(name)) {
            return "nine-ten";
        } else if (getString(R.string.dzs_sm_dh_porter).equals(name)) {
            return "porter-kresge";
        } else {
            throw new IllegalArgumentException(name + "is not a valid dhname.");
        }
    }

    public String getDhName(String name) {
        if (getString(R.string.dzs_sm_dh_cowell).equals(name)) {
            return "cowell-stevenson";
        } else if (getString(R.string.dzs_sm_dh_crown).equals(name)) {
            return "crown-merrill";
        } else if (getString(R.string.dzs_sm_dh_eight).equals(name)) {
            return "oakes-eight";
        } else if (getString(R.string.dzs_sm_dh_nine).equals(name)) {
            return "nine-ten";
        } else if (getString(R.string.dzs_sm_dh_porter).equals(name)) {
            return "porter-kresge";
        } else {
            throw new IllegalArgumentException(name + "is not a valid dhname.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_webview, container, false);
        assert rootView != null;
        myWebView = (WebView) rootView.findViewById(R.id.webview);
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.getSettings().setJavaScriptEnabled(true);//todo do we want/need js?
        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);
        if (savedInstanceState != null) {
            dhName = savedInstanceState.getString("DH_NAME");
        }
        if (savedInstanceState == null) {
            myWebView.loadUrl("http://housing.ucsc.edu/dining/"+ getDhName(dhName) +".html");
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("DH_NAME", dhName);
        super.onSaveInstanceState(outState);
        myWebView.saveState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        myWebView.restoreState(savedInstanceState);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if ("housing.ucsc.edu".equals(Uri.parse(url).getHost())) {
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }

}
