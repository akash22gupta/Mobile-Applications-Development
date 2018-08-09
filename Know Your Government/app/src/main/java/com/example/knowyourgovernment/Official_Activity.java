package com.example.knowyourgovernment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.textservice.TextInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class Official_Activity extends AppCompatActivity {

    private TextView name;
    private TextView party;
    private TextView office;
    private TextView actualphone;
    private TextView actualaddress;
    private TextView actualemail;
    private TextView actualwebsite;
    private TextView location;
    private ImageView officialImage;
    private ImageView facebookimage;
    private ImageView youtubeimage;
    private ImageView twitterimage;
    private ImageView googlePlusimage;
    private String locationToEnter;
    private  String photoUrl;


    HashMap<String, String> tempHash = new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        //locationData =(HashMap<String, String>) getIntent().getSerializableExtra("locationData");
        locationToEnter = getIntent().getStringExtra("locationData");
        location = (TextView) findViewById(R.id.loc);

        location.setText(locationToEnter);

        name = (TextView) findViewById(R.id.name);
        party = (TextView) findViewById(R.id.party);
        office = (TextView) findViewById(R.id.office);
        actualphone = (TextView) findViewById(R.id.actualphone);
        actualaddress = (TextView) findViewById(R.id.actualaddress);
        actualemail = (TextView) findViewById(R.id.actualemail);
        actualwebsite = (TextView) findViewById(R.id.actualweb);
        officialImage = (ImageView) findViewById(R.id.photoOfOfficial);
        facebookimage = (ImageView) findViewById(R.id.facebook);
        youtubeimage = (ImageView) findViewById(R.id.youtube);
        googlePlusimage = (ImageView) findViewById(R.id.googleplus);
        twitterimage = (ImageView) findViewById(R.id.twitter);



        tempHash = (HashMap<String, String>) getIntent().getSerializableExtra("tempHash");

        name.setText(tempHash.get("officialName"));
        String checkParty = tempHash.get("party");
        if(checkParty.equals("Unknown")){
            party.setText("");
        }else
             party.setText("("+tempHash.get("party")+")");

        office.setText(tempHash.get("officesName"));

        String line1 = tempHash.get("line1");
        String line2 = tempHash.get("line2");
        String line3 =tempHash.get("line3");
        String city =tempHash.get("city");
        String state =tempHash.get("state");
        String zip =tempHash.get("zip");
        String address = null;
        String facebook = tempHash.get("facebook");
        String twitter = tempHash.get("twitter");
        String youtube= tempHash.get("youtube");
        String google= tempHash.get("googlePlus");
         photoUrl = tempHash.get("photo");

        if(facebook== null){
            facebookimage.setVisibility(View.GONE);
        }
        if(twitter== null){
            twitterimage.setVisibility(View.GONE);
        }
        if(youtube== null){
            youtubeimage.setVisibility(View.GONE);
        }
        if(google== null){
            googlePlusimage.setVisibility(View.GONE);
        }

        if (line1 != null) {
            address = line1;
        }
        if(line2 != null){
            address = address +"\n"+ line2;
        }
        if(line3 != null){
            address = address + " "+line3;
        }

        if(city != null){
            address = address + '\n' + city;
        }
        if(state != null){
            address = address + ", " + state;
        }
        if(zip != null){
            address = address + " "+zip;
        }

        if(address == null){
            actualaddress.setText("No Data Provided");
        }else {
            actualaddress.setText(address);
            Linkify.addLinks(actualaddress , Linkify.MAP_ADDRESSES);
        }
        String checkEmail = tempHash.get("email");

        if(checkEmail==null){
            actualemail.setText("No Data Provided");

        }else {
            actualemail.setText(tempHash.get("email"));
            Linkify.addLinks(((TextView) findViewById(R.id.actualemail)), Linkify.EMAIL_ADDRESSES);
        }
        String checkPhone = tempHash.get("phone");
        if(checkPhone==null) {
            actualphone.setText("No Data Provided");
        }else {
            actualphone.setText(tempHash.get("phone"));
            Linkify.addLinks(((TextView) findViewById(R.id.actualphone)), Linkify.PHONE_NUMBERS);
        }

        String checkWeb = tempHash.get("url");
        if(checkWeb==null) {
            actualwebsite.setText("No Data Provided");
        }else {
            actualwebsite.setText(tempHash.get("url"));
            Linkify.addLinks(((TextView) findViewById(R.id.actualweb)), Linkify.WEB_URLS);
        }


        if(tempHash.get("party").equals("Democratic") || tempHash.get("party").equals("Democrat"))
            getWindow().getDecorView().setBackgroundColor(Color.BLUE);
            else
                if(tempHash.get("party").equals("Republican"))
                    getWindow().getDecorView().setBackgroundColor(Color.RED);
        else
                    getWindow().getDecorView().setBackgroundColor(Color.BLACK);


        if(photoUrl == null){
            officialImage.setImageResource(R.drawable.missingimage);
        }else
            loadImage(photoUrl);


    }


    private void loadImage(final String imageURL) {

        if (imageURL != null) {
            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
// Here we try https if the http image attempt failed
                    final String changedUrl = imageURL.replace("http:", "https:");
                    picasso.load(changedUrl)
                            .fit()
                            .error(R.drawable.missingimage)
                            .placeholder(R.drawable.placeholder)
                            .into(officialImage);
                }
            }).build();
            picasso.load(imageURL)
                    .fit()
                    .error(R.drawable.missingimage)
                    .placeholder(R.drawable.placeholder)
                    .into(officialImage);
        } else {
            Picasso.with(this).load(imageURL)
                    .fit()
                    .error(R.drawable.missingimage)
                    .placeholder(R.drawable.placeholder)
                    .into(officialImage);
        }
    }


    public void photoClicked(View v) {
        if(photoUrl != null){
            Intent intent = new Intent(this, Photo_Detail_Activity.class);
            intent.putExtra("officialName",tempHash.get("officialName"));
            intent.putExtra("officeName",tempHash.get("officesName"));
            intent.putExtra("photoUrl", tempHash.get("photo"));
            intent.putExtra("party", tempHash.get("party"));
            intent.putExtra("locationData", locationToEnter);
            startActivity(intent);
        }

    }

    public void clickTwitter(View v) {
        String user = tempHash.get("twitter");
        String twitterAppUrl = "twitter://user?screen_name=" + user;
        String twitterWebUrl = "https://twitter.com/" + user;

        Intent intent = null;
        try {
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterAppUrl));
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterWebUrl));
        }
        startActivity(intent);
    }

    public void clickFacebook(View v) {
        String fbName = tempHash.get("facebook");
        String FACEBOOK_URL = "https://www.facebook.com/" + fbName;

        Intent intent = null;
        String urlToUse;
        try {
            getPackageManager().getPackageInfo("com.facebook.katana", 0);

            int versionCode = getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                urlToUse = "fb://page/" + fbName;
            }
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToUse));
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK_URL));
        }

        startActivity(intent);
    }

    public void clickgooglePlus(View v) {
        String name = tempHash.get("googlePlus");
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus",
                    "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", name);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://plus.google.com/" + name)));
        }
    }



    public void clickYoutube(View v) {
        String name = tempHash.get("youtube");
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + name)));
        }
    }
}
