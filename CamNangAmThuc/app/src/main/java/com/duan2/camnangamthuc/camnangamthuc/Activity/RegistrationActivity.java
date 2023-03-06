package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.duan2.camnangamthuc.camnangamthuc.Model.Users;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference userslist;
    Toolbar toolbar;
    EditText editname,editphone,editemail,editpass,editcoderes;
    Button btnRegister;
    CircleImageView imgAvatar;
    ImageView iconphoto;
    ProgressDialog pDialog;
    Uri filePath;
    Users newuUsers;
    Bitmap bitmap;
    private final int PICK_IMAGE_REQUEST = 7171;
    FirebaseStorage storage;
    StorageReference storageReference;
    String role = "user";
    FirebaseAuth firebaseAuth;
    String codesen ;
    AlertDialog.Builder builder;
    ProgressBar progressBar;
    EditText editxatnhan;
    AlertDialog b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        database = FirebaseDatabase.getInstance();
        //Bọc dữ liệu Json
        firebaseAuth = FirebaseAuth.getInstance();
       userslist = database.getReference("Users");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        toolbar = (Toolbar) findViewById(R.id.toolbar_add_user);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        editname = (EditText) findViewById(R.id.edtName);
        editcoderes = (EditText) findViewById(R.id.edtcodereset);
        editphone = (EditText)findViewById(R.id.edtPhone);
        editemail = (EditText)findViewById(R.id.edtEmail);
        editpass = (EditText)findViewById(R.id.edtpassword);
        btnRegister =(Button) findViewById(R.id.btnRegister);
        imgAvatar = (CircleImageView) findViewById(R.id.imgAvatar);
        iconphoto = (ImageView) findViewById(R.id.iconphoto);
        iconphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("pressed");
                boolean kt = true;
                String name = editname.getText().toString();
                String phone = editphone.getText().toString();
                String email = editemail.getText().toString();
                String pass = editpass.getText().toString();
                if(name.isEmpty()){
                    editname.setError("Vui lòng nhập tên");
                    editname.requestFocus();
                    kt = false;
                }
                if(name.isEmpty()){
                    editname.setError("Vui lòng nhập tên");
                    editname.requestFocus();
                    kt = false;
                }
                if(phone.isEmpty()){
                    editphone.setError("Vui lòng nhập SĐT");
                    editphone.requestFocus();
                    kt = false;
                }
                if(email.isEmpty()){
                    editemail.setError("Vui lòng nhập email");
                    editemail.requestFocus();
                    kt = false;
                }
                if(!editemail.getText().toString().trim().contains("@")){
                    editemail.setError("Vui lòng nhập đúng dạng mail");
                    editemail.requestFocus();
                    kt = false;
                }
                if(pass.isEmpty()){
                    editpass.setError("Vui lòng nhập mật khẩu");
                    editpass.requestFocus();
                    kt = false;
                }
//                if(kt)
//                {
//                    Toast.makeText(RegistrationActivity.this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
//                    newuUsers = new Users();
//                    String id = userslist.push().getKey();
//                    newuUsers.setName(editname.getText().toString());
//                    newuUsers.setName(editname.getText().toString());
//                    newuUsers.setPhone(editphone.getText().toString());
//                    newuUsers.setEmail(editemail.getText().toString());
//                    newuUsers.setPassword(editpass.getText().toString());
//                    newuUsers.setCode(editcoderes.getText().toString());
//                    newuUsers.setRole(role);
//                    newuUsers.setImage("uri hinh");
//                    newuUsers.setId(id);
//                    if(newuUsers !=null){
//                        userslist.child(id).setValue(newuUsers);
//                    }
//                    Intent loginIntent=new Intent(RegistrationActivity.this,LoginActivity.class);
//                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(loginIntent);
//                    //them vo firebase
//                    //nhay ve trang dang nhap
//                }
                    if (filePath != null) {
                    pDialog = new ProgressDialog(RegistrationActivity.this);
                    pDialog.setTitle("Đang đăng ký");
                    pDialog.show();
                    final StorageReference imageFolder = storageReference.child("images/" + UUID.randomUUID().toString());
                    imageFolder.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    newuUsers = new Users();
                                    String id = userslist.push().getKey();
                                    newuUsers.setName(editname.getText().toString());
                                    newuUsers.setPhone(editphone.getText().toString());
                                    newuUsers.setEmail(editemail.getText().toString());
                                    newuUsers.setPassword(editpass.getText().toString());
                                    newuUsers.setCode(editcoderes.getText().toString());
                                    newuUsers.setRole(role);
                                    newuUsers.setImage(uri.toString());
                                    newuUsers.setId(id);
                                    if(newuUsers !=null){
                                        userslist.push().setValue(newuUsers);
                                    }
                                }
                            });
                            pDialog.dismiss();
                            Intent loginIntent=new Intent(RegistrationActivity.this,LoginActivity.class);
                            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);                    startActivity(loginIntent);
                            Toast.makeText(RegistrationActivity.this, "Đăng ký thành công !!!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pDialog.dismiss();
                            Toast.makeText(RegistrationActivity.this, "Lỗi "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
        });
        ChayToolBar();
    }

    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Chọn hình ảnh"),PICK_IMAGE_REQUEST);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() !=null){
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgAvatar.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }
}
