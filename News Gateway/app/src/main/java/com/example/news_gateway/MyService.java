package com.example.news_gateway;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by agupt on 5/4/2017.
 */

public class MyService extends Service {

    private static final String TAG = "MyService";
    private boolean running = true;
    static final String ACTION_MSG_TO_SERVICE = "ACTION_MSG_TO_SERVICE";
    private Receiver Receiver;
    ArrayList<HashMap<String, String>> sourceList = new ArrayList<>();
    ArrayList<HashMap<String, String>> storyList =new ArrayList<>();
    private  AsyncArticleTask aat;
    static final String ACTION_NEWS_STORY = "ACTION_NEWS_STORY";
    private static String article_source1 = "https://newsapi.org/v1/articles?source=";
    private static String article_source2 = "&apiKey=7f1cac5c0744428796db48ffe86d7be5";
    private static String news_source_all = "https://newsapi.org/v1/sources?language=en&country=us&apiKey=7f1cac5c0744428796db48ffe86d7be5";
    private static String news_source1 = "https://newsapi.org/v1/sources?language=en&country=us&category=";
    private static String news_source2 = "&apiKey=7f1cac5c0744428796db48ffe86d7be5";

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Receiver = new Receiver();

        IntentFilter filter1 = new IntentFilter(ACTION_MSG_TO_SERVICE);
        registerReceiver(Receiver, filter1);
        //sendMessage("Service Started");


        //Creating new thread for my service
        //Always write your long running tasks in a separate thread, to avoid ANR

        new Thread(new Runnable() {
            @Override
            public void run() {

                while(running){

                    if(storyList.isEmpty()){
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                    else{
                        Intent intent = new Intent();
                        intent.setAction(ACTION_NEWS_STORY);
                        Log.d(TAG, "run: ");
                       // intent.putParcelableArrayListExtra("storylist", (ArrayList<? extends Parcelable>) storyList);
                        sendBroadcast(intent);
                        storyList.clear();
                    }

                }
            }
        }).start();


        return Service.START_STICKY;
    }

@Override
public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
    unregisterReceiver(Receiver);

    running = false;
        super.onDestroy();
        }


    class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case ACTION_MSG_TO_SERVICE:
                    String source=null;
                    if(intent.hasExtra("SOURCEOBJ")){
                        source  =  intent.getStringExtra("SOURCEOBJ");
                        Log.d(TAG, "onReceive: "+ source);

                        if(source.contains("-"))
                            source.replace("-","");
                        //Toast.makeText(getApplicationContext(), "Hello "+source, Toast.LENGTH_SHORT).show();
                       // aat = new AsyncArticleTask(this);
                        //aat.execute(article_source1+id+article_source2);
                    }
                    else
                    {
                        Log.d(TAG, "onReceive: ");
                    }
                    //articleList = intent.getParcelableArrayListExtra("storylist");
                    //Toast.makeText(getApplicationContext(), "Broadcast Message A Received: ", Toast.LENGTH_SHORT).show();
                    break;


            }
        }
    }


    public void setArticles(ArrayList<HashMap<String, String>> storyList1) {
        storyList = storyList1;
    }
}