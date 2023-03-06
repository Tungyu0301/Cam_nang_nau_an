package com.duan2.camnangamthuc.camnangamthuc.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.duan2.camnangamthuc.camnangamthuc.Model.MenuHome;
import com.duan2.camnangamthuc.camnangamthuc.R;

import java.util.ArrayList;


public class CustomView extends ArrayAdapter<MenuHome> {
        Context context;
        int resourceId;
        ArrayList<MenuHome> data = new ArrayList<MenuHome>();

        // Constuctor
	public CustomView(Context context, int resourceId,
                      ArrayList<MenuHome> data) {
            super(context, resourceId, data);
            this.context = context;
            this.resourceId = resourceId;
            this.data = data;
        }

        // Khong hieu
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resourceId, parent, false);

            holder = new RecordHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.txttenmenu);
            holder.imageItem = (ImageView) row.findViewById(R.id.imgmenu);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }
        MenuHome menuHome = data.get(position);
        holder.txtTitle.setText(menuHome.getTenmenu());
        holder.imageItem.setImageBitmap(menuHome.getImage());
        return row;
    }

    static class RecordHolder {
        TextView txtTitle;
        ImageView imageItem;
    }
}
