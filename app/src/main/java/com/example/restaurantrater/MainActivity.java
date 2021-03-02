package com.example.restaurantrater;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Restaurant currentRestaurant;
    int restaurantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initListButton();
        initLocationButton();
        iniSaveButton();
        initTextChangedEvent();
        setForEditing();
        initClear();
        initRateDishButton();


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            initRestaurant(extras.getInt("restaurantId"));
        } else {
            currentRestaurant = new Restaurant();
        }
    }


    private void initListButton() {
        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RestaurantListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initLocationButton(){
        ImageButton ibLocation = findViewById(R.id.imageButtonLocation);
        ibLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DishListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void iniSaveButton(){
        Button saveButton = findViewById(R.id.btnSaveRate);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wasSuccessful;
                hideKeyboard();
                RestaurantDataSource ds = new RestaurantDataSource((MainActivity.this));
                try {
                    ds.open();
                    if(currentRestaurant.getRestaurantID() == -1){
                        wasSuccessful = ds.insertRestaurant(currentRestaurant);
                        if (wasSuccessful){
                            int newID = ds.getLastRestaurantID();
                            currentRestaurant.setRestaurantID(newID);
                        }
                    } else {
                        wasSuccessful = ds.updateRestaurant(currentRestaurant);
                    }
                    ds.close();
                }
                catch (Exception e){
                    wasSuccessful = false;
                }
                restaurantId = currentRestaurant.getRestaurantID();

                if (wasSuccessful){
                    Toast.makeText(MainActivity.this, "Restaurant Saved", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Restaurant Save Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initRateDishButton(){
        Button btnRateDish = findViewById(R.id.btnRateList);
        btnRateDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RatingActivity.class);
                Log.w("rest id", String.valueOf(restaurantId));
                intent.putExtra("restaurantId", restaurantId);
                startActivity(intent);
            }
        });
    }
    private void initTextChangedEvent(){
        final EditText etRestaurant = findViewById(R.id.editRestaurant);
        etRestaurant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentRestaurant.setRestaurantName(etRestaurant.getText().toString());
            }
        });

        final EditText etStreet = findViewById(R.id.editStreet);
        etStreet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentRestaurant.setStreetAddress(etStreet.getText().toString());
            }
        });

        final EditText etCity = findViewById(R.id.editCity);
        etCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentRestaurant.setCity(etCity.getText().toString());
            }
        });

        final EditText etState = findViewById(R.id.editState);
        etState.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentRestaurant.setState(etState.getText().toString());
            }
        });

        final EditText etZipcode = findViewById(R.id.editZipcode);
        etZipcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentRestaurant.setZipCode(etZipcode.getText().toString());
            }
        });
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        EditText editRestaurant = findViewById(R.id.editRestaurant);
        imm.hideSoftInputFromWindow(editRestaurant.getWindowToken(), 0);
        EditText editStreetaddress = findViewById(R.id.editStreet);
        imm.hideSoftInputFromWindow(editStreetaddress.getWindowToken(),0);
        EditText editCity = findViewById(R.id.editCity);
        imm.hideSoftInputFromWindow(editCity.getWindowToken(),0);
        EditText editState = findViewById(R.id.editState);
        imm.hideSoftInputFromWindow(editState.getWindowToken(),0);
        EditText editZipcode = findViewById(R.id.editZipcode);
        imm.hideSoftInputFromWindow(editZipcode.getWindowToken(),0);
    }

    private void setForEditing() {
        EditText editRestaurant = findViewById(R.id.editRestaurant);
        EditText editStreetaddress = findViewById(R.id.editStreet);
        EditText editCity = findViewById(R.id.editCity);
        EditText editState = findViewById(R.id.editState);
        EditText editZipCode = findViewById(R.id.editZipcode);

        editRestaurant.setEnabled(true);
        editStreetaddress.setEnabled(true);
        editCity.setEnabled(true);
        editState.setEnabled(true);
        editZipCode.setEnabled(true);

    }
    private void initClear(){
        Button btCancel = findViewById(R.id.btnCancelRate);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edRestaurant = findViewById(R.id.editRestaurant);
                EditText edStreetaddress = findViewById(R.id.editStreet);
                EditText edCity = findViewById(R.id.editCity);
                EditText edState = findViewById(R.id.editState);
                EditText edZipcode = findViewById(R.id.editZipcode);

                edRestaurant.setText("");
                edStreetaddress.setText("");
                edCity.setText("");
                edState.setText("");
                edZipcode.setText("");
            }
        });
    }

    private  void initRestaurant(int id){
        RestaurantDataSource ds = new RestaurantDataSource(MainActivity.this);
        try {
            ds.open();
            currentRestaurant = ds.getSpecificRestaurant(id);
            ds.close();
        }
        catch (Exception e){
            Toast.makeText(this, "Load Contact Failed", Toast.LENGTH_LONG).show();
        }
        EditText editRestaurant = findViewById(R.id.editRestaurant);
        EditText editStreetaddress = findViewById(R.id.editStreet);
        EditText editCity = findViewById(R.id.editCity);
        EditText editState = findViewById(R.id.editState);
        EditText editZipcode = findViewById(R.id.editZipcode);

        editRestaurant.setText(currentRestaurant.getRestaurantName());
        editStreetaddress.setText(currentRestaurant.getStreetAddress());
        editCity.setText(currentRestaurant.getCity());
        editState.setText(currentRestaurant.getState());
        editZipcode.setText(currentRestaurant.getZipCode());
    }
}
