package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duan2.camnangamthuc.camnangamthuc.Adapter.ViewStatusHoder;
import com.duan2.camnangamthuc.camnangamthuc.Interface.ItemClickListerner;
import com.duan2.camnangamthuc.camnangamthuc.Model.CheckInternet;
import com.duan2.camnangamthuc.camnangamthuc.Model.Common;
import com.duan2.camnangamthuc.camnangamthuc.Model.Community;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class PostedarticleActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference statuslist;
    String statusEmail="";
    Toolbar toolbar;
    FirebaseRecyclerAdapter<Community,ViewStatusHoder> adapter;
    ProgressDialog pDialog;
    AlertDialog dialogwaching;
    Button btn_xoatkaccount,btn_editaccount;
    AlertDialog.Builder builder;
    AlertDialog b;
    EditText edttenmonanuse,edtnguyenlieuuse,edtcongthucuse;
    ImageView imgViewadduse;
    Uri filePath;
    private final int PICK_IMAGE_REQUEST = 7171;
    StorageReference storageReference;
    FirebaseStorage storage;
    boolean like = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postedarticle);
        database = FirebaseDatabase.getInstance();
        //Bọc dữ liệu Json
        statuslist = database.getReference("Communitys");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        toolbar = (Toolbar) findViewById(R.id.toolbar_status);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recystatus);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if (CheckInternet.haveNetworkConnection(this)){
            //load dialog
            dialogwaching = new SpotsDialog(PostedarticleActivity.this);
            dialogwaching.show();
            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    //get sự kiện inten từ Home
                    if(getIntent() !=null){
                        statusEmail = getIntent().getStringExtra("StatusEmail");
                        if (!statusEmail.isEmpty()) {
                            loadliststatus(statusEmail);
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
    //đọc dữ liệu bai viết đã đăng
    private void loadliststatus(String statusEmail) {
        adapter = new FirebaseRecyclerAdapter<Community,ViewStatusHoder>(Community.class,R.layout.item_view_status,ViewStatusHoder.class,
                statuslist.orderByChild("emailusefood").equalTo(statusEmail)){//tìm kiếm : select * from Food where emailusefood
            @Override
            protected void populateViewHolder(ViewStatusHoder viewHolder, final Community model, final int position) {
                final String keylike = getRef(position).getKey();
                viewHolder.txtnamefoodstatus.setText(model.getNamefood());
                viewHolder.txtnamefoodstatus.setMaxLines(1);
                viewHolder.txtnamefoodstatus.setEllipsize(TextUtils.TruncateAt.END);
                viewHolder.txtnamestatus.setText(model.getNameusefood());
                viewHolder.likecountstatus.setText(Integer.toString(model.likecount));
                viewHolder.txtngaydangstatus.setText(DateFormat.format("Đã đăng:"+"(HH:mm:ss) dd-MM-yyyy", model.getTimefood()));
                viewHolder.statusview.setText(model.getStatusfood());
                if(viewHolder.statusview.getText().toString().equalsIgnoreCase("Đang chờ phê duyệt")){
                    viewHolder.statusview.setTextColor(Color.parseColor("#f10619"));
                }else {
                    viewHolder.statusview.setTextColor(Color.parseColor("#42D752"));
                }
               Glide.with(getApplicationContext()).load(model.getImagefood()).into(viewHolder.imageviewstatus);
                Glide.with(getApplicationContext()).load(model.getImageusefood()).apply(RequestOptions.circleCropTransform()).into(viewHolder.imageusestatus);
                viewHolder.setColorLike(keylike);
                final Community community = model;
                viewHolder.setItemListener(new ItemClickListerner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodinfoIntent = new Intent(PostedarticleActivity.this,ViewPosterActivity.class);
                        Common.communityten = community;
                        //lấy id của Category là key,vì vậy lấy key để chỉ item
                        foodinfoIntent.putExtra("StatusId",adapter.getRef(position).getKey());
                        startActivity(foodinfoIntent);
                    }
                });
                //sự kiện comment
                viewHolder.commentfoodstatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent commentintent = new Intent(PostedarticleActivity.this,CommentActivity.class);
                        Common.communityten = model;
                        //lấy id của Category là key,vì vậy lấy key để chỉ item
                        commentintent.putExtra("commentId",adapter.getRef(position).getKey());
                        startActivity(commentintent);
                    }
                });
                //sự kiện like
                viewHolder.likefoodstatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        like = true;
                        statuslist.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(like){
                                    int likecount =0;
                                    if(dataSnapshot.child(keylike).hasChild(Common.userten.getId())){
                                        statuslist.child(keylike).child(Common.userten.getId()).removeValue();
                                        likecount = dataSnapshot.child(keylike).child("likecount").getValue(Integer.class);
                                        statuslist.child(model.getId()).child("likecount").setValue(likecount-1);
                                        like = false;
                                    }else {
                                        statuslist.child(keylike).child(Common.userten.getId()).setValue("Like");
                                        likecount = dataSnapshot.child(keylike).child("likecount").getValue(Integer.class);
                                        statuslist.child(keylike).child("likecount").setValue(likecount+1);
                                        like = false;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
                //sự kiện chỉnh sửa
                viewHolder.editstatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updatetstatus(adapter.getRef(position).getKey(),adapter.getItem(position));
                    }
                });
                //sự kiện xóa
                viewHolder.deletestatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String xoa = "Bạn có muốn xóa bài viết <font color='blue'> <Strong>"+model.getNamefood() + "</Strong></font> ra khỏi danh sách không";
                        AlertDialog.Builder dialogxoa = new AlertDialog.Builder(PostedarticleActivity.this);
                        dialogxoa.setTitle("Xóa tài bài viết");
                        dialogxoa.setIcon(R.drawable.ic_deletestatus);
                        dialogxoa.setMessage(Html.fromHtml(xoa));
                        dialogxoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //lấy vị trí hiện tại
                                deletestatus(adapter.getRef(position).getKey());
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
            }
        };
        //set adapter
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        dialogwaching.dismiss();
    }
