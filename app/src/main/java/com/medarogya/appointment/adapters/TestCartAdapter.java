package com.medarogya.appointment.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.medarogya.appointment.activity.Cart;
import com.medarogya.appointment.activity.PatientDetails;
import com.medarogya.appointment.activity.PickTimeSlot;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.model.CartTestDetailsPojo;
import com.medarogya.appointment.model.RemoveCartDataPojo;
import com.medarogya.appointment.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bhupi on 22-Nov-18.
 */

public class TestCartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Cart context;
    double total = 0.0;
    double totalDeliveryCharge = 0.0;
    List<CartTestDetailsPojo.List> list = new ArrayList<>();
    String UID,passAmount;
    public TestCartAdapter(Cart cart, CartTestDetailsPojo data, String UID) {
        this.context = cart;
        this.UID = UID;
        list.addAll(data.list);
        total = getTotal();
        totalDeliveryCharge = getTotalDeliveryCharge();

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.testcartlayout, parent, false);
        return new MyTestCartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MyTestCartHolder) {
            final MyTestCartHolder testCartHolder = (MyTestCartHolder) holder;

            if (list.size() == 0) {
                Cart.pack_total_amount_ll.setVisibility(View.GONE);
            } else {
                Cart.pack_total_amount_ll.setVisibility(View.VISIBLE);

            }
            testCartHolder.cart_adapter_test_amount.setText("₹" + list.get(position).amount);
            testCartHolder.cart_adapter_test_name.setText("Test Name: " + list.get(position).testName);
            Cart.txt_sample_pickup.setText("Delivery Charge:");
            Cart.txt_sample_pickup_charge.setText("₹" + totalDeliveryCharge);

            double amount = totalDeliveryCharge + total;
            Cart.final_total.setText("₹" + amount);
            passAmount=String.valueOf(amount);
            testCartHolder.cart_adapter_test_lab_name.setText("Lab Name: " + list.get(position).labName);
            testCartHolder.cart_test_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    context.showDialog();
                    String json = "";
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("a_u_id", UID);
                        jsonObject.put("c_id", list.get(position).cId);
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                    json = jsonObject.toString();
                    JsonObjectRequest jsonObjReq = null;

                    try {

                        jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                                ApiUrl.DIAGONOSTIC_BASE_URL + ApiUrl.remove_cartitem, new JSONObject(json),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        context.hideDialog();

                                        Gson gson = new Gson();

                                        RemoveCartDataPojo checkData = gson.fromJson(response.toString(), RemoveCartDataPojo.class);
                                        if (checkData.status == 1) {
                                            context.showToast(checkData.message);
                                            list.remove(position);
                                            notifyDataSetChanged();
                                            total = 0.0;
                                            total = getTotal();
                                            totalDeliveryCharge=getTotalDeliveryCharge();
                                            if (list.size() == 0) {
                                                Cart.pack_total_amount_ll.setVisibility(View.GONE);
                                            } else {
                                                Cart.pack_total_amount_ll.setVisibility(View.VISIBLE);

                                            }
                                        } else {
                                            context.showToast(checkData.message);
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
            });
            Cart.btn_checkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,PickTimeSlot.class);
                    intent.putExtra("passAmount",passAmount);
                    context.startActivity(intent);


                }
            });

        }
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class MyTestCartHolder extends RecyclerView.ViewHolder {
        TextView cart_adapter_test_name, cart_adapter_test_amount, cart_adapter_test_lab_name;
        Button cart_test_remove;

        public MyTestCartHolder(View itemView) {
            super(itemView);
            cart_adapter_test_name = itemView.findViewById(R.id.cart_adapter_test_name);
            cart_adapter_test_amount = itemView.findViewById(R.id.cart_adapter_test_amount);
            cart_adapter_test_lab_name = itemView.findViewById(R.id.cart_adapter_test_lab_name);
            cart_test_remove = itemView.findViewById(R.id.cart_test_remove);

        }
    }

    public double getTotal() {

        double total = 0.0;
        for (int i = 0; i < list.size(); i++) {
            total = total + Double.parseDouble(list.get(i).amount);
        }
        if (list.size() == 0) {
            Cart.pack_total_amount_ll.setVisibility(View.GONE);
        }
        else {
            Cart.pack_total_amount_ll.setVisibility(View.VISIBLE);

        }
        return total;
    }

    public double getTotalDeliveryCharge() {

        double totalDCharge = 0.0;
        for (int i = 0; i < list.size(); i++) {
            totalDCharge = totalDCharge + Double.parseDouble(list.get(i).deliveryCharge);
        }
        if (list.size() == 0) {
            Cart.pack_total_amount_ll.setVisibility(View.GONE);
        }
        else {
            Cart.pack_total_amount_ll.setVisibility(View.VISIBLE);

        }
        return totalDCharge;
    }

}
