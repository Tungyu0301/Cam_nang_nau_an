package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.duan2.camnangamthuc.camnangamthuc.Model.ListShoping;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.duan2.camnangamthuc.camnangamthuc.SQLiteDatabase.SQLiteHandler;

public class InformationViewListShopping extends AppCompatActivity {
    TextView txtviewtitlelistshopping,txtviewcontenlistshopping;
    Button bntdongviewlist;
    Toolbar toolbar;
    private SQLiteHandler db;
    String id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_view_list_shopping);
        toolbar = (Toolbar) findViewById(R.id.toolbar_infolistshopping);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        db = new SQLiteHandler(getApplicationContext());
        inti();
        GetInfomation();
    }
   private void inti(){
        txtviewtitlelistshopping = (TextView) findViewById(R.id.txtviewtitlelistshopping);
       txtviewcontenlistshopping = (TextView) findViewById(R.id.txtviewcontenlistshopping);
       bntdongviewlist = (Button) findViewById(R.id.bntdongviewlist);
       bntdongviewlist.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
       });
    }
    private void GetInfomation(){
        ListShoping listShoping = (ListShoping) getIntent().getSerializableExtra("thongtinchitietdanhsach");
        //
        id = listShoping.getId();
        //
        txtviewtitlelistshopping.setText(listShoping.getTitle());
        txtviewcontenlistshopping.setText(listShoping.getContent());
    }
}
