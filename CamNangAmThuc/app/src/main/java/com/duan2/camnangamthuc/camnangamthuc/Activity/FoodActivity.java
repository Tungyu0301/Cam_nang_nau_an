package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
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
import com.duan2.camnangamthuc.camnangamthuc.Adapter.FoodViewHoder;
import com.duan2.camnangamthuc.camnangamthuc.Interface.ItemClickListerner;
import com.duan2.camnangamthuc.camnangamthuc.Model.CheckInternet;
import com.duan2.camnangamthuc.camnangamthuc.Model.Common;
import com.duan2.camnangamthuc.camnangamthuc.Model.Food;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class FoodActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference foodlist;
    String categoryId="";
    Toolbar toolbar;
    FirebaseRecyclerAdapter<Food,FoodViewHoder> adapter;
    ProgressDialog pDialog;
    AlertDialog dialogwaching;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        //Khai báo Firebase
        database = FirebaseDatabase.getInstance();
        //Bọc dữ liệu Json
        foodlist = database.getReference("Food");
        toolbar = (Toolbar) findViewById(R.id.toolbar_thongtinmon);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyFood);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //kiểm tra kết nối internet
        if (CheckInternet.haveNetworkConnection(this)){
            //load dialog
            dialogwaching = new SpotsDialog(FoodActivity.this);
            dialogwaching.show();
            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    //get sự kiện inten từ Home
                    if(getIntent() !=null){
                        categoryId = getIntent().getStringExtra("CategoryId");
                        if (!categoryId.isEmpty()) {
                            loadlistFood(categoryId);
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
            Intent intent = new Intent(FoodActivity.this,Home.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //đọc dữ liệu food
    private void loadlistFood(String categoryId) {
        final TextView txtthongtintile;
        final CircleImageView imgviewfoodtoobar;
        imgviewfoodtoobar = (CircleImageView) findViewById(R.id.imgviewfoodtoobar);
        txtthongtintile = (TextView)findViewById(R.id.toolbar_title_thongtinmon);
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHoder>(Food.class,R.layout.item_food,FoodViewHoder.class,
                foodlist.orderByChild("menuId").equalTo(categoryId)){//tìm kiếm : select * from Food where MenuId
            @Override
            protected void populateViewHolder(FoodViewHoder viewHolder, Food model, int position) {
                viewHolder.txtFoodView.setText(model.getName());
                viewHolder.txtFoodView.setText(Html.fromHtml(model.getName()));
               Glide.with(getApplicationContext()).load(model.getImage()).into(viewHolder.imgFoodView);
                txtthongtintile.setText(Common.categorygetten.getName());
                txtthongtintile.setSingleLine(true);
                txtthongtintile.setEllipsize(TextUtils.TruncateAt.END);
                Glide.with(getApplicationContext()).load(Common.categorygetten.getImage()).apply(RequestOptions.circleCropTransform()).into(imgviewfoodtoobar);
                final Food local = model;
                viewHolder.setItemListener(new ItemClickListerner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodinfoIntent = new Intent(FoodActivity.this,FoodInfomationActivity.class);
                        Common.foodgetten = local;
                        //lấy id của Category là key,vì vậy lấy key để chỉ item
                        foodinfoIntent.putExtra("FoodId",adapter.getRef(position).getKey());
                        startActivity(foodinfoIntent);
                    }
                });
            }
        };
        //set adapter
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        dialogwaching.dismiss();
    }
}
