package com.medarogya.appointment.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.medarogya.appointment.R;

import com.medarogya.appointment.activity.PickUpAddress;
import com.medarogya.appointment.activity.ReviewTests;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.model.BillingAddressListPojo;
import com.medarogya.appointment.model.PatientBillingAddressPojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Bhupi on 14-Nov-18.
 */

public class BillingAdderssAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    PickUpAddress context;
    String UID, patient_details_id, passAmount, pname, pnumber, pemail;

    List<BillingAddressListPojo.List> addressList = new ArrayList<>();

    public BillingAdderssAdapter(PickUpAddress context, BillingAddressListPojo data, String UID, String patient_details_id, String passAmount,
                                 String pname, String pnumber, String pemail) {
        this.context = context;
        this.UID = UID;
        this.patient_details_id = patient_details_id;
        this.passAmount = passAmount;
        this.pname = pname;
        this.pnumber = pnumber;
        this.pemail = pemail;

        Collections.reverse(data.list);

        addressList.addAll(data.list);
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alllablistlayout, parent, false);
        return new AllLabHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof AllLabHolder) {
            AllLabHolder acceptholder = (AllLabHolder) holder;

            acceptholder.pt_label_frm_list.setText(addressList.get(position).addressLable);
            acceptholder.pt_address_frm_list.setText(addressList.get(position).address);
            acceptholder.pt_landmark_frm_list.setText(addressList.get(position).landmark);
            acceptholder.pt_city_frm_list.setText(addressList.get(position).locality);
            acceptholder.pt_pincode_frm_list.setText(addressList.get(position).pincode);
            acceptholder.btn_select_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (context.isConnected())
                        sendPatientBillingAddressFrmList(position);
                    else
                        context.showToast(context.getString(R.string.nointernet));

                }
            });

        }
    }

    private void sendPatientBillingAddressFrmList(int position) {

        context.showDialog();
        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("a_u_id", UID);
            jsonObject.put("locality", addressList.get(position).locality);
            jsonObject.put("address", addressList.get(position).address);
            jsonObject.put("pincode", addressList.get(position).pincode);
            jsonObject.put("landmark", addressList.get(position).landmark);
            jsonObject.put("address_lable", addressList.get(position).addressLable);
        } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;

        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.DIAGONOSTIC_BASE_URL + ApiUrl.billing_address, new JSONObject(json),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            context.hideDialog();

                            Gson gson = new Gson();

                            PatientBillingAddressPojo billingData = gson.fromJson(response.toString(), PatientBillingAddressPojo.class);
                            if (billingData.status == 1) {


                                Intent i = new Intent(context, ReviewTests.class);
                                i.putExtra("patient_details_id", patient_details_id);
                                i.putExtra("billing_id", billingData.billingId.toString());
                                i.putExtra("passAmount", passAmount);
                                i.putExtra("pemail", pemail);
                                i.putExtra("pname", pname);
                                i.putExtra("pnumber", pnumber);
                                context.startActivity(i);
                                context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


                            } else {
                                context.showToast(billingData.message);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    context.hideDialog();

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
        return addressList.size();
    }

    public class AllLabHolder extends RecyclerView.ViewHolder {
        TextView pt_pincode_frm_list, pt_label_frm_list, pt_address_frm_list, pt_landmark_frm_list, pt_city_frm_list;
        Button btn_select_address;

        public AllLabHolder(View itemView) {
            super(itemView);
            pt_label_frm_list = itemView.findViewById(R.id.pt_label_frm_list);
            pt_address_frm_list = itemView.findViewById(R.id.pt_address_frm_list);
            pt_landmark_frm_list = itemView.findViewById(R.id.pt_landmark_frm_list);
            pt_city_frm_list = itemView.findViewById(R.id.pt_city_frm_list);
            pt_pincode_frm_list = itemView.findViewById(R.id.pt_pincode_frm_list);
            btn_select_address = itemView.findViewById(R.id.btn_select_address);


        }
    }
}
