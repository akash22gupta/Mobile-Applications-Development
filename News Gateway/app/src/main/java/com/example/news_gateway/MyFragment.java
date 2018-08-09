package com.example.news_gateway;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MyFragment extends Fragment {
    ImageView urlToImage1;
    TextView publishedAt1;
    String formattedTime;


    public static final MyFragment newInstance(String sourceName,int i, int s, HashMap<String, String> article)
    {
        MyFragment f = new MyFragment();
        Bundle bdl = new Bundle(1);

        bdl.putString("author", article.get("author"));
        bdl.putString("title", article.get("title"));
        bdl.putString("description", article.get("description"));
        bdl.putString("urlToImage", article.get("urlToImage"));
        bdl.putString("publishedAt", article.get("publishedAt"));
        bdl.putString("pageNo", String.valueOf(i));
        bdl.putString("totalPageNo", String.valueOf(s));
        bdl.putString("sourceName", String.valueOf(sourceName));
        bdl.putString("url", article.get("url"));

        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String author = getArguments().getString("author");
        String title = getArguments().getString("title");
        String description = getArguments().getString("description");
        String urlToImage = getArguments().getString("urlToImage");
        String publishedAt = getArguments().getString("publishedAt");
        String sourceName = getArguments().getString("sourceName");
        final String url = getArguments().getString("url");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("MMM dd,yyyy  hh:mm");
        Date d = null;
        try {
            d = sdf.parse(publishedAt);
            formattedTime = output.format(d);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String pageNo = getArguments().getString("pageNo");
        String totalPageNo = getArguments().getString("totalPageNo");

        View v = inflater.inflate(R.layout.fragment_main, container, false);

        TextView author1 = (TextView)v.findViewById(R.id.author);
        TextView title1 = (TextView)v.findViewById(R.id.title);
        TextView description1 = (TextView)v.findViewById(R.id.description);
        urlToImage1 = (ImageView) v.findViewById(R.id.image);
        publishedAt1 = (TextView)v.findViewById(R.id.time);
        TextView count = (TextView) v.findViewById(R.id.count);


        title1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        urlToImage1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        description1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        if(urlToImage == null){
            urlToImage1.setImageResource(R.drawable.brokenimage);
        }else
            loadImage(urlToImage);


        if(author == "null")
            author1.setText(sourceName);
        else
            author1.setText(author + ",\n"  + sourceName);

        if(title == "null")
            title1.setText("No Title");
        else
        title1.setText(title);

        if(description == "null")
            description1.setText("");
        else
        description1.setText(description);

        if(formattedTime == "null")
            publishedAt1.setText("");
        else
        publishedAt1.setText(formattedTime);

        count.setText(pageNo+ " of " + totalPageNo);


        return v;
    }






    private void loadImage(final String imageURL) {
        if (imageURL != null) {
            Picasso picasso = new Picasso.Builder(getActivity()).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
// Here we try https if the http image attempt failed
                    final String changedUrl = imageURL.replace("http:", "https:");
                    picasso.load(changedUrl)
                            .fit()
                            .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder)
                            .into(urlToImage1);
                }
            }).build();
            picasso.load(imageURL)
                    .fit()
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(urlToImage1);
        } else {
            Picasso.with(getActivity()).load(imageURL)
                    .fit()
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(urlToImage1);
        }
    }

}
