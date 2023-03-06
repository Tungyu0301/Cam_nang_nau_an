package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.duan2.camnangamthuc.camnangamthuc.R;
import com.duan2.camnangamthuc.camnangamthuc.SQLiteDatabase.SQLiteHandler;

public class AddlistShoppingActivity extends AppCompatActivity {
    EditText edittieude,editnoidung;
    Button bntluulailist,bntdonglist;
    Toolbar toolbar;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addlist_shopping);
        toolbar = (Toolbar) findViewById(R.id.toolbar_addlistshopping);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        db = new SQLiteHandler(getApplicationContext());
        inti();
    }
    private void inti(){
        edittieude = (EditText) findViewById(R.id.edittieude);
        editnoidung = (EditText) findViewById(R.id.editnoidung);
        bntluulailist = (Button) findViewById(R.id.bntluulailist);
        bntdonglist = (Button) findViewById(R.id.bntdonglist);
        bntluulailist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themdulieu();
                Intent intent = new Intent(AddlistShoppingActivity.this, ViewListShopping.class);
                startActivity(intent);
                finish();
            }
        });
        bntdonglist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void themdulieu() {
        boolean kt = true;
        String Title = edittieude.getText().toString();
        String Content = editnoidung.getText().toString();
        if (Title.isEmpty()) {
            edittieude.setError("Vui lòng nhập tiêu đề");
            edittieude.requestFocus();
            kt = false;
        }
        if (Content.isEmpty()) {
            editnoidung.setError("Vui lòng nhập nội dung");
            editnoidung.requestFocus();
            kt = false;
        }
        if (kt == true) {
            boolean bl = db.insertDatalist(Title, Content);
            if (bl) {
                Toast.makeText(AddlistShoppingActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AddlistShoppingActivity.this, "Thêm không thành công", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
