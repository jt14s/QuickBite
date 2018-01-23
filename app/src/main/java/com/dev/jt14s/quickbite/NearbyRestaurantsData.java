package com.dev.jt14s.quickbite;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

public class NearbyRestaurantsData extends AsyncTask<Object, String, String> {

    private static Context launchContext;
    private String googlePlacesData;
    private String url;

    public NearbyRestaurantsData(Context c) {
        launchContext = c;
    }

    @Override
    protected String doInBackground(Object... objects) {
        url = (String) objects[0];
        URLDownloader urlDownloader = new URLDownloader();
        googlePlacesData = urlDownloader.readURL(url);

        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        DataParser parser = new DataParser();
        List<Restaurant> nearbyRestaurantsList = parser.parse(s);

        if (nearbyRestaurantsList != null) {
            Intent beginListActivity = new Intent(launchContext, RestaurantListActivity.class);
            Bundle args = new Bundle();
            args.putSerializable("nearby_restaurants", (Serializable) nearbyRestaurantsList);
            beginListActivity.putExtra("bundle", args);
            launchContext.startActivity(beginListActivity);
        } else {
            Toast.makeText(launchContext, "No restaurants were found within the selected range", Toast.LENGTH_LONG).show();
        }
    }

}
