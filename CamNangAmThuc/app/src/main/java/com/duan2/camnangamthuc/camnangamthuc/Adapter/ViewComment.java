package com.duan2.camnangamthuc.camnangamthuc.Adapter;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.duan2.camnangamthuc.camnangamthuc.Interface.ItemClickListerner;
import com.duan2.camnangamthuc.camnangamthuc.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewComment extends RecyclerView.ViewHolder {
    public TextView comment_user;
    public TextView comment_test;
    public TextView comment_time;
    public CircleImageView imgcomment;
    public Button detele_comment;
    public Button edit_comment;
    private ItemClickListerner itemClickListerner;
    public ViewComment(View itemView) {
        super(itemView);
        comment_user = (TextView)itemView.findViewById(R.id.comment_user);
        comment_test = (TextView)itemView.findViewById(R.id.comment_test);
        comment_time = (TextView)itemView.findViewById(R.id.comment_time);
        imgcomment = (CircleImageView)itemView.findViewById(R.id.imgcomment);
        detele_comment = (Button)itemView.findViewById(R.id.detele_comment);
        edit_comment = (Button)itemView.findViewById(R.id.edit_comment);
    }
}
