package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duan2.camnangamthuc.camnangamthuc.Model.CheckInternet;
import com.duan2.camnangamthuc.camnangamthuc.Model.Common;
import com.duan2.camnangamthuc.camnangamthuc.Model.Users;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class AccountinformationActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference accountuse;
    Toolbar toolbar;
    String keyemail = "";
    TextView txtnameuseaccount, txtphoneuseaccount, txtemailaccount, txtngaysinhccount, txtmbmaccount,txtdiachiaccount;
    CircleImageView imgViewaccount;
    AlertDialog dialogwaching;
    Button btn_xoatkaccount,btn_editaccount;
    AlertDialog.Builder builder;
    AlertDialog b;
    Uri filePath;
    CircleImageView imgVieweditaccount;
    private final int PICK_IMAGE_REQUEST = 7171;
    ProgressDialog pDialog;
    StorageReference storageReference;
    FirebaseStorage storage;
    EditText edtngaysinhaccount,edtdiachiaccount,edtnameaccount,edtmabmaccount;
    Dialog picker;
    DatePicker datep;
    Integer month, day, year;
    Button set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountinformation);
        database = FirebaseDatabase.getInstance();
        //Bọc dữ liệu Json
        accountuse = database.getReference("Users");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        toolbar = (Toolbar) findViewById(R.id.toolbartttk);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        txtnameuseaccount = (TextView) findViewById(R.id.txtnameuseaccount);
        txtphoneuseaccount = (TextView) findViewById(R.id.txtphoneuseaccount);
        txtemailaccount = (TextView) findViewById(R.id.txtemailaccount);
        txtngaysinhccount = (TextView) findViewById(R.id.txtngaysinhccount);
        txtmbmaccount = (TextView) findViewById(R.id.txtmbmaccount);
        txtdiachiaccount = (TextView) findViewById(R.id.txtdiachiaccount);
        imgViewaccount = (CircleImageView) findViewById(R.id.imgViewaccount);
        btn_xoatkaccount = (Button) findViewById(R.id.btn_xoatkaccount);
        btn_editaccount = (Button) findViewById(R.id.btn_editaccount);
        if (CheckInternet.haveNetworkConnection(this)) {
            //load dialog
            dialogwaching = new SpotsDialog(AccountinformationActivity.this);
            dialogwaching.show();
            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    //get sự kiện inten từ Home
                    if (getIntent() != null) {
                        keyemail = getIntent().getStringExtra("KeyEmail");
                        if (!keyemail.isEmpty()) {
                            getAccount(keyemail);
                        }
                    }
                    ChayToolBar();
                }
            };
            //set thời gian load dialog
            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 1500);
        } else {
            CheckInternet.ThongBao(this, "Vui lòng kết nối internet");
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

    private void getAccount(final String keyemail) {
//        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/JustDieAlready.ttf");
//        txtthongtintile.setTypeface(typeface);
        accountuse.orderByChild("email").equalTo(keyemail).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                final Users users = dataSnapshot.getValue(Users.class);
                txtnameuseaccount.setText(users.getName());
                txtphoneuseaccount.setText(users.getPhone());
                txtemailaccount.setText(users.getEmail());
                txtngaysinhccount.setText(users.getNgaysinh());
                txtdiachiaccount.setText(users.getDiachi());
                txtmbmaccount.setText(users.getCode());
                Glide.with(getApplicationContext()).load(users.getImage()).apply(RequestOptions.circleCropTransform()).into(imgViewaccount);
                dialogwaching.dismiss();
                btn_editaccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updatett(dataSnapshot.getRef().getKey(),Common.userten);
                    }
                });
                btn_xoatkaccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String xoa = "Bạn có muốn xóa tài khoản của <font color='blue'> <Strong>"+ "mình"+ "</Strong></font> ra khỏi danh sách không";
                        AlertDialog.Builder dialogxoa = new AlertDialog.Builder(AccountinformationActivity.this);
                        dialogxoa.setTitle("Xóa tài khỏan");
                        dialogxoa.setIcon(R.drawable.ic_delete_use);
                        dialogxoa.setMessage(Html.fromHtml(xoa));
                        dialogxoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //lấy vị trí hiện tại
                                deletetk(dataSnapshot.getRef().getKey());
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

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void deletetk(String key){
        //xóa vị trí đã lấy ra khỏi database
        accountuse.child(key).removeValue();
        Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
        Intent homeinteen = new Intent(AccountinformationActivity.this,Home.class);
        homeinteen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeinteen);
    }
    private void updatett(final String key, final Users userten) {
        builder = new AlertDialog.Builder(AccountinformationActivity.this);
        builder.setTitle("Chỉnh sửa thông tin");
        builder.setMessage("Nhập thông tin");
        LayoutInflater layoutInflater = AccountinformationActivity.this.getLayoutInflater();
        final View capnhat = layoutInflater.inflate(R.layout.item_edit_account,null);
        edtnameaccount = (EditText) capnhat.findViewById(R.id.edtnameaccount);
        edtngaysinhaccount = (EditText) capnhat.findViewById(R.id.edtngaysinhaccount);
        edtdiachiaccount = (EditText) capnhat.findViewById(R.id.edtdiachiaccount);
         edtmabmaccount = (EditText) capnhat.findViewById(R.id.edtmabmaccount);
        final Button btnsendsave = (Button) capnhat.findViewById(R.id.btnsendsave);
        final ImageView iconphotoaccount = (ImageView) capnhat.findViewById(R.id.iconphotoaccount);
        imgVieweditaccount = (CircleImageView)capnhat.findViewById(R.id.imgVieweditaccount);
        edtngaysinhaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker = new Dialog(AccountinformationActivity.this);
                picker.setContentView(R.layout.ngaygio);
                picker.setTitle("Chọn ngày tháng năm sinh");
                datep = (DatePicker) picker.findViewById(R.id.datePicker);
                set = (Button) picker.findViewById(R.id.btnSet);
                //picker thời gian
                set.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO Auto-generated method stub
                        month = datep.getMonth();
                        day = datep.getDayOfMonth();
                        year = datep.getYear();
                        edtngaysinhaccount.setText(year + "-" + month + "-"
                                + day);
                        picker.dismiss();
                    }
                });
                picker.show();
            }
        });
        edtnameaccount.setText(userten.getName());
        edtmabmaccount.setText(userten.getCode());
        edtngaysinhaccount.setText(userten.getNgaysinh());
        edtdiachiaccount.setText(userten.getDiachi());
        Glide.with(getApplicationContext()).load(userten.getImage()).apply(RequestOptions.circleCropTransform()).into(imgVieweditaccount);
        builder.setView(capnhat);
        builder.setIcon(R.drawable.ic_vpn_key_black_24dp);
        b = builder.create();
        b.show();
        iconphotoaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        btnsendsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userten.setName(edtnameaccount.getText().toString());
                userten.setNgaysinh(edtngaysinhaccount.getText().toString());
                userten.setDiachi(edtdiachiaccount.getText().toString());
                userten.setCode(edtmabmaccount.getText().toString());
                Uploadimage(key,userten);
                accountuse.child(key).setValue(userten);
                Toast.makeText(AccountinformationActivity.this, "Cập nhật thành công !!!", Toast.LENGTH_SHORT).show();
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
                imgVieweditaccount.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }
    private void Uploadimage(final String key, final Users users){
        if (filePath != null) {
                    pDialog = new ProgressDialog(AccountinformationActivity.this);
                    pDialog.setTitle("Đang cập nhật");
                    pDialog.show();
                    final StorageReference imageFolder = storageReference.child("images/" + UUID.randomUUID().toString());
                    imageFolder.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    users.setImage(uri.toString());
                                    accountuse.child(key).setValue(users);
                                }
                            });
                            pDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pDialog.dismiss();
                            Toast.makeText(AccountinformationActivity.this, "Lỗi "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
