package com.example.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by agupt on 4/6/2017.
 */

public class KygViewholder extends RecyclerView.ViewHolder {

    public TextView designation;
    public TextView name;
    public  TextView party;


    public KygViewholder(View itemView) {
        super(itemView);
        designation = (TextView) itemView.findViewById(R.id.designation);
        name = (TextView) itemView.findViewById(R.id.name);
        party = (TextView) itemView.findViewById(R.id.party);

    }
}
