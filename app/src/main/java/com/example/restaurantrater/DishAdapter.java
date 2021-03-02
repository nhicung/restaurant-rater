package com.example.restaurantrater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import java.util.ArrayList;

public class DishAdapter extends RecyclerView.Adapter {
    private ArrayList<Dish> dishData;
    private View.OnClickListener mOnItemClickListener;
    private boolean isDeleting;
    private Context parentContext;

    public class DishViewHolder extends RecyclerView.ViewHolder{
        public TextView textDish;
        public TextView textType;
        public TextView ratingBar;
        public Button deleteButton;
        public DishViewHolder(@NonNull View itView){
            super(itView);
            textDish = itView.findViewById(R.id.textDishName);
            textType = itView.findViewById(R.id.textType);
            ratingBar = itView.findViewById(R.id.textRate);
            deleteButton = itView.findViewById(R.id.buttonDelete);
            itView.setTag(this);
            itView.setOnClickListener(mOnItemClickListener);
        }

        public TextView getDish(){
            return textDish;
        }

        public TextView getType(){
            return textType;
        }

        public TextView getRate(){
            return ratingBar;
        }

        public Button getDeleteButton(){
            return deleteButton;
        }
    }

    public DishAdapter(ArrayList<Dish> arrayList, Context context){
        dishData = arrayList;
        parentContext = context;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener){
        mOnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_dish,parent,false);
        return new DishViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        DishViewHolder cvh = (DishViewHolder) holder;

        cvh.getDish().setText(dishData.get(position).getDishName());
        cvh.getType().setText(dishData.get(position).getDishType());
        cvh.getRate().setText(String.valueOf(dishData.get(position).getRate()));

//        if (isDeleting){
//            cvh.getDeleteButton().setVisibility(View.VISIBLE);
//            cvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    deleteItem(position);
//                }
//            });
//        } else {
//            cvh.getDeleteButton().setVisibility(View.INVISIBLE);
//        }

    }

    public void setDelete (boolean b){
        isDeleting=b;
    }

    public void deleteItem (int position){
        Dish dish = dishData.get(position);
        RestaurantDataSource ds = new RestaurantDataSource(parentContext);
        try {
            ds.open();
            boolean didDelete = ds.deleteDish(dish.getDishID());
            ds.close();
            if(didDelete){
                dishData.remove(position);
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
        return dishData.size();
    }
}
