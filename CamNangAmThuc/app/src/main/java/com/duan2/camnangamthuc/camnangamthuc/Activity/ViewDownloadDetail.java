package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duan2.camnangamthuc.camnangamthuc.Model.Download;
import com.duan2.camnangamthuc.camnangamthuc.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewDownloadDetail extends AppCompatActivity {
    Toolbar toolbar;
    CircleImageView imgviewdow;
    TextView toolbar_title_dow,titleviewdow,infodow,infoviewchitiet,happydow;
    ImageView imginfodow,imginfoviewdow;
    String id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_download_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar_dow_detaili);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        init();
        ChayToolBar();
        GetInfomation();
    }
    private void init(){
        imgviewdow = (CircleImageView)findViewById(R.id.imgviewdow);
        imginfodow = (ImageView)findViewById(R.id.imginfodow);
        imginfoviewdow = (ImageView)findViewById(R.id.imginfoviewdow);
        toolbar_title_dow = (TextView)findViewById(R.id.toolbar_title_dow);
        titleviewdow = (TextView)findViewById(R.id.titleviewdow);
        infodow = (TextView)findViewById(R.id.infodow);
        infoviewchitiet = (TextView)findViewById(R.id.infoviewchitiet);
        happydow = (TextView)findViewById(R.id.happydow);
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
    private void GetInfomation(){
        Download download = (Download) getIntent().getSerializableExtra("thongtindow");
        //
        id = download.getId();
        //
        Glide.with(getApplicationContext()).load(download.getImage()).apply(RequestOptions.circleCropTransform()).into(imgviewdow);

       Glide.with(getApplicationContext()).load(download.getImage()).into(imginfodow);
        infodow.setText(Html.fromHtml(download.getInfomation()));
       Glide.with(getApplicationContext()).load(download.getImageView()).into(imginfoviewdow);
        toolbar_title_dow.setText(download.getName());
        titleviewdow.setText(download.getName());
        infoviewchitiet.setText(Html.fromHtml(download.getInfomationView()));
        happydow.setText(download.getHappy());
    }
}
