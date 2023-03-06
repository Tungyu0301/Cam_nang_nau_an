package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duan2.camnangamthuc.camnangamthuc.Model.Favorite;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.duan2.camnangamthuc.camnangamthuc.SQLiteDatabase.SQLiteHandler;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewFavoriteInformationActivity extends AppCompatActivity {
    Toolbar toolbar;
    CircleImageView imgFavoriteInfotoobar,imguseFavoriteInfo;
    TextView toolbar_title_FavoriteInfo,txttenFavoriteInfo,txtviewnguyenlieu,txtnguyenlieuFavoriteInfo,txtviewcongthuc,
            txtcongthucFavoriteInfo,txtnameuseFavoriteInfo,txtngaydangFavoriteInfo;
    ImageView imgageFavoriteInfor;
    String id = "";
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_favorite_information);
        toolbar = (Toolbar) findViewById(R.id.toolbar_FavoriteInfo);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        db = new SQLiteHandler(getApplicationContext());
        init();
        ChayToolBar();
        GetInfomation();
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
        imgageFavoriteInfor = (ImageView) findViewById(R.id.imgageFavoriteInfor);
        imgFavoriteInfotoobar = (CircleImageView)findViewById(R.id.imgFavoriteInfotoobar);
        imguseFavoriteInfo = (CircleImageView)findViewById(R.id.imguseFavoriteInfo);
        toolbar_title_FavoriteInfo = (TextView)findViewById(R.id.toolbar_title_FavoriteInfo);
        txttenFavoriteInfo = (TextView)findViewById(R.id.txttenFavoriteInfo);
        txtviewnguyenlieu = (TextView)findViewById(R.id.txtviewnguyenlieu);
        txtnguyenlieuFavoriteInfo = (TextView)findViewById(R.id.txtnguyenlieuFavoriteInfo);
        txtviewcongthuc = (TextView)findViewById(R.id.txtviewcongthuc);
        txtcongthucFavoriteInfo = (TextView)findViewById(R.id.txtcongthucFavoriteInfo);
        txtnameuseFavoriteInfo = (TextView)findViewById(R.id.txtnameuseFavoriteInfo);
        txtngaydangFavoriteInfo = (TextView)findViewById(R.id.txtngaydangFavoriteInfo);
    }
    private void GetInfomation(){
        Favorite favorite = (Favorite) getIntent().getSerializableExtra("thongtinfavorite");
        //
        id = favorite.getId();
        //
        Glide.with(getApplicationContext()).load(favorite.getImagefood()).apply(RequestOptions.circleCropTransform()).into(imgFavoriteInfotoobar);

       Glide.with(getApplicationContext()).load(favorite.getImagefood()).into(imgageFavoriteInfor);
        txtviewnguyenlieu.setText("Nguyên liệu");
        txtviewcongthuc.setText("Công thức");
        toolbar_title_FavoriteInfo.setText(favorite.getNamefood());
        txttenFavoriteInfo.setText(favorite.getNamefood());
        txtnguyenlieuFavoriteInfo.setText(favorite.getResourcesfood());
        txtcongthucFavoriteInfo.setText(favorite.getRecipefood());
        txtnameuseFavoriteInfo.setText(favorite.getNameusefood());
        txtngaydangFavoriteInfo.setText(DateFormat.format("Đã đăng:"+"(HH:mm:ss) dd-MM-yyyy", favorite.getTimefood()));
        Glide.with(getApplicationContext()).load(favorite.getImageusefood()).apply(RequestOptions.circleCropTransform()).into(imguseFavoriteInfo);
    }
}
