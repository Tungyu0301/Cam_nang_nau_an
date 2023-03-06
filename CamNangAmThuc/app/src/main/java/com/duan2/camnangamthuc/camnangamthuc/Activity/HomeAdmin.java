package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duan2.camnangamthuc.camnangamthuc.Adapter.CustomView;
import com.duan2.camnangamthuc.camnangamthuc.Adapter.HomeViewHoderl;
import com.duan2.camnangamthuc.camnangamthuc.Interface.ItemClickListerner;
import com.duan2.camnangamthuc.camnangamthuc.Model.Category;
import com.duan2.camnangamthuc.camnangamthuc.Model.CheckInternet;
import com.duan2.camnangamthuc.camnangamthuc.Model.Common;
import com.duan2.camnangamthuc.camnangamthuc.Model.MenuHome;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import io.paperdb.Paper;

public class HomeAdmin extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemClickListener {
    ListView listViewMenu;
    ArrayList<MenuHome> listArray = new ArrayList<>();
    CustomView customView;
    Bitmap xemdanhgiaIcon, xemtaiveIcon, quanlitktkIcon, dangxuatIcon, baivietdadangIcon,thongtintkIcon,pheduyetIcon;
    FirebaseDatabase database;
    DatabaseReference category;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category, HomeViewHoderl> adapter;
    ProgressDialog pDialog;
    LinearLayout gvcamnang, gvcongdong;
    TextView txtloginad;
    ImageView imgloginad;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_homeadmin);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //Khai báo Firebase
        database = FirebaseDatabase.getInstance();
        //Bọc dữ liệu Json
        category = database.getReference("Category");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutad);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_ad);
        navigationView.setNavigationItemSelectedListener(this);
        txtloginad  = (TextView) findViewById(R.id.textviewloginad);
        imgloginad  = (ImageView)findViewById(R.id.imgloginadmin) ;
        txtloginad.setText(Common.userten.getName());
        /*Picasso.with(getBaseContext()).load(Common.userten.getImage()).into(imgloginad);*/
        Glide.with(getApplicationContext()).load(Common.userten.getImage()).apply(RequestOptions.circleCropTransform()).into(imgloginad);
        Paper.init(this);
        //khai báo listview menu
        quanlitktkIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.qltk);
        listArray.add(new MenuHome("Quản lý tài khoản", quanlitktkIcon));
        thongtintkIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.thongtintaikhoang);
        listArray.add(new MenuHome("Thông tin tài khoản",thongtintkIcon));
        baivietdadangIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.baiviet);
        listArray.add(new MenuHome("Bài viết đã đăng", baivietdadangIcon));
        xemdanhgiaIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.danhgia);
        listArray.add(new MenuHome("Xem đánh dấu", xemdanhgiaIcon));
        pheduyetIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.pheduyet);
        listArray.add(new MenuHome("Bài viết đang chờ", pheduyetIcon));
        xemtaiveIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.taive);
        listArray.add(new MenuHome("Xem tải về", xemtaiveIcon));
        dangxuatIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.dangxau);
        listArray.add(new MenuHome("Đăng xuất", dangxuatIcon));
        //thêm vào adapter
        listViewMenu = (ListView) findViewById(R.id.listviewmenuad);
        customView = new CustomView(this, R.layout.dolistviewmenu, listArray);
        listViewMenu.setAdapter(customView);
        listViewMenu.setOnItemClickListener(this);
        //Load dữ liệu ra home
        recyclerView = (RecyclerView) findViewById(R.id.recyMenuhomead);
      /*  layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);*/
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(),
                R.anim.layout_fall);
        recyclerView.setLayoutAnimation(controller);
        gvcamnang = (LinearLayout) findViewById(R.id.gv_camnagad);
        gvcamnang.setBackgroundResource(R.drawable.bachgrounk_item_list);
        gvcamnang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeAdmin.this, HomeAdmin.class);
                startActivity(intent);

            }
        });
        gvcongdong = (LinearLayout) findViewById(R.id.gv_congdongad);
        gvcongdong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(HomeAdmin.this, CommunityAdminActivity.class);
                startActivity(intent2);
            }
        });

        //kiểm tra xem đã kết nối internet hay chưa
        //khi có internet sẽ khởi chạy ViewFlipper và ActionBar
        if (CheckInternet.haveNetworkConnection(this)) {
            //load dialog
            pDialog = new ProgressDialog(HomeAdmin.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Đang tải dữ liệu...");
            pDialog.show();
            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    loadMenu();
                }
            };
            //set thời gian load dialog
            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 1500);
        } else {
            CheckInternet.ThongBao(this, "Vui lòng kết nối internet");
        }
    }

    //lấy dữ liệu tên và img đổ ra màng hình
    private void loadMenu() {
        adapter = new FirebaseRecyclerAdapter<Category, HomeViewHoderl>(Category.class, R.layout.item_menu, HomeViewHoderl.class, category) {
            @Override
            protected void populateViewHolder(HomeViewHoderl viewHolder, Category model, int position) {
                viewHolder.txtMenụView.setText(model.getName());
                Glide.with(HomeAdmin.this).load(model.getImage()).into(viewHolder.imgMenuView);
                final Category clickitem = model;
                //sử lý sự kiện click cho dư liệu đổ về
                viewHolder.setItemListener(new ItemClickListerner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //get id của Category và gửi qua activity mới
                        //Log.d("", "onClick: " +adapter.getRef(position).getKey());
                        Intent foodIntent = new Intent(HomeAdmin.this, FoodActivity.class);
                        Common.categorygetten = clickitem;
                        //lấy id của Category là key,vì vậy lấy key để chỉ item
                        foodIntent.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(foodIntent);
                        //Toast.makeText(Home.this, ""+adapter.getRef(position).getKey(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
        pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutad);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent intent = new Intent(HomeAdmin.this, HomeAdmin.class);
            startActivity(intent);
            finish();
            return true;
        } if (id == R.id.action_seach) {
            builder = new AlertDialog.Builder(HomeAdmin.this);
            builder.setTitle("Nhập tên món cần tìm");
            builder.setMessage("Viết hoa chữ cái đầu tiên || Nhập tên món phải có dấu");
            LayoutInflater layoutInflater = HomeAdmin.this.getLayoutInflater();
            final View sendcode = layoutInflater.inflate(R.layout.item_seach, null);
            final EditText editseach = (EditText) sendcode.findViewById(R.id.edtseachname);
            final Button bntthoat = (Button) sendcode.findViewById(R.id.btn_thoat);
            final Button bnttim = (Button) sendcode.findViewById(R.id.btn_tim);
            builder.setView(sendcode);
            builder.setIcon(R.drawable.ic_seach_showdialog);
            final AlertDialog b = builder.create();
            b.show();
            bntthoat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    b.dismiss();
                }
            });
            bnttim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String seach = editseach.getText().toString();
                    if (seach.isEmpty()) {
                        editseach.setError("Vui lòng nhập tên cần tim");
                        editseach.requestFocus();
                        return;
                    } else {
                        Intent foodinfoIntent = new Intent(HomeAdmin.this, LoadSeachActivity.class);
                        //lấy id của Category là key,vì vậy lấy key để chỉ item
                        foodinfoIntent.putExtra("KeyGet", seach);
                        startActivity(foodinfoIntent);
                        b.dismiss();
                    }
                }
            });
            return true;
        }
        if (id == R.id.action_folder) {
            Intent intent = new Intent(HomeAdmin.this,ViewListShopping.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //sử lý sự kiện click cho listview Menu
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                Intent listusead = new Intent(HomeAdmin.this,ViewListUseAdminActivity.class);
                startActivity(listusead);
                break;
            case 1:
                Intent account = new Intent(HomeAdmin.this,AccountinformationActivity.class);
                account.putExtra("KeyEmail", Common.userten.getEmail());
                startActivity(account);
                break;
            case 2:
                Intent status = new Intent(HomeAdmin.this,PostedarticleActivity.class);
                status.putExtra("StatusEmail", Common.userten.getEmail());
                startActivity(status);
                break;
            case 3:
                Intent favorite = new Intent(HomeAdmin.this,FavoriteViewActivity.class);
                startActivity(favorite);
                break;
            case 4:
                Intent approved = new Intent(HomeAdmin.this,StatusApprovedActivity.class);
                startActivity(approved);
                break;
            case 5:
                Intent intent2 = new Intent(HomeAdmin.this, ViewDownload.class);
                startActivity(intent2);
                break;
            case 6:
                Paper.book().destroy();
                Intent longoutent = new Intent(HomeAdmin.this,Home.class);
                longoutent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(longoutent);
                break;
        }
    }
}