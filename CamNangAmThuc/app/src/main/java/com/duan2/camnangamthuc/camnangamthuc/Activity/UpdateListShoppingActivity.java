package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.duan2.camnangamthuc.camnangamthuc.Model.ListShoping;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.duan2.camnangamthuc.camnangamthuc.SQLiteDatabase.SQLiteHandler;


public class UpdateListShoppingActivity extends AppCompatActivity {
    EditText edittieudeupdate,editnoidungupdate;
    Button bntluulaiupdatelist,bntdongupdatelist;
    Toolbar toolbar;
    private SQLiteHandler db;
    String id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_list_shopping);
        toolbar = (Toolbar) findViewById(R.id.toolbar_updatelistshopping);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        db = new SQLiteHandler(getApplicationContext());
        inti();
        GetInfomation();
    }
    private void inti(){
        edittieudeupdate = (EditText) findViewById(R.id.edittieudeupdate);
        editnoidungupdate = (EditText) findViewById(R.id.editnoidungupdate);
        bntluulaiupdatelist = (Button) findViewById(R.id.bntluulaiupdatelist);
        bntdongupdatelist = (Button) findViewById(R.id.bntdongupdatelist);
        bntluulaiupdatelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capnhatdulieu();
                Intent intent = new Intent(UpdateListShoppingActivity.this, ViewListShopping.class);
                intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        bntdongupdatelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void GetInfomation(){
        ListShoping listShoping = (ListShoping) getIntent().getSerializableExtra("thongtindanhsach");
        //
        id = listShoping.getId();
        //
        edittieudeupdate.setText(listShoping.getTitle());
        editnoidungupdate.setText(listShoping.getContent());
    }
    private void capnhatdulieu() {
        boolean kt = true;
        String Title = edittieudeupdate.getText().toString();
        String Content = editnoidungupdate.getText().toString();
        if (Title.isEmpty()) {
            edittieudeupdate.setError("Vui l??ng nh???p ti??u ?????");
            edittieudeupdate.requestFocus();
            kt = false;
        }
        if (Content.isEmpty()) {
            editnoidungupdate.setError("Vui l??ng nh???p n???i dung");
            editnoidungupdate.requestFocus();
            kt = false;
        }
        if (kt == true) {
            boolean bl = db.updateDataList(id,Title, Content);
            if (bl) {
                Toast.makeText(UpdateListShoppingActivity.this, "C???p nh???t th??nh c??ng", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UpdateListShoppingActivity.this, "C???p nh???t kh??ng th??nh c??ng", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
