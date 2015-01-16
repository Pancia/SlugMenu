package com.dayzerostudio.utils.network;

import android.accounts.NetworkErrorException;
import android.util.Log;

import com.dayzerostudio.slugguide.slugmenu.menu.menuobjects.JsonMenuObject;
import com.dayzerostudio.slugguide.slugmenu.server.MenuParser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class JsonHttpRequest {

    private static final String TAG = JsonHttpRequest.class.getSimpleName();
    private static final int GET = 0;
    private static final int POST = 1;
    private final String url;
    private final int dtdate;
    private String dh;

    public JsonHttpRequest(String url) {
        this.url = url;
        this.dtdate = -1;//invalid
    }

    public JsonHttpRequest(String dh, int dtdate) {
        this.url = MenuParser.getUrl(dh, dtdate);
        this.dh = dh;
        this.dtdate = dtdate;
    }

    public JsonMenuObject getJsonMenu() throws NetworkErrorException {
        return MenuParser.parseString(
                makeJsonHttpRequest(JsonHttpRequest.GET),
                this.dh, this.dtdate);
    }

    public String getJsonRating(int method) {
        return makeJsonHttpRequest(method);
    }

    public String makeJsonHttpRequest(int method) {
        return getStringFromHttp(makeHttpRequest(method));
    }

    public InputStream makeHttpRequest(int method) {
        Log.i(TAG, this.url);
        InputStream inpStr = null;
        try {
            if (method == POST) {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(this.url);
                //httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                inpStr = httpEntity.getContent();
            } else if (method == GET) {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                //this.url += "?" + URLEncodedUtils.format(params, "utf-8");
                HttpGet httpGet = new HttpGet(this.url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                inpStr = httpEntity.getContent();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inpStr;
    }

    public String getStringFromHttp(InputStream inpStr) {
        String jsonString = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inpStr, "iso-8859-1"), 8);
            StringBuilder strBldr = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                strBldr.append(line).append("\n");
            }
            inpStr.close();
            jsonString = strBldr.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonString;
    }

}