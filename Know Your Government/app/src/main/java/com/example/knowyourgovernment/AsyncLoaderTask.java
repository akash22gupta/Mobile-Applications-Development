package com.example.knowyourgovernment;

import android.content.Intent;
import android.graphics.BitmapFactory;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by agupt on 4/8/2017.
 */

    public class AsyncLoaderTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "";
    private MainActivity mainActivity;
    private HashMap<String, String> wData = new HashMap<>();
    ArrayList<HashMap<String, String>> officesList = new ArrayList<>();



    private int count;

    private static String civicApi = "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyBKNhSFxYJqbGtCR1Zy8My9j9XnDf_yQcg&address=";

    public AsyncLoaderTask(MainActivity ma) {
        mainActivity = ma;
    }

    @Override
    protected void onPreExecute() {


    }

    @Override
    protected void onPostExecute(String sb) {
        if (sb == null) {
            mainActivity.noDataFound();

        } else {

            parseJSON(sb);
            mainActivity.updateData(wData, officesList);


        }
    }

    @Override
    protected String doInBackground(String... params) {


        Uri symbolUri = Uri.parse(civicApi + params[0]);

        Log.d(TAG, String.valueOf(Uri.parse(civicApi + params[0])));

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
        return sb.toString();


    }


    private void parseJSON(String s) {

        try {
            JSONObject jObjMain = new JSONObject(s);
            JSONObject jNormalizedInput = jObjMain.getJSONObject("normalizedInput");
            wData.put("city", jNormalizedInput.getString("city"));
            wData.put("state", jNormalizedInput.getString("state"));
            wData.put("zip", jNormalizedInput.getString("zip"));

            JSONArray offices = jObjMain.getJSONArray("offices");
            //System.out.println("Offices length:" + offices.length());

            for (int i = 0; i < offices.length(); i++) {
                JSONObject o = offices.getJSONObject(i);
                String officesName = o.getString("name");

                int indices[] = new int[100];
                JSONArray officialIndices = o.getJSONArray("officialIndices");
                for (int j = 0; j < officialIndices.length(); j++) {
                    HashMap<String, String> official = new HashMap<>();
                    indices[j] = officialIndices.getInt(j);
                    JSONArray officials = jObjMain.getJSONArray("officials");
                    JSONObject of = officials.getJSONObject(indices[j]);
                    JSONArray address;

                    String line1 = null;
                    String line2 = null;
                    String line3 =null;
                    String city =null;
                    String state = null;
                    String zip = null;
                    String urls = null;
                    String party = "Unknown";
                    String phone = null;
                    String email = null;
                    String photo = "Unknown";
                    String googlePlus = null;
                    String facebook = null;
                    String twitter = null;
                    String youtube = null;

                    if (of.has("address")) {
                        address = of.getJSONArray("address");

                        for (int k = 0; k < address.length(); k++) {
                            JSONObject addressObj = address.getJSONObject(k);
                            if (addressObj.has("line1")) {
                                line1 = addressObj.getString("line1");
                            }
                            if (addressObj.has("line2")) {
                                line2 = addressObj.getString("line2");
                            }
                            if (addressObj.has("line3")) {
                                line3 = addressObj.getString("line3");
                            }
                            if (addressObj.has("city")) {
                                city = addressObj.getString("city");
                            }
                            if (addressObj.has("state")) {
                                state = addressObj.getString("state");
                            }
                            if (addressObj.has("zip")) {
                                zip = addressObj.getString("zip");
                            }
                        }
                    }
                    JSONArray phoneno;
                    if (of.has("phones")) {
                        phoneno = of.getJSONArray("phones");

                        for (int k = 0; k < phoneno.length(); k++) {
                            phone = phoneno.getString(0);
                        }
                    }


                    JSONArray url;
                    if (of.has("urls")) {
                        url = of.getJSONArray("urls");
                        for(int k=0; k<url.length();k++) {
                            urls = url.getString(0);
                        }                    }

                    if (of.has("emails")){
                        JSONArray emails = of.getJSONArray("emails");
                        for(int k=0; k< emails.length();k++) {
                            email = emails.getString(0);
                        }                    }


                    String officialName = of.getString("name");
                    if (of.has("party")) {
                         party = of.getString("party");
                    }

                    if (of.has("photoUrl")){
                        photo = of.getString("photoUrl");
                    }
                    else{
                        photo = null;
                    }

                    JSONArray channels;
                    if (of.has("channels")) {
                        channels = of.getJSONArray("channels");


                        for (int k = 0; k < channels.length(); k++) {
                            JSONObject channelsObj = channels.getJSONObject(k);
                            String type;
                            type = channelsObj.getString("type");
                            switch (type) {
                                case "GooglePlus":
                                    googlePlus = channelsObj.getString("id");
                                case "Facebook":
                                    facebook = channelsObj.getString("id");
                                case "Twitter":
                                    twitter = channelsObj.getString("id");
                                case "YouTube":
                                    youtube = channelsObj.getString("id");
                            }
                        }
                    }
                    official.put("officialName", officialName);
                    official.put("officesName", officesName);
                    official.put("party", party);
                    official.put("line1",line1);
                    official.put("line2",line2);
                    official.put("line3",line3);
                    official.put("city",city);
                    official.put("state",state);
                    official.put("zip",zip);
                    official.put("url",urls);
                    official.put("phone",phone);
                    official.put("email",email);
                    official.put("photo",photo);
                    official.put("googlePlus",googlePlus);
                    official.put("facebook",facebook);
                    official.put("twitter",twitter);
                    official.put("youtube",youtube);

                    officesList.add(official);
                }
            }
            } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}