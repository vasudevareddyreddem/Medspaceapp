package com.medarogya.appointment.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.medarogya.appointment.R;
import com.medarogya.appointment.activity.BaseActivity;
import com.medarogya.appointment.activity.HealthReports;
import com.medarogya.appointment.activity.MyReportDownload;
import com.medarogya.appointment.activity.MyReportUpload;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.apis.MyService;
import com.medarogya.appointment.model.AcceptListPJ;
import com.medarogya.appointment.model.RegResult;
import com.medarogya.appointment.utils.MessageToast;
import com.medarogya.appointment.utils.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Timer;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Bhupi on 29-Oct-18.
 */

public class AcceptAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;

    List<AcceptListPJ> acceptList;

    SessionManager manager;
    MyService service;
    public AcceptAdapter(Context context, List<AcceptListPJ> acceptList, SessionManager manager, MyService service) {
  this.acceptList=acceptList;
  this.context=context;
  this.manager=manager;
  this.service=service;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.accept_list_card, parent, false);
        return new AcceptHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {


        if (holder instanceof AcceptHolder){
            AcceptHolder acceptholder= (AcceptHolder) holder;
            acceptholder.tv_date.setText(acceptList.get(position).getDate());
            acceptholder.cardholdername.setText(acceptList.get(position).getPatinet_name());
            acceptholder.tv_hospital_name.setText(acceptList.get(position).getHospitalName());
            acceptholder.tv_time_slot.setText(acceptList.get(position).getTime());
            acceptholder.tv_dpt_name.setText(acceptList.get(position).getDepartment());
            acceptholder.btn_my_rescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(context,MyReportDownload. class);
                    i.putExtra("hos_id",acceptList.get(position).getHos_id());
                    context.startActivity(i);
                }
            });

            acceptholder.uploadReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i=new Intent(context,MyReportUpload. class);
                    i.putExtra("hos_id",acceptList.get(position).getHos_id());
                    context.startActivity(i);

                }
            });
    }}

    @Override
    public int getItemCount() {
        return acceptList.size();
    }

    public class AcceptHolder extends RecyclerView.ViewHolder{
        TextView tv_date,tv_dpt_name,tv_time_slot,tv_hospital_name,cardholdername;
        Button btn_my_rescription,uploadReport;
        public AcceptHolder(View itemView) {
            super(itemView);
            tv_date= itemView.findViewById(R.id.tv_date);
            tv_dpt_name= itemView.findViewById(R.id.tv_dpt_name);
            tv_hospital_name= itemView.findViewById(R.id.tv_hospital_name);
            tv_time_slot= itemView.findViewById(R.id.tv_time_slot);
            btn_my_rescription= itemView.findViewById(R.id.btn_my_rescription);
            uploadReport= itemView.findViewById(R.id.upload_report);
            cardholdername= itemView.findViewById(R.id.cardholdername);


        }
    }

}


