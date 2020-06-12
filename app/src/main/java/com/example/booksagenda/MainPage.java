package com.example.booksagenda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainPage extends AppCompatActivity {

    Button buttonOpenNE, buttonOpenReg, buttonExport;
    ExportCSV export;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        buttonOpenNE = findViewById(R.id.buttonANE);
        buttonOpenReg = findViewById(R.id.buttonOPReg);
        buttonExport = findViewById(R.id.buttonExport);

        export = new ExportCSV();

        permissions();

        buttonOpenNE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainPage.this, MainActivity.class));
            }
        });

        buttonOpenReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainPage.this, BookRegister.class));
            }
        });

        permissions();

        buttonExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                export.exportData(MainPage.this);
            }
        });
    }

    public void permissions(){
        /*if (ContextCompat.checkSelfPermission(MainPage.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainPage.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }*/
        ActivityCompat.requestPermissions(MainPage.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
    }

}
