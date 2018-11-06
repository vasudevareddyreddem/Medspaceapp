package com.medspaceit.appointments.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medspaceit.appointments.R;
import com.medspaceit.appointments.activity.StatusActivity;
import com.medspaceit.appointments.model.AppStatus;

import java.util.ArrayList;
import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final static int date_type=12;
    final static int status_type=15;
     int adapterType;
    List<AppStatus> list=new ArrayList<AppStatus>();
    StatusAction action=new StatusAction() {
        @Override
        public void accept(AppStatus status) {

        }

        @Override
        public void reject(AppStatus status) {

        }
    };

    public void setAction(StatusAction action) {
        this.action = action;
    }

    public StatusAdapter(List<AppStatus> list, int adaterType) {
/*
     for (AppStatus status:list)
        status.setStatus("1");*/
        this.list = list;
        this.adapterType=adaterType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =null;
        if(viewType==date_type)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_card, parent, false);
            return new DateHolder(view);
        }
        else
        {
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.status_card, parent, false);
            return new StatusHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final AppStatus status=list.get(position);
        if(holder instanceof DateHolder){
             ((DateHolder) holder).date.setText(status.getDate());
             }else if (holder instanceof StatusHolder){
               StatusHolder statusHolder= (StatusHolder) holder;
            statusHolder.hos_name.setText(status.getHosName());
            statusHolder.spl_name.setText(status.getSpecialistName());
            statusHolder.dept_name.setText(status.getDepartment());
            statusHolder.doct_name.setText(status.getDoctor_name());
            statusHolder.time_slot.setText(status.getTime());
            statusHolder.chname.setText(status.getPatinetName());

            setStatus(status.getStatus(),statusHolder.status_txt);
            if(adapterType== StatusActivity.stausListApi){
            if(Integer.valueOf(status.getStatus())==1){
                //TODO: if u want accept and reject button visible change Gone to visible
                statusHolder.action_layout.setVisibility(View.GONE);
                statusHolder.coupon_layout.setVisibility(View.VISIBLE);
                statusHolder.tv_coupon.setText(status.getCouponCode());



//                statusHolder.accept.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        list.remove(position);
//                        notifyDataSetChanged();
//                        action.accept(status);
//                    }
//                });
//                statusHolder.reject.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        list.get(position).setStatus("2");
//                        notifyDataSetChanged();
//                        action.reject(status);
//                    }
//                });
            }else {
                statusHolder.action_layout.setVisibility(View.GONE);
            }
            }else if(adapterType== StatusActivity.historyListApi){
              if(status.getCouponCode()!=null&&!status.getCouponCode().isEmpty()){
                  statusHolder.coupn_lay.setVisibility(View.VISIBLE);
                  statusHolder.coupnCode.setText(status.getCouponCode());
              }
            }

            statusHolder.add_txt.setText(status.getAddress());
             }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {

            return list.get(position).getView_type();

    }

    public class StatusHolder extends RecyclerView.ViewHolder{
        TextView status_txt;
        TextView hos_name;
        TextView time_slot;
        TextView dept_name,chname;
        TextView spl_name;
        TextView add_txt;
        LinearLayout action_layout;
        LinearLayout coupn_lay;
        TextView accept;
        TextView reject;
        TextView coupnCode;
        TextView doct_name;
        TextView tv_coupon;
        LinearLayout coupon_layout;

        public StatusHolder(View itemView) {
            super(itemView);
            status_txt=itemView.findViewById(R.id.status);
            hos_name=itemView.findViewById(R.id.hospital_name);
            time_slot=itemView.findViewById(R.id.time_slot);
            dept_name=itemView.findViewById(R.id.dept_name);
            spl_name=itemView.findViewById(R.id.spl_name);
            add_txt=itemView.findViewById(R.id.address);
            action_layout =itemView.findViewById(R.id.action_lay);
            coupn_lay=itemView.findViewById(R.id.coupn_lay);
            coupnCode=itemView.findViewById(R.id.coupn_code);
            accept=itemView.findViewById(R.id.accept);
            reject=itemView.findViewById(R.id.reject);
            doct_name=itemView.findViewById(R.id.doct_name);
            tv_coupon=itemView.findViewById(R.id.tv_coupon);
            coupon_layout=itemView.findViewById(R.id.coupon_layout);
            chname=itemView.findViewById(R.id.chname);
        }
    }

    private void setStatus(String status,TextView textView) {
        String color="#fff";
        String text="";

        switch (Integer.valueOf(status)){
            case 0:
                color="#ff9802";
                text="Pending";
                break;
            case 1:
                color="#70d449";
                text="Accept";
                break;
            case 2:
                color="#ff9802";
                text="Reject";
                break;
            case 3:
                color="#70d449";
                text="Approved";
                break;

        }
        textView.setText(text);
        textView.setTextColor(Color.parseColor(color));
    }

    public class DateHolder extends RecyclerView.ViewHolder{
       TextView date;
        public DateHolder(View itemView) {
            super(itemView);
           date= itemView.findViewById(R.id.date);


        }
    }
    public interface  StatusAction{
        public void accept(AppStatus status);
        public void reject(AppStatus status);

    }
}
