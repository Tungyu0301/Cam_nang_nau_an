package com.duan2.camnangamthuc.camnangamthuc.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.duan2.camnangamthuc.camnangamthuc.Interface.ItemClickListerner;
import com.duan2.camnangamthuc.camnangamthuc.R;

public class FoodViewHoder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtFoodView;
    public ImageView imgFoodView;
    private ItemClickListerner itemClickListerner;
    public FoodViewHoder(View itemView) {
        super(itemView);
        txtFoodView = (TextView)itemView.findViewById(R.id.food_name);
        imgFoodView = (ImageView)itemView.findViewById(R.id.food_img);
        itemView.setOnClickListener(this);
    }
    public void setItemListener(ItemClickListerner itemClickListerner){
        this.itemClickListerner = itemClickListerner;
    }
    @Override
    public void onClick(View view) {
        itemClickListerner.onClick(view,getAdapterPosition(),false);
    }
}
