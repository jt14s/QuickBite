package com.dev.jt14s.quickbite;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantListActivity extends AppCompatActivity{

    private RecyclerView.Adapter adapter;
    private List<Restaurant> restaurants;
    private List<Restaurant> favorites;

    @BindView(R.id.restaurantCardList) RecyclerView recyclerView;
    /* TODO::Add filter functionality
    @BindView(R.id.filters) BottomNavigationView filters;
    */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);
        ButterKnife.bind(this);

        //TODO::Filter functionality
        //filters.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        receiveTransferredInformation();
        recyclerSetup();
    }

    private void recyclerSetup() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RestaurantListAdapter(restaurants, this);
        recyclerView.setAdapter(adapter);
    }

    private void receiveTransferredInformation() {
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("bundle");
        restaurants = (ArrayList<Restaurant>) args.getSerializable("nearby_restaurants");
    }

    /*TODO:: Update recycler view with filtered restaurants
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.filters_distance:
                    adapter = new RestaurantListAdapter(restaurants, RestaurantListActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                case R.id.filters_favorite:
                    favorites = new ArrayList<>();

                    for (Restaurant restaurant : restaurants)
                        if (restaurant.isFavorited())
                            favorites.add(restaurant);

                    adapter = new RestaurantListAdapter(favorites, RestaurantListActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    return true;

                case R.id.filters_price:
                    return true;
            }
            return false;
        }
    };
    */

}
