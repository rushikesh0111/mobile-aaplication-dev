package com.example.weatherapp.activities;

import static com.example.weatherapp.static_config.StaticConfig.WEATHER_MODEL;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.adapters.DailyForecastAdapter;

import java.util.Objects;

public class DailyActivity extends AppCompatActivity {
    private RecyclerView RVDaily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);

        loadDailyActivityUI();
        loadDailyActivityData();
    }

    //this method will load the data for daily activity
    private void loadDailyActivityData() {

        String location = getIntent().getStringExtra("location");
        String tempUnit = getIntent().getStringExtra("unit");

        Objects.requireNonNull(getSupportActionBar()).setTitle(location +" "+ WEATHER_MODEL.dayModelArrayList.size() + " Day");

        DailyForecastAdapter DFA = new DailyForecastAdapter(getApplicationContext(), tempUnit);
        RVDaily.setAdapter(DFA);

    }

    //this method will load the UI for daily activity
    private void loadDailyActivityUI() {

        RVDaily = findViewById(R.id.recyclerViewDaily);
        RVDaily.setHasFixedSize(true);
        RVDaily.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }
}