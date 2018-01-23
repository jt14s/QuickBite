package com.dev.jt14s.quickbite;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static int REQUEST_LOCATION = 1;
    private GoogleMap mMap;
    private Restaurant restaurant;
    private SupportMapFragment mapFragment;

    @BindView(R.id.navigation) BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("bundle");
        restaurant = (Restaurant) args.getSerializable("restaurant");

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_route:
                    calculateRoute(getUserLocation());
                    return true;
                case R.id.navigation_invite:
                    inviteFriends();
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(5);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(false);
        LatLng restaurantCoordinates = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());
        mMap.addMarker(new MarkerOptions().position(restaurantCoordinates).title("Marker on restaurant"));

        calculateAndShowMidPoint();
    }

    //centers the camera in between user and location
    private void calculateAndShowMidPoint() {
        LatLng userLocation = getUserLocation();
        LatLng restaurantCoordinates = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());

        if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            mMap.setMyLocationEnabled(true);
            if (userLocation != null) {
                LatLngBounds.Builder bounds = new LatLngBounds.Builder();
                bounds.include(restaurantCoordinates).include(new LatLng(userLocation.latitude, userLocation.longitude));
                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), width, height, 150));

            } else {
                Toast.makeText(this, "Could not get your current location. Try enabling it.", Toast.LENGTH_SHORT).show();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(restaurantCoordinates));
            }
        }
    }

    //returns user's current location
    private LatLng getUserLocation() {
        Location location = null;

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    //opens the installed maps app and routes user there.
    //Can add my own routing logic so as to not open anther app
    private void calculateRoute(LatLng userLocation) {
        String uri = "http://maps.google.com/maps?saddr=" + userLocation.latitude + "," + userLocation.longitude +
                "&daddr=" + restaurant.getLatitude() + "," + restaurant.getLongitude();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    //opens messaging app to allow user to send sms to chosen contacts
    private void inviteFriends() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("sms:"));
        intent.putExtra("sms_body", "Hey! I'm going to eat at " + restaurant.getName() +
                ". The address is " + restaurant.getAddress() + ". Meet me there!");
        startActivity(intent);
    }

}
