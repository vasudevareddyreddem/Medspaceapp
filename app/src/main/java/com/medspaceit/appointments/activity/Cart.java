package com.medspaceit.appointments.activity;

import butterknife.BindView;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.medspaceit.appointments.adapters.TestCartAdapter;
import com.medspaceit.appointments.apis.ApiUrl;
import com.medspaceit.appointments.model.CartPackageDetailsPojo;
import com.medspaceit.appointments.model.CartTestDetailsPojo;
import com.medspaceit.appointments.model.CheckCartTypePojo;
import com.medspaceit.appointments.model.RemoveCartDataPojo;
import com.medspaceit.appointments.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class Cart extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.txt_package_name_cart)
    TextView txt_package_name_cart;

    @BindView(R.id.txt_test_no_cart)
    TextView txt_test_no_cart;

    @BindView(R.id.txt_test_names_cart)
    TextView txt_test_names_cart;

    @BindView(R.id.txt_package_amount_cart)
    TextView txt_package_amount_cart;

    @BindView(R.id.txt_package_percentage_cart)
    TextView txt_package_percentage_cart;

    @BindView(R.id.txt_package_discuount_cart)
    TextView txt_package_discuount_cart;

    @BindView(R.id.txt_package_final_amount_cart)
    TextView txt_package_final_amount_cart;


    @BindView(R.id.btn_remove_package)
    Button btn_remove_package;

    @BindView(R.id.test_cart_recycler_view)
    RecyclerView test_cart_recycler_view;

    @BindView(R.id.package_card_details_ll)
    LinearLayout package_card_details_ll;

    public static LinearLayout pack_total_amount_ll;

    public static TextView txt_sample_pickup;
    public static TextView txt_sample_pickup_charge;
    public static TextView final_total;
    public static Button btn_checkout;
    String passAmount;


    public static String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        test_cart_recycler_view.setLayoutManager(llm);
        txt_sample_pickup = findViewById(R.id.txt_sample_pickup);
        txt_sample_pickup_charge = findViewById(R.id.txt_sample_pickup_charge);
        final_total = findViewById(R.id.final_total);
        pack_total_amount_ll = findViewById(R.id.pack_total_amount_ll);
        btn_checkout = findViewById(R.id.btn_checkout);


        if (isConnected()) {
            showDialog();
            getCartData();

        } else {
            showToast(getString(R.string.nointernet));
        }
    }

    private void getCartData() {

        showDialog();
        String json = "";
        JSONObject jsonObject = new JSONObject();
        UID = manager.getSingleField(SessionManager.KEY_ID);
        try {
            jsonObject.put("a_u_id", UID);
        } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;

        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.DIAGONOSTIC_BASE_URL + ApiUrl.cart, new JSONObject(json),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();

                            Gson gson = new Gson();

                            CheckCartTypePojo checkData = gson.fromJson(response.toString(), CheckCartTypePojo.class);
                            if (checkData.status == 1) {
                                //TODO: 1 for test. 0 for package
                                if ((checkData.list.get(0).type).equals("1")) {
                                    CartTestDetailsPojo testdata = gson.fromJson(response.toString(), CartTestDetailsPojo.class);
                                    test_cart_recycler_view.setVisibility(View.VISIBLE);
                                    TestCartAdapter tca = new TestCartAdapter(Cart.this, testdata, UID);
                                    test_cart_recycler_view.setAdapter(tca);


                                } else {
                                    final CartPackageDetailsPojo packagedata = gson.fromJson(response.toString(), CartPackageDetailsPojo.class);
                                    package_card_details_ll.setVisibility(View.VISIBLE);
                                    pack_total_amount_ll.setVisibility(View.VISIBLE);
                                    txt_package_name_cart.setText(packagedata.list.get(0).testPackageName);
                                    ArrayList packList = new ArrayList();
                                    for (int i = 0; i < packagedata.list.get(0).packageTestList.size(); i++) {
                                        packList.add(i, packagedata.list.get(0).packageTestList.get(i).testName);

                                    }
                                    String packTestName = packList.toString();

                                    packTestName = packTestName.replace("[", "");
                                    packTestName = packTestName.replace("]", "");

                                    txt_test_no_cart.setText("Include " + packagedata.list.get(0).packageTestList.size() + " test:");
                                    txt_test_names_cart.setText(packTestName);
                                    txt_package_amount_cart.setText("MRP : ₹" + packagedata.list.get(0).orgAmount);
                                    txt_package_amount_cart.setPaintFlags(txt_package_amount_cart.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                    String str=packagedata.list.get(0).percentage;
                                    String aaa=  str.substring(0, str.length() - 3);
                                    txt_package_percentage_cart.setText(aaa+"% Off");
                                    txt_package_discuount_cart.setText("- ₹" + packagedata.list.get(0).discount);
                                    txt_package_final_amount_cart.setText("Amount: ₹" + packagedata.list.get(0).amount);
                                    txt_sample_pickup.setText("Sample pickup Charges: ");
                                    txt_sample_pickup_charge.setText(packagedata.list.get(0).deliveryCharge);

                                    String amts = packagedata.list.get(0).amount;
                                    double d1 = Double.parseDouble(amts);
                                    String chr = packagedata.list.get(0).deliveryCharge;
                                    double d2 = Double.parseDouble(chr);
                                    double finalamt = d1 + d2;

                                    final_total.setText("₹" + finalamt);
                                    passAmount = String.valueOf(finalamt);
                                    btn_remove_package.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            showDialog();
                                            String json = "";
                                            JSONObject jsonObject = new JSONObject();
                                            String UID = manager.getSingleField(SessionManager.KEY_ID);
                                            try {
                                                jsonObject.put("a_u_id", UID);
                                                jsonObject.put("c_id", packagedata.list.get(0).cId);
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
                                                                hideDialog();

                                                                Gson gson = new Gson();

                                                                RemoveCartDataPojo checkData = gson.fromJson(response.toString(), RemoveCartDataPojo.class);
                                                                if (checkData.status == 1) {
                                                                    showToast(checkData.message);
                                                                    finish();
                                                                } else {
                                                                    showToast(checkData.message);
                                                                }
                                                            }
                                                        }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        hideDialog();

                                                    }
                                                });
                                                RequestQueue queue = Volley.newRequestQueue(Cart.this);
                                                queue.add(jsonObjReq);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });


                                }
                                btn_checkout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(Cart.this, PickTimeSlot.class);
                                        i.putExtra("passAmount", passAmount);
                                        startActivity(i);
                                    }
                                });

                            } else {
                                showToast(checkData.message);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();

                }
            });
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjReq);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;


        }
    }
}
