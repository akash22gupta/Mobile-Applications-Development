package com.example.multinotes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by agupt on 2/26/2017.
 */

public class MyViewHolder extends RecyclerView.ViewHolder{
    public TextView title;
    public TextView datetime;
    public TextView cardnote;

    public MyViewHolder(View view){
        super(view);
        title = (TextView) view.findViewById(R.id.title);
        datetime = (TextView) view.findViewById(R.id.datetime);
        cardnote = (TextView) view.findViewById(R.id.cardnote);
    }
}
