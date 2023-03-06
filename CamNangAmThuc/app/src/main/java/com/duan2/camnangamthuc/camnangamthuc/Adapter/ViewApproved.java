package com.duan2.camnangamthuc.camnangamthuc.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.duan2.camnangamthuc.camnangamthuc.Interface.ItemClickListerner;
import com.duan2.camnangamthuc.camnangamthuc.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewApproved  extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtnamefoodstatusApproved;
    public TextView txtnamestatusApproved;
    public TextView txtngaydangstatusApproved;
    public TextView statusviewApproved;
    public ImageView imageviewstatusApproved;
    public ImageView foodstatusApproved;
    public ImageView deletefoodstatusApproved;
    public CircleImageView imageusestatusApproved;
    private ItemClickListerner itemClickListerner;
    public ViewApproved(View itemView) {
        super(itemView);
        txtnamefoodstatusApproved = (TextView)itemView.findViewById(R.id.txtnamefoodstatusApproved);
        txtnamestatusApproved = (TextView)itemView.findViewById(R.id.txtnamestatusApproved);
        txtngaydangstatusApproved = (TextView)itemView.findViewById(R.id.txtngaydangstatusApproved);
        statusviewApproved = (TextView)itemView.findViewById(R.id.statusviewApproved);
        imageviewstatusApproved = (ImageView)itemView.findViewById(R.id.imageviewstatusApproved);
        foodstatusApproved = (ImageView)itemView.findViewById(R.id.foodstatusApproved);
        deletefoodstatusApproved = (ImageView)itemView.findViewById(R.id.deletefoodstatusApproved);
        imageusestatusApproved = (CircleImageView) itemView.findViewById(R.id.imageusestatusApproved);
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
