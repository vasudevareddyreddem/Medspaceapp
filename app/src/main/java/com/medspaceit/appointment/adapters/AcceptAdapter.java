package com.medspaceit.appointment.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.medspaceit.appointment.R;
import com.medspaceit.appointment.activity.MyReportDownload;
import com.medspaceit.appointment.apis.ApiUrl;
import com.medspaceit.appointment.model.AcceptListPJ;

import java.util.List;

/**
 * Created by Bhupi on 29-Oct-18.
 */

public class AcceptAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<AcceptListPJ> acceptList;

    public AcceptAdapter(Context context, List<AcceptListPJ> acceptList) {
  this.acceptList=acceptList;
  this.context=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.accept_list_card, parent, false);
        return new AcceptHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof AcceptHolder){
            AcceptHolder acceptholder= (AcceptHolder) holder;
            acceptholder.tv_date.setText(acceptList.get(position).getDate());
            acceptholder.tv_hospital_name.setText(acceptList.get(position).getHospitalName());
            acceptholder.tv_time_slot.setText(acceptList.get(position).getTime());
            acceptholder.tv_dpt_name.setText(acceptList.get(position).getDepartment());
            acceptholder.btn_my_rescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(context,MyReportDownload. class);
                    context.startActivity(i);
                }
            });
    }}

    @Override
    public int getItemCount() {
        return acceptList.size();
    }

    public class AcceptHolder extends RecyclerView.ViewHolder{
        TextView tv_date,tv_dpt_name,tv_time_slot,tv_hospital_name;
        Button btn_my_rescription,btn_lab_reports;
        public AcceptHolder(View itemView) {
            super(itemView);
            tv_date= itemView.findViewById(R.id.tv_date);
            tv_dpt_name= itemView.findViewById(R.id.tv_dpt_name);
            tv_hospital_name= itemView.findViewById(R.id.tv_hospital_name);
            tv_time_slot= itemView.findViewById(R.id.tv_time_slot);
            btn_my_rescription= itemView.findViewById(R.id.btn_my_rescription);
            btn_lab_reports= itemView.findViewById(R.id.btn_lab_reports);


        }
    }
}


