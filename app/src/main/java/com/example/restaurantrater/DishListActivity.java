package com.example.restaurantrater;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DishListActivity extends AppCompatActivity {

    RecyclerView dishList;
    DishAdapter dishAdapter;
    int restaurantId;

    ArrayList<Dish> dishes;
    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder= (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            int dishID = dishes.get(position).getDishID();
            String dishName = dishes.get(position).getDishName();
            String dishType = dishes.get(position).getDishType();
            Float ratingBar = dishes.get(position).getRate();
            Intent intent = new Intent(DishListActivity.this, RatingActivity.class);
            intent.putExtra("dishID", dishID);
            intent.putExtra("dishName", dishName);
            intent.putExtra("dishType", dishType);
            intent.putExtra("rating", ratingBar);
            Log.w("rating", String.valueOf(ratingBar));
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_list);

        Bundle extras = getIntent().getExtras();
        restaurantId = -1;
        if(extras != null){
            restaurantId = extras.getInt("restaurantId");
        }

        initAddDishButton();
        initDeleteSwitch();
        initLocationButton();
        initListButton();

    }
    @Override
    public void onResume() {
        super.onResume();

        String sortBy = getSharedPreferences("MyDishListPreferences", Context.MODE_PRIVATE).getString("sortfield", "dishname");
        String sortOrder = getSharedPreferences("MyDishListPreferences", Context.MODE_PRIVATE).getString("sortorder", "ASC");

        RestaurantDataSource ds = new RestaurantDataSource(this);
        try {


            ds.open();
            dishes = ds.getDishes(restaurantId);
            ds.close();
                dishList = findViewById(R.id.rvDishRate);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                dishList.setLayoutManager(layoutManager);
                dishAdapter = new DishAdapter(dishes, this);
                dishAdapter.setOnItemClickListener(onItemClickListener);
                dishList.setAdapter(dishAdapter);

        }
        catch (Exception e) {
            Toast.makeText(this, "Error retrieving contacts", Toast.LENGTH_LONG).show();
        }

    }
    private void initListButton(){
        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DishListActivity.this, RestaurantListActivity.class);
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
                Intent intent = new Intent(DishListActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initAddDishButton(){
        Button newDish = findViewById(R.id.buttonAddDish);
        newDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DishListActivity.this, RatingActivity.class);
                intent.putExtra("restaurantId", restaurantId);
                startActivity(intent);
            }
        });
    }

    private void initDeleteSwitch() {
        Switch s = findViewById(R.id.switchDelete);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Boolean status = buttonView.isChecked();
                dishAdapter.setDelete(status);
                dishAdapter.notifyDataSetChanged();
            }
        });
    }
}
