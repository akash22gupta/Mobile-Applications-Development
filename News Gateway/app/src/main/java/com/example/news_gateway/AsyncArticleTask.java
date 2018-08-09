package com.example.news_gateway;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by agupt on 5/4/2017.
 */


public class AsyncArticleTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "";
    private MainActivity mainActivity;
    private MyService myService;
    ArrayList<HashMap<String, String>> articleList = new ArrayList<>();

    private int count;

    public AsyncArticleTask(MainActivity ma) {mainActivity = ma;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(String sb) {
        if (sb == null) {
            //  mainActivity.noDataFound();

        } else {
            parseJSON(sb);
            mainActivity.updateData1(articleList);
        }
    }

    @Override
    protected String doInBackground(String... params) {



        Uri symbolUri = Uri.parse(params[0]);
        System.out.println("\n\n\n\n\n\n" + symbolUri);
        Log.d(TAG, String.valueOf(Uri.parse(params[0])));

        String urlToUse = symbolUri.toString();
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;


            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }


        } catch (Exception e) {
            return null;
        }

        System.out.println("\n\n\n\n\n\n" + sb);

        return sb.toString();


    }


    private void parseJSON(String s) {

        try {
            JSONObject jObjMain = new JSONObject(s);
            JSONArray article = jObjMain.getJSONArray("articles");
            for (int i = 0; i < article.length(); i++) {
                String title = null;
                String author = null;
                String description= null;
                String urlToImage= null;
                String publishedAt= null;
                String url= null;



                JSONObject o = article.getJSONObject(i);
                HashMap<String, String> source = new HashMap<>();

                if(o.has("author"))
                    author = o.getString("author");
                if(o.has("title"))
                    title = o.getString("title");
                if(o.has("description"))
                    description = o.getString("description");
                if(o.has("urlToImage"))
                    urlToImage = o.getString("urlToImage");
                if(o.has("publishedAt"))
                    publishedAt = o.getString("publishedAt");
                if(o.has("url"))
                    url = o.getString("url");


                source.put("author", author);
                source.put("title", title);
                source.put("urlToImage", urlToImage);
                source.put("publishedAt", publishedAt);
                source.put("description", description);
                source.put("url", url);



                articleList.add(source);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

