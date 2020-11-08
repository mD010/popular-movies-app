package com.ydeng.popular_movies_app.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    private static final String TAG = "NetworkUtils";

    private static final String BASE_URL = "https://api.themoviedb.org/3/movie";
    private static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500/";
    private static final String API_KEY = "api_key";

    // this apiKey needs to be replaced to the actual key in order to run the app
    private static final String apiKey = "{api_key}";


    public static URL buildUrl(String subUrl) {
        String urlString = String.format("%s/%s", BASE_URL, subUrl);
        Uri builtUri = Uri.parse(urlString).buildUpon()
                .appendQueryParameter(API_KEY, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);
        return url;
    }

    public static String buildImageUrlString(String path) {
        String urlString = String.format("%s/%s", BASE_IMAGE_URL, path);
        return Uri.parse(urlString).buildUpon()
                .appendQueryParameter(API_KEY, apiKey)
                .build().toString();
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
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
}
