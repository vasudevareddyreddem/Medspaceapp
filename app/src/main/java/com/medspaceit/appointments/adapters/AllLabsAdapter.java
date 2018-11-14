package com.medspaceit.appointments.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medspaceit.appointments.R;

import com.medspaceit.appointments.activity.BookTestPackageOnline;
import com.medspaceit.appointments.model.AllLabPJ;

import java.util.ArrayList;

/**
 * Created by Bhupi on 14-Nov-18.
 */

public class AllLabsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<AllLabPJ> allLabList;
    public AllLabsAdapter(Context context, ArrayList<AllLabPJ> allLabList) {
        this.context=context;
        this.allLabList=allLabList;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alllablistlayout, parent, false);
        return new AllLabHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof AllLabHolder) {
            AllLabHolder acceptholder = (AllLabHolder) holder;

            acceptholder.a1.setText(allLabList.get(position).getName());
            acceptholder.b1.setText(allLabList.get(position).getTest());
            acceptholder.c1.setText(allLabList.get(position).getPractice());
            acceptholder.linearLayoutLab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context,BookTestPackageOnline.class));
                }
            });

    }}

    @Override
    public int getItemCount() {
        return allLabList.size();
    }
    public class AllLabHolder extends RecyclerView.ViewHolder {
        TextView a1,b1,c1,d1;
LinearLayout linearLayoutLab;
        public AllLabHolder(View itemView) {
            super(itemView);
            a1 = itemView.findViewById(R.id.a1);
            b1 = itemView.findViewById(R.id.b1);
            c1 = itemView.findViewById(R.id.c1);
            d1 = itemView.findViewById(R.id.d1);
            linearLayoutLab = itemView.findViewById(R.id.linearlayoutlab);


        }}
}
