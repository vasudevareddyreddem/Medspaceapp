package com.medarogya.appointment.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medarogya.appointment.R;
import com.medarogya.appointment.activity.IPActivity;
import com.medarogya.appointment.model.IPListPojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Bhupi on 28-Dec-18.
 */

public class IPListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    IPListPojo data;
    IPActivity context;
    String UID;
    List<IPListPojo.List> Iplist=new ArrayList<>();
    public IPListAdapter(IPActivity context, IPListPojo data, String UID) {
        this.data=data;
        this.UID=UID;
        this.context=context;
        Collections.reverse(data.list);
        Iplist.addAll(data.list);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.ip_layout,parent,false);
        return new IPViewHpolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof IPViewHpolder) {
            IPViewHpolder viewHpolder = (IPViewHpolder) holder;
            viewHpolder.ip_coupon_code.setText(Iplist.get(position).couponcodeName);
            viewHpolder.ip_city_name.setText(Iplist.get(position).hosBasCity);
            viewHpolder.ip_hos_name.setText(Iplist.get(position).hosBasName);
            viewHpolder.ip_coupon_id.setText(Iplist.get(position).cCLId);
            viewHpolder.ip_datetime.setText(Iplist.get(position).createdAt);

        }



    }

@Override
public int getItemCount() {
        return Iplist.size();
        }

private class IPViewHpolder extends RecyclerView.ViewHolder {

    TextView ip_city_name,ip_hos_name,ip_coupon_code,ip_coupon_id,ip_datetime;
    public IPViewHpolder(View view) {
        super(view);
        ip_city_name=view.findViewById(R.id.ip_city_name);
        ip_hos_name=view.findViewById(R.id.ip_hos_name);
        ip_coupon_code=view.findViewById(R.id.ip_coupon_code);
        ip_coupon_id=view.findViewById(R.id.ip_coupon_id);
        ip_datetime=view.findViewById(R.id.ip_datetime);

    }
}
}