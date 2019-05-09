package com.medarogya.appointment.adapters;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.medarogya.appointment.R;
import com.medarogya.appointment.activity.WalletHistory;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.model.GenerateCouponPojo;
import com.medarogya.appointment.model.WalletHistoryPojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Bhupi on 28-Dec-18.
 */

public class WalletHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    WalletHistoryPojo data;
    WalletHistory context;
    String UID;
    List<WalletHistoryPojo.List> walletlist = new ArrayList<>();

    public WalletHistoryAdapter(WalletHistory context, WalletHistoryPojo data, String UID) {
        this.data = data;
        this.UID = UID;
        this.context = context;
        Collections.reverse(data.list);
        walletlist.addAll(data.list);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wallethistory_layout, parent, false);
        return new OPViewHpolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OPViewHpolder) {
            OPViewHpolder viewHpolder = (OPViewHpolder) holder;
            viewHpolder.wallet_hospital_name.setText(walletlist.get(position).hosBasName);
            viewHpolder.wallet_patient_name.setText(walletlist.get(position).name);
            viewHpolder.wallet_mobile_no.setText(walletlist.get(position).mobile);
            viewHpolder.wallet_city.setText(walletlist.get(position).hosBasCity);
            viewHpolder.wallet_doct_name.setText(walletlist.get(position).doctorname);
            viewHpolder.wallet_date.setText(walletlist.get(position).date);
            viewHpolder.wallet_date_time.setText( walletlist.get(position).time);
            viewHpolder.wallet_amount.setText(walletlist.get(position).amount);
            viewHpolder.wallet_tv_coupon.setText(walletlist.get(position).couponCode);
            viewHpolder.wallet_type.setText(walletlist.get(position).type);
            viewHpolder.wallet_tv_couponcode_amount.setText(walletlist.get(position).couponCodeAmount);

            float tamt = Float.parseFloat(walletlist.get(position).amount);
            float camt = Float.parseFloat(walletlist.get(position).couponCodeAmount);

            float finalamt = tamt - camt;
            viewHpolder.wallet_final_amount.setText(finalamt + "");
            if (walletlist.get(position).consultationFee == null) {
                viewHpolder.wallet_appointment_fee.setText("nill");
            } else {
                viewHpolder.wallet_appointment_fee.setText(String.valueOf(walletlist.get(position).consultationFee));

            }
            if(walletlist.get(position).type.equals("Op"))
            {
                viewHpolder.AFee_ll.setVisibility(View.VISIBLE);
                viewHpolder.DC_ll.setVisibility(View.VISIBLE);
                viewHpolder.time_ll.setVisibility(View.VISIBLE);

            }
            else {
                viewHpolder.AFee_ll.setVisibility(View.GONE);
                viewHpolder.DC_ll.setVisibility(View.GONE);
                viewHpolder.time_ll.setVisibility(View.GONE);
            }

        }


    }

    @Override
    public int getItemCount() {
        return walletlist.size();
    }

    private class OPViewHpolder extends RecyclerView.ViewHolder {

        TextView wallet_hospital_name, wallet_patient_name, wallet_mobile_no, wallet_city, wallet_doct_name,
                wallet_date_time, wallet_dept_name, wallet_amount, wallet_appointment_fee, wallet_tv_coupon,
                wallet_type, wallet_tv_couponcode_amount, wallet_final_amount,wallet_date;
LinearLayout DC_ll,AFee_ll,time_ll;
        public OPViewHpolder(View view) {
            super(view);
            wallet_hospital_name = view.findViewById(R.id.wallet_hospital_name);
            wallet_patient_name = view.findViewById(R.id.wallet_patient_name);
            wallet_mobile_no = view.findViewById(R.id.wallet_mobile_no);
            wallet_city = view.findViewById(R.id.wallet_city);
            wallet_doct_name = view.findViewById(R.id.wallet_doct_name);
            wallet_date_time = view.findViewById(R.id.wallet_date_time);
            wallet_dept_name = view.findViewById(R.id.wallet_dept_name);
            wallet_appointment_fee = view.findViewById(R.id.wallet_appointment_fee);
            wallet_tv_coupon = view.findViewById(R.id.wallet_tv_coupon);
            wallet_amount = view.findViewById(R.id.wallet_amount);
            wallet_tv_couponcode_amount = view.findViewById(R.id.wallet_tv_couponcode_amount);
            wallet_final_amount = view.findViewById(R.id.wallet_final_amount);
            wallet_type = view.findViewById(R.id.wallet_type);
            DC_ll = view.findViewById(R.id.DC_ll);
            AFee_ll = view.findViewById(R.id.AFee_ll);
            wallet_date = view.findViewById(R.id.wallet_date);
            time_ll = view.findViewById(R.id.time_ll);
        }
    }
}
