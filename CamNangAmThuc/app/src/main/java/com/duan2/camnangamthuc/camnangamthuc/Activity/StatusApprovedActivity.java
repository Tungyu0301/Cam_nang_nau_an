package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duan2.camnangamthuc.camnangamthuc.Adapter.ViewApproved;
import com.duan2.camnangamthuc.camnangamthuc.Interface.ItemClickListerner;
import com.duan2.camnangamthuc.camnangamthuc.Model.CheckInternet;
import com.duan2.camnangamthuc.camnangamthuc.Model.Common;
import com.duan2.camnangamthuc.camnangamthuc.Model.Community;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class StatusApprovedActivity extends AppCompatActivity {
    String statusfood = "Đang chờ phê duyệt";
    AlertDialog.Builder builder;
    AlertDialog dialogwaching;
    FirebaseDatabase database;
    DatabaseReference approvedlist;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Community,ViewApproved> adapter;
    Toolbar toolbar;
    AlertDialog b;
    Spinner spinner;
    String updatestatusfood = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_approved);
        database = FirebaseDatabase.getInstance();
        //Bọc dữ liệu Json
        approvedlist = database.getReference("Communitys");
        toolbar = (Toolbar) findViewById(R.id.toolbar_statusApproved);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView)findViewById(R.id.recystatusApproved);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(),
                R.anim.layout_fall);
        recyclerView.setLayoutAnimation(controller);

        //chạy khi có internet
        if (CheckInternet.haveNetworkConnection(this)){
            //load dialog
            dialogwaching = new SpotsDialog(StatusApprovedActivity.this);
            dialogwaching.show();
            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    if (!statusfood.isEmpty()) {
                        loadMenuApproved(statusfood);
                    }
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
    //lấy dữ liệu tên và img đổ ra màng hình
    private void loadMenuApproved(String statusfood) {
        adapter = new FirebaseRecyclerAdapter<Community,ViewApproved>(Community.class,R.layout.item_approved_status,ViewApproved.class,
                approvedlist.orderByChild("statusfood").equalTo(statusfood)){//tìm kiếm : select * from Food where emailusefood
            @Override
            protected void populateViewHolder(ViewApproved viewHolder, final Community model, final int position) {
                viewHolder.txtnamefoodstatusApproved.setText(model.getNamefood());
                viewHolder.txtnamefoodstatusApproved.setMaxLines(1);
                viewHolder.txtnamefoodstatusApproved.setEllipsize(TextUtils.TruncateAt.END);
                viewHolder.txtnamestatusApproved.setText(model.getNameusefood());
                viewHolder.statusviewApproved.setText(model.getStatusfood());
                viewHolder.txtngaydangstatusApproved.setText(DateFormat.format("Đã đăng:"+"(HH:mm:ss) dd-MM-yyyy", model.getTimefood()));
               Glide.with(getApplicationContext()).load(model.getImagefood()).into(viewHolder.imageviewstatusApproved);
                Glide.with(getApplicationContext()).load(model.getImageusefood()).apply(RequestOptions.circleCropTransform()).into(viewHolder.imageusestatusApproved);
                final Community community = model;
                viewHolder.setItemListener(new ItemClickListerner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodinfoIntent = new Intent(StatusApprovedActivity.this,ViewPosterActivity.class);
                        Common.communityten = community;
                        //lấy id của Category là key,vì vậy lấy key để chỉ item
                        foodinfoIntent.putExtra("StatusId",adapter.getRef(position).getKey());
                        startActivity(foodinfoIntent);
                    }
                });
                viewHolder.deletefoodstatusApproved.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String xoa = "Bạn có muốn xóa bài viết <font color='green'> <Strong>"+model.getNamefood() + "</Strong></font> ra khỏi danh sách không";
                        AlertDialog.Builder dialogxoa = new AlertDialog.Builder(StatusApprovedActivity.this);
                        dialogxoa.setTitle("Xóa tài bài viết");
                        dialogxoa.setIcon(R.drawable.ic_delete_approved);
                        dialogxoa.setMessage(Html.fromHtml(xoa));
                        dialogxoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //lấy vị trí hiện tại
                                deletestatusadmin(adapter.getRef(position).getKey());
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
                viewHolder.foodstatusApproved.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder = new AlertDialog.Builder(StatusApprovedActivity.this);
                        builder.setTitle("Phê duyệt bài viết");
                        builder.setMessage("Vui lòng chọn đã phê duyệt để phê duyệt bài viết");
                        LayoutInflater layoutInflater = StatusApprovedActivity.this.getLayoutInflater();
                        final View updateapp = layoutInflater.inflate(R.layout.item_approved_public,null);
                        spinner = (Spinner) updateapp.findViewById(R.id.spinerupdate);
                        final Button bntupdate = (Button) updateapp.findViewById(R.id.btnupdateapproved);
                        builder.setView(updateapp);
                        builder.setIcon(R.drawable.ic_approved);
                        b = builder.create();
                        b.show();
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch (i){
                                    case 0:
                                        updatestatusfood = "Đang chờ phê duyệt";
                                        break;
                                    case 1:
                                        updatestatusfood = "Đã phê duyệt";
                                        break;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                        bntupdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final AlertDialog dialogwaching = new SpotsDialog(StatusApprovedActivity.this);
                                dialogwaching.show();
                                        final Map<String, Object> pheduyet = new HashMap<>();
                                pheduyet.put("statusfood", updatestatusfood);
                                        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Communitys");
                                reference.child(adapter.getRef(position).getKey()).updateChildren(pheduyet)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                b.dismiss();
                                                dialogwaching.dismiss();
                                                Toast.makeText(StatusApprovedActivity.this, "Đã phê duyệt", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(StatusApprovedActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        };
        //set adapter
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        dialogwaching.dismiss();
    }

    private void deletestatusadmin(String key) {
        //xóa vị trí đã lấy ra khỏi database
        approvedlist.child(key).removeValue();
        Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
        Intent homeinteen = new Intent(StatusApprovedActivity.this,CommunityAdminActivity.class);
        startActivity(homeinteen);
    }
}
