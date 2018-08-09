package com.example.stockwatch;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by agupt on 3/19/2017.
 */

public class TempStockAdapter extends RecyclerView.Adapter<StockViewHolder>{

    private List<TempStock> stockList;
    private MainActivity mainAct;


    public TempStockAdapter(List<TempStock> empList, MainActivity ma) {
        this.stockList = empList;
        mainAct = ma;
    }

    @Override
    public StockViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_list, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new StockViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StockViewHolder holder, int position) {
        TempStock stock = stockList.get(position);
        Double check = Double.parseDouble(stock.getChange());
         if(check > 0){
             holder.symbol.setText(stock.getSymbol());
             holder.symbol.setTextColor(Color.parseColor("#3CE522"));
             holder.name.setText(stock.getName());
             holder.name.setTextColor(Color.parseColor("#3CE522"));

             holder.value.setText(stock.getValue());
             holder.value.setTextColor(Color.parseColor("#3CE522"));

             //boolean
             holder.upDown.setImageResource(R.drawable.up_arrow);

             holder.change.setText(stock.getChange());
             holder.change.setTextColor(Color.parseColor("#3CE522"));

             holder.changep.setText("(" + stock.getChangep() + "%" + ")");
             holder.changep.setTextColor(Color.parseColor("#3CE522"));

         }
        else {
             holder.symbol.setText(stock.getSymbol());
             holder.symbol.setTextColor(Color.parseColor("#FD0504"));
             holder.name.setText(stock.getName());
             holder.name.setTextColor(Color.parseColor("#FD0504"));

             holder.value.setText(stock.getValue());
             holder.value.setTextColor(Color.parseColor("#FD0504"));

             //boolean
             holder.upDown.setImageResource(R.drawable.down_arrow);
             holder.change.setText(stock.getChange());
             holder.change.setTextColor(Color.parseColor("#FD0504"));

             holder.changep.setText("(" + stock.getChangep() + "%" + ")");
             holder.changep.setTextColor(Color.parseColor("#FD0504"));

         }
    }

    @Override
    public int getItemCount() {
        return stockList.size();    }
}
