package com.example.knowyourgovernment;

import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Photo_Detail_Activity extends AppCompatActivity {

    private TextView location;
    private TextView officailName;
    private TextView officeName;
    private ImageView photo;
    private String locationToEnter;

    HashMap<String, String> locationData = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo__detail_);
        officailName = (TextView) findViewById(R.id.name);
        officeName = (TextView) findViewById(R.id.office);
        photo = (ImageView) findViewById(R.id.facebook);
        String url = null;
        String party;

        locationToEnter = getIntent().getStringExtra("locationData");
        location = (TextView) findViewById(R.id.loc);
        location.setText(locationToEnter);

        officeName.setText(getIntent().getStringExtra("officeName"));
        officailName.setText(getIntent().getStringExtra("officialName"));
        url = getIntent().getStringExtra("photoUrl");
        party = getIntent().getStringExtra("party");

        if(url == null){
            photo.setImageResource(R.drawable.missingimage);
        }else
            loadImage(url);


        if (party.equals("Democratic") || party.equals("Democrat"))
            getWindow().getDecorView().setBackgroundColor(Color.BLUE);
        else if (party.equals("Republican"))
            getWindow().getDecorView().setBackgroundColor(Color.RED);
        else
            getWindow().getDecorView().setBackgroundColor(Color.BLACK);

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
                            .into(photo);
                }
            }).build();
            picasso.load(imageURL)
                    .fit()
                    .error(R.drawable.missingimage)
                    .placeholder(R.drawable.placeholder)
                    .into(photo);
        } else {
            Picasso.with(this).load(imageURL)
                    .fit()
                    .error(R.drawable.missingimage)
                    .placeholder(R.drawable.placeholder)
                    .into(photo);
        }
    }
}

