package com.duan2.camnangamthuc.camnangamthuc.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.duan2.camnangamthuc.camnangamthuc.R;
import com.github.barteksc.pdfviewer.PDFView;

public class GuideViewActivity extends AppCompatActivity {
    Toolbar toolbar;
    PDFView pdfView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar_guide);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ChayToolBar();
        inti();
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
    private void inti(){
        pdfView = (PDFView) findViewById(R.id.pdfguide);
        pdfView.fromAsset("guidelist.pdf").load();
    }
}
