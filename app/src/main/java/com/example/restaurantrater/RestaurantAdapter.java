package com.example.restaurantrater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Toast;

import java.util.ArrayList;

public class RestaurantAdapter extends RecyclerView.Adapter {
    private ArrayList<Restaurant> restaurantData;
    private View.OnClickListener mOnItemClickListener;
    private boolean isDeleting;
    private Context parentContext;

    public class RestaurantViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewRestaurant;
        public TextView textStreet;
        public TextView textCity;
        public TextView textState;
        public TextView textZipcode;
        public Button deleteButton;
        public RestaurantViewHolder(@NonNull View itemView){
            super(itemView);
            textViewRestaurant = itemView.findViewById(R.id.textRestaurant);
            textStreet = itemView.findViewById(R.id.textStreet);
            textCity = itemView.findViewById(R.id.textCity);
            textState = itemView.findViewById(R.id.textState);
            textZipcode = itemView.findViewById(R.id.textZipcode);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }


        public TextView getStreetTextView(){
            return textStreet;
        }

        public TextView getCityTextView(){
            return textCity;
        }

        public TextView getStateTextView(){
            return textState;
        }

        public TextView getZipcodeTextView(){
            return textZipcode;
        }

        public Button getDeleteButton(){
            return deleteButton;
        }

        public TextView getRestaurantTextView(){
            return textViewRestaurant;
        }
    }

    public RestaurantAdapter(ArrayList<Restaurant> arrayList, Context context){
        restaurantData = arrayList;
        parentContext = context;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener){
        mOnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new RestaurantViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        RestaurantViewHolder cvh = (RestaurantViewHolder) holder;
        cvh.getRestaurantTextView().setText(restaurantData.get(position).getRestaurantName());
        cvh.getStreetTextView().setText(restaurantData.get(position).getStreetAddress());
        cvh.getCityTextView().setText(restaurantData.get(position).getCity());
        cvh.getStateTextView().setText(restaurantData.get(position).getState());
        cvh.getZipcodeTextView().setText(restaurantData.get(position).getZipCode());

        if (isDeleting){
            cvh.getDeleteButton().setVisibility(View.VISIBLE);
            cvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(position);
                }
            });
        } else {
            cvh.getDeleteButton().setVisibility(View.INVISIBLE);
        }

    }

    public void setDelete (boolean b){
        isDeleting=b;
    }

    public void deleteItem (int position){
        Restaurant restaurant = restaurantData.get(position);
        RestaurantDataSource ds = new RestaurantDataSource(parentContext);
        try {
            ds.open();
            boolean didDelete = ds.deleteRestaurant(restaurant.getRestaurantID());
            ds.close();
            if(didDelete){
                restaurantData.remove(position);
                notifyDataSetChanged();
            } else {
                Toast.makeText(parentContext, "Delete Failed" , Toast.LENGTH_LONG).show();
            }
        } catch (Exception e){
            Toast.makeText(parentContext, "Delete Failed" , Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return restaurantData.size();
    }
}
