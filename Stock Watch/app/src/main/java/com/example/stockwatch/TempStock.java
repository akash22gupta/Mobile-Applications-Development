package com.example.stockwatch;

import java.io.Serializable;

/**
 * Created by agupt on 3/19/2017.
 */

public class TempStock implements Serializable {

    private String symbol;
    private String name;
    private String value;
    private String updown;
    private String change;
    private String changep;


    public TempStock(String sym, String nm, String vle, String ud, String cha, String chap) {


    symbol=sym;
    name=nm;
    value=vle;
    updown=ud;
    change=cha;
    changep=chap;
}

    public String getSymbol(){return symbol;}

    public String getName(){return name;}

    public String getValue(){return value;}

    public String getUpdown(){return updown;}

    public String getChange() {return change;}

    public String getChangep(){return changep;}
}
