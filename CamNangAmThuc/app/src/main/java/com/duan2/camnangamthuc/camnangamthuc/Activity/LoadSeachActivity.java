package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.duan2.camnangamthuc.camnangamthuc.Adapter.FoodInfomationViewHoder;
import com.duan2.camnangamthuc.camnangamthuc.Interface.ItemClickListerner;
import com.duan2.camnangamthuc.camnangamthuc.Model.CheckInternet;
import com.duan2.camnangamthuc.camnangamthuc.Model.Common;
import com.duan2.camnangamthuc.camnangamthuc.Model.FoodInfomation;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class LoadSeachActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference foodInfomationlist;
    Toolbar toolbar;
    FirebaseRecyclerAdapter<FoodInfomation, FoodInfomationViewHoder> adapter;
    ProgressDialog pDialog;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String KetGet = "";
    TextView txtthongtintile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_seach);
        database = FirebaseDatabase.getInstance();
        //Bọc dữ liệu Json
        foodInfomationlist = database.getReference("FoodInfomation");
        toolbar = (Toolbar) findViewById(R.id.toolbar_loadseach);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerViewloadseach);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if (CheckInternet.haveNetworkConnection(this)) {
            //load dialog
            pDialog = new ProgressDialog(LoadSeachActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Đang tải dữ liệu...");
            pDialog.show();
            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    //get sự kiện inten từ Home
                    if (getIntent() != null) {
                        //lấy dữ liệu đc push từ intent
                        //tham số truyền vào KeyGet
                        KetGet = getIntent().getStringExtra("KeyGet");
                        if (!KetGet.isEmpty()) {
                            starload(KetGet);
                            Log.d("KLKKK", KetGet);
                        }
                    }
                    ChayToolBar();
                }
            };
            //set thời gian load dialog
            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 1500);
        } else {
            CheckInternet.ThongBao(this, "Vui lòng kết nối internet");
        }
    }

    private void ChayToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtthongtintile = (TextView) findViewById(R.id.toolbar_title_loadseach);
        txtthongtintile.setSingleLine(true);
        txtthongtintile.setEllipsize(TextUtils.TruncateAt.END);
        txtthongtintile.setText(KetGet);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //tìm kiếm trong database theo tên
    private void starload(final String text) {
        Query fireQuery = foodInfomationlist.orderByChild("Name").startAt(text).endAt(text + "\uf8ff");
        adapter = new FirebaseRecyclerAdapter<FoodInfomation, FoodInfomationViewHoder>(FoodInfomation.class, R.layout.item_foodinfomation, FoodInfomationViewHoder.class,
                fireQuery) {//tìm kiếm : select * from Food where MenuId
            @Override
            protected void populateViewHolder(FoodInfomationViewHoder viewHolder, FoodInfomation model, int position) {
                //hiển thị tên,hình ảnh đã đc tìm kiếm
                viewHolder.txtInfomationViewName.setText(model.getName());
                viewHolder.txtInfomationViewInfo.setText(model.getInfomation());
                viewHolder.txtInfomationViewInfo.setMaxLines(2);
                viewHolder.txtInfomationViewInfo.setEllipsize(TextUtils.TruncateAt.END);
                Glide.with(LoadSeachActivity.this).load(model.getImage()).into(viewHolder.imgFoodInfomationView);
                final FoodInfomation foodInfomation = model;
                //click vào từg item
                viewHolder.setItemListener(new ItemClickListerner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodinfoviewIntent = new Intent(LoadSeachActivity.this, FoodInfomationViewActivity.class);
                        Common.foodinfogetten = foodInfomation;
                        //lấy id của Category là key,vì vậy lấy key để chỉ item
                        foodinfoviewIntent.putExtra("FoodInfomationId", adapter.getRef(position).getKey());
                        startActivity(foodinfoviewIntent);
                    }
                });
            }
        };
        //set adapter
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        pDialog.dismiss();
    }
}
