package com.example.restaurantrater;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RestaurantDBHelp extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "restaurantRater.db";
    private static final int DATABASE_VERSION = 2;

    private static final String CREATE_TABLE_RESTAURANT = "create table restaurant (_id integer primary key autoincrement," + " restaurantname text not null, " + " streetaddress text, " + " city text, " + "state text," + " zipcode text);";
    private static final String CREATE_TABLE_DISH =
            "create table dish (dishId integer primary key autoincrement, "
                    + "name text, type text, rating real, restaurantId int, FOREIGN KEY(restaurantId) REFERENCES restaurant(_id));";


    public RestaurantDBHelp(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase db){
        db.execSQL("PRAGMA foreign_keys = ON");
        db.execSQL(CREATE_TABLE_RESTAURANT);
        db.execSQL(CREATE_TABLE_DISH);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.w(RestaurantDBHelp.class.getName(), "Upgrading database from version" + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS restaurant");
        db.execSQL("DROP TABLE IF EXISTS dish");
        onCreate(db);
    }
}
