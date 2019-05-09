package com.medarogya.appointment.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.medarogya.appointment.R;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.model.Formatter;
import com.medarogya.appointment.model.HealthCity;
import com.medarogya.appointment.model.HealthDate;
import com.medarogya.appointment.model.HealthDepartment;
import com.medarogya.appointment.model.HealthGetCityPojo;
import com.medarogya.appointment.model.HealthGetDatePojo;
import com.medarogya.appointment.model.HealthGetDepartPojo;
import com.medarogya.appointment.model.HealthGetHosPojo;
import com.medarogya.appointment.model.HealthGetTimePojo;
import com.medarogya.appointment.model.HealthHospital;
import com.medarogya.appointment.model.HealthTime;
import com.medarogya.appointment.model.RequestdataPojo;
import com.medarogya.appointment.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HealthCampActivity extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.hc_city_ll)
    LinearLayout hc_city_ll;
    @BindView(R.id.hc_city_txt)
    TextView hc_city_txt;

    @BindView(R.id.hc_hos_ll)
    LinearLayout hc_hos_ll;
    @BindView(R.id.hc_hos_txt)
    TextView hc_hos_txt;

    @BindView(R.id.hc_dep_ll)
    LinearLayout hc_dep_ll;
    @BindView(R.id.hc_dep_txt)
    TextView hc_dep_txt;

    @BindView(R.id.hc_date_ll)
    LinearLayout hc_date_ll;
    @BindView(R.id.hc_date)
    TextView hc_date_txt;

    @BindView(R.id.hc_time_lay)
    LinearLayout hc_time_lay;
    @BindView(R.id.hc_time_txt)
    TextView hc_time_txt;

    @BindView(R.id.hc_molibe_nmbr)
    EditText hc_molibe_nmbr;
    @BindView(R.id.hc_user_age)
    EditText hc_user_age;
    @BindView(R.id.hc_user_name)
    EditText hc_user_name;
    @BindView(R.id.btn_hc_submit)
    Button btn_hc_submit;
    String mobile, username, age, UID;

    List<HealthHospital> hospitalList = null;
    List<HealthCity> citylist = null;
    List<HealthDepartment> departmentlist = null;
    List<HealthDate> datelist = null;
    List<HealthTime> timelist = null;

    String city, hospital, hospitalid, department, date, time,campId;
    int precitypos = -1;
    int prehospos = -1;
    int predepartpos = -1;
    int predatepos = -1;
    int pretimepos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_camp);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        btn_hc_submit.setOnClickListener(this);
        UID = manager.getSingleField(SessionManager.KEY_ID);
        if (isConnected()) {
            getCitys();
            showDialog();
            hideSoftKeyboard();
        } else {
            showToast("No Internet");

        }
    }

    private void getCitys() {

        String json = "";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", UID);

        } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;

        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.HEALTHCAMPS_BASE_URL + ApiUrl.get_hos_city, new JSONObject(json),
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();
                            Gson gson = new Gson();
                            HealthGetCityPojo data = gson.fromJson(String.valueOf(response), HealthGetCityPojo.class);
                            if (data.status == 1) {
                                HealthGetCityPojo healthGetCityPojo = data;
                                citylist = healthGetCityPojo.getCities();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();

                }
            });
            RequestQueue queue = Volley.newRequestQueue(HealthCampActivity.this);
            queue.add(jsonObjReq);
        } catch (JSONException e) {
            e.printStackTrace();
            hideDialog();

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_hc_submit:

                mobile = hc_molibe_nmbr.getText().toString();
                username = hc_user_name.getText().toString();
                age = hc_user_age.getText().toString();
//              int values[]={1,0,8,5,3};
//              for(int i=0;i<values.length;i++)
                if (city == null || city.isEmpty()) {
                    showToast("Please Select city");
                    return;
                }
                if (hospital == null || hospital.isEmpty()) {
                    showToast("Please Select Hospital");
                    return;
                }
                if (department == null || department.isEmpty()) {
                    showToast("Please Select Department");
                    return;
                }
                if (date == null || date.isEmpty()) {
                    showToast("Please Select Date");
                    return;
                }
                if (time == null || time.isEmpty()) {
                    showToast("Please Select Time");
                    return;
                }

                if (username == null || username.isEmpty()) {
                    showToast("Please Enter Name");
                    return;
                }
                if (age == null || age.isEmpty()) {
                    showToast("Please Enter Age");
                    return;
                }
                if (mobile == null || mobile.isEmpty()) {
                    showToast("Please Enter Mobile No.");
                    return;
                }
                if (mobile.length() != 10) {
                    showToast("Please Enter valid Mobile No.");
                    return;
                }
                if (isConnected()) {
                    showDialog();
                    sendAllDataInServer(username, age, mobile);
                }
                else
                    showToast("No Internet Connection");
                break;
        }
    }

    private void sendAllDataInServer(String username, String age, String mobile) {
        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", UID);
            jsonObject.put("name", username);
            jsonObject.put("age", age);
            jsonObject.put("mobile", mobile);
            jsonObject.put("camp_id",campId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        json = jsonObject.toString();
        JsonObjectRequest jsonObjectRequest = null;

        try {
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ApiUrl.HEALTHCAMPS_BASE_URL + ApiUrl.user_select_health_camp, new JSONObject(json), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Gson gson = new Gson();
                    hideDialog();
                    RequestdataPojo data = gson.fromJson(String.valueOf(response), RequestdataPojo.class);

                    showToast(data.message);
                    finish();


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }

    int data_type = 0;

    public void openSingleChoice(View view) {

        List list = null;
        String title = null;
        switch (view.getId()) {
            case R.id.hc_city_ll:
                list = citylist;
                title = "Select City";
                data_type = 1;
                break;
            case R.id.hc_hos_ll:
                title = "Select Hospital";
                list = hospitalList;
                data_type = 2;
                break;
            case R.id.hc_dep_ll:
                title = "Select Department";
                list = departmentlist;
                data_type = 3;
                break;
            case R.id.hc_date_ll:
                title = "Select Date";
                list = datelist;
                data_type = 4;
                break;
            case R.id.hc_time_lay:
                title = "Select Time";
                list = timelist;
                data_type = 5;
                break;
        }

        if (list != null) {
            final String[] items = getStringArray(list);
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
            View view1 = getLayoutInflater().inflate(R.layout.spinner_item, null);
            TextView mTitle = view1.findViewById(R.id.txt_item);
            mTitle.setText(title);
            switch (data_type) {
                case 1:
                    builder.setCustomTitle(view1)
                            .setSingleChoiceItems(items, precitypos, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    fetchData(data_type, which);
                                    data_type = 0;


                                }
                            });
                    break;
                case 2:
                    builder.setCustomTitle(view1)
                            .setSingleChoiceItems(items, prehospos, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    fetchData(data_type, which);
                                    data_type = 0;


                                }
                            });
                    break;
                case 3:
                    builder.setCustomTitle(view1)
                            .setSingleChoiceItems(items, predepartpos, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    fetchData(data_type, which);
                                    data_type = 0;


                                }
                            });
                    break;
                case 4:
                    builder.setCustomTitle(view1)
                            .setSingleChoiceItems(items, predatepos, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    fetchData(data_type, which);
                                    data_type = 0;


                                }
                            });
                    break;
                case 5:
                    builder.setCustomTitle(view1)
                            .setSingleChoiceItems(items, pretimepos, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    fetchData(data_type, which);
                                    data_type = 0;


                                }
                            });
                    break;
            }
            builder.create().show();
        }
    }

    private String[] getStringArray(List formatters) {
        String[] strings = new String[formatters.size()];
        for (int i = 0; i < formatters.size(); i++) {
            strings[i] = ((Formatter) formatters.get(i)).getValue();
        }
        return strings;
    }


    private void fetchData(int data_type, int position) {
        switch (data_type) {
            case 1:
                city = citylist.get(position).getHosBasCity();
                hc_city_txt.setText(city);
                precitypos = position;
                prehospos = -1;
                predepartpos = -1;
                predatepos = -1;
                pretimepos = -1;
                showDialog();
                FetchHosList(city);
                hospitalList = null;
                departmentlist = null;
                datelist = null;
                timelist = null;
                hospital = "";
                department = "";
                date = "";
                time = "";
                hc_hos_txt.setText("Select Hospital");
                hc_dep_txt.setText("Select Department");
                hc_date_txt.setText("Select Date");
                hc_time_txt.setText("Select Time");
                break;
            case 2:
                hospital = hospitalList.get(position).getValue();
                hospitalid = hospitalList.get(position).getHosId();
                showDialog();
                FetchDepList(hospitalid);
                predepartpos = -1;
                predatepos = -1;
                pretimepos = -1;

                hc_hos_txt.setText(hospital);
                prehospos = position;
                departmentlist = null;
                datelist = null;
                timelist = null;
                department = "";
                date = "";
                time = "";
                hc_dep_txt.setText("Select Department");
                hc_date_txt.setText("Select Date");
                hc_time_txt.setText("Select Time");


                break;
            case 3:
                department = departmentlist.get(position).getValue();
                hc_dep_txt.setText(department);
                predepartpos = position;
                showDialog();
                FetchDateList(hospitalid, department);
                date = "";
                time = "";
                predatepos = -1;
                pretimepos = -1;
                datelist = null;
                timelist = null;

                hc_date_txt.setText("Select Date");
                hc_time_txt.setText("Select Time");

                break;

            case 4:
                date = datelist.get(position).getValue();
                hc_date_txt.setText(date);
                predatepos = position;
                hc_time_txt.setText("Select Time");
                timelist = null;
                time = "";
                pretimepos = -1;
                showDialog();
                FetchTimeList(hospitalid, department, date);

                break;

            case 5:
                time = timelist.get(position).getValue();
                campId = timelist.get(position).getCampId();
                hc_time_txt.setText(time);
                pretimepos = position;
                break;
        }
    }

    private void FetchTimeList(String hospitalid, String department, String date) {
        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", UID);
            jsonObject.put("hos_id", hospitalid);
            jsonObject.put("dept_name", department);
            jsonObject.put("cdate", date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        json = jsonObject.toString();
        JsonObjectRequest jsonObjectRequest = null;

        try {
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ApiUrl.HEALTHCAMPS_BASE_URL + ApiUrl.get_camp_times, new JSONObject(json), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Gson gson = new Gson();
                    hideDialog();
                    HealthGetTimePojo data = gson.fromJson(String.valueOf(response), HealthGetTimePojo.class);
                    if (data.status == 1) {

                        HealthGetTimePojo blockRespone = data;
                        timelist = blockRespone.getHealthTime();
                    } else {
                        showToast("No Time Availables");
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            hideDialog();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }

    private void FetchDateList(String hospitalid, String department) {
        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", UID);
            jsonObject.put("hos_id", hospitalid);
            jsonObject.put("dept_name", department);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        json = jsonObject.toString();
        JsonObjectRequest jsonObjectRequest = null;

        try {
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ApiUrl.HEALTHCAMPS_BASE_URL + ApiUrl.get_camp_date, new JSONObject(json), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Gson gson = new Gson();
                    hideDialog();
                    HealthGetDatePojo data = gson.fromJson(String.valueOf(response), HealthGetDatePojo.class);
                    if (data.status == 1) {

                        HealthGetDatePojo blockRespone = data;
                        datelist = blockRespone.getHealthDate();
                    } else {
                        showToast("No Dates Availables");
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            hideDialog();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }

    private void FetchDepList(String hospitalid) {
        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", UID);
            jsonObject.put("hos_id", hospitalid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        json = jsonObject.toString();
        JsonObjectRequest jsonObjectRequest = null;

        try {
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ApiUrl.HEALTHCAMPS_BASE_URL + ApiUrl.get_departments, new JSONObject(json), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Gson gson = new Gson();
                    hideDialog();
                    HealthGetDepartPojo data = gson.fromJson(String.valueOf(response), HealthGetDepartPojo.class);
                    if (data.status == 1) {

                        HealthGetDepartPojo blockRespone = data;
                        departmentlist = blockRespone.getDeptList();
                    } else {
                        showToast("No Department Availables");
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            hideDialog();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }

    private void FetchHosList(String city) {
        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", UID);
            jsonObject.put("city", city);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        json = jsonObject.toString();
        JsonObjectRequest jsonObjectRequest = null;

        try {
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ApiUrl.HEALTHCAMPS_BASE_URL + ApiUrl.get_hospitals, new JSONObject(json), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Gson gson = new Gson();
                    hideDialog();
                    HealthGetHosPojo data = gson.fromJson(String.valueOf(response), HealthGetHosPojo.class);
                    if (data.status == 1) {

                        HealthGetHosPojo blockRespone = data;
                        hospitalList = blockRespone.getHealthHospital();
                    } else {
                        showToast("No Hospital Availables");
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            hideDialog();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
