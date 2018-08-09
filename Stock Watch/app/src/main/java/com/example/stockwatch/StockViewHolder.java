package com.example.stockwatch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by agupt on 3/17/2017.
 */

public class StockViewHolder extends RecyclerView.ViewHolder {

    public TextView symbol;
    public TextView name;
    public TextView value;
    public ImageView upDown;
    //public TextView updown;
    public TextView change;
    public TextView changep;

    public StockViewHolder(View view) {
        super(view);
        symbol = (TextView) view.findViewById(R.id.sSymbol);
        name = (TextView) view.findViewById(R.id.sName);
        value = (TextView) view.findViewById(R.id.sValue);
        upDown = (ImageView) view.findViewById(R.id.sUpDown);
       // updown = (TextView) view.findViewById(R.id.sUpDown);
        change = (TextView) view.findViewById(R.id.sChange);
        changep = (TextView) view.findViewById(R.id.sChangeP);
    }
}
