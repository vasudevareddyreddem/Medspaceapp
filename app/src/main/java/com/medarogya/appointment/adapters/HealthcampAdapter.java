package com.medarogya.appointment.adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.medarogya.appointment.R;
import com.medarogya.appointment.activity.HealthNotification;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.model.HealthCampDetails;
import com.medarogya.appointment.model.HealthCampNotification;
import com.medarogya.appointment.model.IPListPojo;
import com.medarogya.appointment.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Bhupi on 05-Feb-19.
 */

public class HealthcampAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    HealthCampNotification data;
    HealthNotification context;
    String UID;
    List<HealthCampNotification.List> notiList=new ArrayList<>();
    public HealthcampAdapter(HealthNotification context, HealthCampNotification data,String UID) {
        this.data=data;
        this.UID=UID;
        this.context=context;
        Collections.reverse(data.list);
        notiList.addAll(data.list);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.noti_layout,parent,false);
        return new IPViewHpolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof IPViewHpolder) {
            IPViewHpolder viewHpolder = (IPViewHpolder) holder;
            viewHpolder.noti_bookingdate.setText(notiList.get(position).bookingDate);
            viewHpolder.noti_city_name.setText(notiList.get(position).cityName);
            viewHpolder.noti_department_name.setText(notiList.get(position).deptName);
            viewHpolder.noti_from_time.setText(notiList.get(position).fromTime);
            viewHpolder.noti_to_time.setText(notiList.get(position).toTime);
            viewHpolder.noti_cardview.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String campid=notiList.get(position).campId;
        if(context.isConnected())
        {
            getAllCampDetails(campid,UID);
        }
        else {
            context.showToast("No Internet Connection");
        }

    }
});
        }



    }

    private void getAllCampDetails(String campid, String UID) {

        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", UID);
            jsonObject.put("camp_id", campid);
        } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;

        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.HEALTHCAMPS_BASE_URL + ApiUrl.get_health_camp_details, new JSONObject(json),
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Gson gson = new Gson();

                            HealthCampDetails noti = gson.fromJson(response.toString(), HealthCampDetails.class);
                            if(noti.status==1)
                            {
                                String allDetails="City :"+noti.campInfo.cityName+"\nHospital :"+noti.campInfo.hosBasName+"\nDepartment :"+noti.campInfo.deptName+"\nBooking Date :"+noti.campInfo.bookingDate+"\nFrom Time :"+noti.campInfo.fromTime+"\nTo Time :"+noti.campInfo.toTime+"\nCreated Date :"+noti.campInfo.createdDate+"\n";
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                                dialogBuilder.setTitle("Details");
                                dialogBuilder.setMessage(allDetails);

                                AlertDialog alertDialogObject = dialogBuilder.create();
                                alertDialogObject.show();

                            }
                            else {

                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                }
            });
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(jsonObjReq);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return notiList.size();
    }

    private class IPViewHpolder extends RecyclerView.ViewHolder {
CardView noti_cardview;
        TextView noti_city_name,noti_department_name,noti_bookingdate,noti_from_time,noti_to_time;
        public IPViewHpolder(View view) {
            super(view);
            noti_city_name=view.findViewById(R.id.noti_city_name);
            noti_department_name=view.findViewById(R.id.noti_department_name);
            noti_bookingdate=view.findViewById(R.id.noti_bookingdate);
            noti_from_time=view.findViewById(R.id.noti_from_time);
            noti_to_time=view.findViewById(R.id.noti_to_time);
            noti_cardview=view.findViewById(R.id.noti_cardview);

        }}
    }
