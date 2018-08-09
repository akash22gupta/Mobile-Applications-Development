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

public class AsyncLoaderTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "";
    private MainActivity mainActivity;
    ArrayList<HashMap<String, String>> sourceList = new ArrayList<>();

    private int count;

    public AsyncLoaderTask(MainActivity ma) {
        mainActivity = ma;
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
            mainActivity.updateData(sourceList);

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
            JSONArray sources = jObjMain.getJSONArray("sources");
            String id = null;
            String name = null;
            String url=null;
            String category=null;

            if(sources.length()>0 && sources!=null)
            for (int i = 0; i < sources.length(); i++) {
                JSONObject o = sources.getJSONObject(i);
                HashMap<String, String> source = new HashMap<>();

                if(o.has("id"))
                    id = o.getString("id");

                if(o.has("name"))
                     name = o.getString("name");

                if(o.has("url"))
                     url = o.getString("url");

                if(o.has("category"))
                     category = o.getString("category");

                source.put("id", id);
                source.put("name", name);
                source.put("url", url);
                source.put("category", category);

                sourceList.add(source);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
