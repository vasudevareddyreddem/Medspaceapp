package com.medarogya.appointment.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.medarogya.appointment.R;
import com.medarogya.appointment.activity.PharmacyMyOrders;
import com.medarogya.appointment.activity.PharmacyOrderDetails;
import com.medarogya.appointment.activity.TrackOrder;
import com.medarogya.appointment.model.PharmacyMyReportPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bhupi on 19-Mar-19.
 */

public class PharmacyMyReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    PharmacyMyReportPojo data;
    PharmacyMyOrders context;
    String track;
    List<PharmacyMyReportPojo.Detail> list = new ArrayList<>();

    public PharmacyMyReportAdapter(PharmacyMyOrders context, PharmacyMyReportPojo data, String track) {
        this.context = context;
        this.data = data;
        this.track = track;
        list.addAll(data.details);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.pharmacymyreportlayout, parent, false);
        return new MyPAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyPAdapter) {
            final MyPAdapter myholder = (MyPAdapter) holder;
           if(track.equals("1"))
           {
               myholder.btn_trackorder.setVisibility(View.VISIBLE);
               myholder.btn_detailsorder.setVisibility(View.GONE);
           }
           else {
               myholder.btn_trackorder.setVisibility(View.GONE);
               myholder.btn_detailsorder.setVisibility(View.VISIBLE);
               myholder.btn_detailsorder.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent i=new Intent(context,PharmacyOrderDetails.class);
                       i.putExtra("phar_id",list.get(position).id);
                       context.startActivity(i);;


                   }
               });
           }


            myholder.btn_trackorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(context,TrackOrder.class);
                    i.putExtra("phar_id",list.get(position).id);
                   context. startActivity(i);
                }
            });



            myholder.tv_pmr.setText("Address : \n" + list.get(position).address);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.labplaceholder);

            Glide.with(context)
                    .setDefaultRequestOptions(requestOptions)
                    .load(data.path + list.get(position).medImg)
                    .into(myholder.img_pmr);

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyPAdapter extends RecyclerView.ViewHolder {
        TextView tv_pmr;
        ImageView img_pmr;
        Button btn_trackorder,btn_detailsorder;
        LinearLayout ll_pmr;

        public MyPAdapter(View itemView) {
            super(itemView);
            img_pmr = itemView.findViewById(R.id.img_pmr);
            tv_pmr = itemView.findViewById(R.id.tv_pmr);
            ll_pmr = itemView.findViewById(R.id.ll_pmr);
            btn_trackorder = itemView.findViewById(R.id.btn_trackorder);
            btn_detailsorder = itemView.findViewById(R.id.btn_detailsorder);
        }
    }
}
