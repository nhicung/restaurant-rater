package com.example.restaurantrater;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class RestaurantDataSource {

    private SQLiteDatabase database;
    private RestaurantDBHelp dbHelper;

    public RestaurantDataSource(Context context){
        dbHelper = new RestaurantDBHelp(context);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertRestaurant(Restaurant r){
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("restaurantname", r.getRestaurantName());
            initialValues.put("streetaddress", r.getStreetAddress());
            initialValues.put("city", r.getCity());
            initialValues.put("state", r.getState());
            initialValues.put("zipcode", r.getZipCode());

            didSucceed = database.insert("restaurant", null, initialValues)>0;
        }
        catch (Exception e){

        }
        return didSucceed;
    }

    public boolean updateRestaurant(Restaurant r){
        boolean didSucceed = false;
        try {
            Long rowID = (long) r.getRestaurantID();
            ContentValues updateValue = new ContentValues();

            updateValue.put("restaurantname", r.getRestaurantName());
            updateValue.put("streetaddress", r.getStreetAddress());
            updateValue.put("city", r.getCity());
            updateValue.put("state", r.getState());
            updateValue.put("zipcode", r.getZipCode());

            didSucceed = database.update("restaurant", updateValue, "_id"+rowID, null) > 0 ;
        }catch (Exception e){

        }
        return didSucceed;
    }

    public boolean insertDish(Dish d) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("name", d.getDishName());
            initialValues.put("type", d.getDishType());
            initialValues.put("rating", d.getRate());
            initialValues.put("restaurantId", d.getRestaurantID());

            didSucceed = database.insert("dish", null, initialValues) > 0;

        }
        catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return didSucceed;
    }

    public boolean updateDish(Dish d) {
        boolean didSucceed = false;
        try {
            long rowid = (long) d.getDishID();
            ContentValues updateValues = new ContentValues();

            updateValues.put("name", d.getDishName());
            updateValues.put("type", d.getDishType());
            updateValues.put("rating", d.getRate());
            updateValues.put("restaurantId", d.getRestaurantID());

            didSucceed = database.update("dish", updateValues, "dishId=" + rowid, null) > 0;
        }
        catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return didSucceed;
    }

    public int getLastRestaurantID(){
        int lastID;
        try {
            String query = "Select MAX(_id) from restaurant ";
            Cursor cursor = database.rawQuery(query,null);
            cursor.moveToFirst();
            lastID = cursor.getInt(0);
            cursor.close();
        } catch (Exception e){
            lastID=-1;
        }
        return  lastID;
    }

    public ArrayList<String> getRestaurantName(){
        ArrayList<String> restaurantNames = new ArrayList<>();

        try {
            String query = "Select restaurantname from restaurant";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                restaurantNames.add(cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e){
            restaurantNames = new ArrayList<String>();
        }
        return restaurantNames;
    }

    public ArrayList<Restaurant> getRestaurants(String sortField, String sortOrder) {
        ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
        try {
            String query = "SELECT * FROM restaurant";
            Cursor cursor = database.rawQuery(query, null);

            Restaurant newRestaurant;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                newRestaurant = new Restaurant();
                newRestaurant.setRestaurantID(cursor.getInt(0));
                newRestaurant.setRestaurantName(cursor.getString(1));
                newRestaurant.setStreetAddress(cursor.getString(2));
                newRestaurant.setCity(cursor.getString(3));
                newRestaurant.setState(cursor.getString(4));
                newRestaurant.setZipCode(cursor.getString(5));
                restaurants.add(newRestaurant);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            restaurants = new ArrayList<Restaurant>();
        }
        return restaurants;
    }

    public ArrayList<Dish> getDishes(int restaurantid) {
        ArrayList<Dish> dishes = new ArrayList<Dish>();
        try {
            String query = "SELECT * FROM dish WHERE dish.restaurantId = " + restaurantid;
            Cursor cursor = database.rawQuery(query, null);

            Dish newDish;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                newDish = new Dish();
                newDish.setDishID(cursor.getInt(0));
                newDish.setDishName(cursor.getString(1));
                newDish.setDishType(cursor.getString(2));
                newDish.setRate(cursor.getFloat(3));
                dishes.add(newDish);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            dishes = new ArrayList<Dish>();
        }
        return dishes;
    }

    public Restaurant getSpecificRestaurant (int restaurantId){
        Restaurant restaurant = new Restaurant();
        String query = "SELECT * FROM restaurant WHERE _id =" + restaurantId;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()){
            restaurant.setRestaurantID(cursor.getInt(0));
            restaurant.setRestaurantName(cursor.getString(1));
            restaurant.setStreetAddress(cursor.getString(2));
            restaurant.setCity(cursor.getString(3));
            restaurant.setState(cursor.getString(4));
            restaurant.setZipCode(cursor.getString(5));

            cursor.close();
        }
        return restaurant;
    }

    public Dish getSpecificDish(int dishId){
        Dish dish = new Dish();
        String query = "SELECT * FROM dish WHERE _id =" + dishId;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()){
            dish.setRestaurantID(cursor.getInt(0));
            dish.setDishName(cursor.getString(1));
            dish.setDishType(cursor.getString(2));
            dish.setRate(cursor.getFloat(3));
            cursor.close();
        }
        return dish;
    }

    public boolean deleteRestaurant(int restaurantId){
        boolean didDelete = false;
        try {
            didDelete = database.delete("restaurant", "_id =" +restaurantId, null)> 0;
        }
        catch (Exception e){

        }
        return didDelete;
    }

    public boolean deleteDish(int dishId){
        boolean didDelete = false;
        try {
            didDelete = database.delete("dish", "dishId =" +dishId, null)> 0;
        }
        catch (Exception e){

        }
        return didDelete;
    }

}
