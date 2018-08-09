package com.example.knowyourgovernment;

import java.io.Serializable;
import java.io.StringReader;

/**
 * Created by agupt on 4/6/2017.
 */

public class Official implements Serializable{

    private String designation;
    private String name;
    private String party;
    private static int ctr = 1;


    public Official(String desig, String nm, String par){
        designation = desig;
        name = nm;
        party = par;
    }


    public String getDesignation(){
        return designation;
    }

    public String getName(){
        return name;
    }

    public String getParty(){
        return party;
    }


    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParty(String party){this.party = party;}

    @Override
    public String toString() {
        return "Official{" +
                "designation='" + designation + '\'' +
                ", name='" + name + '\'' +
                ", party='" + party + '\'' +
                '}';
    }
}
