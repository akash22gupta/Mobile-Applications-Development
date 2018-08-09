package com.example.stockwatch;

import java.io.Serializable;

/**
 * Created by agupt on 3/17/2017.
 */

public class Stock implements Serializable {

    private String symbol;
    private String name;

    public Stock() {
    }

    public Stock(String sym, String nm) {

        //int vle, String ud, int cha, double chap
        symbol = sym;
        name= nm;
    }

    public String getSymbol(){return symbol;}

    public String getName(){return name;}


}
