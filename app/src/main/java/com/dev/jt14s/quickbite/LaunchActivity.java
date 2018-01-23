package com.dev.jt14s.quickbite;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

public class LaunchActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;

    @BindView(R.id.launchFloatingButton)
    FloatingActionButton launchFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.launchFloatingButton)
    public void displaySearchView() {
        AlertDialog.Builder searchRestaurants = new AlertDialog.Builder(this);
        final View customDialog = getLayoutInflater().inflate(R.layout.radius_picker_fragment, null);
        final Spinner miles = (Spinner) customDialog.findViewById(R.id.launchViewMileSelector);
        searchRestaurants.setView(customDialog);

        searchRestaurants.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                double radius = Double.parseDouble(miles.getSelectedItem().toString()) / 0.00062137;
                String googlePlacesURL = getLocationURL(radius);
                handleBuiltURL(googlePlacesURL);
            }
        });
        searchRestaurants.setNegativeButton("Cancel", null);
        searchRestaurants.show();
    }

    private String getLocationURL(double radius) {
        StringBuilder locationURL = new StringBuilder("");

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(LaunchActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(LaunchActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LaunchActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                return constructURL(radius, locationURL, location);
            } else
                return "no_location";
        }

        return locationURL.toString();
    }

    private String constructURL(double radius, StringBuilder locationURL, Location location) {
        locationURL.append("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=");
        locationURL.append(location.getLatitude() + "," + location.getLongitude() + "&radius=" + radius);
        locationURL.append("&type=restaurant&keyword=restaurant&key=");
        locationURL.append(BuildConfig.GOOGLE_PLACES_API_KEY);
        return locationURL.toString();
    }

    private void handleBuiltURL(String url) {
        switch (url) {
            case "no_location":
                Toast.makeText(this, "Cannot determine location. Try enabling it.", Toast.LENGTH_SHORT).show();
                break;
            case "":
                break;
            default:
                NearbyRestaurantsData restaurantsData = new NearbyRestaurantsData(this);
                restaurantsData.execute(url);
        }
    }

}
