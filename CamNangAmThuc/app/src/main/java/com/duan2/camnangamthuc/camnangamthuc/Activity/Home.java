package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.duan2.camnangamthuc.camnangamthuc.Adapter.CustomView;
import com.duan2.camnangamthuc.camnangamthuc.Adapter.HomeViewHoderl;
import com.duan2.camnangamthuc.camnangamthuc.Interface.ItemClickListerner;
import com.duan2.camnangamthuc.camnangamthuc.Model.Category;
import com.duan2.camnangamthuc.camnangamthuc.Model.CheckInternet;
import com.duan2.camnangamthuc.camnangamthuc.Model.Common;
import com.duan2.camnangamthuc.camnangamthuc.Model.MenuHome;
import com.duan2.camnangamthuc.camnangamthuc.Model.Users;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

import io.paperdb.Paper;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemClickListener{
    ListView listViewMenu;
    ArrayList<MenuHome> listArray = new ArrayList<>();
    CustomView customView;
    Bitmap xemdanhgiaIcon,xemtaiveIcon,gopyIcon,huongdanIcon,loginIon;
    FirebaseDatabase database;
    DatabaseReference category;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category,HomeViewHoderl> adapter;
    ProgressDialog pDialog;
    LinearLayout gvcamnang,gvcongdong;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //Khai b??o Firebase
        database = FirebaseDatabase.getInstance();
        //B???c d??? li???u Json
        category = database.getReference("Category");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Paper.init(this);
        String email= Paper.book().read(Common.USE_KEY);
        String password= Paper.book().read(Common.PAW_KEY);
        if(email != null && password !=null){
            if(!email.isEmpty() && !password.isEmpty()){
                loginghinho(email,password);
            }
        }
        //khai b??o listview menu
        loginIon = BitmapFactory.decodeResource(this.getResources(),R.drawable.login);
        listArray.add(new MenuHome("????ng nh???p t??i kho???ng",loginIon));
        xemtaiveIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.taive);
        listArray.add(new MenuHome("Xem t???i v???",xemtaiveIcon));
        gopyIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.icongmail);
        listArray.add(new MenuHome("G??p ??",gopyIcon));
        huongdanIcon = BitmapFactory.decodeResource(this.getResources(),R.drawable.huongdan);
        listArray.add(new MenuHome("H?????ng d???n",huongdanIcon));
        //th??m v??o adapter
        listViewMenu = (ListView) findViewById(R.id.listviewmenu);
        customView = new CustomView(this,R.layout.dolistviewmenu,listArray);
        listViewMenu.setAdapter(customView);
        listViewMenu.setOnItemClickListener(this);
        //Load d??? li???u ra home
        recyclerView = (RecyclerView)findViewById(R.id.recyMenu);
      /*  layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);*/
      recyclerView.setLayoutManager(new GridLayoutManager(this ,2));
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(),
                R.anim.layout_fall);
        recyclerView.setLayoutAnimation(controller);
        gvcamnang =(LinearLayout)findViewById(R.id.gv_camnag);
        gvcamnang.setBackgroundResource(R.drawable.bachgrounk_item_list);
        gvcamnang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Home.this,Home.class);
                startActivity(intent);

            }
        });
        gvcongdong =(LinearLayout)findViewById(R.id.gv_congdong);
        gvcongdong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(Home.this,CongDongActivity.class);
                startActivity(intent2);
            }
        });

        //ki???m tra xem ???? k???t n???i internet hay ch??a
        //khi c?? internet s??? kh???i ch???y ViewFlipper v?? ActionBar
        if (CheckInternet.haveNetworkConnection(this)){
            //load dialog
            pDialog = new ProgressDialog(Home.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("??ang t???i d??? li???u...");
            pDialog.show();
            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                   loadMenu();
                }
            };
            //set th???i gian load dialog
            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 1500);
        }else {
            CheckInternet.ThongBao(this,"Vui l??ng k???t n???i internet");
        }
    }
    private void loginghinho(final String email , final String password){
       final FirebaseDatabase firebaseDatabase;
       final DatabaseReference listuser;
        firebaseDatabase = FirebaseDatabase.getInstance();
        listuser = firebaseDatabase.getReference("Users");
        pDialog = new ProgressDialog(Home.this);
        listuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pDialog.dismiss();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Users users = postSnapshot.getValue(Users.class);
                    if (!users.getEmail().equals(email) &&
                            !users.getPassword().equals(password)) {
                    }else {
                        if (users.getEmail().equals(email) &&
                                users.getPassword().equals(password)
                                && users.getRole().equalsIgnoreCase("admin")) {
                            Intent adminIntent = new Intent(Home.this, HomeAdmin.class);
                            Common.userten = users;;
                            startActivity(adminIntent);

                        }
                        if (users.getEmail().equals(email) &&
                                users.getPassword().equals(password)
                                && users.getRole().equalsIgnoreCase("user")) {
                            Intent userIntent = new Intent(Home.this, HomeUsers.class);
                            Common.userten = users;
                            startActivity(userIntent);

                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
//l???y d??? li???u t??n v?? img ????? ra m??ng h??nh
    private void loadMenu() {
         adapter = new FirebaseRecyclerAdapter<Category, HomeViewHoderl>(Category.class,R.layout.item_menu,HomeViewHoderl.class,category) {
             @Override
             protected void populateViewHolder(HomeViewHoderl viewHolder, Category model, int position) {
                 viewHolder.txtMen???View.setText(model.getName());
                 Glide.with(Home.this).load(model.getImage()).into(viewHolder.imgMenuView);
                 final Category clickitem = model;
                 //s??? l?? s??? ki???n click cho d?? li???u ????? v???
                 viewHolder.setItemListener(new ItemClickListerner() {
                     @Override
                     public void onClick(View view, int position, boolean isLongClick) {
                         //get id c???a Category v?? g???i qua activity m???i
                         //Log.d("", "onClick: " +adapter.getRef(position).getKey());
                         Intent foodIntent = new Intent(Home.this,FoodActivity.class);
                         Common.categorygetten = clickitem;
                         //l???y id c???a Category l?? key,v?? v???y l???y key ????? ch??? item
                         foodIntent.putExtra("CategoryId",adapter.getRef(position).getKey());
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        final int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent intent = new Intent(Home.this,Home.class);
            startActivity(intent);
            finish();
            return true;
        }
        if (id == R.id.action_seach) {
            builder = new AlertDialog.Builder(Home.this);
            builder.setTitle("Nh???p t??n m??n c???n t??m");
            builder.setMessage("Vi???t hoa ch??? c??i ?????u ti??n || Nh???p t??n m??n ph???i c?? d???u");
            LayoutInflater layoutInflater = Home.this.getLayoutInflater();
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
                        editseach.setError("Vui l??ng nh???p t??n c???n tim");
                        editseach.requestFocus();
                        return;
                    } else {
                        Intent foodinfoIntent = new Intent(Home.this, LoadSeachActivity.class);
                        //l???y id c???a Category l?? key,v?? v???y l???y key ????? ch??? item
                        foodinfoIntent.putExtra("KeyGet", seach);
                        startActivity(foodinfoIntent);
                        b.dismiss();
                    }
                }
            });
            return true;
        }
        if (id == R.id.action_folder) {
            Intent intent = new Intent(Home.this,ViewListShopping.class);
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
    //send mail g??p ??
    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {"buivietphii@gmail.com"};
        String[] CC = {"buivietphii@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:buivietphii@gmail.com"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Th?? G??p ??");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Xin ch??o nh?? ph??t tri???n App C???m Nang ???m Th???c");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Home.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
//s??? l?? s??? ki???n click cho listview Menu
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
               Intent intent = new Intent(Home.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case 1:
                Intent intent2 = new Intent(Home.this, ViewDownload.class);
                startActivity(intent2);
                break;
            case 2:
                sendEmail();
                break;
            case 3:
                Intent intent3 = new Intent(Home.this, GuideViewActivity.class);
                startActivity(intent3);
                break;
        }
    }
}
