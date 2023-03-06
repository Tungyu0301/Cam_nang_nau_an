package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duan2.camnangamthuc.camnangamthuc.Adapter.FoodInfomationViewHoder;
import com.duan2.camnangamthuc.camnangamthuc.Interface.ItemClickListerner;
import com.duan2.camnangamthuc.camnangamthuc.Model.CheckInternet;
import com.duan2.camnangamthuc.camnangamthuc.Model.Common;
import com.duan2.camnangamthuc.camnangamthuc.Model.FoodInfomation;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class FoodInfomationActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference foodInfomationlist;
    String FoodId="";
    Toolbar toolbar;
    FirebaseRecyclerAdapter<FoodInfomation,FoodInfomationViewHoder> adapter;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_infomation);
        //Khai báo Firebase
        database = FirebaseDatabase.getInstance();
        //Bọc dữ liệu Json
        foodInfomationlist = database.getReference("FoodInfomation");
        toolbar = (Toolbar) findViewById(R.id.toolbar_show);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerViewshow);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //kiểm tra kết nối internet
        if (CheckInternet.haveNetworkConnection(this)){
            //load dialog
            pDialog = new ProgressDialog(FoodInfomationActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Đang tải dữ liệu...");
            pDialog.show();
            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    //get sự kiện inten từ Home
                    if(getIntent() !=null){
                        FoodId = getIntent().getStringExtra("FoodId");
                        if (!FoodId.isEmpty()) {
                            loadlistFoodInfomation(FoodId);
                        }
                    }
                    ChayToolBar();
                }
            };
            //set thời gian load dialog
            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 1500);
        }else {
            CheckInternet.ThongBao(this,"Vui lòng kết nối internet");
        }
    }
    private void ChayToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menutt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_menutt) {
            Intent intent = new Intent(FoodInfomationActivity.this,Home.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //đọc dữ liệu foodInfomation
    private void loadlistFoodInfomation(String foodId) {
        final TextView txtthongtintile;
        final CircleImageView imgviewfoodinfomationtoobar;
        imgviewfoodinfomationtoobar = (CircleImageView) findViewById(R.id.imgviewfoodinfomationtoobar);
        txtthongtintile = (TextView)findViewById(R.id.toolbar_title_show);
        adapter = new FirebaseRecyclerAdapter<FoodInfomation, FoodInfomationViewHoder>(FoodInfomation.class,R.layout.item_foodinfomation,FoodInfomationViewHoder.class,
                foodInfomationlist.orderByChild("MenuId").equalTo(foodId)){//tìm kiếm : select * from Food where MenuId
            @Override
            protected void populateViewHolder(FoodInfomationViewHoder viewHolder, FoodInfomation model, int position) {
                viewHolder.txtInfomationViewName.setText(model.getName());
                viewHolder.txtInfomationViewInfo.setText(model.getInfomation());
                viewHolder.txtInfomationViewInfo.setMaxLines(2);
                viewHolder.txtInfomationViewInfo.setEllipsize(TextUtils.TruncateAt.END);
               Glide.with(getApplicationContext()).load(model.getImage()).into(viewHolder.imgFoodInfomationView);
                txtthongtintile.setText(Common.foodgetten.getName());
                txtthongtintile.setSingleLine(true);
                txtthongtintile.setEllipsize(TextUtils.TruncateAt.END);
                Glide.with(getApplicationContext()).load(Common.foodgetten.getImage()).apply(RequestOptions.circleCropTransform()).into(imgviewfoodinfomationtoobar);
                final FoodInfomation foodInfomation = model;
                viewHolder.setItemListener(new ItemClickListerner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodinfoviewIntent = new Intent(FoodInfomationActivity.this,FoodInfomationViewActivity.class);
                        Common.foodinfogetten = foodInfomation;
                        //lấy id của Category là key,vì vậy lấy key để chỉ item
                        foodinfoviewIntent.putExtra("FoodInfomationId",adapter.getRef(position).getKey());
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
