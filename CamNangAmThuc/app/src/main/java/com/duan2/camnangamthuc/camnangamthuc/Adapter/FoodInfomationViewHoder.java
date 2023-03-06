package com.duan2.camnangamthuc.camnangamthuc.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.duan2.camnangamthuc.camnangamthuc.Interface.ItemClickListerner;
import com.duan2.camnangamthuc.camnangamthuc.R;

public class FoodInfomationViewHoder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtInfomationViewName;
    public TextView txtInfomationViewInfo;
    public ImageView imgFoodInfomationView;
    private ItemClickListerner itemClickListerner;
    public FoodInfomationViewHoder(View itemView) {
        super(itemView);
        txtInfomationViewName = (TextView)itemView.findViewById(R.id.txttitle);
        txtInfomationViewInfo = (TextView)itemView.findViewById(R.id.txtinfo);
        imgFoodInfomationView = (ImageView)itemView.findViewById(R.id.imghinhanh);
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
