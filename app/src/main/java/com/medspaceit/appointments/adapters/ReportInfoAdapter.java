package com.medspaceit.appointments.adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.medspaceit.appointments.R;
import com.medspaceit.appointments.activity.MyDiagnosticReportDownload;
import com.medspaceit.appointments.activity.MyReports;
import com.medspaceit.appointments.apis.ApiUrl;
import com.medspaceit.appointments.model.CancelOrderPojo;
import com.medspaceit.appointments.model.ViewAllMyOrdersPojo;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bhupi on 19-Nov-18.
 */

public class ReportInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    MyReports context;
    String UID;

    List<ViewAllMyOrdersPojo.List> list = new ArrayList<>();

    public ReportInfoAdapter(MyReports context, ViewAllMyOrdersPojo data, String UID) {
        this.context = context;
        this.UID = UID;
        list.addAll(data.list);

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reportinfolayout, parent, false);
        return new MyReportHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MyReportHolder) {
            final MyReportHolder reportholder = (MyReportHolder) holder;
            if (list.get(position).testName.equals("")) {
                reportholder.pac_or_test_name_header.setText("Package Name: ");
                reportholder.f_pack_test_name.setText(list.get(position).testPackageName);
            }
            if (list.get(position).testPackageName.equals("")) {
                reportholder.pac_or_test_name_header.setText("Test Name: ");
                reportholder.f_pack_test_name.setText(list.get(position).testName);
            }
            if (list.get(position).paymentType.equals("1")) {
                reportholder.f_test_payment_type.setText("Online Payment");
            }
            if (list.get(position).paymentType.equals("2")) {
                reportholder.f_test_payment_type.setText("Cash On Delivery");
            }
            reportholder.f_patient_name.setText(list.get(position).pName);
            reportholder.f_patient_mobile.setText(list.get(position).mobile);
            if (list.get(position).testDuartion == null) {
                reportholder.f_test_duration.setText("");
            } else {
                reportholder.f_test_duration.setText(list.get(position).testDuartion);
            }
            reportholder.f_datetime.setText(list.get(position).sample_pickup);
            reportholder.f_test_amount.setText(list.get(position).amount);
            reportholder.f_test_delivery_chrg.setText(list.get(position).deliveryCharge);
            if ((list.get(position).labStatus.equals("0")) && (list.get(position).status.equals("1"))) {
                reportholder.f_labstatus.setText("Pending");
                reportholder.f_status.setText("Canceled");
                reportholder.btn_cancel_report.setVisibility(View.VISIBLE);
            } else {
                reportholder.f_labstatus.setText("Pending");
                reportholder.f_status.setText("Canceled");

            }
            if (list.get(position).status.equals("1")) {
                reportholder.f_status.setText("Success");
            }
            if (list.get(position).labStatus.equals("1")) {
                reportholder.f_labstatus.setText("Accepted");
                reportholder.btn_view_report_for_download.setVisibility(View.VISIBLE);
            }
            if (list.get(position).labStatus.equals("2")) {
                reportholder.f_labstatus.setText("Rejected");
                reportholder.btn_view_report_for_download.setVisibility(View.GONE);
                reportholder.btn_cancel_report.setVisibility(View.GONE);

            }

            reportholder.btn_view_report_for_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(context, MyDiagnosticReportDownload.class);
                    i.putExtra("UID", UID);
                    i.putExtra("order_item_id", list.get(position).orderItemId);
                    context.startActivity(i);


                }
            });

            reportholder.btn_cancel_report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(context);


                    final EditText edittext = new EditText(context);
                    alert.setMessage("Enter Your Reason");

                    alert.setView(edittext);

                    alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //What ever you want to do with the value
                            String reason = edittext.getText().toString();
                            if (reason.equals("")) {
                                context.showToast("Please enter reason");
                            } else {
                                if (context.isConnected()) {
                                    alert.setCancelable(true);
                                    cancelOrder(position, reason, reportholder);

                                } else {
                                    context.showToast(context.getString(R.string.nointernet));
                                }
                            }
                        }
                    });


                    alert.show();


                }
            });


        }
    }

    private void cancelOrder(int position, String reason, final MyReportHolder reportholder) {

        context.showDialog();
        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("a_u_id", UID);
            jsonObject.put("order_item_id", list.get(position).orderItemId);
            jsonObject.put("reason", reason);
        } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;

        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.DIAGONOSTIC_BASE_URL + ApiUrl.cancel_order, new JSONObject(json),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            context.hideDialog();

                            Gson gson = new Gson();

                            CancelOrderPojo cancelorder = gson.fromJson(response.toString(), CancelOrderPojo.class);
                            if (cancelorder.status == 1) {
                                context.showToast(cancelorder.message);
                                reportholder.f_status.setText("Canceled");
                                reportholder.btn_cancel_report.setVisibility(View.GONE);
                                notifyDataSetChanged();
                            } else {
                                context.showToast(cancelorder.message);
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

        return list.size();
    }

    public class MyReportHolder extends RecyclerView.ViewHolder {
        TextView pac_or_test_name_header, f_status, f_labstatus, f_pack_test_name, f_patient_name, f_patient_mobile, f_test_duration, f_datetime, f_test_amount, f_test_delivery_chrg, f_test_payment_type;
        Button btn_view_report_for_download, btn_cancel_report;

        public MyReportHolder(View itemView) {
            super(itemView);
            pac_or_test_name_header = itemView.findViewById(R.id.pac_or_test_name_header);
            f_pack_test_name = itemView.findViewById(R.id.f_pack_test_name);
            f_patient_name = itemView.findViewById(R.id.f_patient_name);
            f_patient_mobile = itemView.findViewById(R.id.f_patient_mobile);
            f_test_duration = itemView.findViewById(R.id.f_test_duration);
            f_datetime = itemView.findViewById(R.id.f_datetime);
            f_test_amount = itemView.findViewById(R.id.f_test_amount);
            f_test_payment_type = itemView.findViewById(R.id.f_test_payment_type);
            f_test_delivery_chrg = itemView.findViewById(R.id.f_test_delivery_chrg);
            f_labstatus = itemView.findViewById(R.id.f_labstatus);
            f_status = itemView.findViewById(R.id.f_status);
            btn_view_report_for_download = itemView.findViewById(R.id.btn_view_report_for_download);
            btn_cancel_report = itemView.findViewById(R.id.btn_cancel_report);


        }
    }

}
