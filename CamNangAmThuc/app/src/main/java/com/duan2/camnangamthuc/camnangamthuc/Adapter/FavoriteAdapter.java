package com.duan2.camnangamthuc.camnangamthuc.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.duan2.camnangamthuc.camnangamthuc.Activity.FavoriteViewActivity;
import com.duan2.camnangamthuc.camnangamthuc.Activity.ViewDownload;
import com.duan2.camnangamthuc.camnangamthuc.Model.Download;
import com.duan2.camnangamthuc.camnangamthuc.Model.Favorite;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.duan2.camnangamthuc.camnangamthuc.SQLiteDatabase.SQLiteHandler;

import java.util.List;

public class FavoriteAdapter extends BaseAdapter {
    private FavoriteViewActivity context;
    private int layout;
    private List<Favorite> favorites;
    int id = 0;
    private SQLiteHandler db;
    public FavoriteAdapter(FavoriteViewActivity context, List<Favorite> favorites) {
        this.context = context;
        this.favorites = favorites;
    }

    @Override
    public int getCount() {
        return favorites.size();
    }

    @Override
    public Object getItem(int i) {
        return favorites.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    private class ViewHolder{
        TextView txtnamefavorite;
        public ImageView deletefavorite ;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            db = new SQLiteHandler(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_view_favorite, null);
            viewHolder.deletefavorite = (ImageView) view.findViewById(R.id.deletefavorite);
            viewHolder.txtnamefavorite = (TextView) view.findViewById(R.id.txtnamefavorite);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final Favorite favorite = favorites.get(i);
        viewHolder.txtnamefavorite.setText(favorite.getNamefood());
        viewHolder.deletefavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xatnhanxoa(favorite.getNamefood(),favorite.getKeyid());
            }
        });
        return view;
    }
    private void xatnhanxoa(String ten , final String keyid){
        String xoa = "B???n c?? mu???n b??? ????nh d???u<font color='blue'> <Strong>"+ten + "</Strong></font>";
        AlertDialog.Builder dialogxoa = new AlertDialog.Builder(context);
        dialogxoa.setTitle("B??? ????nh d???u");
        dialogxoa.setMessage(Html.fromHtml(xoa));
        dialogxoa.setPositiveButton("C??", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                context.deletedow(keyid);
            }
        });
        dialogxoa.setNegativeButton("Kh??ng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialogxoa.show();
    }
}
