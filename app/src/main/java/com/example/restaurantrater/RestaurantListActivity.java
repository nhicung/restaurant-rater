package com.example.restaurantrater;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

public class RestaurantListActivity extends AppCompatActivity {
    RecyclerView restaurantList;
    RestaurantAdapter restaurantAdapter;
    ArrayList<Restaurant> restaurants;
    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder= (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            int restaurantID = restaurants.get(position).getRestaurantID();
            Intent intent = new Intent(RestaurantListActivity.this, DishListActivity.class);
            intent.putExtra("restaurantId", restaurantID);
            Log.w("restId", String.valueOf(intent));
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        initListButton();
        initLocationButton();
        initDeleteSwitch();

    }
    @Override
    public void onResume() {
        super.onResume();

        String sortBy = getSharedPreferences("MyRestaurantListPreferences", Context.MODE_PRIVATE).getString("sortfield", "restaurantname");
        String sortOrder = getSharedPreferences("MyRestaurantListPreferences", Context.MODE_PRIVATE).getString("sortorder", "ASC");

        RestaurantDataSource ds = new RestaurantDataSource(this);
        try {
            ds.open();
            restaurants = ds.getRestaurants(sortBy, sortOrder);
            ds.close();
            if (restaurants.size() > 0) {
                restaurantList = findViewById(R.id.rvRestaurant);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                restaurantList.setLayoutManager(layoutManager);
                restaurantAdapter = new RestaurantAdapter(restaurants, this);
                restaurantAdapter.setOnItemClickListener(onItemClickListener);
                restaurantList.setAdapter(restaurantAdapter);
            }
            else {
                Intent intent = new Intent(RestaurantListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
        catch (Exception e) {
            Toast.makeText(this, "Error retrieving contacts", Toast.LENGTH_LONG).show();
        }

    }
    private void initListButton(){
        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantListActivity.this, RestaurantListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initLocationButton(){
        ImageButton ibLocation = findViewById(R.id.imageButtonLocation);
        if(ibLocation == null)
            Log.w("ibLocal","its null");
        ibLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantListActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

//    private void initAddRestaurantButton(){
//        Button newRestaurant = findViewById(R.id.buttonAddRestaurants);
//        newRestaurant.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(RestaurantListActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });
//    }

    private void initDeleteSwitch() {
        Switch s = findViewById(R.id.switchDelete);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Boolean status = buttonView.isChecked();
                restaurantAdapter.setDelete(status);
                restaurantAdapter.notifyDataSetChanged();
            }
        });
    }
}
