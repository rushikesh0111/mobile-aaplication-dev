package com.example.abc.newsaggregator.controllers;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionUtils {

    //
    //
    //this class will check for a network connection
    public static boolean hasNetworkConnection(Activity activity) {
        ConnectivityManager connectivityManager =
                activity.getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
