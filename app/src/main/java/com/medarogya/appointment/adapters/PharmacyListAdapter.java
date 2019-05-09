package com.medarogya.appointment.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.medarogya.appointment.R;
import com.medarogya.appointment.activity.MyPharmacyReportUpload;
import com.medarogya.appointment.activity.MyReportDownload;
import com.medarogya.appointment.activity.MyReportUpload;
import com.medarogya.appointment.activity.OrderMedicines;
import com.medarogya.appointment.activity.PharmacyUploadPic;
import com.medarogya.appointment.apis.MyService;
import com.medarogya.appointment.model.AcceptListPJ;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bhupi on 07-Mar-19.
 */

public class PharmacyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    OrderMedicines context;
    List<PharmacyUploadPic.Message> pharmacyList = new ArrayList<>();
    PharmacyUploadPic data;
    public PharmacyListAdapter(OrderMedicines context, PharmacyUploadPic data) {
        this.data=data;
        this.context=context;
        pharmacyList.addAll(data.message);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharmacy_list_card, parent, false);
        return new AcceptHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {


        if (holder instanceof AcceptHolder){
            AcceptHolder acceptholder= (AcceptHolder) holder;
            acceptholder.pharmacy_name.setText("Pharmacy: "+pharmacyList.get(position).name);



            acceptholder.btn_my_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i=new Intent(context,MyPharmacyReportUpload. class);
                    i.putExtra("phar_id",String.valueOf(pharmacyList.get(position).pharId));
                    context.startActivity(i);

                }
            });
        }}

    @Override
    public int getItemCount() {
        return pharmacyList.size();
    }

    public class AcceptHolder extends RecyclerView.ViewHolder{
        TextView pharmacy_name;
        Button btn_my_upload;
        public AcceptHolder(View itemView) {
            super(itemView);
            pharmacy_name= itemView.findViewById(R.id.pharmacy_name);
            btn_my_upload= itemView.findViewById(R.id.btn_my_upload);



        }
    }

}


