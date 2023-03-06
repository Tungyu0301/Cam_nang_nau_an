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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.duan2.camnangamthuc.camnangamthuc.Adapter.ListShoppingAdapter;
import com.duan2.camnangamthuc.camnangamthuc.Model.ListShoping;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.duan2.camnangamthuc.camnangamthuc.SQLiteDatabase.SQLiteHandler;

import java.util.ArrayList;

public class ViewListShopping extends AppCompatActivity {
    LinearLayout gvdanhsach,gvluutru;
    TextView txtdsdc;
    private SQLiteHandler db;
    private ArrayList<ListShoping> listShopings;
    private ListShoppingAdapter listShoppingAdapter;
    private ListView lvsotaydicho;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_shopping);
        toolbar = (Toolbar) findViewById(R.id.toolbar_listshopping);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        db = new SQLiteHandler(getApplicationContext());
        init();
        loaddulieu();
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
    private void init(){
        gvdanhsach = (LinearLayout) findViewById(R.id.gv_danhsachdicho);
        gvluutru = (LinearLayout) findViewById(R.id.gv_luutru);
        txtdsdc =(TextView) findViewById(R.id.txtdsdc);
        gvdanhsach.setBackgroundResource(R.drawable.backgroud_viewlist);
        txtdsdc.setTextColor(Color.parseColor("#ffffff"));
        lvsotaydicho = (ListView) findViewById(R.id.lvsotaydicho);
        gvluutru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewListShopping.this, ViewListStogare.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void loaddulieu(){
        listShopings= new ArrayList<>();
        listShopings = getListShopings();
        listShoppingAdapter = new ListShoppingAdapter(this, listShopings);
        lvsotaydicho.setAdapter(listShoppingAdapter);
        lvsotaydicho.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ViewListShopping.this, UpdateListShoppingActivity.class);
                intent.putExtra("thongtindanhsach",listShopings.get(i));
                startActivity(intent);
            }
        });
    }
    private ArrayList<ListShoping> getListShopings() {
        Cursor cursor = db.getAllDataDanhSach();
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
            Intent intent = new Intent(ViewListShopping.this,AddlistShoppingActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void deletelistshopping(String id){
        db.deleteDataList(id);
        loaddulieu();
        Toast.makeText(ViewListShopping.this, "Đã chuyển qua mục lưu trữ", Toast.LENGTH_SHORT).show();
    }
}
