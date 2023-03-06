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

import com.duan2.camnangamthuc.camnangamthuc.Adapter.FavoriteAdapter;
import com.duan2.camnangamthuc.camnangamthuc.Model.Favorite;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.duan2.camnangamthuc.camnangamthuc.SQLiteDatabase.SQLiteHandler;

import java.util.ArrayList;

public class FavoriteViewActivity extends AppCompatActivity {
    private SQLiteHandler db;
    private ArrayList<Favorite> favorites;
    private FavoriteAdapter favoriteAdapter;
    private ListView lvfavorite;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar_favorite);
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
    public void loaddulieu() {
        lvfavorite = (ListView) findViewById(R.id.lvfavorite);
        favorites = new ArrayList<>();
        favorites = getFavoriteInfomations();
        favoriteAdapter = new FavoriteAdapter(this, favorites);
        lvfavorite.setAdapter(favoriteAdapter);
        lvfavorite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(FavoriteViewActivity.this, ViewFavoriteInformationActivity.class);
                intent.putExtra("thongtinfavorite", favorites.get(i));
                startActivity(intent);
            }
        });
    }
    private ArrayList<Favorite> getFavoriteInfomations() {
        Cursor cursor = db.getAllfavorite();
        ArrayList<Favorite> favorites = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String keyid = cursor.getString(1);
            String namefood = cursor.getString(2);
            String imagefood = cursor.getString(3);
            String resourcesfood = cursor.getString(4);
            String recipefood = cursor.getString(5);
            String nameusefood = cursor.getString(6);
            String emailusefood = cursor.getString(7);
            String imageusefood = cursor.getString(8);
            long timefood = cursor.getLong(9);
            Favorite favorite = new Favorite(id,keyid,namefood,imagefood,resourcesfood,recipefood,nameusefood
            ,emailusefood,imageusefood,timefood);
            favorites.add(favorite);
        }
        return favorites;
    }
    public void deletedow(String keyid){
        db.deletefavorite(keyid);
        loaddulieu();
        Toast.makeText(this, "Đã bỏ đánh dấu", Toast.LENGTH_SHORT).show();
    }
}
