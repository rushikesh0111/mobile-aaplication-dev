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

public class LanguagesLoader implements Runnable {

    private final MainActivity activity;

    //
    //
    //this class will load the language-wise articles
    public LanguagesLoader(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void run() {

        InputStream is = activity.getResources().openRawResource(R.raw.language_codes);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        try {
            StringBuilder result = new StringBuilder();

            for (String line; (line = br.readLine()) != null; ) {
                result.append(line);
            }

            Map<String, String> languages = new HashMap<>();
            JSONArray jsonArray = new JSONArray(result.toString());

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                languages.put(jsonObject.getString("code"), jsonObject.getString("name"));
            }
            activity.runOnUiThread(() -> activity.receiveFullLanguages(languages));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            activity.runOnUiThread(() -> activity.receiveFullLanguages(null));
        }
    }
}
