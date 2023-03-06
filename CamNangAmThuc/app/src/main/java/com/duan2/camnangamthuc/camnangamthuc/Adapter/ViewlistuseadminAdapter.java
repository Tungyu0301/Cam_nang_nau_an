package com.duan2.camnangamthuc.camnangamthuc.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.duan2.camnangamthuc.camnangamthuc.Interface.ItemClickListerner;
import com.duan2.camnangamthuc.camnangamthuc.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewlistuseadminAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtViewliseName;
    public TextView txtPhoneusead;
    public TextView txtEmailusead;
    public TextView txtcodeusead;
    public TextView txtdiachiusead;
    public TextView txtngaysinhusead;
    public CircleImageView imgAvatarusead;
    public ImageView deleteoption;
    private ItemClickListerner itemClickListerner;
    public ViewlistuseadminAdapter(View itemView) {
        super(itemView);
        deleteoption = (ImageView) itemView.findViewById(R.id.iconOptiondeleteuse);
        txtViewliseName = (TextView)itemView.findViewById(R.id.txtNameusead);
        txtPhoneusead = (TextView)itemView.findViewById(R.id.txtPhoneusead);
        txtEmailusead = (TextView)itemView.findViewById(R.id.txtEmailusead);
        txtcodeusead = (TextView)itemView.findViewById(R.id.txtcodeusead);
        txtdiachiusead = (TextView)itemView.findViewById(R.id.txtdiachiusead);
        txtngaysinhusead = (TextView)itemView.findViewById(R.id.txtngaysinhusead);
        imgAvatarusead = (CircleImageView)itemView.findViewById(R.id.imgAvatarusead);
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

