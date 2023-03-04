package com.example.CivilAdvocacyApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getName();
    private static int MY_LOCATION_REQUEST_CODE_ID = 329;
    private String currentLatLon;
    private String geoCodedLatLon;

    private SwipeRefreshLayout android_swiper;
    private RecyclerView rv;
    private TextView location, no_location;
    private ArrayList<OfficialModel> officialModelArrayList = new ArrayList<>();
    private OfficialAdapter officialAdapter;
    private LocationManager locationManager;
    private Criteria criteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupComponents();

        android_swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadLocation();
                convertLatLon();
                if (!currentLatLon.equals("")) {
                    if (networkChecker()) {
                        new OfficialLoader(MainActivity.this).execute(geoCodedLatLon);
                    } else {
                        noNetworkDialog(getString(R.string.networkErrorMsg1));
                    }
                } else {
                    LocationDialog(getString(R.string.locationErrorMsg1), 0);
                }
                android_swiper.setRefreshing(false);
            }
        });

        officialAdapter = new OfficialAdapter(officialModelArrayList, this);
        rv.setAdapter(officialAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        loadLocation();
        convertLatLon();

        if (!currentLatLon.equals("")) {
            if (networkChecker()) {
                new OfficialLoader(this).execute(geoCodedLatLon);
            } else {
                noNetworkDialog(getString(R.string.networkErrorMsg1));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opt_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                if (networkChecker()) {
                    search();
                } else
                    noNetworkDialog(getString(R.string.networkErrorMsg2));
                break;
            case R.id.about:
                Intent i = new Intent(this, AboutActivity.class);
                startActivity(i);
                break;
            default:
                Toast.makeText(this, "Invalid Option", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int position = rv.getChildAdapterPosition(view);
        OfficialModel temp = officialModelArrayList.get(position);

        Intent i = new Intent(this, OfficialActivity.class);
        i.putExtra("location", location.getText());
        i.putExtra("official", temp);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    //this method will convert location to langitute and lattitude
    public void convertLatLon() {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses;

            String loc = currentLatLon;
            if (!loc.trim().isEmpty()) {
                String[] latLon = loc.split(",");
                double lat = Double.parseDouble(latLon[0]);
                double lon = Double.parseDouble(latLon[1]);

                addresses = geocoder.getFromLocation(lat, lon, 1);

                if (!addresses.get(0).getPostalCode().equals("")) {
                    geoCodedLatLon = addresses.get(0).getPostalCode();
                } else if (!addresses.get(0).getLocality().equals("")) {
                    geoCodedLatLon = addresses.get(0).getLocality();
                }
                Log.d(TAG, "bp: convertLatLon: addresses: " + addresses.get(0).getPostalCode());
                Toast.makeText(this, "Location Detected: " + addresses.get(0).getLocality(), Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            Log.d(TAG, "EXCEPTION | convertLatLon: bp: " + e);
        }

    }

    //this method will setup the components
    public void setupComponents() {
        android_swiper = findViewById(R.id.swiper);
        rv = findViewById(R.id.recycler_view);
        location = findViewById(R.id.location);
        no_location = findViewById(R.id.location_not_found);
        currentLatLon = "";
        geoCodedLatLon = "";
    }

    //this method will detect the location
    public void detectLocation() {
        if (!isLocationEnabled()) {
            Toast.makeText(getApplicationContext(), "Location Access Required!", Toast.LENGTH_LONG).show();
            return;
        }

        String bestProvider = locationManager.getBestProvider(criteria, true);
        assert bestProvider != null;
        @SuppressLint("MissingPermission") Location currentLocation = locationManager.getLastKnownLocation(bestProvider);
        if (currentLocation != null) {
            currentLatLon = String.format(Locale.getDefault(), "%.4f, %.4f", currentLocation.getLatitude(), currentLocation.getLongitude());
            location.setText(currentLatLon);
        } else {
            location.setText(R.string.no_locs);
        }
    }

    //this method will load the location
    public void loadLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();

        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE_ID);
        } else {
            detectLocation();
        }
    }

    //this method will check the network every time when user will regresh the page
    public boolean networkChecker() {
        ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMan == null) return false;
        NetworkInfo networkInfo = conMan.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    //this method will check whether the location is enabled
    public boolean isLocationEnabled() {
        int locationMode;
        try {
            locationMode = Settings.Secure.getInt(getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return locationMode != Settings.Secure.LOCATION_MODE_OFF;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_LOCATION_REQUEST_CODE_ID) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[0] == PERMISSION_GRANTED) {
                detectLocation();
                if (networkChecker()) {
                    LocationDialog("", 1);
                    loadLocation();
                    convertLatLon();
                    new OfficialLoader(this).execute(geoCodedLatLon);
                } else {
                    noNetworkDialog(getString(R.string.networkErrorMsg1));
                }
                return;
            }
        }

        LocationDialog(getString(R.string.locationErrorMsg1), 0);
        location.setText(R.string.no_perm);


    }

    //this method will be called when app need location access
    private void LocationDialog(String message, int dismissFlag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.create();

        if (dismissFlag == 0) {
            builder.setIcon(R.drawable.ic_baseline_error_24);
            builder.setTitle(R.string.locationErrorTitle);
            builder.setMessage(message);

            dialog = builder.create();
            dialog.show();
        } else if (dismissFlag == 1)
            dialog.dismiss();
    }

    //this method will be called when there is no network connection
    public void noNetworkDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(R.drawable.ic_baseline_error_24);
        builder.setTitle(R.string.networkErrorTitle);
        builder.setMessage(message);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //this method will update the officials data
    public void updateOfficialData(ArrayList<OfficialModel> tempList) {
        officialModelArrayList.clear();
        if (tempList.size() != 0) {
            officialModelArrayList.addAll(tempList);
            no_location.setVisibility(View.GONE);
        } else {
            location.setText(getText(R.string.no_locs));
            no_location.setVisibility(View.VISIBLE);
        }
        officialAdapter.notifyDataSetChanged();

    }

    //this method will search for a new location
    public void search() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText text = new EditText(this);
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setView(text);
        builder.setIcon(R.drawable.ic_search_accent);

        builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String searchString = text.getText().toString().trim();
                if (!searchString.equals("")) {
                    location.setText("");
                    new OfficialLoader(MainActivity.this).execute(searchString);
                } else {
                    Toast.makeText(MainActivity.this, R.string.nullSearchStringMsg, Toast.LENGTH_SHORT).show();
                    search();
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.setMessage(R.string.searchMsg);
        builder.setTitle(R.string.searchTitle);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}