package com.duan2.camnangamthuc.camnangamthuc.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.duan2.camnangamthuc.camnangamthuc.Interface.ItemClickListerner;
import com.duan2.camnangamthuc.camnangamthuc.R;

/**
 * Created by PC on 10/13/2018.
 */

public class HomeViewHoderl extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtMenụView;
    public ImageView imgMenuView;
    private ItemClickListerner itemClickListerner;
    public HomeViewHoderl(View itemView) {
        super(itemView);
        txtMenụView = (TextView)itemView.findViewById(R.id.menu_name);
        imgMenuView = (ImageView)itemView.findViewById(R.id.menu_img);
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
