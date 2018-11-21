package com.medspaceit.appointments.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.medspaceit.appointments.R;
import com.medspaceit.appointments.adapters.MyReportDownloadAdapter;
import com.medspaceit.appointments.model.MyReportDownloadPoojo;
import com.medspaceit.appointments.model.SearchRelatedTest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bhupi on 20-Nov-18.
 */

class SearchRelatedInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;

    List<SearchRelatedTest.List> list=new ArrayList<>();
    public SearchRelatedInfoAdapter(Context context,SearchRelatedTest data) {
        this.context=context;
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
            if(list.get(position).testNames.size()!=0) {
//                for (int i = 0; i <list.get(position).testNames.size() ; i++) {
//                    String s = TextUtils.join(", ", new String[]{list.get(i).testNames.get(i).testName});
//                    Log.d("comma data====", s+"");
//                }

                reportholder.lab_test.setText(list.get(position).testNames.get(position).testName);
            }
            reportholder.lab_view_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,SearchTestActivity.class);
                    intent.putExtra("a_id",list.get(position).aId);
                    context.startActivity(intent);


                }
            });


        }}

    @Override
    public int getItemCount() {

        return list.size();
    }
    public class MyReportHolder extends RecyclerView.ViewHolder {
        TextView lab_name,lab_test,txt3;
        ImageView lab_iamge;
        Button lab_view_profile;
        public MyReportHolder(View itemView) {
            super(itemView);
            lab_name = itemView.findViewById(R.id.lab_name);
            lab_test = itemView.findViewById(R.id.lab_test);
            txt3 = itemView.findViewById(R.id.txt3);
            lab_iamge = itemView.findViewById(R.id.lab_iamge);
            lab_view_profile = itemView.findViewById(R.id.lab_view_profile);

        }}

}
