package com.medarogya.appointment.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.medarogya.appointment.R;
import com.medarogya.appointment.model.SearchRelatedTest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bhupi on 20-Nov-18.
 */

class SearchRelatedInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    SearchRelatedTest data;
    List<SearchRelatedTest.List> list = new ArrayList<>();

    public SearchRelatedInfoAdapter(Context context, SearchRelatedTest data) {
        this.context = context;
        this.data = data;
        list.addAll(data.list);

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_labs_layout, parent, false);
        return new MyReportHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MyReportHolder) {
            final MyReportHolder reportholder = (MyReportHolder) holder;

            reportholder.lab_name.setText(list.get(position).name);
            if(list.get(position).accrediations==null)
            {            reportholder.lab_accrediations.setText("");
            }
            else {
                reportholder.lab_accrediations.setText(list.get(position).accrediations.toString());
            }if (list.get(position).testNames.size() == 1) {

                reportholder.lab_test.setText(list.get(position).testNames.get(0).testName);
            }
            if (list.get(position).testNames.size() == 2) {

                reportholder.lab_test.setText(list.get(position).testNames.get(0).testName + "," + list.get(position).testNames.get(1).testName);
            }
            if (list.get(position).testNames.size() >= 3) {

                reportholder.lab_test.setText(list.get(position).testNames.get(0).testName + "," + list.get(position).testNames.get(1).testName + "," + list.get(position).testNames.get(2).testName);
            }
            reportholder.lab_view_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SearchTestActivity.class);
                    intent.putExtra("a_id", list.get(position).aId);
                    context.startActivity(intent);


                }
            });
            if (list.get(position).profilePic==null) {

                Glide.with(context)
                        .load(R.drawable.labplaceholder)

                        .into(reportholder.lab_iamge);

            } else {
                Glide.with(context)
                        .load(data.imgpath +"/"+ list.get(position).profilePic)

                        .into(reportholder.lab_iamge);
            }
        }
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class MyReportHolder extends RecyclerView.ViewHolder {
        TextView lab_name, lab_test, txt3,lab_accrediations;
        ImageView lab_iamge;
        Button lab_view_profile;

        public MyReportHolder(View itemView) {
            super(itemView);
            lab_name = itemView.findViewById(R.id.lab_nam);
            lab_test = itemView.findViewById(R.id.lab_test);
            txt3 = itemView.findViewById(R.id.txt3);
            lab_iamge = itemView.findViewById(R.id.lab_iamge);
            lab_view_profile = itemView.findViewById(R.id.lab_view_profile);
            lab_accrediations = itemView.findViewById(R.id.lab_accrediations);

        }
    }

}
