package com.dayzerostudio.slugguide.slugmenu.server;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.dayzerostudio.slugguide.slugmenu.activities.DisplayMenuActivity;
import com.dayzerostudio.slugguide.slugmenu.menu.menuobjects.JsonMenuObject;
import com.dayzerostudio.slugguide.slugmenu.menu.storage.MenuStorage;
import com.dayzerostudio.utils.network.MyConnectivity;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * @version 2
 * <br> CREATED: 7/11/13
 * <br> LAST MODIFIED 11/23/13 BY: Anthony
 */
public class AsyncHttpRequestManager extends AsyncTask<String, String, String> {

    private Boolean success = false;
    private Activity myActivity;
    //private List<NameValuePair> params = new ArrayList<NameValuePair>();

    //localhost = http://10.0.2.2:8080/ when using GAE
    //remote    = http://slugmenu.appspot.com/
    private String TAG, dh;

    private ProgressDialog prgDlg;
    private boolean hasCallback;
    private MyFutureTask myFutureTask;
    private JsonMenuObject jmo;
    private boolean showProgressDialog = false;

    private int myDtDate = 0;
    private static String slugMenuElixirMenuServerBaseUrl = "https://hidden-mesa-8412.herokuapp.com";

    public AsyncHttpRequestManager(Activity activity) {
        this.myActivity = activity;
        this.TAG = "AHRM#getMenu";
    }

    public AsyncHttpRequestManager setCallback(MyFutureTask listener) {
        this.myFutureTask = listener;
        this.hasCallback = true;
        return this;
    }

    public AsyncHttpRequestManager setShowProgressDialog(boolean show) {
        this.showProgressDialog = show;
        return this;
    }

    public AsyncHttpRequestManager selectDiningHall(String dh) {
        this.dh = dh.toLowerCase();
        return this;
    }

    public AsyncHttpRequestManager setDtDate(int time) {
        if (time < 0 || time > 7)
            return null;
        this.myDtDate = time;
        return this;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (!MyConnectivity.isOnline() && !MenuStorage.hasMenu(dh, myDtDate)) {
            cancel(true);
            ((DisplayMenuActivity)this.myActivity).showGetInternetDialog();
            return;
        }
        if (this.showProgressDialog) {
            this.prgDlg = new ProgressDialog(this.myActivity);
            this.prgDlg.setMessage(getStringForDialog());
            this.prgDlg.setIndeterminate(false);
            this.prgDlg.setCancelable(true);
            this.prgDlg.show();
        }
    }

    class MyErrorHandler implements ErrorHandler {
        @Override
        public Throwable handleError(RetrofitError cause) {
            Response r = cause.getResponse();
            if (r != null && r.getStatus() != 200) {
                return new Exception(cause);
            }
            return cause;
        }
    }
    interface ElixirMenuService {
        @GET("/menu/{dh}/{dtdate}")
        JsonMenuObject getMenu(@Path("dh") String dh, @Path("dtdate") String dtdate) throws Exception;
    }
    @Override
    protected String doInBackground(String... arg0) {
        if (isCancelled())
            return null;
        JsonMenuObject json;
        if (!MenuStorage.hasMenu(dh, myDtDate)) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(slugMenuElixirMenuServerBaseUrl)
                  //.setLogLevel(RestAdapter.LogLevel.BASIC)
                    .setErrorHandler(new MyErrorHandler())
                    .build();
            ElixirMenuService service = restAdapter.create(ElixirMenuService.class);
            try {
                json = service.getMenu(dh, String.valueOf(myDtDate));
                Log.e(ElixirMenuService.class.getSimpleName(), json.toString());
            } catch (Exception e) {
                e.printStackTrace();
                ((DisplayMenuActivity)myActivity).showGetInternetDialog();
                this.success = false;
                this.jmo = new JsonMenuObject();
                this.jmo.dh = dh;
                return null;
            }
            json.dh = dh;
        } else {
            json = MenuStorage.getJsonMenuObject(dh, myDtDate);
        }
        if (json == null)
            return null;

        MenuStorage.saveMenu(dh, json, myDtDate);
        if (json.wasSuccessful()) {
            this.success = true;
            this.jmo = json;
        } else {
            this.success = false;
            this.jmo = new JsonMenuObject();
            this.jmo.dh = dh;
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String file_url) {
        super.onPostExecute(file_url);
        if (this.hasCallback)
            this.myFutureTask.onTaskCompleted(this.jmo, this.success);
        if (this.showProgressDialog)
            this.prgDlg.dismiss();
    }

    private String getStringForDialog() {
        if (this.TAG.equals("AHRM#getMenu")) {
            return ("Getting Menu...");
        } else {
            return ("INCORRECT TASK, WHAT HAVE YOU DONE!!!...");
        }
    }

}
