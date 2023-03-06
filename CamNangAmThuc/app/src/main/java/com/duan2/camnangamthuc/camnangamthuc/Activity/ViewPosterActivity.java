package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duan2.camnangamthuc.camnangamthuc.Model.CheckInternet;
import com.duan2.camnangamthuc.camnangamthuc.Model.Common;
import com.duan2.camnangamthuc.camnangamthuc.Model.Community;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.duan2.camnangamthuc.camnangamthuc.SQLiteDatabase.SQLiteHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class ViewPosterActivity extends AppCompatActivity {
    TextView txttenviewposte,txtviewtextnguyenlieu,txtnguyenlieuviewposte,txtviewtextcongthuc,txtcongthucviewposte,
            txtnameuseviewposte,txtngaydangviewposte,toolbar_title_viewposte;
    ImageView imgageviewposter;
    CircleImageView imguseviewposte,imgviewpostetoobar;
    String StatusId="";
    FirebaseDatabase database;
    Toolbar toolbar;
    DatabaseReference viewcommnitylist;
    ProgressDialog pDialog;
    AlertDialog dialogwaching;
   FloatingActionButton fabchecklike;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_poster);
        database = FirebaseDatabase.getInstance();
        //Bọc dữ liệu Json
        viewcommnitylist = database.getReference("Communitys");
        toolbar = (Toolbar) findViewById(R.id.toolbar_viewposte);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        db = new SQLiteHandler(getApplicationContext());
        txttenviewposte = (TextView) findViewById(R.id.txttenviewposte);
        txtviewtextnguyenlieu = (TextView) findViewById(R.id.txtviewtextnguyenlieu);
        txtnguyenlieuviewposte = (TextView) findViewById(R.id.txtnguyenlieuviewposte);
        txtviewtextcongthuc = (TextView) findViewById(R.id.txtviewtextcongthuc);
        txtcongthucviewposte = (TextView) findViewById(R.id.txtcongthucviewposte);
        txtnameuseviewposte = (TextView) findViewById(R.id.txtnameuseviewposte);
        txtngaydangviewposte = (TextView) findViewById(R.id.txtngaydangviewposte);
        toolbar_title_viewposte = (TextView) findViewById(R.id.toolbar_title_viewposte);
        imgageviewposter = (ImageView) findViewById(R.id.imgageviewposter);
        imguseviewposte = (CircleImageView) findViewById(R.id.imguseviewposte);
        imgviewpostetoobar = (CircleImageView) findViewById(R.id.imgviewpostetoobar);
        fabchecklike = (FloatingActionButton) findViewById(R.id.fabchecklike);
        if (CheckInternet.haveNetworkConnection(this)){
            //load dialog
            dialogwaching = new SpotsDialog(ViewPosterActivity.this);
            dialogwaching.show();
            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    //get sự kiện inten từ Home
                    if(getIntent() !=null){
                        StatusId = getIntent().getStringExtra("StatusId");
                        if (!StatusId.isEmpty()) {
                            getFoodInfomation(StatusId);
                        }
                    }
                    ChayToolBar();
                }
            };
            //set thời gian load dialog
            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 1500);
        }else {
            CheckInternet.ThongBao(this,"Vui lòng kết nối internet");
        }
    }
    private void getFoodInfomation(final String statusId){
//        txtthongtintile.setTypeface(typeface);
        viewcommnitylist.child(statusId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Community community = dataSnapshot.getValue(Community.class);
                txttenviewposte.setText(community.getNamefood());
                txtnguyenlieuviewposte.setText(community.getResourcesfood());
                txtcongthucviewposte.setText(community.getRecipefood());
                txtnameuseviewposte.setText(community.getNameusefood());
                txtviewtextnguyenlieu.setText("Nguyên liệu");
                txtviewtextcongthuc.setText("Công thức");
                txtngaydangviewposte.setText(DateFormat.format("Đã đăng:"+"(HH:mm:ss) dd-MM-yyyy", community.getTimefood()));
                Glide.with(getApplicationContext()).load(community.getImageusefood()).apply(RequestOptions.circleCropTransform()).into(imguseviewposte);
                Glide.with(getApplicationContext()).load(Common.communityten.getImagefood()).apply(RequestOptions.circleCropTransform()).into(imgviewpostetoobar);
               Glide.with(getApplicationContext()).load(community.getImagefood()).into(imgageviewposter);
                toolbar_title_viewposte.setText(Common.communityten.getNamefood());
                toolbar_title_viewposte.setSingleLine(true);;
                toolbar_title_viewposte.setEllipsize(TextUtils.TruncateAt.END);
                dialogwaching.dismiss();
                if(db.isfavorite(community.getId())){
                    fabchecklike.setImageResource(R.drawable.ic_check_view_24dp);
                }
                fabchecklike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!db.isfavorite(community.getId())){
                            db.addtofavorite(community.getId(),community.getNamefood(),community.getImagefood(),
                                    community.getResourcesfood(),community.getRecipefood(),community.getNameusefood(),
                                    community.getEmailusefood(),community.getImageusefood(),community.getTimefood());
                            fabchecklike.setImageResource(R.drawable.ic_check_view_24dp);
                            Toast.makeText(ViewPosterActivity.this, "Đã đánh dấu", Toast.LENGTH_SHORT).show();
                        }else {
                            db.deletefavorite(community.getId());
                            fabchecklike.setImageResource(R.drawable.ic_check_black_24dp);
                            Toast.makeText(ViewPosterActivity.this, "Đã hủy đánh dấu", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
}
