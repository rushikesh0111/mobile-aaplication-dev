package com.example.abc.newsaggregator.controllers;

import static com.android.volley.Request.Method.GET;

import android.net.Uri;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.abc.newsaggregator.activities.MainActivity;
import com.example.abc.newsaggregator.models.ArticleModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticlesLoaderRunnable implements Runnable {

    private final String sourceId;
    private final MainActivity mainActivity;
    private static final String DATA_URL = "https://newsapi.org/v2/top-headlines";
    private static final String API_KEY = "a2aaff3763294444a686404790ec439b";

    public ArticlesLoaderRunnable(String sourceId, MainActivity mainActivity) {
        this.sourceId = sourceId;
        this.mainActivity = mainActivity;
    }

    //
    //
    //
    @Override
    public void run() {
        Uri.Builder bld = Uri.parse(DATA_URL).buildUpon();

        bld.appendQueryParameter("apiKey", API_KEY);
        bld.appendQueryParameter("sources", sourceId);

        String urlToUse = bld.build().toString();

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
    //
    private String getStringField(JSONObject obj, String field) throws JSONException {

        return obj.has(field) && !obj.isNull(field) ? obj.getString(field) : null;
    }

    //
    //
    //this method will be responsible for handling the results
    private void handleResults(final String json) {
        if (json != null) {
            try {
                JSONObject mainObject = new JSONObject(json);
                JSONArray articleList = mainObject.getJSONArray("articles");
                List<ArticleModel> articles = new ArrayList<>();

                for (int i = 0; i < articleList.length(); i++) {
                    JSONObject a = articleList.getJSONObject(i);

                    String publishedAtStr = a.has("publishedAt") && !a.isNull("publishedAt") ? a
                            .getString("publishedAt") : null;

                    ArticleModel article =
                            new ArticleModel(getStringField(a, "author"), getStringField(a, "title"),
                                    getStringField(a, "description"), getStringField(a, "url"),
                                    getStringField(a, "urlToImage"),
                                    DateTimeUtils.parseDate(publishedAtStr));
                    articles.add(article);
                }

                mainActivity.runOnUiThread(() -> mainActivity.fetchingArticlesSuccess(articles));
            } catch (JSONException e) {
                e.printStackTrace();
                mainActivity.runOnUiThread(mainActivity::updatingSourcesFailed);
            }
        } else {
            mainActivity.runOnUiThread(mainActivity::fetchingArticlesFailed);
        }
    }

}
