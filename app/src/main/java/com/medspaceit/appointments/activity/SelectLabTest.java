package com.medspaceit.appointments.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.medspaceit.appointments.R;
import com.medspaceit.appointments.apis.ApiUrl;
import com.medspaceit.appointments.model.CitiesList;
import com.medspaceit.appointments.model.DGCitiesList;
import com.medspaceit.appointments.model.Formatter;
import com.medspaceit.appointments.model.SearchRelatedTest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectLabTest extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.searchimage)
    ImageView searchimage;

    @BindView(R.id.city_text)
    TextView city_text;

    @BindView(R.id.et_search_test)
    EditText et_search_test;

    @BindView(R.id.searchLAbsRecyclerView)
    RecyclerView searchLAbsRecyclerView;


    List<DGCitiesList> cities = null;
    String city = "null";
    String searchtest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_lab_test);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        searchimage.setOnClickListener(this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        searchLAbsRecyclerView.setLayoutManager(llm);
        if (isConnected()) {
            FeatchCity();
        } else {
            showToast(getString(R.string.nointernet));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.searchimage:
                searchtest = et_search_test.getText().toString().trim();
                city = city_text.getText().toString();
                if (city.equals("Select City")) {
                    city = "";
                }
                if (city.equals("") && searchtest.equals("")) {
                    showToast("Please Select City/Test");
                } else if (!searchtest.equals("") && city.equals("")) {
                    showToast("Please Select City");

                } else {
                    hideSoftKeyboard();
                    if (isConnected()) {
                        getRelatedLabs(city, searchtest);
                    } else {
                        showToast(getString(R.string.nointernet));
                    }
                }
                break;
        }
    }

    private void getRelatedLabs(String city, String searchtest) {
        showDialog();
        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("city", city);
            jsonObject.put("search_value", searchtest);

        } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;

        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.DIAGONOSTIC_BASE_URL + ApiUrl.search, new JSONObject(json),
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Info===== ", " Respone" + response.toString());
                            Gson gson = new Gson();
                            hideDialog();

                            SearchRelatedTest data = gson.fromJson(response.toString(), SearchRelatedTest.class);
                            if (data.status == 1) {
                                searchLAbsRecyclerView.setVisibility(View.VISIBLE);

                                SearchRelatedInfoAdapter sriAdapter = new SearchRelatedInfoAdapter(SelectLabTest.this, data);
                                searchLAbsRecyclerView.setAdapter(sriAdapter);
                            } else {
                                showToast(data.message);
                                searchLAbsRecyclerView.setVisibility(View.GONE);
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();
                    Log.e("Info", " Error " + error.getMessage());
                }
            });
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjReq);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Info ", "Error  try " + e.getMessage());
        }
    }

    public void openSingleChoice(View view) {

        if (cities != null) {
            final String[] items = getStringArray(cities);
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
            View view1 = getLayoutInflater().inflate(R.layout.spinner_item, null);
            TextView mTitle = view1.findViewById(R.id.txt_item);
            mTitle.setText("Select City");
            builder.setCustomTitle(view1)
                    .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            fetchData(0, which);


                        }
                    });
            builder.create().show();
        }
    }

    private void fetchData(int data_type, int position) {
        switch (data_type) {
            case 0:
                city = cities.get(position).getValue();
                city_text.setText(city);

                break;
        }
    }

    private String[] getStringArray(List formatters) {
        String[] strings = new String[formatters.size()];
        for (int i = 0; i < formatters.size(); i++) {
            strings[i] = ((Formatter) formatters.get(i)).getValue();
        }
        return strings;
    }

    private void FeatchCity() {
        Call<CitiesList> call = servicedg.getCities(new JsonObject(), ApiUrl.content_type);
        showDialog();
        call.enqueue(new Callback<CitiesList>() {
            @Override
            public void onResponse(Call<CitiesList> call, Response<CitiesList> response) {
                hideDialog();

                if (!response.isSuccessful()) {
                    showToast("Server side error");
                    return;
                }


                CitiesList cityList = response.body();

                if (cityList != null) {


                    if (cityList.getStatus() == 1) {
                        cities = cityList.getCitys();
                    } else
                        showToast(cityList.getMessage());
                }


            }

            @Override
            public void onFailure(Call<CitiesList> call, Throwable t) {
                hideDialog();
            }
        });
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
