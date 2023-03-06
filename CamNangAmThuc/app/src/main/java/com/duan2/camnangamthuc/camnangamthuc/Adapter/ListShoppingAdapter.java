package com.duan2.camnangamthuc.camnangamthuc.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.duan2.camnangamthuc.camnangamthuc.Activity.InformationViewListShopping;
import com.duan2.camnangamthuc.camnangamthuc.Activity.ViewListShopping;
import com.duan2.camnangamthuc.camnangamthuc.Model.ListShoping;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.duan2.camnangamthuc.camnangamthuc.SQLiteDatabase.SQLiteHandler;

import java.util.List;

public class ListShoppingAdapter extends BaseAdapter {
    private ViewListShopping context;
    private int layout;
    private List<ListShoping> listShopings;
    private SQLiteHandler db;
    private String id = "";
    String Title = "";
    String Content = "";
    public ListShoppingAdapter(ViewListShopping context, List<ListShoping> listShopings) {
        this.context = context;
        this.listShopings = listShopings;
    }

    @Override
    public int getCount() {
        return listShopings.size();
    }

    @Override
    public Object getItem(int i) {
        return listShopings.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    private class ViewHolder{
        TextView txttitlelistshopping;
        public ImageView imgviewlist,imgstorage ;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            db = new SQLiteHandler(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_view_list_shopping, null);
            viewHolder.imgviewlist = (ImageView) view.findViewById(R.id.imgviewlist);
            viewHolder.imgstorage = (ImageView) view.findViewById(R.id.imgstorage);
            viewHolder.txttitlelistshopping = (TextView) view.findViewById(R.id.txttitlelistshopping);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final ListShoping listShoping = listShopings.get(i);
        viewHolder.txttitlelistshopping.setText(listShoping.getTitle());
        viewHolder.imgviewlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InformationViewListShopping.class);
                intent.putExtra("thongtinchitietdanhsach",listShoping);
                context.startActivity(intent);
            }
        });
        viewHolder.imgstorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xatnhanxoa(listShoping.getTitle(),listShoping.getId());
                Title = listShoping.getTitle();
                Content = listShoping.getContent();
            }
        });
        return view;
    }
    private void xatnhanxoa(String ten , final String id){
        String xoa = "Bạn có muốn chuyển <font color='blue'> <Strong>"+ten + "</Strong></font> vào danh sách lưu trữ không";
        AlertDialog.Builder dialogxoa = new AlertDialog.Builder(context);
        dialogxoa.setMessage(Html.fromHtml(xoa));
        dialogxoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                context.deletelistshopping(id);
                insert();
            }
        });
        dialogxoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialogxoa.show();
    }
    private void insert() {
        db.insertDataStogare(Title, Content);
    }
}
