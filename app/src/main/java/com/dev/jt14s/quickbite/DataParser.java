package com.dev.jt14s.quickbite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//This class parses an api response and extracts the data sent back
//Restaurant data is saved into a Restaurant class and is sent back to the caller

public class DataParser {

    public List<Restaurant> parse(String jsonData) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;
        String resultStatus = "ZERO_RESULTS";

        try {
            jsonObject = new JSONObject(jsonData);
            resultStatus = jsonObject.getString("status");
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (resultStatus.equals("OK"))
            return getPlaces(jsonArray);
        else
            return null;
    }

    private List<Restaurant> getPlaces(JSONArray jsonArray) {
        List<Restaurant> restaurantList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); ++i) {
            try {
                restaurantList.add(getPlace((JSONObject) jsonArray.get(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return restaurantList;
    }

    private Restaurant getPlace(JSONObject googlePlaceJSON) {
        Restaurant restaurant = new Restaurant();

        try {
            if (!googlePlaceJSON.isNull("name")) {
                restaurant.setName(googlePlaceJSON.getString("name"));
            }
            if (!googlePlaceJSON.isNull("vicinity")) {
                restaurant.setAddress(googlePlaceJSON.getString("vicinity"));
            }
            if (!googlePlaceJSON.isNull("price_level")) {
                restaurant.setPriceLevel(Double.parseDouble(googlePlaceJSON.getString("price_level")));
            }
            if (!googlePlaceJSON.isNull("rating")) {
                restaurant.setRating(Double.parseDouble(googlePlaceJSON.getString("rating")));
            }
            if (!googlePlaceJSON.isNull("open_now")) {
                restaurant.setOpenStatus(googlePlaceJSON.getJSONObject("opening_hours").getString("open_now"));
            }
            restaurant.setLatitude(Double.parseDouble(googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lat")));
            restaurant.setLongitude(Double.parseDouble(googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lng")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return restaurant;
    }
}
