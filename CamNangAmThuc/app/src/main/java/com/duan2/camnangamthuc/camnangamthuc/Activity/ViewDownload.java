package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.duan2.camnangamthuc.camnangamthuc.Adapter.DownloadAdapter;
import com.duan2.camnangamthuc.camnangamthuc.Model.Download;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.duan2.camnangamthuc.camnangamthuc.SQLiteDatabase.SQLiteHandler;

import java.util.ArrayList;

public class ViewDownload extends AppCompatActivity {
    private SQLiteHandler db;
    private ArrayList<Download> downloads;
    private DownloadAdapter downloadAdapter;
    private ListView lvdow;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_download);
        toolbar = (Toolbar) findViewById(R.id.toolbar_dowload);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        db = new SQLiteHandler(getApplicationContext());
        ChayToolBar();
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
    public void loaddulieu(){
        lvdow = (ListView) findViewById(R.id.lvdow);
        downloads= new ArrayList<>();
        downloads = getFoodInfomations();
        downloadAdapter = new DownloadAdapter(this, downloads);
        lvdow.setAdapter(downloadAdapter);
        lvdow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ViewDownload.this, ViewDownloadDetail.class);
                intent.putExtra("thongtindow",downloads.get(i));
                startActivity(intent);
            }
        });

    }
    private ArrayList<Download> getFoodInfomations() {
        Cursor cursor = db.getAllData();
        ArrayList<Download> downloads = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String Name = cursor.getString(1);
            String Image = cursor.getString(2);
            String ImageView = cursor.getString(3);
            String Infomation = cursor.getString(4);
            String InfomationView = cursor.getString(5);
            String Happy = cursor.getString(6);
            Download download = new Download(id,Name,Image,ImageView,Infomation,InfomationView,Happy);
            downloads.add(download);
        }
        return downloads;
    }
    public void deletedow(String id){
        db.deleteDatadow(id);
        loaddulieu();
        Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
    }
}
