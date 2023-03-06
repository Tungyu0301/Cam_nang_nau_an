package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.duan2.camnangamthuc.camnangamthuc.Adapter.CustomView;
import com.duan2.camnangamthuc.camnangamthuc.Model.MenuHome;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class CongDongActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemClickListener{
    CustomView customView;
    ListView listViewMenu;
    ArrayList<MenuHome> listArray = new ArrayList<>();
    Bitmap xemdanhgiaIcon,xemtaiveIcon,gopyIcon,huongdanIcon,loginIon;
    LinearLayout gvcamnang,gvcongdong;
    Button btnsendlogin;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cong_dong);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarcongdong);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutcongdong);
        btnsendlogin = (Button)findViewById(R.id.btnsendlogin);
        btnsendlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CongDongActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_viewcongdong);
        navigationView.setNavigationItemSelectedListener(this);
        //khai báo listview menu
        loginIon = BitmapFactory.decodeResource(this.getResources(),R.drawable.login);
        listArray.add(new MenuHome("Đăng nhập tài khoảng",loginIon));
        xemtaiveIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.taive);
        listArray.add(new MenuHome("Xem tải về",xemtaiveIcon));
        gopyIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.icongmail);
        listArray.add(new MenuHome("Góp ý",gopyIcon));
        huongdanIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.huongdan);
        listArray.add(new MenuHome("Hướng dẫn",huongdanIcon));
        //thêm vào adapter
        listViewMenu = (ListView) findViewById(R.id.listviewmenucongdong);
        customView = new CustomView(this,R.layout.dolistviewmenu,listArray);
        listViewMenu.setAdapter(customView);
        listViewMenu.setOnItemClickListener(this);
        gvcamnang =(LinearLayout)findViewById(R.id.gv_camnagacti);
        gvcamnang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(CongDongActivity.this,Home.class);
                startActivity(intent);

            }
        });
        gvcongdong =(LinearLayout)findViewById(R.id.gv_congdongacti);
        gvcongdong.setBackgroundResource(R.drawable.bachgrounk_item_list);
        gvcongdong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(CongDongActivity.this,CongDongActivity.class);
                startActivity(intent2);
            }
        });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutcongdong);
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
            Intent intent = new Intent(CongDongActivity.this,Home.class);
            startActivity(intent);
            finish();
            return true;
        }
        if (id == R.id.action_seach) {
            builder = new AlertDialog.Builder(CongDongActivity.this);
            builder.setTitle("Nhập tên món cần tìm");
            builder.setMessage("Viết hoa chữ cái đầu tiên || Nhập tên món phải có dấu");
            LayoutInflater layoutInflater = CongDongActivity.this.getLayoutInflater();
            final View sendcode = layoutInflater.inflate(R.layout.item_seach,null);
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
                        Intent foodinfoIntent = new Intent(CongDongActivity.this, LoadSeachActivity.class);
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
            Intent intent = new Intent(CongDongActivity.this,ViewListShopping.class);
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
    //send mail góp ý
    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {"buivietphii@gmail.com"};
        String[] CC = {"buivietphii@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:buivietphii@gmail.com"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Thư Góp Ý");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Xin chào nhà phát triển App Cẩm Nang Ẩm Thực");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(CongDongActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
    //sử lý sự kiện click cho listview Menu
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                Intent intent = new Intent(CongDongActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent2 = new Intent(CongDongActivity.this, ViewDownload.class);
                startActivity(intent2);
                break;
            case 2:
                sendEmail();
                break;
            case 3:
                Intent intent3 = new Intent(CongDongActivity.this, GuideViewActivity.class);
                startActivity(intent3);
                break;
        }
    }
}
