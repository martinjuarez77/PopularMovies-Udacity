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

    /**
     * Build URL with query parameters
     * @param themoviedb_url
     * @param query
     * @param apiKey
     * @return
     */
    public static URL buildUrl(String themoviedb_url, String query, String apiKey) {
        Uri builtUri = Uri.parse(themoviedb_url + query).buildUpon()
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

    /**
     * Get JSON response from the server
     * @param url
     * @return
     * @throws IOException
     * @throws SocketTimeoutException
     */
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

    /**
     * Check internet connection
     * @param themoviedb_url_check_connection
     * @param context
     * @return
     */
    public static boolean checkInternetAccess(String themoviedb_url_check_connection, Context context) {

        boolean hasInternetAccess = false;

        try {

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                URL url = new URL(themoviedb_url_check_connection);
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
