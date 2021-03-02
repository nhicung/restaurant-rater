package com.example.restaurantrater;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;
import android.util.Log;

public class RatingActivity extends AppCompatActivity {

    private Dish currentDish;
    int restaurantId;
    String dishName;
    String dishType;
    Float ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_rate);

        currentDish = new Dish();
        Bundle extras = getIntent().getExtras();
        Log.w("extra", String.valueOf(extras));
        restaurantId = -1;
        if(extras != null){
            restaurantId = extras.getInt("restaurantId");
        }

        Intent intent = getIntent();
        dishName = intent.getStringExtra("dishName");
        dishType = intent.getStringExtra("dishType");
        ratingBar = (Float) intent.getFloatExtra("rating", 0);

        initLocationButton();
        initListButton();
        initSaveButton();
//        setForEditing();
        initTextChangedEvent();
        initUpdate();
    }

    private void initLocationButton(){
        ImageButton ibLocation = findViewById(R.id.imageButtonLocation);
        ibLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(RatingActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initListButton() {
        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(RatingActivity.this, RestaurantListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initSaveButton() {
        Button button = findViewById(R.id.btnSaveRate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dish dish = new Dish();
//                dish.setRestaurantID(getSharedPreferences("currentRestaurant", MODE_PRIVATE).getInt("restaurantId", -1));

                Log.w("restaurant ID", String.valueOf(restaurantId));
                EditText editName = findViewById(R.id.editDish);
                EditText editType = findViewById(R.id.editType);
                RatingBar ratingBar = findViewById(R.id.ratingBar);

                editName.setEnabled(true);
                editType.setEnabled(true);
                ratingBar.setEnabled(true);

                dish.setRestaurantID(restaurantId);
                dish.setDishName(editName.getText().toString());
                dish.setDishType(editType.getText().toString());
                dish.setRate(ratingBar.getRating());

                RestaurantDataSource ds = new RestaurantDataSource(RatingActivity.this);
                boolean wasSuccessful;
                try {
                    ds.open();
                    if (dish.getDishID() == -1) {
                        wasSuccessful = ds.insertDish(dish);
                    }
                    else {
                        wasSuccessful = ds.updateDish(dish);
                    }
                    ds.close();

                }
                catch (Exception e) {
                    wasSuccessful = false;
                    ds.close();
                }

                if (wasSuccessful) {
                    Toast.makeText(RatingActivity.this, "Dish Saved!", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(RatingActivity.this, "Dish Save Failed!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void initUpdate(){
        EditText etdishName = findViewById(R.id.editDish);
        etdishName.setText(dishName);
        EditText etdishType = findViewById(R.id.editType);
        etdishType.setText(dishType);
        RatingBar rating = findViewById(R.id.ratingBar);
        rating.setRating(ratingBar);
    }

    private void setForEditing() {
        EditText editDishName = findViewById(R.id.editDish);
        EditText editType = findViewById(R.id.editType);
        RatingBar editRating = findViewById(R.id.ratingBar);

        editDishName.setEnabled(true);
        editType.setEnabled(true);
        editRating.setEnabled(true);

    }

    private void initTextChangedEvent(){
        final EditText etDish = findViewById(R.id.editDish);
        etDish.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentDish.setDishName(etDish.getText().toString());
            }
        });
        final EditText etType = findViewById(R.id.editDish);
        etType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentDish.setDishName(etType.getText().toString());
            }
        });

//        final RatingBar etBar = findViewById(R.id.ratingBar);
//        Button saveButton =  findViewById(R.id.btnSaveRate);
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }
//
//    private  void initDish(int id){
//        RestaurantDataSource ds = new RestaurantDataSource(RatingActivity.this);
//        try {
//            ds.open();
//            currentDish = ds.getSpecificDish(id);
//            ds.close();
//        }
//        catch (Exception e){
//            Toast.makeText(this, "Load Contact Failed", Toast.LENGTH_LONG).show();
//        }
//        EditText editDish = findViewById(R.id.editRestaurant);
//        EditText editType = findViewById(R.id.editStreet);
//        EditText editRate = findViewById(R.id.editCity);
//
//
//        editDish.setText(currentDish.getDishName());
//        editType.setText(currentDish.getDishType());
//        editRate.setText(String.valueOf(currentDish.getRate()));
//    }
}

