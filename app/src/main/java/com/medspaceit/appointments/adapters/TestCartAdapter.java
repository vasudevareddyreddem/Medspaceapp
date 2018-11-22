package com.medspaceit.appointments.adapters;

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
import com.medspaceit.appointments.R;
import com.medspaceit.appointments.activity.Cart;
import com.medspaceit.appointments.apis.ApiUrl;
import com.medspaceit.appointments.model.CartTestDetailsPojo;
import com.medspaceit.appointments.model.RemoveCartDataPojo;
import com.medspaceit.appointments.utils.SessionManager;

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
    List<CartTestDetailsPojo.List> list = new ArrayList<>();

    public TestCartAdapter(Cart cart, CartTestDetailsPojo data) {
        this.context = cart;
        list.addAll(data.list);
        total = getTotal();

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
            Cart.txt_sample_pickup.setText("Delivery Charge");
            Cart.txt_sample_pickup_charge.setText("₹" + list.get(position).deliveryCharge);
            String delchrg = list.get(position).deliveryCharge;
            double del = Double.parseDouble(delchrg);
            double amount = del + total;
            Cart.final_total.setText("₹" + amount);
            testCartHolder.cart_adapter_test_lab_name.setText("Lab Name: " + list.get(position).labName);
            testCartHolder.cart_test_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    context.showDialog();
                    String json = "";
                    JSONObject jsonObject = new JSONObject();
                    String UID = Cart.UID;
                    try {
                        jsonObject.put("a_u_id", UID);
                        jsonObject.put("c_id", list.get(0).cId);
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
                                Log.e("Info crt 2", " Error " + error.getMessage());

                            }
                        });
                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(jsonObjReq);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("Info crt 3", "Error  try " + e.getMessage());
                    }
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
        Button cart_test_remove, btn_test_checkout;

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

}
