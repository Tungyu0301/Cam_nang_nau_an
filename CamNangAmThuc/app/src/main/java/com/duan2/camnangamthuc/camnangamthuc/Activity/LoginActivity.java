package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.duan2.camnangamthuc.camnangamthuc.Model.Common;
import com.duan2.camnangamthuc.camnangamthuc.Model.Users;
import com.duan2.camnangamthuc.camnangamthuc.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    EditText editloginemail,editloginpassword;
    Button bntlogin,bntchanreque;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference listuser;
    ProgressDialog pDialog;
    CheckBox checkBoxghinho;
    TextView txtforgot;
    AlertDialog.Builder builder;
    AlertDialog b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editloginemail = (EditText) findViewById(R.id.edtEmaillogin);
        editloginpassword = (EditText)findViewById(R.id.edtpasswordlogin);
        txtforgot = (TextView) findViewById(R.id.textquenmk);
        bntlogin = (Button) findViewById(R.id.btnlogin);
        bntchanreque = (Button) findViewById(R.id.btnLinkToLoginScreen);
        checkBoxghinho = (CheckBox) findViewById(R.id.checkghinho);
        Paper.init(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        listuser = firebaseDatabase.getReference("Users");
                bntchanreque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent requeIntent = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(requeIntent);
            }
        });
                bntlogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkBoxghinho.isChecked()){
                            Paper.book().write(Common.USE_KEY,editloginemail.getText().toString());
                            Paper.book().write(Common.PAW_KEY,editloginpassword.getText().toString());
                        }
                        pDialog = new ProgressDialog(LoginActivity.this);
                        pDialog.setTitle("????ng nh???p");
                        pDialog.setMessage("Vui l??ng ?????i...");
                        pDialog.show();
                        listuser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                pDialog.dismiss();
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    Users users = postSnapshot.getValue(Users.class);
                                    //so s??nh d??? li???u t??? database l???y v???  v???i s??? ki???n g?? l??n
                                    //s??? ki???n g?? l??n kh??ng tr??ng v???i database
                                    if (!users.getEmail().equals(editloginemail.getText().toString()) &&
                                            !users.getPassword().equals(editloginpassword.getText().toString())) {
                                        Toast.makeText(LoginActivity.this, "????ng nh???p kh??ng th??nh c??ng", Toast.LENGTH_SHORT).show();
                                    //ng?????c l???i tr??ng
                                    }else {
                                        //so s??nh d??? li???u t??? database l???y v???  v???i s??? ki???n g?? l??n
                                        //truy???n v??o 1 h??m equalsIgnoreCase l???y ra t??? 1 bi???n trong database
                                        //bi???n truy???n v??? t??? database l?? admin
                                        if (users.getEmail().equals(editloginemail.getText().toString()) &&
                                                users.getPassword().equals(editloginpassword.getText().toString())
                                                && users.getRole().equalsIgnoreCase("admin")) {
                                            Intent adminIntent = new Intent(LoginActivity.this, HomeAdmin.class);
                                            Common.userten = users;
                                            //chuy???n qua tab m???i//????ng tab hi???n t???i
                                            adminIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(adminIntent);
                                            Toast.makeText(LoginActivity.this, "????ng nh???p th??nh c??ng", Toast.LENGTH_SHORT).show();
                                        }
                                        //so s??nh d??? li???u t??? database l???y v???  v???i s??? ki???n g?? l??n
                                        //truy???n v??o 1 h??m equalsIgnoreCase l???y ra t??? 1 bi???n trong database
                                        //bi???n truy???n v??? t??? database l?? use
                                        if (users.getEmail().equals(editloginemail.getText().toString()) &&
                                                users.getPassword().equals(editloginpassword.getText().toString())
                                                && users.getRole().equalsIgnoreCase("user")) {
                                            Intent userIntent = new Intent(LoginActivity.this, HomeUsers.class);
                                            Common.userten = users;
                                            userIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(userIntent);
                                            Toast.makeText(LoginActivity.this, "????ng nh???p th??nh c??ng", Toast.LENGTH_SHORT).show();
                                            listuser.removeEventListener(this);
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
                txtforgot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle("Qu??n m???t kh???u");
                        builder.setMessage("Nh???p th??ng tin b??n d?????i ????? l???y l???i m???t kh???u");
                        LayoutInflater layoutInflater = LoginActivity.this.getLayoutInflater();
                        final View sendcode = layoutInflater.inflate(R.layout.show_forgotpass,null);
                        final EditText editmailforgot = (EditText) sendcode.findViewById(R.id.edtEmailforgot);
                        final EditText codeforgot = (EditText) sendcode.findViewById(R.id.edtcoderesetforgot);
                        final Button bntsend = (Button) sendcode.findViewById(R.id.btnsendmkforgot);
                        builder.setView(sendcode);
                        builder.setIcon(R.drawable.ic_security_black_24dp);
                        b = builder.create();
                        b.show();
                        bntsend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                               listuser.addValueEventListener(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                       for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                           Users users = postSnapshot.getValue(Users.class);
                                           if (users.getEmail().equals(editmailforgot.getText().toString()) && users.getCode().equals(codeforgot.getText().toString())){
                                               b.dismiss();
                                               sendmatkhau(users.getPassword());
                                           }

                                       }
                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError databaseError) {

                                   }
                               });
                            }
                        });
                    }
                });
    }
    private void sendmatkhau(String matkhau){
        String mk = "M???t kh???u c???a b???n l?? <font color='blue'> <Strong>"+ matkhau+ "</Strong></font>";
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("G???i m???t kh???u");
        dialog.setMessage(Html.fromHtml(mk));
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }
}
