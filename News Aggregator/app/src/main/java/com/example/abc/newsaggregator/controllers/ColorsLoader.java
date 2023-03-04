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
import java.util.ArrayList;

public class ColorsLoader implements Runnable {

    private final MainActivity activity;

    //
    //
    //this class will be responsible for loading the colors for appropriate categories
    public ColorsLoader(MainActivity activity) {
        this.activity = activity;
    }

    //
    //
    //
    @Override
    public void run() {
        InputStream is = activity.getResources().openRawResource(R.raw.colors);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        try {
            StringBuilder result = new StringBuilder();
            for (String line; (line = br.readLine()) != null; ) {
                result.append(line);
            }

            ArrayList<String> colors = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(result.toString());

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                colors.add(jsonObject.getString("color"));
            }
            activity.runOnUiThread(() -> activity.receiveColors(colors));

        } catch (IOException | JSONException e) {

            e.printStackTrace();
            activity.runOnUiThread(() -> activity.receiveColors(null));
        }
    }
}
