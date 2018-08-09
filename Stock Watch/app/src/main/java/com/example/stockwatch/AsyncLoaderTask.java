package com.example.stockwatch;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by agupt on 3/17/2017.
 */

public class AsyncLoaderTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "";
    private MainActivity mainActivity;
    private int count;

    private static String stocksearch = "http://stocksearchapi.com/api/?api_key=632afd94f44dcab479ed47473527f21853d679c5&search_text=";
   // private static String stockdownload = "http://finance.google.com/finance/info?client=ig&q=";

    public AsyncLoaderTask(MainActivity ma) {
        mainActivity = ma;
    }

    @Override
    protected void onPreExecute() {

        //Toast.makeText(mainActivity, "Finding Stock Symbol..", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String sb) {
        if (sb == null) {
            mainActivity.noSymbolFound();

        } else {

            ArrayList<Stock> stockArrayList = parseJSON(sb);
            mainActivity.chooseSymbol(stockArrayList);
        }
    }

    @Override
    protected String doInBackground(String... params) {


        Uri symbolUri = Uri.parse(stocksearch + params[0]);

        Log.d(TAG, String.valueOf(Uri.parse(stocksearch + params[0])));

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

        Log.d(TAG, "inside doinBackground");
        return sb.toString();

    }


    private ArrayList<Stock> parseJSON(String s) {

        ArrayList<Stock> stockArrayList = new ArrayList<>();
        try {
            JSONArray jObjMain = new JSONArray(s);
            count = jObjMain.length();
            Log.d(TAG, String.valueOf(count));
            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jStock = (JSONObject) jObjMain.get(i);
                String name = jStock.getString("company_name");
                String symbol = jStock.getString("company_symbol");

                Log.d(TAG,"Inside parseJASON" + i);
                stockArrayList.add(
                        new Stock(symbol, name));

            }
            return stockArrayList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
