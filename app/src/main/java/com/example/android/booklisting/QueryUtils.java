package com.example.android.booklisting;

import android.graphics.drawable.Drawable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017-09-25.
 */

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();
    private static final String NO_JSON_OBJECT_STRING = "N/A";
    private static final int NO_JSON_OBJECT_INT = 0;

    private QueryUtils() {
    }

    public static List<Book> fetchBookData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);//
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making Http request.", e);
        }

        List<Book> books = extractFeatureFromJson(jsonResponse);
        return books;
    }

    //HELPER METHODS
    public static ArrayList<Book> extractFeatureFromJson(String booksJson) {

        ArrayList<Book> books = new ArrayList<>();

        try {
            JSONObject rootJson = new JSONObject(booksJson);

            if (rootJson.getInt("totalItems") != 0) {

                    JSONArray itemsJson = rootJson.getJSONArray("items");

                for(int i=0; i<itemsJson.length(); i++) {
                    JSONObject itemsElementJson = itemsJson.getJSONObject(i);
                    JSONObject volumeInfoJson = itemsElementJson.getJSONObject("volumeInfo");

                    String title = volumeInfoJson.getString("title");

                    ArrayList<String> authors = new ArrayList<>();
                    authors.add(NO_JSON_OBJECT_STRING);
                    if(volumeInfoJson.has("authors")) {
                        authors.remove(NO_JSON_OBJECT_STRING);
                        JSONArray authorsJson  = volumeInfoJson.getJSONArray("authors");
                        for(int j=0; j<authorsJson.length(); j++) {
                            authors.add(authorsJson.getString(j));
                        }
                    }

                    String publisher = NO_JSON_OBJECT_STRING;
                    if(volumeInfoJson.has("publisher")) {
                        publisher = volumeInfoJson.getString("publisher");
                    }

                    String publishedDate = NO_JSON_OBJECT_STRING;
                    if(volumeInfoJson.has("publishedDate")) {
                        publishedDate = volumeInfoJson.getString("publishedDate");
                    }

                    int pageCount = NO_JSON_OBJECT_INT;
                    if(volumeInfoJson.has("pageCount")) {
                        pageCount = volumeInfoJson.getInt("pageCount");
                    }

                    String canonicalVolumeLink = NO_JSON_OBJECT_STRING;
                    if(volumeInfoJson.has("canonicalVolumeLink")) {
                        canonicalVolumeLink = volumeInfoJson.getString("canonicalVolumeLink");
                    }

                    double averageRating = NO_JSON_OBJECT_INT;
                    if(volumeInfoJson.has("averageRating")) {
                        averageRating = volumeInfoJson.getDouble("averageRating");
                    }

                    String smallThumbnailLink = NO_JSON_OBJECT_STRING;
                    //if(volumeInfoJson.has("imageLinks")) {
                        JSONObject imageLinksJson = volumeInfoJson.getJSONObject("imageLinks");
                        smallThumbnailLink = imageLinksJson.getString("smallThumbnail");
                        Drawable thumbnailImage = LoadImageFromWebOperations(smallThumbnailLink);
                    //}

                    books.add(new Book(title, authors, publisher, publishedDate, pageCount,
                            canonicalVolumeLink, averageRating, thumbnailImage));
                }

                Log.i(LOG_TAG, "Numb of items objects in JsonArray = " + itemsJson.length());
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the books JSON results.", e);
        }

        return books;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL", e);
        }
        return url;
    }

    private static String makeHttpRequest (URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");

            if(urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.");
        } finally {
            if (urlConnection == null) {
                urlConnection.disconnect();
            }
            if (inputStream == null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream (InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if(inputStream != null) {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    //GIST helper method - download image
    private static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

}
