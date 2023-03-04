package com.example.CivilAdvocacyApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class OfficialActivity extends AppCompatActivity {
    private static final String TAG = OfficialActivity.class.getName();

    private ConstraintLayout CL1;
    private ConstraintLayout CL2;

    private TextView textViewTitle;
    private TextView textViewName;
    private TextView textViewParty;
    private TextView textViewAddress;
    private TextView textViewAddressLab;
    private TextView textViewEmail;
    private TextView textViewEmailLab;
    private TextView textViewURL;
    private TextView textViewURLLab;
    private TextView textViewPhone;
    private TextView textViewPhoneLab;
    private TextView textViewLocation;

    private ImageView imageViewProfile;
    private ImageView imageViewPartyLogo;
    private ImageView imageViewFBIcon;
    private ImageView imageViewTwitterIcon;
    private ImageView imageViewYoutubeIcon;
    private ImageView imageView_gp;

    private ChannelModel fbHandle;
    private ChannelModel twitterHandle;
    private ChannelModel youtubeHandle;
    private ChannelModel gpHandle;

    private OfficialModel temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        loadUI();
        updateLocation();
        loadData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    //this method will be called after clicking on the party logo
    public void partyLogoClicked(View v) {
        String demo_URL = "https://democrats.org";
        String republic_URL = "https://www.gop.com";

        if (temp.getParty().toLowerCase().trim().contains("democratic")) {

            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(demo_URL));
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        } else if (temp.getParty().toLowerCase().trim().contains("republican")) {

            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(republic_URL));
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        }
    }

    //this method will load the UI
    void loadUI() {
        CL1 = findViewById(R.id.constrainedLayout);
        CL2 = findViewById(R.id.detailsCard);

        textViewLocation = findViewById(R.id.location);
        textViewTitle = findViewById(R.id.title);
        textViewName = findViewById(R.id.name);
        textViewParty = findViewById(R.id.party);
        textViewAddress = findViewById(R.id.address);
        textViewAddressLab = findViewById(R.id.addrress_text);
        textViewEmail = findViewById(R.id.email);
        textViewEmailLab = findViewById(R.id.email_text);
        textViewURL = findViewById(R.id.website);
        textViewURLLab = findViewById(R.id.website_text);
        textViewPhone = findViewById(R.id.phone);
        textViewPhoneLab = findViewById(R.id.phone_text);
        imageViewProfile = findViewById(R.id.profile_photo);
        imageViewPartyLogo = findViewById(R.id.partyLogo);

        imageViewFBIcon = findViewById(R.id.facebook);
        imageViewTwitterIcon = findViewById(R.id.twitter);
        imageViewYoutubeIcon = findViewById(R.id.youtube);
        //imageView_gp = findViewById(R.id.gplus);
    }

    //this method will update the location
    void updateLocation() {
        if (getIntent().hasExtra("location"))
            textViewLocation.setText(getIntent().getStringExtra("location"));
        else
            textViewLocation.setText("");
    }

    //this method will load the data
    void loadData() {
        if (getIntent().hasExtra("official")) {

            temp = (OfficialModel) getIntent().getSerializableExtra("official");
            ArrayList<ChannelModel> channelModels;

            assert temp != null;

            textViewTitle.setText(temp.getTitle());
            textViewName.setText(temp.getName());
            textViewParty.setText(temp.getParty());

            if (!temp.getAddress().equals("")) {

                textViewAddress.setText(temp.getAddress().trim());
                Linkify.addLinks(textViewAddress, Linkify.ALL);
                textViewAddress.setLinkTextColor(getColor(R.color.white));

            } else {

                textViewAddressLab.setVisibility(View.GONE);
                textViewAddress.setVisibility(View.GONE);

                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, 0);

                textViewPhoneLab.setLayoutParams(params);

            }

            if (!temp.getPhones().equals("")) {

                textViewPhone.setText(temp.getPhones());
                textViewPhone.setLinkTextColor(getColor(R.color.white));

                Linkify.addLinks(textViewPhone, Linkify.ALL);
            } else {
                textViewPhoneLab.setVisibility(View.GONE);
                textViewPhone.setVisibility(View.GONE);
            }

            if (!temp.getEmails().equals("")) {

                textViewEmail.setText(temp.getEmails());
                textViewEmail.setLinkTextColor(getColor(R.color.white));

                Linkify.addLinks(textViewEmail, Linkify.ALL);

            } else {

                textViewEmailLab.setVisibility(View.GONE);
                textViewEmail.setVisibility(View.GONE);
            }

            if (!temp.getUrls().equals("")) {

                textViewURL.setText(temp.getUrls());
                textViewURL.setLinkTextColor(getColor(R.color.white));

                Linkify.addLinks(textViewURL, Linkify.ALL);

            } else {

                textViewURLLab.setVisibility(View.GONE);
                textViewURL.setVisibility(View.GONE);
            }


            if (temp.getParty().trim().toLowerCase().contains("democratic")){
                loadDemocraticTheme();
            }
            else if (temp.getParty().trim().toLowerCase().contains("republican")) {
                loadRepublicanTheme();
            }
            else {
                loadNonPartisanTheme();
            }

            loadProfilePicture(temp.getPhotoURL().trim());
            channelModels = temp.getChannels();

            if (channelModels.size() > 0) {
                for (ChannelModel single_channelModel : channelModels) {

                    if (single_channelModel.getType().equals("Facebook")) {

                        fbHandle = single_channelModel;
                        imageViewFBIcon.setVisibility(View.VISIBLE);

                    }
                    if (single_channelModel.getType().equals("Twitter")) {

                        twitterHandle = single_channelModel;
                        imageViewTwitterIcon.setVisibility(View.VISIBLE);

                    }
                    if (single_channelModel.getType().equals("GooglePlus")) {

                        gpHandle = single_channelModel;
                        imageView_gp.setVisibility(View.VISIBLE);

                    }
                    if (single_channelModel.getType().equals("YouTube")) {

                        youtubeHandle = single_channelModel;
                        imageViewYoutubeIcon.setVisibility(View.VISIBLE);

                    }
                }
            }
        }
    }

    //this method will load the profile picture
    void loadProfilePicture(String URL) {
        Log.d(TAG, "bp: loadProfilePicture: URL: " + URL);

        if (URL.equals("")) {
            imageViewProfile.setImageResource(R.drawable.default_dp);
        } else {
            Picasso.get()
                    .load(URL)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.broken_image)
                    .into(imageViewProfile);
        }
    }

    //this method will load the theme for democratic party
    void loadDemocraticTheme() {

        textViewLocation.setBackgroundResource(R.color.dark_blue);
        imageViewPartyLogo.setImageResource(R.drawable.dem_logo);
        CL1.setBackgroundResource(R.color.blue);
        getWindow().setNavigationBarColor(getColor(R.color.blue));

    }

    //this method will load the theme for Republic party
    void loadRepublicanTheme() {

        textViewLocation.setBackgroundResource(R.color.dark_red);
        imageViewPartyLogo.setImageResource(R.drawable.rep_logo);
        CL1.setBackgroundResource(R.color.red);
        getWindow().setNavigationBarColor(getColor(R.color.red));

    }

    //this method will load the theme for Non party member
    void loadNonPartisanTheme() {

        textViewLocation.setBackgroundResource(R.color.colorPrimaryDark);
//        imageViewPartyLogo.setImageResource(R.drawable.non_logo);
        CL1.setBackgroundResource(R.color.dark_grey);
        imageViewProfile.setBackgroundResource(R.drawable.dp_background_non);
        getWindow().setNavigationBarColor(getColor(R.color.dark_grey));

    }

    //this method will expand the image
    public void expandImage(View v) {

        if (!temp.getPhotoURL().equals("")) {

            Intent i = new Intent(this, PhotoDetailActivity.class);
            i.putExtra("location", textViewLocation.getText());
            i.putExtra("official", temp);
            startActivity(i);

        } else {
            Toast.makeText(this, "No Profile Picture", Toast.LENGTH_SHORT).show();
        }
    }

    //this method will open the handle of youtube if available
    public void youTubeClicked(View v) {

        String id = youtubeHandle.getId();
        Intent intent;

        try {

            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + id));
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        } catch (ActivityNotFoundException e) {

            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + id)));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        }
    }

    //this method will open the handle of Twitter if available
    public void clickOnTwitter(View v) {

        Intent intent;
        String id = twitterHandle.getId();

        try {
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + id));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        } catch (Exception e) {

            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + id));
        }

        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    //this method will open the handle of Facebook if available
    public void clickOnFacebook(View v) {

        String id = fbHandle.getId();
        String FACEBOOK_URL = "https://www.facebook.com/" + id;
        String urlToUse;

        PackageManager packageManager = getPackageManager();

        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                urlToUse = "fb://page/" + id;
            }

        } catch (PackageManager.NameNotFoundException e) {

            urlToUse = FACEBOOK_URL; //normal web url
        }

        Intent fbIntent = new Intent(Intent.ACTION_VIEW);
        fbIntent.setData(Uri.parse(urlToUse));
        startActivity(fbIntent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    /*public void googlePlusClicked(View v) {
        String id = gpHandle.getId();
        Intent intent;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus",
                    "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", id);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + textViewName)));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }*/
}
