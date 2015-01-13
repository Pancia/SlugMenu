package com.dayzerostudio.utils.taskers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

public abstract class AbstractAsyncTask extends AsyncTask<String, String, String>{

    public Activity myActivity;
    public FragmentActivity myFragment;
    public ProgressDialog prgDlg = null;
    public Boolean success = false;

    public AbstractAsyncTask(Activity activity) {
        this.myActivity = activity;
    }

    public AbstractAsyncTask(FragmentActivity fragment) {
        this.myFragment = fragment;
    }

    public AbstractAsyncTask(Activity a, FragmentActivity f) {
        this.myActivity = a;
        this.myFragment = f;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... arg0) {
        return null;
    }

    @Override
    protected void onPostExecute(String file_url) {
        super.onPostExecute(file_url);
    }

}
