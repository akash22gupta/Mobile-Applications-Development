package com.example.notepad;

/**
 * Created by agupt on 2/8/2017.
 */

public class Lastnote {

    private String datetime;
    private String notes;

    public String getnotes()
    {
        return notes;
    }


    public void setnotes(String notes)
    {
        this.notes = notes;
    }

    public String getdatetime()
    {
        return (datetime);
    }

    public void setdatetime(String datetime)

    {
        this.datetime = datetime;
    }

    public String toString()
    {
        return datetime + ": " + notes;

    }
}
