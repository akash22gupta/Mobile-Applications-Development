package com.example.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by agupt on 4/5/2017.
 */

public class KygAdapter extends RecyclerView.Adapter<KygViewholder> {

    private MainActivity mainact;
    private List<Official> officialList;
   // ArrayList<HashMap<String, String>> officesList;



    public KygAdapter(List<Official> officialList, MainActivity mainActivity) {
        this.officialList=officialList;
        mainact=mainActivity;
    }


    @Override
    public KygViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list,parent,false);

        itemView.setOnClickListener(mainact);
        return new KygViewholder(itemView);
    }


    @Override
    public void onBindViewHolder(KygViewholder holder, int position) {
        Official o = officialList.get(position);
        holder.designation.setText(o.getDesignation());
        holder.name.setText(o.getName());
        holder.party.setText( " (" + o.getParty() + ")");
    }

    @Override
    public int getItemCount() {
        return officialList.size();
    }
}