//xóa
    private void deletestatus(String key) {
        //xóa vị trí đã lấy ra khỏi database
        statuslist.child(key).removeValue();
        Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
    }

    //sửa thông tin
    private void updatetstatus(final String key, final Community item) {
        builder = new AlertDialog.Builder(PostedarticleActivity.this);
        builder.setTitle("Chỉnh sữa bài viết chia sẽ");
        builder.setMessage("Nhập thông tin chỉnh sữa bên dưới");
        LayoutInflater layoutInflater = PostedarticleActivity.this.getLayoutInflater();
        final View addfood = layoutInflater.inflate(R.layout.add_congdonguse, null);
        imgViewadduse= (ImageView) addfood.findViewById(R.id.imgViewadduse);
        edttenmonanuse = (EditText) addfood.findViewById(R.id.edttenmonanuse);
        edtnguyenlieuuse = (EditText) addfood.findViewById(R.id.edtnguyenlieuuse);
        edtcongthucuse = (EditText) addfood.findViewById(R.id.edtcongthucuse);
        final Button bntchonhinh = (Button) addfood.findViewById(R.id.iconphotoadduse);
        final Button bntdangbai = (Button) addfood.findViewById(R.id.btndangbaiviet);
        builder.setView(addfood);
        builder.setIcon(R.drawable.ic_editstatus);
        final AlertDialog b = builder.create();
        b.show();
        edttenmonanuse.setText(item.getNamefood());
        edtnguyenlieuuse.setText(item.getResourcesfood());
        edtcongthucuse.setText(item.getRecipefood());
        final String name = Common.userten.getName();
        final String image = Common.userten.getImage();
       Glide.with(getApplicationContext()).load(item.getImagefood()).into(imgViewadduse);
        bntchonhinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        bntdangbai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setNameusefood(name);
                item.setImageusefood(image);
                item.setNamefood(edttenmonanuse.getText().toString());
                item.setResourcesfood(edtnguyenlieuuse.getText().toString());
                item.setRecipefood(edtcongthucuse.getText().toString());
                Uploadimage(key,item);
                statuslist.child(key).setValue(item);
                Toast.makeText(PostedarticleActivity.this, "Cập nhật thành công !!!", Toast.LENGTH_SHORT).show();
                b.dismiss();
            }
        });
    }
    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Chọn hình ảnh"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() !=null){
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgViewadduse.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }
    //cập nhật hình ảnh
    private void Uploadimage(final String key, final Community community){
        if (filePath != null) {
            pDialog = new ProgressDialog(PostedarticleActivity.this);
            pDialog.setTitle("Đang cập nhật");
            pDialog.show();
            final StorageReference imageFolder = storageReference.child("images/" + UUID.randomUUID().toString());
            imageFolder.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            community.setImagefood(uri.toString());
                            statuslist.child(key).setValue(community);
                        }
                    });
                    pDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pDialog.dismiss();
                    Toast.makeText(PostedarticleActivity.this, "Lỗi "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("AAAA",e.getMessage());
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double proga = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    pDialog.setMessage("Vui lòng đợi " + (int)proga + "%");
                }
            });
        }
    }

}
