package com.medspaceit.appointments.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.medspaceit.appointments.R;
import com.medspaceit.appointments.model.DownloadReportPJ;
import com.medspaceit.appointments.model.MyReportDownloadPoojo;
import com.medspaceit.appointments.model.MyReportPJ;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bhupi on 19-Nov-18.
 */

public class ReportInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context context;
        boolean flag=true;
    List<MyReportDownloadPoojo.Report>list=new ArrayList<>();
    public ReportInfoAdapter(Context context,MyReportDownloadPoojo data) {
        this.context=context;
        list.addAll(data.report);

    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reportinfolayout, parent, false);
        return new MyReportHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyReportHolder) {
            final MyReportHolder reportholder = (MyReportHolder) holder;

            reportholder.Labname.setText(list.get(position).labname);
            reportholder.Total.setText(list.get(position).total);
            reportholder.Discount.setText(list.get(position).discount);
            reportholder.Address.setText(list.get(position).address);
            reportholder.txt_view_report_for_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(flag)
                    {
                        reportholder.txt_view_report_for_download.setText("Hide Details");
                    reportholder.downloadReportSpinner.performClick();
                    reportholder.downloadReportSpinner.setVisibility(View.VISIBLE);
                     flag=false;
                    }
                    else {
                        reportholder.downloadReportSpinner.setVisibility(View.GONE);
                        reportholder.txt_view_report_for_download.setText("View Details");
                        flag=true;
                    }
                }
            });

            MyReportDownloadAdapter adapter=new MyReportDownloadAdapter(context,list.get(position).viewDetails);
            reportholder.downloadReportSpinner.setAdapter(adapter);


        }}

    @Override
    public int getItemCount() {

        return list.size();
    }
    public class MyReportHolder extends RecyclerView.ViewHolder {
        TextView Labname,Total,Discount,Address,txt_view_report_for_download;
        Spinner downloadReportSpinner;
        public MyReportHolder(View itemView) {
            super(itemView);
            Labname = itemView.findViewById(R.id.lab_name);
            Total = itemView.findViewById(R.id.total);
            Discount = itemView.findViewById(R.id.discount);
            Address = itemView.findViewById(R.id.labaddress);
            txt_view_report_for_download = itemView.findViewById(R.id.txt_view_report_for_download);
            downloadReportSpinner = itemView.findViewById(R.id.downloadReportSpinner);

        }}

}
