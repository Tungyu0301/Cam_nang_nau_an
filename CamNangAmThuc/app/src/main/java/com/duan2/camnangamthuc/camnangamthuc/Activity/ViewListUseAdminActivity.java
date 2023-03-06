package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duan2.camnangamthuc.camnangamthuc.Adapter.ViewlistuseadminAdapter;
import com.duan2.camnangamthuc.camnangamthuc.Model.CheckInternet;
import com.duan2.camnangamthuc.camnangamthuc.Model.Users;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dmax.dialog.SpotsDialog;

public class ViewListUseAdminActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference listusead;
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<Users,ViewlistuseadminAdapter> adapter;
    AlertDialog dialogwaching;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_use_admin);
        toolbar = (Toolbar) findViewById(R.id.toolbar_viewlistuseadmin);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //Khai báo Firebase
        database = FirebaseDatabase.getInstance();
        //Bọc dữ liệu Json
        listusead = database.getReference("Users");
        recyclerView = (RecyclerView)findViewById(R.id.recyviewlistuseadmin);
        recyclerView.setLayoutManager(new GridLayoutManager(this ,2));
        if (CheckInternet.haveNetworkConnection(this)){
            //load dialog
            dialogwaching = new SpotsDialog(ViewListUseAdminActivity.this);
            dialogwaching.show();
            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    loadusead();
                }
            };
            //set thời gian load dialog
            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 1500);
        }else {
            CheckInternet.ThongBao(this,"Vui lòng kết nối internet");
        }
        ChayToolBar();
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
            Intent intent = new Intent(ViewListUseAdminActivity.this,HomeAdmin.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void loadusead() {
        adapter = new FirebaseRecyclerAdapter<Users, ViewlistuseadminAdapter>(Users.class,R.layout.item_list_user,ViewlistuseadminAdapter.class,listusead) {
            @Override
            protected void populateViewHolder(ViewlistuseadminAdapter viewHolder, final Users model, final int position) {
                viewHolder.txtViewliseName.setText(model.getName());
                viewHolder.txtViewliseName.setSingleLine(true);
                viewHolder.txtViewliseName.setEllipsize(TextUtils.TruncateAt.END);
                viewHolder.txtPhoneusead.setText(model.getPhone());
                viewHolder.txtPhoneusead.setSingleLine(true);
                viewHolder.txtPhoneusead.setEllipsize(TextUtils.TruncateAt.END);
                viewHolder.txtEmailusead.setText(model.getEmail());
                viewHolder.txtEmailusead.setSingleLine(true);
                viewHolder.txtEmailusead.setEllipsize(TextUtils.TruncateAt.END);
                viewHolder.txtngaysinhusead.setText(model.getNgaysinh());
                viewHolder.txtngaysinhusead.setSingleLine(true);
                viewHolder.txtngaysinhusead.setEllipsize(TextUtils.TruncateAt.END);
                viewHolder.txtdiachiusead.setText(model.getDiachi());
                viewHolder.txtdiachiusead.setSingleLine(true);
                viewHolder.txtdiachiusead.setEllipsize(TextUtils.TruncateAt.END);
                viewHolder.txtcodeusead.setText(model.getCode());
                viewHolder.txtcodeusead.setSingleLine(true);
                viewHolder.txtcodeusead.setEllipsize(TextUtils.TruncateAt.END);
                Glide.with(getBaseContext()).load(model.getImage()).apply(RequestOptions.circleCropTransform()).into(viewHolder.imgAvatarusead);
                viewHolder.deleteoption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String xoa = "Bạn có muốn xóa tài khoản của <font color='red'> <Strong>"+ model.getName()+ "</Strong></font> ra khỏi danh sách không";
                        AlertDialog.Builder dialogxoa = new AlertDialog.Builder(ViewListUseAdminActivity.this);
                        dialogxoa.setTitle("Xóa tài khỏan");
                        dialogxoa.setIcon(R.drawable.ic_delete_use);
                        dialogxoa.setMessage(Html.fromHtml(xoa));
                        dialogxoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //lấy vị trí hiện tại
                                deleteuselist(adapter.getRef(position).getKey());
                            }
                        });
                        dialogxoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        dialogxoa.show();

                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
        dialogwaching.dismiss();
    }
    private void deleteuselist(String key){
        //xóa vị trí đã lấy ra khỏi database
        listusead.child(key).removeValue();
        Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
    }
}
