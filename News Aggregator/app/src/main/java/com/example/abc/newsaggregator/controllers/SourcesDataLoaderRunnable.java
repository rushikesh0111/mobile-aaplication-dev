package com.example.abc.newsaggregator.controllers;

import static com.android.volley.Request.Method.GET;

import android.net.Uri;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.abc.newsaggregator.activities.MainActivity;
import com.example.abc.newsaggregator.models.SourceModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SourcesDataLoaderRunnable implements Runnable {
    private final String TAG = getClass().getName();

    private final MainActivity mainActivity;
    private static final String DATA_URL = "https://newsapi.org/v2/sources";
    private static final String API_KEY = "a2aaff3763294444a686404790ec439b";

    public SourcesDataLoaderRunnable(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    //
    //
    //
    @Override
    public void run() {
        Uri.Builder builder = Uri.parse(DATA_URL).buildUpon();

        builder.appendQueryParameter("apiKey", API_KEY);

        String urlToUse = builder.build().toString();
        StringRequest stringRequestSources = new StringRequest(GET, urlToUse, this::handleResults, error -> mainActivity.runOnUiThread(mainActivity::updatingSourcesFailed)) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("User-agent", "News-App");
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mainActivity.getApplicationContext());
        requestQueue.add(stringRequestSources);
    }

    //
    //
    //this method will handle the results
    private void handleResults(final String json) {
        if (json != null) {
            try {
                Log.d(TAG, "JSON -> " + json);
                JSONObject jObjMain = new JSONObject(json);
                JSONArray sourceList = jObjMain.getJSONArray("sources");
                ArrayList<SourceModel> sources = new ArrayList<>();
                Set<String> languages = new HashSet<>();
                Set<String> categories = new HashSet<>();
                Set<String> countries = new HashSet<>();

                for (int i = 0; i < sourceList.length(); i++) {
                    JSONObject s = sourceList.getJSONObject(i);
                    String language = s.getString("language");
                    String category = s.getString("category");
                    String country = s.getString("country");
                    SourceModel dto = new SourceModel(s.getString("id"), s.getString("name"), category,
                            language, country);
                    languages.add(language);
                    categories.add(category);
                    countries.add(country);
                    sources.add(dto);
                }

                mainActivity.runOnUiThread(() -> mainActivity.updatingSourcesSuccess(sources,
                        languages, categories, countries));
            } catch (JSONException e) {
                e.printStackTrace();
                mainActivity.runOnUiThread(mainActivity::updatingSourcesFailed);
            }
        } else {
            mainActivity.runOnUiThread(mainActivity::updatingSourcesFailed);
        }
    }
}
