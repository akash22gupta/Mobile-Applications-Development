package com.example.stockwatch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by agupt on 3/17/2017.
 */

public class StockAdapter extends RecyclerView.Adapter<StockViewHolder>{

    private List<Stock> stockList;
    private MainActivity mainAct;


    public StockAdapter(List<Stock> empList, MainActivity ma) {
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
        Stock stock = stockList.get(position);
        holder.symbol.setText(stock.getSymbol());
        holder.name.setText(stock.getName());


    }

    @Override
    public int getItemCount() {
        return stockList.size();    }
}
