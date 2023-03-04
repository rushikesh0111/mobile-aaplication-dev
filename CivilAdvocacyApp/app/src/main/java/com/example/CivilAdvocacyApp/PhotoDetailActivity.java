package com.example.CivilAdvocacyApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends AppCompatActivity {
    private static final String TAG = PhotoDetailActivity.class.getName();

    private ConstraintLayout CL;

    private ImageView imageViewDP;
    private ImageView imageViewPartyLogo;

    private TextView textViewLocation;
    private TextView textViewTitle;
    private TextView textViewName;
    private TextView textViewParty;

    private OfficialModel temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        loadUI();
        updateLocation();
        loadData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    //this method will load the data which is require to load the Democratic party theme
    void loadDemocraticTheme() {

        textViewLocation.setBackgroundResource(R.color.dark_blue);
        imageViewPartyLogo.setImageResource(R.drawable.dem_logo);

        CL.setBackgroundResource(R.color.blue);
        getWindow().setNavigationBarColor(getColor(R.color.blue));

    }

    //this method will load the data which is require to load the Republic party theme
    void loadRepublicanTheme() {

        textViewLocation.setBackgroundResource(R.color.dark_red);
        imageViewPartyLogo.setImageResource(R.drawable.rep_logo);

        CL.setBackgroundResource(R.color.red);
        getWindow().setNavigationBarColor(getColor(R.color.red));

    }

    //this method will load the data which is require to load the non party theme
    void loadNonPartisanTheme() {

        textViewLocation.setBackgroundResource(R.color.extra_dark_grey);
//        imageViewPartyLogo.setImageResource(R.drawable.non_logo);

        CL.setBackgroundResource(R.color.dark_grey);
        getWindow().setNavigationBarColor(getColor(R.color.dark_grey));

    }

    //this method will load the data which is require to load the application
    void loadData() {
        if(getIntent().hasExtra("official"))
        {
            temp = (OfficialModel) getIntent().getSerializableExtra("official");
            assert temp != null;
            textViewTitle.setText(temp.getTitle());
            textViewName.setText(temp.getName());
//            textViewParty.setText(temp.getParty());

            if(temp.getParty().trim().toLowerCase().contains("democratic"))
                loadDemocraticTheme();
            else if(temp.getParty().trim().toLowerCase().contains("republican"))
                loadRepublicanTheme();
            else
                loadNonPartisanTheme();

            loadProfilePic(temp.getPhotoURL().trim());
        }

    }

    //this method will load the UI
    void loadUI() {
        CL = findViewById(R.id.constrainedLayout);
        textViewLocation = findViewById(R.id.location);
        textViewTitle = findViewById(R.id.title);
        textViewName = findViewById(R.id.name);
        textViewParty = findViewById(R.id.party);
        imageViewDP = findViewById(R.id.profile_photo);
        imageViewPartyLogo = findViewById(R.id.partyLogo);
    }

    //this method will load the data which is require to load the Democratic party theme
    void loadProfilePic(String URL) {
        Log.d(TAG, "bp: loadProfilePicture: URL: " + URL);

        Picasso.get()
                .load(URL)
                .error(R.drawable.broken_image)
                .placeholder(R.drawable.placeholder)
                .into(imageViewDP);
    }

    //this method will be called after clicking on the party logo
    public void partyLogoClicked(View v) {
        String democratic_URL = "https://democrats.org";
        String republican_URL = "https://www.gop.com";

        if(temp.getParty().toLowerCase().trim().contains("democratic"))
        {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(democratic_URL));
            startActivity(i);
        }
        else if(temp.getParty().toLowerCase().trim().contains("republican"))
        {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(republican_URL));
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    //this method will update the location
    void updateLocation() {
        if(getIntent().hasExtra("location"))
            textViewLocation.setText(getIntent().getStringExtra("location"));
        else
            textViewLocation.setText("");
    }
}