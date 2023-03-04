package com.example.abc.newsaggregator.controllers;

import com.example.abc.newsaggregator.activities.MainActivity;
import com.example.abc.newsaggregator.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class CountriesLoader implements Runnable {

    private final MainActivity activity;

    //
    //
    //this class will load the country-wise articles
    public CountriesLoader(MainActivity activity) {
        this.activity = activity;
    }

    //
    //
    //
    @Override
    public void run() {
        InputStream is = activity.getResources().openRawResource(R.raw.country_codes);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        try {
            StringBuilder result = new StringBuilder();

            for (String line; (line = br.readLine()) != null; ) {
                result.append(line);
            }
            Map<String, String> countries = new HashMap<>();
            JSONArray jsonArray = new JSONArray(result.toString());

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                countries.put(jsonObject.getString("code"), jsonObject.getString("name"));
            }
            activity.runOnUiThread(() -> activity.receiveFullCountries(countries));

        } catch (IOException | JSONException e) {

            e.printStackTrace();
            activity.runOnUiThread(() -> activity.receiveFullCountries(null));
        }
    }
}
