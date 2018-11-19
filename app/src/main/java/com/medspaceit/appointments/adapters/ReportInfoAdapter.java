package com.medspaceit.appointments.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.medspaceit.appointments.R;
import com.medspaceit.appointments.activity.BookTestPackageOnline;
import com.medspaceit.appointments.activity.MyReportDownload;
import com.medspaceit.appointments.activity.MyReports;
import com.medspaceit.appointments.model.AllLabPJ;
import com.medspaceit.appointments.model.DownloadReportPJ;
import com.medspaceit.appointments.model.MyReportPJ;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bhupi on 19-Nov-18.
 */

public class ReportInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context context;
    List<MyReportPJ> reportInfoList;
    List<DownloadReportPJ> downloadReportList;
    public ReportInfoAdapter(Context context, List<MyReportPJ> reportInfoList, List<DownloadReportPJ> downloadReportList) {
        this.context=context;
        this.reportInfoList=reportInfoList;
        this.downloadReportList=downloadReportList;
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
            MyReportHolder reportholder = (MyReportHolder) holder;

            reportholder.Labname.setText(reportInfoList.get(position).getLabname());
            reportholder.Total.setText(reportInfoList.get(position).getTotal());
            reportholder.Discount.setText(reportInfoList.get(position).getDiscount());
            reportholder.Address.setText(reportInfoList.get(position).getAddress());

            MyReportDownloadAdapter adapter=new MyReportDownloadAdapter(context,downloadReportList);
            reportholder.downloadReportListview.setAdapter(adapter);


        }}

    @Override
    public int getItemCount() {

        return reportInfoList.size();
    }
    public class MyReportHolder extends RecyclerView.ViewHolder {
        TextView Labname,Total,Discount,Address;
        ListView downloadReportListview;
        public MyReportHolder(View itemView) {
            super(itemView);
            Labname = itemView.findViewById(R.id.lab_name);
            Total = itemView.findViewById(R.id.total);
            Discount = itemView.findViewById(R.id.discount);
            Address = itemView.findViewById(R.id.labaddress);
            downloadReportListview = itemView.findViewById(R.id.downloadReportListview);

        }}

}
