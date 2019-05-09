package com.medarogya.appointment.adapters;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.medarogya.appointment.R;
import com.medarogya.appointment.activity.OPActivity;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.model.GenerateCouponPojo;
import com.medarogya.appointment.model.OPListPojo;
import com.medarogya.appointment.model.ViewCouponPojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by Bhupi on 27-Dec-18.
 */

public class OPListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    OPListPojo data;
    OPActivity context;
    String UID;
    List<OPListPojo.List> oplist = new ArrayList<>();

    public OPListAdapter(OPActivity context, OPListPojo data, String UID) {
        this.data = data;
        this.UID = UID;
        this.context = context;
        Collections.reverse(data.list);
        oplist.addAll(data.list);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.op_layout, parent, false);
        return new OPViewHpolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OPViewHpolder) {
            OPViewHpolder viewHpolder = (OPViewHpolder) holder;
            viewHpolder.op_hospital_name.setText(oplist.get(position).hosBasName);
            viewHpolder.op_patient_name.setText(oplist.get(position).patinetName);
            viewHpolder.op_mobile_no.setText(oplist.get(position).mobile);
            viewHpolder.op_city.setText(oplist.get(position).city);
            viewHpolder.op_doct_name.setText(oplist.get(position).doctorname);
            viewHpolder.op_date_time.setText(oplist.get(position).date+" "+oplist.get(position).time);
            viewHpolder.op_dept_name.setText(oplist.get(position).department);

            if (oplist.get(position).consultationFee == null) {
                viewHpolder.op_appointment_fee.setText("nill");
            } else {
                viewHpolder.op_appointment_fee.setText(String.valueOf(oplist.get(position).consultationFee));

            }
            viewHpolder.tv_generate_coupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (context.isConnected()) {
                        context.showDialog();
                        String json = "";
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("a_u_id", UID);
                            jsonObject.put("b_id", oplist.get(position).bId);
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                        json = jsonObject.toString();
                        JsonObjectRequest jsonObjReq = null;

                        try {

                            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                                    ApiUrl.WalletBaseUrl + ApiUrl.generateopcoupon, new JSONObject(json),
                                    new com.android.volley.Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            context.hideDialog();
                                            Gson gson = new Gson();
                                            GenerateCouponPojo data = gson.fromJson(String.valueOf(response), GenerateCouponPojo.class);

                                            context.showToast(data.message);


                                        }
                                    }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    context.hideDialog();
                                }
                            });
                            RequestQueue queue = Volley.newRequestQueue(context);
                            queue.add(jsonObjReq);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            context.hideDialog();
                        }

                    } else {
                        context.showToast("No Internet");
                    }

                }
            });
            viewHpolder.tv_view_coupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (context.isConnected()) {
                        context.showDialog();
                        String json = "";
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("b_id", oplist.get(position).bId);
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                        json = jsonObject.toString();
                        JsonObjectRequest jsonObjReq = null;

                        try {

                            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                                    ApiUrl.WalletBaseUrl + ApiUrl.viewcoupon_code, new JSONObject(json),
                                    new com.android.volley.Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            context.hideDialog();
                                            Gson gson = new Gson();
                                            ViewCouponPojo data = gson.fromJson(String.valueOf(response), ViewCouponPojo.class);


//                                    AlertDialog dialog;
//                                    AlertDialog.Builder builder;
//// The TextView to show your Text
//                                    TextView showText = new TextView(context);
//                                    showText.setText(data.message);
//// Add the Listener
//                                    showText.setOnLongClickListener(new View.OnLongClickListener() {
//
//                                        @Override
//                                        public boolean onLongClick(View v) {
//                                            // Copy the Text to the clipboard
//                                            ClipboardManager manager =
//                                                    (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
//                                            TextView showTextParam = (TextView) v;
//                                            manager.setText( showTextParam.getText() );
//                                            // Show a message:
//                                            Toast.makeText(v.getContext(), "Text in clipboard",
//                                                    Toast.LENGTH_SHORT)
//                                                    .show();
//                                            return true;
//                                        }
//                                    });
//// Build the Dialog
//                                    builder = new AlertDialog.Builder(context);
//                                    builder.setView(showText);
//                                    dialog = builder.create();
//// Some eye-candy
//                                    dialog.setCancelable(true);
//                                    dialog.show();
                                            if (data.status == 0) {
                                                context.showToast(data.message);
                                            } else {
                                                final AlertDialog alertDialog = new AlertDialog.Builder(context).create(); //Read Update
                                                alertDialog.setTitle(data.couponcodeName.trim());
                                                alertDialog.setMessage(data.message.trim());
                                                alertDialog.setCancelable(false);

                                                alertDialog.setButton("Ok Thanks..", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        alertDialog.dismiss();
                                                    }
                                                });

                                                alertDialog.show();
                                            }


                                        }
                                    }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    context.hideDialog();
                                }
                            });
                            RequestQueue queue = Volley.newRequestQueue(context);
                            queue.add(jsonObjReq);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            context.hideDialog();
                        }

                    } else {
                        context.showToast("No Internet");
                    }


                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return oplist.size();
    }

    private class OPViewHpolder extends RecyclerView.ViewHolder {

        TextView op_hospital_name, op_patient_name, op_mobile_no, op_city, op_doct_name, op_date_time, op_dept_name, op_appointment_fee, tv_generate_coupon, tv_view_coupon;

        public OPViewHpolder(View view) {
            super(view);
            op_hospital_name = view.findViewById(R.id.op_hospital_name);
            op_patient_name = view.findViewById(R.id.op_patient_name);
            op_mobile_no = view.findViewById(R.id.op_mobile_no);
            op_city = view.findViewById(R.id.op_city);
            op_doct_name = view.findViewById(R.id.op_doct_name);
            op_date_time = view.findViewById(R.id.op_date_time);
            op_dept_name = view.findViewById(R.id.op_dept_name);
            op_appointment_fee = view.findViewById(R.id.op_appointment_fee);
            tv_generate_coupon = view.findViewById(R.id.tv_generate_coupon);
            tv_view_coupon = view.findViewById(R.id.tv_view_coupon);
        }
    }
}
