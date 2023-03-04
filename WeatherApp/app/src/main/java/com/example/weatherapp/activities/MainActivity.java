package com.example.weatherapp.activities;


import static com.android.volley.Request.Method.GET;
import static com.example.weatherapp.static_config.StaticConfig.API_KEY;
import static com.example.weatherapp.static_config.StaticConfig.BASE_URL;
import static com.example.weatherapp.static_config.StaticConfig.PREF_WEATHER_SETTINGS;
import static com.example.weatherapp.static_config.StaticConfig.WEATHER_MODEL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.R;
import com.example.weatherapp.adapters.HourlyWeatherAdapter;
import com.example.weatherapp.models.CurrentConditionModel;
import com.example.weatherapp.models.DayModel;
import com.example.weatherapp.models.HourModel;
import com.example.weatherapp.models.WeatherModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private String unit, loc;

    private ScrollView sv;
    private ImageView weatherIcon;

    private TextView titleBar;
    private TextView temp;
    private TextView subStringTemp;
    private TextView weatherDescr;
    private TextView wind;
    private TextView humidity;
    private TextView UVIndex;
    private TextView visibility;

    private TextView morningTemp;
    private TextView afternoonTemp;
    private TextView eveningTemp;
    private TextView nightTemp;

    private TextView sunrise;
    private TextView sunset;

    private SwipeRefreshLayout SRL;

    final private HourlyWeatherAdapter HWA = new HourlyWeatherAdapter(MainActivity.this, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unit = "imperial";
        loc = "Chicago";

        loadWeatherUI();       // linking xml with java file
        LoadWeatherData();
        setListeners();
        getWeatherData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.main_menu, menu);

        if (unit.equals("imperial")) {

            menu.getItem(0).setIcon(R.drawable.units_f);

        } else if (unit.equals("metric")) {

            menu.getItem(0).setIcon(R.drawable.units_c);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mItem) {

        if(isConnected()) {

            if (mItem.getItemId() == android.R.id.home) {

                onBackPressed();
                return true;

            } else if (mItem.getItemId() == R.id.temp_unit){

                if (unit.equals("imperial")) {

                    mItem.setIcon(R.drawable.units_c);
                    unit = "metric";
                    getWeatherData();

                    return true;
                }
                if (unit.equals("metric")) {

                    mItem.setIcon(R.drawable.units_f);
                    unit = "imperial";
                    getWeatherData();

                    return true;
                }

            } else if (mItem.getItemId() == R.id.daily_forecast) {

                Intent i = new Intent(getApplicationContext(), DailyActivity.class);

                i.putExtra("location", loc);
                i.putExtra("unit", unit);

                startActivity(i);

                return true;

            } else if (mItem.getItemId() == R.id.location) {

                EditText edittext = new EditText(MainActivity.this);

                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle("Enter a Location");
                alert.setMessage("For US locations, enter as 'City' or 'City, State'\n\nFor international locations enter as 'City, Country'");
                alert.setView(edittext);

                alert.setPositiveButton("Ok", (dialog, whichButton) -> {loc = edittext.getText().toString();
                    changeAppLocation();});
                alert.setNegativeButton("Cancel", (dialog, whichButton) -> dialog.dismiss());

                alert.show();

                return true;

            } else { return super.onOptionsItemSelected(mItem); }
        }
        return super.onOptionsItemSelected(mItem);
    }

    private void setListeners() {
        SRL.setOnRefreshListener(this::getWeatherData);
    }

    //this method will load the data which is required to start the application
    private void LoadWeatherData() {
        SharedPreferences prefs = getSharedPreferences(PREF_WEATHER_SETTINGS, MODE_PRIVATE);

        unit = prefs.getString("unit", "imperial");
        loc = prefs.getString("location", "Chicago, Illinois");

        Objects.requireNonNull(getSupportActionBar()).setTitle("" + loc);
    }

    //this method will sav ethe data into variables
    private void saveWeatherData() {
        SharedPreferences.Editor editor = getSharedPreferences(PREF_WEATHER_SETTINGS, MODE_PRIVATE).edit();
        editor.putString("unit", unit);
        editor.putString("location", loc);
        editor.apply();
    }

    //this method will fetch the weather data
    private void getWeatherData() {

        if (!isConnected())
        {
            if (sv != null) sv.setVisibility(View.GONE);
            sunset.setVisibility(View.GONE);
            sunrise.setVisibility(View.GONE);
            titleBar.setText(R.string.no_internet);
            Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_LONG).show();
            SRL.setRefreshing(false);
            return;
        }
        else
        {
            if (sv != null) sv.setVisibility(View.VISIBLE);
            sunset.setVisibility(View.VISIBLE);
            sunrise.setVisibility(View.VISIBLE);
        }

        String showUnit = unit;
        String localURL;

        if (unit.equals("imperial")) showUnit = "us";


        try {
            localURL = new URL(BASE_URL + loc + "?unitGroup=" + showUnit + "&lang=en&key=" + API_KEY).toString();
            Log.d(TAG, "URL - " + localURL);
            StringRequest weatherRequest = new StringRequest(GET, localURL, response -> {
                Log.d(TAG, "Response " + response);
                handleWeatherObject(response);
            }, error -> {
                Toast.makeText(getApplicationContext(), "Unable to fetch weather data for given location", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Error " + error.getLocalizedMessage());
            });

            RequestQueue rq = Volley.newRequestQueue(MainActivity.this);
            rq.add(weatherRequest);

        } catch (MalformedURLException e) {

            e.printStackTrace();
            SRL.setRefreshing(false);
        }
    }

    //this method will load the UI to its default settings
    private void loadWeatherUI() {
        sv = findViewById(R.id.scroll_view);
        weatherIcon = findViewById(R.id.view_weather_icon_image);
        titleBar = findViewById(R.id.title_bar);
        temp = findViewById(R.id.temparature_display);
        subStringTemp = findViewById(R.id.feels_like_temperature);
        weatherDescr = findViewById(R.id.weather_description);
        wind = findViewById(R.id.windspeed_view);
        humidity = findViewById(R.id.humidity_view);
        UVIndex = findViewById(R.id.UV_index_view);
        visibility = findViewById(R.id.visibility_view);
        morningTemp = findViewById(R.id.morning_temp_view);
        afternoonTemp = findViewById(R.id.afternoon_temp);
        eveningTemp = findViewById(R.id.evening_temp);
        nightTemp = findViewById(R.id.night_temp);
        sunrise = findViewById(R.id.sunrise_textView);
        sunset = findViewById(R.id.sunset_textView);
        SRL = findViewById(R.id.swipe_down_refresh);
        RecyclerView rv = findViewById(R.id.weather_recycler_view);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        rv.setAdapter(HWA);
    }

    //this method will be called whenever the UI gets updated
    private void updateWeatherUI() {
        String unitVar;

        if (unit.equals("metric")) unitVar = "C";
        else unitVar = "F";
        if (WEATHER_MODEL == null) return;

        String direction = getDirection(WEATHER_MODEL.currentConditionModel.winddir);
        SimpleDateFormat formatter = new SimpleDateFormat("EE MMM d hh:mm a, yyyy", Locale.ENGLISH);
        SimpleDateFormat timeOnly = new SimpleDateFormat("h:mm a", Locale.getDefault());
        String dateString = formatter.format(new Date(WEATHER_MODEL.currentConditionModel.datetimeEpoch * 1000L));
        String sunrise = timeOnly.format(new Date(WEATHER_MODEL.currentConditionModel.sunriseEpoch * 1000L));
        String sunset = timeOnly.format(new Date(WEATHER_MODEL.currentConditionModel.sunsetEpoch * 1000L));

        int id = getWeatherIcon(WEATHER_MODEL.currentConditionModel.icon);
        if (id != 0) weatherIcon.setImageResource(id);

        titleBar.setText(MessageFormat.format("{0}", dateString));
        temp.setText(MessageFormat.format("{0}°{1}", WEATHER_MODEL.currentConditionModel.temp, unitVar));
        subStringTemp.setText(MessageFormat.format("Feels Like {0}°{1}", WEATHER_MODEL.currentConditionModel.feelslike, unitVar));
        weatherDescr.setText(MessageFormat.format("{0} ({1}% clouds)", WEATHER_MODEL.currentConditionModel.conditions, WEATHER_MODEL.currentConditionModel.cloudcover));
        wind.setText(MessageFormat.format("Winds: {0} at {1} mph gusting to {2} mph", direction, WEATHER_MODEL.currentConditionModel.windspeed, WEATHER_MODEL.currentConditionModel.windgust));
        humidity.setText(MessageFormat.format("Humidity: {0}%", WEATHER_MODEL.currentConditionModel.humidity));
        UVIndex.setText(MessageFormat.format("UV Index: {0}", WEATHER_MODEL.currentConditionModel.uvindex));
        visibility.setText(MessageFormat.format("Visibility: {0} mi", WEATHER_MODEL.currentConditionModel.visibility));
        morningTemp.setText(MessageFormat.format("{0}°{1}", WEATHER_MODEL.dayModelArrayList.get(0).hourModelArrayList.get(8).temp, unitVar));
        afternoonTemp.setText(MessageFormat.format("{0}°{1}", WEATHER_MODEL.dayModelArrayList.get(0).hourModelArrayList.get(13).temp, unitVar));
        eveningTemp.setText(MessageFormat.format("{0}°{1}", WEATHER_MODEL.dayModelArrayList.get(0).hourModelArrayList.get(17).temp, unitVar));
        nightTemp.setText(MessageFormat.format("{0}°{1}", WEATHER_MODEL.dayModelArrayList.get(0).hourModelArrayList.get(23).temp, unitVar));
        this.sunrise.setText(MessageFormat.format("Sunrise {0}", sunrise));
        this.sunset.setText(MessageFormat.format("Sunset {0}", sunset));

        HWA.updateHourlyData(unit);
    }



    // this method will gather data from api response
    private void handleWeatherObject(String response) {
        try {
            ArrayList<DayModel> dayModelArrayList = new ArrayList<>();

            JSONObject JOResponce = new JSONObject(response);
            JSONObject JOCurrentConditions = JOResponce.getJSONObject("currentConditions");
            JSONArray JArray = JOResponce.getJSONArray("days");

            WeatherModel wm = new WeatherModel();
            CurrentConditionModel CCModel = new CurrentConditionModel();

            for (int k=0; k<JArray.length(); k++) {

                DayModel dm = new DayModel();
                ArrayList<HourModel> hourModelArrayList = new ArrayList<>();
                JSONObject day = JArray.getJSONObject(k);
                JSONArray hours = day.getJSONArray("hours");


                for (int j=0; j<hours.length(); j++) {

                    JSONObject JBHour = hours.getJSONObject(j);
                    HourModel hModel = new HourModel();
                    hModel.datetimeEpoch = JBHour.getLong("datetimeEpoch");
                    hModel.temp = JBHour.getDouble("temp");
                    hModel.conditions = JBHour.getString("conditions");
                    hModel.icon = JBHour.getString("icon");
                    hourModelArrayList.add(hModel);
                }

                dm.hourModelArrayList = hourModelArrayList;
                dm.datetimeEpoch = day.getLong("datetimeEpoch");
                dm.tempmax = day.getDouble("tempmax");
                dm.tempmin = day.getDouble("tempmin");
                dm.precipprob = day.getDouble("precipprob");
                dm.uvindex = day.getDouble("uvindex");
                dm.description = day.getString("description");
                dm.weatherIcon = day.getString("icon");
                dayModelArrayList.add(dm);
            }

            CCModel.datetimeEpoch = JOCurrentConditions.getLong("datetimeEpoch");
            CCModel.sunriseEpoch = JOCurrentConditions.getLong("sunriseEpoch");
            CCModel.sunsetEpoch = JOCurrentConditions.getLong("sunsetEpoch");
            CCModel.temp = JOCurrentConditions.getDouble("temp");
            CCModel.feelslike = JOCurrentConditions.getDouble("feelslike");
            CCModel.humidity = JOCurrentConditions.getDouble("humidity");

            if (!JOCurrentConditions.isNull("windgust"))
                CCModel.windgust = JOCurrentConditions.getDouble("windgust");
            else
                CCModel.windgust = 0.0;


            CCModel.windspeed = JOCurrentConditions.getDouble("windspeed");
            CCModel.winddir = JOCurrentConditions.getDouble("winddir");
            CCModel.visibility = JOCurrentConditions.getDouble("visibility");
            CCModel.cloudcover = JOCurrentConditions.getDouble("cloudcover");
            CCModel.uvindex = JOCurrentConditions.getDouble("uvindex");
            CCModel.conditions = JOCurrentConditions.getString("conditions");
            CCModel.icon = JOCurrentConditions.getString("icon");

            wm.address = JOResponce.getString("address");
            wm.timezone = JOResponce.getString("timezone");
            wm.tzoffset = JOResponce.getInt("tzoffset");
            wm.dayModelArrayList = dayModelArrayList;
            wm.currentConditionModel = CCModel;

            WEATHER_MODEL = wm;

            updateWeatherUI();

            SRL.setRefreshing(false);
            saveWeatherData();

        } catch (JSONException e) {

            e.printStackTrace();
            SRL.setRefreshing(false);
        }
    }


    //this method will change the current location of the application
    private void changeAppLocation() {
        getWeatherData();
        Objects.requireNonNull(getSupportActionBar()).setTitle("" + loc);
    }

    // this method will check whether the application has active connection to the network
    private boolean isConnected() {

        ConnectivityManager cm = getSystemService(ConnectivityManager.class);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }


    //this method will return the icon which this application is using
    private int getWeatherIcon(String iconName) {

        String i = iconName;
        i = i.replace("-", "_");

        int iconID = MainActivity.this.getResources().getIdentifier(i, "drawable", MainActivity.this.getPackageName());

        if (iconID == 0) {

            Log.d(TAG, "parseCurrentRecord: CANNOT FIND ICON " + i);
            return 0;
        }
        return iconID;
    }

    //this method will return the direction to the user
    private String getDirection(double degrees) {

        if (degrees >= 337.5 || degrees < 22.5)
            return "N";
        if (degrees >= 22.5 && degrees < 67.5)
            return "NE";
        if (degrees >= 67.5 && degrees < 112.5)
            return "E";
        if (degrees >= 112.5 && degrees < 157.5)
            return "SE";
        if (degrees >= 157.5 && degrees < 202.5)
            return "S";
        if (degrees >= 202.5 && degrees < 247.5)
            return "SW";
        if (degrees >= 247.5 && degrees < 292.5)
            return "W";
        if (degrees >= 292.5)
            return "NW";
        return "X"; //this is for default value that is bad
    }
}