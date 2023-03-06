package com.duan2.camnangamthuc.camnangamthuc.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.duan2.camnangamthuc.camnangamthuc.Activity.CommunityUserActivity;
import com.duan2.camnangamthuc.camnangamthuc.Activity.CongDongActivity;
import com.duan2.camnangamthuc.camnangamthuc.Activity.ViewDownload;
import com.duan2.camnangamthuc.camnangamthuc.Model.Download;
import com.duan2.camnangamthuc.camnangamthuc.Model.FoodInfomation;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.duan2.camnangamthuc.camnangamthuc.SQLiteDatabase.SQLiteHandler;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadAdapter extends BaseAdapter {
    private ViewDownload context;
    private int layout;
    private List<Download> downloads;
    int id = 0;
    private SQLiteHandler db;
    public DownloadAdapter(ViewDownload context, List<Download> downloads) {
        this.context = context;
        this.downloads = downloads;
    }

    @Override
    public int getCount() {
        return downloads.size();
    }

    @Override
    public Object getItem(int i) {
        return downloads.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    private class ViewHolder{
        TextView txtnamedow;
       public ImageView deletedow ;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            db = new SQLiteHandler(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_view_download, null);
            viewHolder.deletedow = (ImageView) view.findViewById(R.id.deletedow);
            viewHolder.txtnamedow = (TextView) view.findViewById(R.id.txtnamedow);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final Download download = downloads.get(i);
        viewHolder.txtnamedow.setText(download.getName());
        viewHolder.deletedow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xatnhanxoa(download.getName(),download.getId());
            }
        });
        return view;
    }
    private void xatnhanxoa(String ten , final String id){
        String xoa = "Bạn có muốn xóa <font color='blue'> <Strong>"+ten + "</Strong></font> ra khỏi danh sách tải về không";
        AlertDialog.Builder dialogxoa = new AlertDialog.Builder(context);
        dialogxoa.setTitle("Xóa tải về");
        dialogxoa.setMessage(Html.fromHtml(xoa));
        dialogxoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                context.deletedow(id);
            }
        });
        dialogxoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialogxoa.show();
    }
}