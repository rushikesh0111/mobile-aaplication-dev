package com.example.androidnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            this.getSupportActionBar().hide();
        }catch(Exception e){
            e.printStackTrace();
        }
        setContentView(R.layout.activity_about);
    }
}