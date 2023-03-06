package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;

import com.duan2.camnangamthuc.camnangamthuc.R;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;


public class MainActivity extends AppCompatActivity {
    RingProgressBar ringProgressBar;
    int i = 0;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ringProgressBar = (RingProgressBar) findViewById(R.id.loadingg);
        loading();
    }
    private void loading(){
        ringProgressBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {
            @Override
            public void progressToComplete() {
                Intent loadinginten = new Intent(MainActivity.this, Home.class);
                loadinginten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loadinginten);
            }
        });
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== 0){
                    if(i<100){
                        i++;
                        ringProgressBar.setProgress(i);
                    }
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int x = 0; x < 100; x++) {
                    try {
                        Thread.sleep(50);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
