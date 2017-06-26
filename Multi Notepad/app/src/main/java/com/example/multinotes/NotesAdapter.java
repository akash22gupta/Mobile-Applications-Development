package com.example.multinotes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by agupt on 2/26/2017.
 */

public class NotesAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private List<Lastnote> noteList;
    private MainActivity mainAct;


    public NotesAdapter(List<Lastnote> ntsList, MainActivity ma) {
        this.noteList = ntsList;
        mainAct = ma;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Lastnote lastnote = noteList.get(position);
        holder.title.setText(lastnote.gettitle());
        holder.datetime.setText(lastnote.getdatetime());
        holder.cardnote.setText(lastnote.getnotes());
        String newlastnote;
        newlastnote= lastnote.getnotes().replace("\n", "");
        if(lastnote.getnotes().length()>80)
            holder.cardnote.setText(newlastnote.substring(0,50)+"...");
        else
            holder.cardnote.setText(newlastnote);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}