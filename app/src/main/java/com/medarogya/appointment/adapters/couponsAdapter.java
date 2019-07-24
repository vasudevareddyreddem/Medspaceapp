package com.medarogya.appointment.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medarogya.appointment.R;
import com.medarogya.appointment.activity.NewCouponActivity;
import com.medarogya.appointment.model.couponsPojo;

import java.util.ArrayList;
import java.util.List;

public class couponsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    NewCouponActivity context;
     List<couponsPojo.List> callintlist = new ArrayList<>();
    couponsPojo data;


    public couponsAdapter(NewCouponActivity context, couponsPojo data) {
        this.context = context;
        this.data = data;
        callintlist.addAll(data.list);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_newcoupon, parent, false);
        return new couponsAdapter1(v);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof couponsAdapter1) {
            couponsAdapter1 myholder = (couponsAdapter1) holder;
            myholder.tv_c_name.setText("Name - "+callintlist.get(position).cName);
            myholder.tv_c_amount.setText("Amount - "+callintlist.get(position).cAmount);
            myholder.tv_c_date.setText("Date - "+callintlist.get(position).eDate);
myholder.ll_coupons_sel.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

    }
});
        }
    }

    @Override
    public int getItemCount() {
        return callintlist.size();
    }

    public class couponsAdapter1 extends RecyclerView.ViewHolder {
        TextView tv_c_name,tv_c_amount,tv_c_date;
LinearLayout ll_coupons_sel;

        public couponsAdapter1(View itemView) {
            super(itemView);

            ll_coupons_sel=itemView.findViewById(R.id.ll_coupons_sel);
            tv_c_name=itemView.findViewById(R.id.tv_c_name);
            tv_c_amount=itemView.findViewById(R.id.tv_c_amount);
            tv_c_date=itemView.findViewById(R.id.tv_c_date);


        }
    }
}


