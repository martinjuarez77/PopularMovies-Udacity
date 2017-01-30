package com.martincho.udacity.popularmovies.util;

/**
 * Created by martincho on 28/1/17.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.StrictMode;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String THEMOVIEDB_URL = "http://api.themoviedb.org/3";
    private static final String THEMOVIEDB_URL_CHECK_URL = "https://www.themoviedb.org";


    public static URL buildUrl(String query, String apiKey) {
        Uri builtUri = Uri.parse(THEMOVIEDB_URL + query).buildUpon()
                .appendQueryParameter("api_key", apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException, SocketTimeoutException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(10000);
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static boolean checkInternetAccess(Context context) {

        boolean hasInternetAccess = false;

        try {

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                URL url = new URL(THEMOVIEDB_URL_CHECK_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(5000);
                urlConnection.connect();
                if (urlConnection.getResponseCode() == 200) {
                    hasInternetAccess = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasInternetAccess;
    }
}
