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

public class ViewCommunityUse extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtnamefoodcongdong;
    public TextView txtnamecongdonguse;
    public TextView txtngaydangcongdong;
    public TextView likecountuse;
    public ImageView viewimagecongdonguse;
    public ImageView likefood;
    public ImageView commentfood;
    public CircleImageView imageusecongdong;
    private ItemClickListerner itemClickListerner;
    FirebaseDatabase database;
    DatabaseReference congdonglist;
    public ViewCommunityUse(View itemView) {
        super(itemView);
        database = FirebaseDatabase.getInstance();
        //Bọc dữ liệu Json
        congdonglist = database.getReference("Communitys");
        txtnamefoodcongdong = (TextView)itemView.findViewById(R.id.txtnamefoodcongdong);
        txtnamecongdonguse = (TextView)itemView.findViewById(R.id.txtnamecongdonguse);
        txtngaydangcongdong = (TextView)itemView.findViewById(R.id.txtngaydangcongdong);
        likecountuse = (TextView)itemView.findViewById(R.id.likecountuse);
        viewimagecongdonguse = (ImageView)itemView.findViewById(R.id.viewimagecongdonguse);
        likefood = (ImageView)itemView.findViewById(R.id.likefood);
        commentfood = (ImageView)itemView.findViewById(R.id.commentfood);
        imageusecongdong = (CircleImageView) itemView.findViewById(R.id.imageusecongdong);
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
                    likefood.setImageResource(R.drawable.ic_like_food_check);
                }else {
                    likefood.setImageResource(R.drawable.ic_like_food);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
