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

public class ViewStatusHoder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtnamefoodstatus;
    public TextView txtnamestatus;
    public TextView txtngaydangstatus;
    public TextView statusview;
    public TextView likecountstatus;
    public ImageView imageviewstatus;
    public ImageView likefoodstatus;
    public ImageView commentfoodstatus;
    public ImageView editstatus;
    public ImageView deletestatus;
    public CircleImageView imageusestatus;
    private ItemClickListerner itemClickListerner;
    FirebaseDatabase database;
    DatabaseReference congdonglist;
    public ViewStatusHoder(View itemView) {
        super(itemView);
        database = FirebaseDatabase.getInstance();
        //Bọc dữ liệu Json
        congdonglist = database.getReference("Communitys");
        txtnamefoodstatus = (TextView)itemView.findViewById(R.id.txtnamefoodstatus);
        txtnamestatus = (TextView)itemView.findViewById(R.id.txtnamestatus);
        txtngaydangstatus = (TextView)itemView.findViewById(R.id.txtngaydangstatus);
        statusview = (TextView)itemView.findViewById(R.id.statusview);
        likecountstatus = (TextView)itemView.findViewById(R.id.likecountstatus);
        imageviewstatus = (ImageView)itemView.findViewById(R.id.imageviewstatus);
        likefoodstatus = (ImageView)itemView.findViewById(R.id.likefoodstatus);
        commentfoodstatus = (ImageView)itemView.findViewById(R.id.commentfoodstatus);
        editstatus = (ImageView)itemView.findViewById(R.id.editstatus);
        deletestatus = (ImageView)itemView.findViewById(R.id.deletestatus);
        imageusestatus = (CircleImageView) itemView.findViewById(R.id.imageusestatus);
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
                    likefoodstatus.setImageResource(R.drawable.ic_like_food_check);
                }else {
                    likefoodstatus.setImageResource(R.drawable.ic_like_food);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
