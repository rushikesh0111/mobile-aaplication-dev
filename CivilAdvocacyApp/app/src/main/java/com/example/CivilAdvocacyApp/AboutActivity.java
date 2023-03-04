package com.example.CivilAdvocacyApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class AboutActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    //this method will set the link for Google API reference
    public void apiClicked(View v)
    {
        String app_URL = "https://developers.google.com/civic-information/";
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(app_URL));
        startActivity(i);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
