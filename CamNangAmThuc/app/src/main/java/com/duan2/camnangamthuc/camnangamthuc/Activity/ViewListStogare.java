package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.duan2.camnangamthuc.camnangamthuc.Adapter.ListStogareAdapter;
import com.duan2.camnangamthuc.camnangamthuc.Model.ListShoping;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.duan2.camnangamthuc.camnangamthuc.SQLiteDatabase.SQLiteHandler;

import java.util.ArrayList;

public class ViewListStogare extends AppCompatActivity {
    LinearLayout gv_liststogaredsdc,gv_liststogareluutru;
    TextView luutru;
    private SQLiteHandler db;
    private ArrayList<ListShoping> listShopings;
    private ListStogareAdapter listStogareAdapter;
    private ListView lvliststogare;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_stogare);
        toolbar = (Toolbar) findViewById(R.id.toolbar_liststogare);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        db = new SQLiteHandler(getApplicationContext());
        ChayToolBar();
        init();
        loaddulieu();
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

    private void init(){
        gv_liststogaredsdc = (LinearLayout) findViewById(R.id.gv_liststogaredsdc);
        gv_liststogareluutru = (LinearLayout) findViewById(R.id.gv_liststogareluutru);
        luutru =(TextView) findViewById(R.id.luutru);
        gv_liststogareluutru.setBackgroundResource(R.drawable.backgroud_viewlist);
        luutru.setTextColor(Color.parseColor("#ffffff"));
        lvliststogare = (ListView) findViewById(R.id.lvliststogare);
        gv_liststogaredsdc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewListStogare.this, ViewListShopping.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void loaddulieu(){
        listShopings= new ArrayList<>();
        listShopings = getListShopings();
        listStogareAdapter = new ListStogareAdapter(this, listShopings);
        lvliststogare.setAdapter(listStogareAdapter);
    }
    private ArrayList<ListShoping> getListShopings() {
        Cursor cursor = db.getAllStogare();
        ArrayList<ListShoping> listShopings = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String Title = cursor.getString(1);
            String Content = cursor.getString(2);
            ListShoping listShoping = new ListShoping(id,Title,Content);
            listShopings.add(listShoping);
        }
        return listShopings;
    }
    public void gotoliststogare(String id){
        db.deleteDataStogare(id);
        loaddulieu();
        Toast.makeText(ViewListStogare.this, "Đã chuyển qua mục danh sách đi chợ", Toast.LENGTH_SHORT).show();
    }
    public void deleteliststogare(String id){
        db.deleteDataStogare(id);
        loaddulieu();
        Toast.makeText(ViewListStogare.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.listshopping, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent intent = new Intent(ViewListStogare.this,AddlistShoppingActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
