package com.example.stockwatch;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
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
 * Created by agupt on 3/19/2017.
 */

public class AsyncDataLoader extends AsyncTask<String,Void,String>{

    private static final String TAG ="" ;
    private static String name;
    private MainActivity mainActivity;
    private int count;

    private static String stockdownload = "http://finance.google.com/finance/info?client=ig&q=";

    public AsyncDataLoader (MainActivity ma) {
        mainActivity = ma;
    }

    @Override
    protected void onPreExecute() {

       // Toast.makeText(mainActivity, "Finding Stock Symbol..", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String sb) {
        if (sb == null) {
            mainActivity.noDataFound();

        } else {
            String clean = sb.replaceAll("//", "");
            System.out.print("Sb data after clean:"+clean);
            TempStock tempStockObject = parseJSON(clean);
            mainActivity.tempchooseSymbol(tempStockObject);
        }
    }

    @Override
    protected String doInBackground(String... params) {


        Uri symbolUri = Uri.parse(stockdownload + params[0]);
        name = params[1];
        System.out.println("Company Name in AsynDataLoader" + name);
        System.out.println("Company Symbol in AsynDataLoader" + params[0]);

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


    private TempStock parseJSON(String s) {

        //TempStock tempStockObject = new TempStock;
        try {
            JSONArray jObjMain = new JSONArray(s);
            count = jObjMain.length();
            System.out.println("New Count=" + count);
            Log.d(TAG, String.valueOf(count));
            TempStock StockObject = null;
            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jStock = (JSONObject) jObjMain.get(i);
                String symbol = jStock.getString("t");
               // String name = jStock.getString("t");
                //String name = mainActivity.findCompanyName(symbol);
                //System.out.println("Hie my name is: "+name);

                String value = jStock.getString("l");
                String updown = jStock.getString("c");
                String change = jStock.getString("c");
                String changep = jStock.getString("cp");


                System.out.println("Company Name in AsynDataLoader" + name);
                

                StockObject = new TempStock(symbol, name, value, updown, change, changep);

            }
            return StockObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
