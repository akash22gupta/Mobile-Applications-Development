package com.example.multinotes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by agupt on 2/26/2017.
 */

public class Lastnote implements Parcelable{
    private String datetime;
    private String notes;
    private String title;
    int id=0;

    public Lastnote(int id, String title, String notes, String datetime) {
        this.id = id;
        this.title = title;
        this.datetime = datetime;
        this.notes = notes;
    }

    protected Lastnote(Parcel in) {
        readFromParcel(in);
    }


    public static final Creator<Lastnote> CREATOR = new Creator<Lastnote>() {
        @Override
        public Lastnote createFromParcel(Parcel in) {
            return new Lastnote(in);
        }

        @Override
        public Lastnote[] newArray(int size) {
            return new Lastnote[size];
        }
    };

    public Lastnote() {

    }


    public void setId(int id){
        this.id=id;
    }

    public int getId(){
        return id;
    }

    public String getnotes(){
        return notes;
    }

    public String gettitle(){
        return title;
    }
    public String getdatetime(){return datetime;}

    public void setnotes(String notes){
        this.notes = notes;
    }

    public void setdatetime(String datetime) {
        this.datetime = datetime;
    }

    public void settitle(String title){
        this.title = title;
    }

    @Override
    public String toString() {
        return "Notes{" +
                "title='" + title + '\'' +
                ", notes='" + notes + '\'' +
                ", date='" + datetime + '\'' +
                '}';
    }
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(notes);
        dest.writeString(datetime);
        dest.writeInt(id);
    }

    private void readFromParcel(Parcel in) {
        title = in.readString();
        notes = in.readString();
        datetime=in.readString();
        id = in.readInt();


    }

}
