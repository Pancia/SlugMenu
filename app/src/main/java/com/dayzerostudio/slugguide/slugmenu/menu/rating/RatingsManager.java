package com.dayzerostudio.slugguide.slugmenu.menu.rating;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.dayzerostudio.slugguide.slugmenu.menu.menuobjects.MenuItem;
import com.dayzerostudio.slugguide.slugmenu.menu.rating.database.MyRatingsDB;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;

public class RatingsManager {

    private static String TAG = RatingsManager.class.getSimpleName();
    private MyRatingsDB mydb;

    private static final String slugMenuElixirRatingServerBaseUrl = "http://169.233.58.235:8080";
    private ElixirRatingService service;

    private String android_id;

    public RatingsManager(Context context) {
        this.mydb = new MyRatingsDB(context);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(slugMenuElixirRatingServerBaseUrl)
                .build();
        this.service = restAdapter.create(ElixirRatingService.class);
        android_id = Settings.Secure.getString(context.getContentResolver(),
                            Settings.Secure.ANDROID_ID);
    }

    public void storeRatingsFor(MenuItem menuObj, float rating) {
        MenuItemRating menuItemRating = new MenuItemRating();
        menuItemRating.rating = rating;
        menuItemRating.status = "ok";
        menuItemRating.user   = android_id;
        service.postRatingForItem(menuObj.getName().replace(" ", "_").toLowerCase(), menuItemRating,
                new Callback<MenuItemRating>() {
                    @Override
                    public void success(MenuItemRating menuItemRating, Response response) {
                        Log.i(TAG+"#storeRatingsFor", menuItemRating.toString());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                    }
                });

        this.mydb.addRating(menuObj.getName(), rating);
    }

    public void storeRatingsFor(List<MenuItem> selectedMenuItems, float rating) {
        for (MenuItem item : selectedMenuItems) {
            storeRatingsFor(item, rating);
        }
    }

    public Float getRatingFor(final String item) {
        Float cachedRating = this.mydb.getRatingFor(item);
        if (cachedRating >= 0) {
            return cachedRating;
        }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Float> callable = new Callable<Float>() {
            @Override
            public Float call() {
                return service.getRatingForItem(item.replace(" ", "_").toLowerCase()).rating;
            }
        };
        Future<Float> future = executor.submit(callable);
        executor.shutdown();

        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return -1f;
    }

    public MenuItem getRatingFor(MenuItem menuObj) {
        return new MenuItem( menuObj.getName(), this.getRatingFor(menuObj.getName()) );
    }

    public List<MenuItem> getRatingsFor(List<MenuItem> selectedMenuItems) {
        if (selectedMenuItems == null)
                return null;
        List<MenuItem> myRatingsList = new ArrayList<MenuItem>();
        for (MenuItem menuObj : selectedMenuItems) {
            myRatingsList.add(getRatingFor(menuObj));
        }
        return myRatingsList;
    }

    public void closeDB() {
        this.mydb.closeDB();
    }

    private class MenuItemRating {
        public Float rating;
        public String status;
        public String user;

        @Override
        public String toString() {
            return "MenuItemRating#{"
                    + "rating: "  + rating.toString()
                    + " status: " + status
                    + " user: "   + user
                    + "}";
        }
    }

    interface ElixirRatingService {
        @GET("/ratings/nine/{item}")
        MenuItemRating getRatingForItem(@Path("item") String item);

        @Headers("Content-type: application/json")
        @POST("/ratings/nine/{item}")
        void postRatingForItem(@Path("item") String item, @Body MenuItemRating rating,
                               Callback<MenuItemRating> ignore);
    }

}