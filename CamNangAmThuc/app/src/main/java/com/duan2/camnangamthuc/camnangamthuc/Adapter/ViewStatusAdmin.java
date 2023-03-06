package com.duan2.camnangamthuc.camnangamthuc.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duan2.camnangamthuc.camnangamthuc.Interface.ItemClickListerner;
import com.duan2.camnangamthuc.camnangamthuc.Model.Common;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewStatusAdmin  extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtnamefoodstatusadmin;
    public TextView txtnamestatusadmin;
    public TextView txtngaydangstatusadmin;
    public TextView likecountadmin;
    public ImageView imageviewstatusadmin;
    public ImageView likefoodstatusadmin;
    public ImageView commentfoodstatusadmin;
    public ImageView deletestatusadmin;
    public CircleImageView imageusestatusadmin;
    private ItemClickListerner itemClickListerner;
    FirebaseDatabase database;
    DatabaseReference congdonglist;
    public ViewStatusAdmin(View itemView) {
        super(itemView);
        database = FirebaseDatabase.getInstance();
        //Bọc dữ liệu Json
        congdonglist = database.getReference("Communitys");
        txtnamefoodstatusadmin = (TextView)itemView.findViewById(R.id.txtnamefoodstatusadmin);
        txtnamestatusadmin = (TextView)itemView.findViewById(R.id.txtnamestatusadmin);
        txtngaydangstatusadmin = (TextView)itemView.findViewById(R.id.txtngaydangstatusadmin);
        likecountadmin = (TextView)itemView.findViewById(R.id.likecountadmin);
        imageviewstatusadmin = (ImageView)itemView.findViewById(R.id.imageviewstatusadmin);
        likefoodstatusadmin = (ImageView)itemView.findViewById(R.id.likefoodstatusadmin);
        commentfoodstatusadmin = (ImageView)itemView.findViewById(R.id.commentfoodstatusadmin);
        deletestatusadmin = (ImageView)itemView.findViewById(R.id.deletestatusadmin);
        imageusestatusadmin = (CircleImageView) itemView.findViewById(R.id.imageusestatusadmin);
        itemView.setOnClickListener(this);
    }
    public void setItemListener(ItemClickListerner itemClickListerner){
        this.itemClickListerner = itemClickListerner;
    }
    @Override
    public void onClick(View view) {
        itemClickListerner.onClick(view,getAdapterPosition(),false);
    }
    public void setColorLike(final String keylike){
        congdonglist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(keylike).hasChild(Common.userten.getId())){
                    likefoodstatusadmin.setImageResource(R.drawable.ic_like_food_check);
                }else {
                    likefoodstatusadmin.setImageResource(R.drawable.ic_like_food);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
