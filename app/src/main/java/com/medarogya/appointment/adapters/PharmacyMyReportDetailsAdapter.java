package com.medarogya.appointment.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.medarogya.appointment.R;
import com.medarogya.appointment.activity.PharmacyOrderDetails;
import com.medarogya.appointment.model.PharmacyMyReportDetailsPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bhupi on 19-Mar-19.
 */

public class PharmacyMyReportDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    PharmacyMyReportDetailsPojo data;
    PharmacyOrderDetails context;
    List<PharmacyMyReportDetailsPojo.Detail> list = new ArrayList<>();

    public PharmacyMyReportDetailsAdapter(PharmacyOrderDetails context, PharmacyMyReportDetailsPojo data) {
        this.context = context;
        this.data = data;
        list.addAll(data.details);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.pharmacymyreportdetailslayout, parent, false);
        return new MyPAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyPAdapter) {
            final MyPAdapter myholder = (MyPAdapter) holder;


            myholder.phrd_medicine_name.setText(list.get(position).medicineName);
            myholder.phrd_unit_price.setText(list.get(position).unitPrice);
            myholder.phrd_quantity.setText(list.get(position).quantity);
            myholder.phrd_discount.setText(list.get(position).discount);
            myholder.phrd_total.setText(list.get(position).total);
            myholder.phrd_created_date.setText(list.get(position).createdDate);

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyPAdapter extends RecyclerView.ViewHolder {
        TextView phrd_medicine_name,phrd_unit_price,phrd_quantity,phrd_discount,phrd_total,phrd_created_date;

        public MyPAdapter(View itemView) {
            super(itemView);
            phrd_medicine_name = itemView.findViewById(R.id.phrd_medicine_name);
            phrd_unit_price = itemView.findViewById(R.id.phrd_unit_price);
            phrd_quantity = itemView.findViewById(R.id.phrd_quantity);
            phrd_total = itemView.findViewById(R.id.phrd_total);
            phrd_discount = itemView.findViewById(R.id.phrd_discount);
            phrd_created_date = itemView.findViewById(R.id.phrd_created_date);

        }
    }
}
