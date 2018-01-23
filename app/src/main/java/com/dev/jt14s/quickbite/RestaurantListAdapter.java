package com.dev.jt14s.quickbite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {

    private List<Restaurant> listItems;
    private Context context;

    public RestaurantListAdapter(List<Restaurant> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Restaurant restaurant = listItems.get(position);
        holder.restaurantName.setText(restaurant.getName());
        holder.restaurantAddress.setText(restaurant.getAddress());
        holder.restaurantPrice.setText(String.valueOf(restaurant.getPriceLevel()));
        holder.restaurantRating.setText(String.valueOf(restaurant.getRating()));
    }

    public void addItems(ArrayList<Restaurant> updatedItems) {
        this.listItems = updatedItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener{

        @BindView(R.id.restaurantCardName) TextView restaurantName;
        @BindView(R.id.restaurantCardAddress) TextView restaurantAddress;
        @BindView(R.id.restaurantCardRating) TextView restaurantRating;
        @BindView(R.id.restaurantCardPrice) TextView restaurantPrice;
        @BindView(R.id.restaurantCardFavorite) ToggleButton restaurantFavoriteStatus;
        @BindView(R.id.restaurantCardMapCourse) ImageButton restaurantMapCourse;
        
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            restaurantFavoriteStatus.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) this);
        }

        @OnClick(R.id.restaurantCardFavorite)
        public void checkIsClicked() {
            //TODO::Add a way to save favorites between sessions


        }

        @OnClick(R.id.restaurantCardMapCourse)
        public void openMapToRestaurant() {
            Intent intent = new Intent(context, MapActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("restaurant", listItems.get(getAdapterPosition()));
            intent.putExtra("bundle", bundle);
            context.startActivity(intent);
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

        }
    }
    
}
