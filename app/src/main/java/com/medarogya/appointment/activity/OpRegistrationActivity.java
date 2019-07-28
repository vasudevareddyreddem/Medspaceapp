package com.medarogya.appointment.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.medarogya.appointment.R;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.model.Appointment;
import com.medarogya.appointment.model.City;
import com.medarogya.appointment.model.CityList;
import com.medarogya.appointment.model.Department;
import com.medarogya.appointment.model.DepartmentList;
import com.medarogya.appointment.model.Doctorlist;
import com.medarogya.appointment.model.Doctorlists;
import com.medarogya.appointment.model.Formatter;
import com.medarogya.appointment.model.Hospital;
import com.medarogya.appointment.model.HospitalList;
import com.medarogya.appointment.model.DoctorConsultFeePojo;
import com.medarogya.appointment.model.NewPatientDetailsPojo;
import com.medarogya.appointment.model.OnlinePaymentPojo;
import com.medarogya.appointment.model.RegResult;
import com.medarogya.appointment.model.Specialist;
import com.medarogya.appointment.model.Specialists;
import com.medarogya.appointment.model.TimeSlot;
import com.medarogya.appointment.model.TimeSlotlists;
import com.medarogya.appointment.utils.SessionManager;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OpRegistrationActivity extends BaseActivity implements PaymentResultListener {
   int b_id;
    @BindView(R.id.date_of_reg)
    EditText dateOfReg;

    @BindView(R.id.age)
    EditText edt_age;

    @BindView(R.id.ac_name)
    AutoCompleteTextView ac_name;
    @BindView(R.id.ac_mobile)
    AutoCompleteTextView ac_mobile;

    @BindView(R.id.cardNum)
    EditText edt_cardNum;


    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.city_txt)
    TextView city_txt;

    @BindView(R.id.txt_consultationfee)
    TextView txt_consultationfee;

    @BindView(R.id.dept_txt)
    TextView dept_txt;

    @BindView(R.id.spl_txt)
    TextView spl_txt;

    @BindView(R.id.hos_txt)
    TextView hos_txt;

    @BindView(R.id.doctor_txt)
    TextView doctor_txt;

    @BindView(R.id.time_txt)
    TextView time_txt;

    @BindView(R.id.check_fee_box)
    CheckBox checkboxfee;


    @BindView(R.id.hos_lay)
    LinearLayout layout;

    @BindView(R.id.btn_add_op)
    Button btn_addOp;

    @BindView(R.id.btn_pay_online)
    Button btn_paynows;
    @BindView(R.id.edt_coupon_enter)
    TextView edt_coupon_enter;
    DoctorConsultFeePojo fee;
    List<City> cities = null;
    List<Department> departments = null;
    List<Specialist> specialists = null;
    List<Doctorlist> doctorlists = null;
    List<Hospital> hospitals = null;
    List<TimeSlot> timeSlots = null;
    String city, dept, spl, doct, time, hospital, hos_id, dept_id, spl_id, doct_id,cp_id, cAmount, cName;
    @BindView(R.id.radio_online)
    RadioButton radio_online;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.ll_radiobutton)
    LinearLayout ll_radiobutton;

    int precitypos = -1,prehospos = -1,predepartpos = -1,prespecpos = -1,predoctpos = -1,pretimepos = -1,amt;
    private int request_code = 100;
    double total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_op_registration);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        btn_addOp.setOnClickListener(this);
        btn_paynows.setOnClickListener(this);
        dateOfReg.setOnClickListener(this);
        radioGroup.setOnClickListener(this);
        ac_name.setOnClickListener(this);
        ac_mobile.setOnClickListener(this);
        edt_cardNum.addTextChangedListener(new PhoneNumberFormattingTextWatcher() {
            private boolean backspacingFlag = false;
            private boolean editedFlag = false;
            private int cursorComplement;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                cursorComplement = s.length() - edt_cardNum.getSelectionStart();
                if (count > after) {
                    backspacingFlag = true;
                } else {
                    backspacingFlag = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);

            }

            @Override
            public synchronized void afterTextChanged(Editable s) {


                String string = s.toString();
                String phone = string.replaceAll("[^\\d]", "");

                if (!editedFlag) {

                    //example: 999999999 <- 6+ digits already typed
                    // masked: (999) 999-999
                    if (phone.length() >= 12 && !backspacingFlag) {
                        editedFlag = true;
                        String ans = phone.substring(0, 4) + " " + phone.substring(4, 8) + " " + phone.substring(8, 12);
                        edt_cardNum.setText(ans);
                        edt_cardNum.setSelection(edt_cardNum.getText().length() - cursorComplement);

                    }
                } else {
                    editedFlag = false;
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_online) {
                    btn_paynows.setVisibility(View.VISIBLE);
                    edt_coupon_enter.setVisibility(View.VISIBLE);
                    btn_addOp.setVisibility(View.GONE);
                    Intent intent = new Intent(OpRegistrationActivity.this, NewCouponActivity.class);
                    startActivityForResult(intent, request_code);

                }
                if (checkedId == R.id.radio_hospital) {
                    btn_paynows.setVisibility(View.GONE);
                    edt_coupon_enter.setVisibility(View.GONE);
                    btn_addOp.setVisibility(View.VISIBLE);
                    txt_consultationfee.setText("Consultation fee: ₹" +Integer.parseInt(fee.consultationFee));
                }
            }
        });


        if (isConnected()) {
            FeatchUserDetails();
            FeatchCity();
        } else
            showToast(getString(R.string.nointernet));


    }

    private void FeatchUserDetails() {
        String a_u_id=manager.getSingleField(SessionManager.KEY_ID);
        Toast.makeText(this, a_u_id, Toast.LENGTH_SHORT).show();
        String json="";
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("a_u_id",a_u_id);
            json=jsonObject.toString();
           } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest=null;
        try {
            jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, ApiUrl.NewLoginBaseUrl + ApiUrl.details, new JSONObject(json), new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Gson gson=new Gson();
                    NewPatientDetailsPojo data=gson.fromJson(String.valueOf(response),NewPatientDetailsPojo.class);
                    if(data.status==1)
                    {
                        List<String> namelist=new ArrayList<>();
                        List<String> moblist=new ArrayList<>();


                        for (int i = 0; i <data.details.size() ; i++) {
                            namelist.add(data.details.get(i).patientName);
                            moblist.add(data.details.get(i).mobileNum);

                        }
                        String[] item = namelist.toArray(new String[namelist.size()]);

                        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>
                                (OpRegistrationActivity.this,android.R.layout.select_dialog_item,item);
                        ac_name.setThreshold(1);//will start working from first character
                        ac_name.setAdapter(adapter3);


                        String[] item1 = moblist.toArray(new String[moblist.size()]);

                        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>
                                (OpRegistrationActivity.this,android.R.layout.select_dialog_item,item1);
                        ac_mobile.setThreshold(1);//will start working from first character
                        ac_mobile.setAdapter(adapter1);
                    }
                    else {
                        showToast(data.message);
                    }


                }
            }, error -> {

            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue=Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == request_code && resultCode == RESULT_OK){
                cName = data.getStringExtra("cName");
                cAmount = data.getStringExtra("cAmount");
                cp_id = data.getStringExtra("cp_id");
            //Toast.makeText(this, cAmount, Toast.LENGTH_SHORT).show();
                edt_coupon_enter.setText(cName);
                amt=Integer.parseInt(fee.consultationFee)-Integer.parseInt(cAmount);
                txt_consultationfee.setText("Consultation fee: ₹" + amt);

        }
    }

    private void fetchData(int data_type, int position) {
        switch (data_type) {
            case 1:
                city = cities.get(position).getValue();
                city_txt.setText(city);
                precitypos = position;
                prehospos = -1;
                predepartpos = -1;
                prespecpos = -1;
                predoctpos = -1;
                pretimepos = -1;
                FetchHospitals(city);
                dept = null;

                dept_txt.setText("Select Department");
                spl = null;
                spl_txt.setText("Select Speciality");
                specialists = null;
                hospitals = null;
                doctorlists = null;
                departments = null;
                timeSlots = null;
                hos_txt.setText("Select Hospital");
                doct = null;
                doctor_txt.setText("Select Doctor");
                time = null;
                time_txt.setText("Select time");
                checkboxfee.setEnabled(false);

                txt_consultationfee.setVisibility(View.INVISIBLE);
                ll_radiobutton.setVisibility(View.GONE);
                break;
            case 2:
                hospital = hospitals.get(position).getValue();
                hos_id = hospitals.get(position).getId();
                prehospos = position;
                predepartpos = -1;
                prespecpos = -1;
                predoctpos = -1;
                pretimepos = -1;
                hos_txt.setText(hospital);
                FetchDepatments(hos_id);

                departments = null;
                specialists = null;
                doctorlists = null;
                timeSlots = null;
                time = null;
                time_txt.setText("Select time");
                spl = null;
                spl_txt.setText("Select Speciality");
                dept = null;
                dept_txt.setText("Select Department");
                doct = null;
                doctor_txt.setText("Select Doctor");
                time_txt.setText("Select time");
                checkboxfee.setEnabled(false);
                checkboxfee.setChecked(false);
                txt_consultationfee.setVisibility(View.INVISIBLE);
                ll_radiobutton.setVisibility(View.GONE);
                break;
            case 3:
                dept = departments.get(position).getValue();
                dept_txt.setText(dept);
                predepartpos = position;

                prespecpos = -1;
                predoctpos = -1;
                pretimepos = -1;
                dept_id = departments.get(position).getDepartment_id();
                FetchSpecialists(hos_id, dept_id);
                specialists = null;
                doctorlists = null;
                timeSlots = null;
                time = null;
                time_txt.setText("Select time");
                spl = null;
                spl_txt.setText("Select Speciality");
                doct = null;
                doctor_txt.setText("Select Doctor");
                checkboxfee.setEnabled(false);
                checkboxfee.setChecked(false);
                txt_consultationfee.setVisibility(View.INVISIBLE);
                ll_radiobutton.setVisibility(View.GONE);

                break;
            case 4:
                spl = specialists.get(position).getValue();
                prespecpos = position;
                predoctpos = -1;
                pretimepos = -1;
                spl_id = specialists.get(position).getSpecialist_id();
                spl_txt.setText(spl);
                doctorlists = null;
                FetchDoctors(hos_id, spl_id);

                time = null;
                time_txt.setText("Select time");
                doct = null;
                timeSlots = null;
                doctor_txt.setText("Select Doctor");
                checkboxfee.setEnabled(false);
                checkboxfee.setChecked(false);
                txt_consultationfee.setVisibility(View.INVISIBLE);
                ll_radiobutton.setVisibility(View.GONE);


                break;
            case 5:
                doct = doctorlists.get(position).getValue();
                predoctpos = position;

                pretimepos = -1;
                doct_id = doctorlists.get(position).getDoctor_id();
                doctor_txt.setText(doct);
                FetchDoctorConsultFee(doct_id);
                FetchDoctorTimeSlot(hos_id, doct_id);
                checkboxfee.setEnabled(true);
                timeSlots = null;
                time = null;
                time_txt.setText("Select time");
                break;

            case 6:
                time = timeSlots.get(position).getStime();
                pretimepos = position;

                time_txt.setText(time.toString());


        }

    }

    private void FetchDoctorConsultFee(final String doct_id) {


        showDialog();
        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("doctor_id", doct_id);

        } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;

        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.BaseUrl + ApiUrl.doctors_consultation_fee, new JSONObject(json),
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();
                            Gson gson = new Gson();

                             fee = gson.fromJson(response.toString(), DoctorConsultFeePojo.class);
                            if (fee.status == 1) {
                                if (fee.consultationFee == null) {
                                    txt_consultationfee.setText("Consultation fee : nill");
                                } else {
                                    txt_consultationfee.setText("Consultation fee: ₹" + fee.consultationFee);

                                }

                            } else {
                                showToast(fee.message);

                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();

                }
            });
            RequestQueue queue = Volley.newRequestQueue(OpRegistrationActivity.this);
            queue.add(jsonObjReq);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void FetchDoctorTimeSlot(String hos_id, String doct_id) {

        JsonObject object = new JsonObject();
        object.addProperty("hos_id", hos_id);
        object.addProperty("doctor_id", doct_id);


        Call<TimeSlotlists> call = service.getTimeSlots(object, ApiUrl.content_type);

        showDialog();
        call.enqueue(new Callback<TimeSlotlists>() {
            @Override
            public void onResponse(Call<TimeSlotlists> call, Response<TimeSlotlists> response) {
                hideDialog();

                if (!response.isSuccessful()) {
                    showToast("Server side error");
                    return;
                }
                TimeSlotlists detps = response.body();

                if (detps != null) {

                    if (detps.getStatus() == 1) {
                        timeSlots = detps.getList();
                    } else {
                        doct = null;
                        doctor_txt.setText("No Doctor");
                        showToast(detps.getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<TimeSlotlists> call, Throwable t) {
                hideDialog();
            }

        });
    }

    private void FetchHospitals(String city) {
        JsonObject object = new JsonObject();

        object.addProperty("city", city);
        Call<HospitalList> call = service.getHospitals(object, ApiUrl.content_type);
        showDialog();
        call.enqueue(new Callback<HospitalList>() {
            @Override
            public void onResponse(Call<HospitalList> call, Response<HospitalList> response) {
                hideDialog();
                if (!response.isSuccessful()) {
                    showToast("Server side error");
                    return;
                }

                HospitalList detps = response.body();
                if (detps != null) {

                    if (detps.getStatus() == 1) {
                        hospitals = detps.getHospital();

                    } else {
                        hos_txt.setText("No Hospital");
                        showToast(detps.getMessage());

                    }
                }

            }

            @Override
            public void onFailure(Call<HospitalList> call, Throwable t) {
                hideDialog();
            }

        });
    }

    private void FetchSpecialists(String hos_id, String department_id) {
        JsonObject object = new JsonObject();
        object.addProperty("hos_id", hos_id);
        object.addProperty("department_id", department_id);


        Call<Specialists> call = service.getSpecialists(object, ApiUrl.content_type);

        showDialog();
        call.enqueue(new Callback<Specialists>() {
            @Override
            public void onResponse(Call<Specialists> call, Response<Specialists> response) {
                hideDialog();

                if (!response.isSuccessful()) {
                    showToast("Server side error");
                    return;
                }
                Specialists detps = response.body();

                if (detps != null) {

                    if (detps.getStatus() == 1) {
                        specialists = detps.getSpecialist();
                    } else {
                        dept = null;
                        dept_txt.setText("No Specialties");
                        showToast(detps.getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<Specialists> call, Throwable t) {
                hideDialog();
            }

        });
    }


    private void FetchDoctors(String hos_id, String specialist_id) {
        JsonObject object = new JsonObject();
        object.addProperty("hos_id", hos_id);
        object.addProperty("specialist_id", specialist_id);


        Call<Doctorlists> call = service.getDocterlists(object, ApiUrl.content_type);

        showDialog();
        call.enqueue(new Callback<Doctorlists>() {
            @Override
            public void onResponse(Call<Doctorlists> call, Response<Doctorlists> response) {
                hideDialog();

                if (!response.isSuccessful()) {
                    showToast("Server side error");
                    return;
                }
                Doctorlists doc = response.body();

                if (doc != null) {

                    if (doc.getStatus() == 1) {
                        doctorlists = doc.getDoctorlist();
                    } else {
                        spl = null;
                        spl_txt.setText("No Speciality");
                        showToast(doc.getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<Doctorlists> call, Throwable t) {
                hideDialog();
            }

        });
    }

    private void FetchDepatments(String hos_id) {
        JsonObject object = new JsonObject();

        object.addProperty("hos_id", hos_id);
        Call<DepartmentList> call = service.getDepts(object, ApiUrl.content_type);

        showDialog();
        call.enqueue(new Callback<DepartmentList>() {
            @Override
            public void onResponse(Call<DepartmentList> call, Response<DepartmentList> response) {
                hideDialog();

                if (!response.isSuccessful()) {
                    showToast("Server side error");
                    return;
                }
                DepartmentList detps = response.body();

                if (detps != null) {

                    if (detps.getStatus() == 1) {
                        departments = detps.getDepartment();
                        dept = null;
                        dept_txt.setText("Select Department");
                    } else {
                        showToast(detps.getMessage());
                        dept = null;
                        dept_txt.setText("No Department");
                    }

                }
            }

            @Override
            public void onFailure(Call<DepartmentList> call, Throwable t) {
                hideDialog();
            }

        });
    }

    private void FeatchCity() {
        Call<CityList> call = service.getCitys(new JsonObject(), ApiUrl.content_type);
        showDialog();
        call.enqueue(new Callback<CityList>() {
            @Override
            public void onResponse(Call<CityList> call, Response<CityList> response) {
                hideDialog();
                if (!response.isSuccessful()) {
                    showToast("Server side error");
                    return;
                }


                CityList cityList = response.body();

                if (cityList != null) {


                    if (cityList.getStatus() == 1) {
                        cities = cityList.getCitys();
                    } else
                        showToast(cityList.getMessage());
                }


            }

            @Override
            public void onFailure(Call<CityList> call, Throwable t) {
                hideDialog();
            }
        });
    }


    private void addAppointmentApi(String onoffstatus) {
        if (city == null || city.isEmpty()) {
            showToast("no city is selected");
            return;
        }
        if (hospital == null || hospital.isEmpty()) {
            showToast("no Hospital is selected");
            return;
        }
        if (dept == null || dept.isEmpty()) {
            showToast("no department is selected");
            return;
        }
        if (spl == null || spl.isEmpty()) {
            showToast("no Specialist is selcted");
            return;
        }
        if (doct == null || doct.isEmpty()) {
            showToast("no Doctor is selcted");
            return;
        }
        String name = ac_name.getText().toString();
        if (name == null || name.isEmpty()) {
            ac_name.setError("Please enter Name");
            return;
        }
        String mob = ac_mobile.getText().toString();
        if (mob == null || mob.isEmpty()) {
            ac_mobile.setError("Please enter Mobile No");
            return;
        }
        if (mob.length()<10) {
            ac_mobile.setError("Please enter valid Mobile No");
            return;
        }

//        String cardNum = edt_cardNum.getText().toString();
//        if (cardNum == null || cardNum.isEmpty()) {
//            edt_cardNum.setError("Please enter card number");
//            return;
//        }
//        if (cardNum.length()<14) {
//            edt_cardNum.setError("Please enter valid card number");
//            return;
//        }
//


        String age = edt_age.getText().toString();
        if (age == null || age.isEmpty()) {
            edt_age.setError("Please enter age");
            return;
        }
        String date = dateOfReg.getText().toString();
        if (date == null || date.isEmpty()) {
            showToast("Date not selected");
            return;
        }
        if (time == null || time.isEmpty()) {
            showToast("Time is not selected");
            return;
        }


        Appointment appointment = new Appointment();
        appointment.setAUId(manager.getSingleField(SessionManager.KEY_ID));
        appointment.setCity(city);
        appointment.setHospital(hos_id);
        appointment.setDepartmentName(dept_id);
        appointment.setSpecialistName(spl_id);
        appointment.setDoctorName(doct_id);

        appointment.setName(ac_name.getText().toString());
        appointment.setMobile(ac_mobile.getText().toString());
        appointment.setPatientAge(edt_age.getText().toString());
        appointment.setDate(dateOfReg.getText().toString());
        appointment.setTime(time);
        Log.e("==@@--",manager.getSingleField(SessionManager.KEY_ID));
        Log.e("==@@--",city);
        Log.e("==@@--",hos_id);
        Log.e("==@@--",dept_id);
        Log.e("==@@--",spl_id);
        Log.e("==@@--",doct_id);
        Log.e("==@@--",ac_name.getText().toString());
        Log.e("==@@--",ac_mobile.getText().toString());
        Log.e("==@@--",edt_age.getText().toString());
        Log.e("==@@--",dateOfReg.getText().toString());
        Log.e("==@@--",time);


        Call<RegResult> call = service.addAppointments(appointment, ApiUrl.content_type);
        showDialog();
        call.enqueue(new Callback<RegResult>() {
            @Override
            public void onResponse(Call<RegResult> call, Response<RegResult> response) {
                hideDialog();
                if (!response.isSuccessful()) {
                    showToast("Server side error");
                    return;
                }


                RegResult regResult = response.body();
                if (regResult.getStatus() == 1) {
                    Toast.makeText(OpRegistrationActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                    if(onoffstatus.equals("online"))
                    {
                        callOnlinePayment(regResult.getB_id());}
                    else if(onoffstatus.equals("offline"))
                    {
                        CallofflineApi(regResult.getB_id());
                    }
                } else
                    showToast(regResult.getMessage());
            }

            @Override
            public void onFailure(Call<RegResult> call, Throwable t) {
                hideDialog();

            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back:
                finish();
                hideSoftKeyboard();
                break;
            case R.id.btn_add_op:
                addAppointment("offline");
                break;
            case R.id.date_of_reg:
                showDatePicker();
                break;
                case R.id.btn_pay_online:
                    if(edt_coupon_enter.getText().equals(""))
                    {
                    showToast("Please select coupon");
                    }
                    else
                    {
                  //  callOnlinePayment();
                        addAppointment("online");
                    }
                    break;

        }
    }


    int mYear, mMonth, mDay;

    public void showDatePicker() {
        // To show current date in the datepicker
        Calendar mcurrentDate = Calendar.getInstance();
        mYear = mcurrentDate.get(Calendar.YEAR);
        mMonth = mcurrentDate.get(Calendar.MONTH);
        mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker = new DatePickerDialog(OpRegistrationActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                Calendar myCalendar = Calendar.getInstance();
                try {

                    myCalendar.set(Calendar.YEAR, selectedyear);
                    myCalendar.set(Calendar.MONTH, selectedmonth);
                    myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                    String myFormat = "yyyy-MM-dd"; //Change as you need
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                    Date date = myCalendar.getTime();
                    String sDate = sdf.format(date);

                    dateOfReg.setText(sDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mDay = selectedday;
                mMonth = selectedmonth;
                mYear = selectedyear;
            }
        }, mYear, mMonth, mDay);
        //mDatePicker.setTitle("Select date");

        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        mDatePicker.show();
    }

    private void addAppointment(String onoffstatus) {
        if (isConnected()) {
            addAppointmentApi(onoffstatus);
        } else {
            showToast(getString(R.string.nointernet));

        }

    }

    int data_type = 0;

    public void openSingleChoice(View view) {
        List list = null;
        String title = null;
        switch (view.getId()) {
            case R.id.city_lay:
                list = cities;
                title = "Select City";
                data_type = 1;
                break;
            case R.id.hos_lay:
                title = "Select Hospital";
                list = hospitals;
                data_type = 2;
                break;

            case R.id.dept_lay:
                title = "Select Department";
                list = departments;
                data_type = 3;
                break;
            case R.id.spl_lay:
                title = "Select Specialties";
                list = specialists;
                data_type = 4;
                break;
            case R.id.doctor_lay:
                title = "Select Doctors";
                list = doctorlists;
                data_type = 5;
                break;
            case R.id.time_lay:
                title = "Select time";
                list = timeSlots;
                data_type = 6;
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
                            .setSingleChoiceItems(items, prespecpos, new DialogInterface.OnClickListener() {
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
                            .setSingleChoiceItems(items, predoctpos, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    fetchData(data_type, which);
                                    data_type = 0;


                                }
                            });
                    break;
                case 6:
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

    ArrayList<Hospital> out = new ArrayList<>();


    private String[] getStringArray(List formatters) {
        String[] strings = new String[formatters.size()];
        for (int i = 0; i < formatters.size(); i++) {
            strings[i] = ((Formatter) formatters.get(i)).getValue();
        }
        return strings;
    }


    public void showConsultationFee(View view) {
        if (checkboxfee.isChecked()) {
            txt_consultationfee.setVisibility(View.VISIBLE);
            ll_radiobutton.setVisibility(View.VISIBLE);
            btn_addOp.setBackgroundResource(R.drawable.sign_button_shape);
            btn_addOp.setEnabled(true);
            btn_paynows.setBackgroundResource(R.drawable.sign_button_shape);
            btn_paynows.setEnabled(true);
        } else {
            txt_consultationfee.setVisibility(View.INVISIBLE);
            ll_radiobutton.setVisibility(View.GONE);

            btn_addOp.setBackgroundResource(R.drawable.disable_burtton_shape);
            btn_addOp.setEnabled(false);
            btn_paynows.setBackgroundResource(R.drawable.disable_burtton_shape);
            btn_paynows.setEnabled(false);
        }
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
    public void callOnlinePayment(Integer bid) {
        /**
         * You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", manager.getSingleField(SessionManager.KEY_NAME));
            options.put("description", "Payment");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            total = Double.parseDouble(String.valueOf(amt));
            total = total * 100;

            options.put("amount", total);

            JSONObject preFill = new JSONObject();
            preFill.put("email", manager.getSingleField(SessionManager.KEY_EMAIL));
            preFill.put("contact", manager.getSingleField(SessionManager.KEY_NUMBER));

            options.put("prefill", preFill);
            b_id=bid;

            co.open(activity, options);
        } catch (Exception e) {
            total = 0;
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {


        CallApi(razorpayPaymentID);
    }

    private void CallofflineApi(Integer b_id) {

        showDialog();
        String json = "";
        JSONObject jsonObject = new JSONObject();
        final String UID = manager.getSingleField(SessionManager.KEY_ID);

        try {
            jsonObject.put("a_u_id", UID);
            jsonObject.put("cp_id", "");
            jsonObject.put("payment_type", "0");
            jsonObject.put("payment_id", "");
            jsonObject.put("coupon", "");
            jsonObject.put("b_id", b_id);
            jsonObject.put("t_amount", fee.consultationFee.toString());
            jsonObject.put("paid_amount", "");
            jsonObject.put("coupon_amount", "");

            jsonObject.put("razorpay_payment_id", "");
            jsonObject.put("razorpay_order_id", "");
            jsonObject.put("razorpay_signature", "");


        } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;

        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.NewLoginBaseUrl + ApiUrl.appoinmentpayments, new JSONObject(json),
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();

                            Gson gson = new Gson();

                            OnlinePaymentPojo codData = gson.fromJson(response.toString(), OnlinePaymentPojo.class);
                            if (codData.status == 1) {
                                finish();

                                showToast("Successfully Order");
                                amt = 0;
                            } else {
                                showToast("UnSuccessful Order");
                                amt = 0;
                            }
                        }

                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    amt=0;
                    showToast(getResources().getString(R.string.serverproblem));

                }
            });

            RequestQueue queue = Volley.newRequestQueue(OpRegistrationActivity.this);
            queue.add(jsonObjReq);
        } catch (JSONException e) {
            e.printStackTrace();
            hideDialog();
            amt = 0;
            showToast(getResources().getString(R.string.serverproblem));

        }


    }
    private void CallApi(String razorpayPaymentID) {

        showDialog();
        String json = "";
        JSONObject jsonObject = new JSONObject();
        final String UID = manager.getSingleField(SessionManager.KEY_ID);

        try {
            jsonObject.put("a_u_id", UID);
            jsonObject.put("cp_id", cp_id);
            jsonObject.put("payment_type", "1");
            jsonObject.put("payment_id", "");
            jsonObject.put("coupon", cName);
            jsonObject.put("t_amount", fee.consultationFee.toString());
            jsonObject.put("paid_amount", amt);
            jsonObject.put("coupon_amount", cAmount);
            jsonObject.put("b_id", b_id);

            jsonObject.put("razorpay_payment_id", razorpayPaymentID);
            jsonObject.put("razorpay_order_id", "");
            jsonObject.put("razorpay_signature", "");


        } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;

        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.NewLoginBaseUrl + ApiUrl.appoinmentpayments, new JSONObject(json),
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideDialog();

                            Gson gson = new Gson();

                            OnlinePaymentPojo codData = gson.fromJson(response.toString(), OnlinePaymentPojo.class);
                            if (codData.status == 1) {
                                finish();

                                showToast("Successfully Order");
                                amt = 0;
                            } else {
                                showToast("UnSuccessful Order");
                                amt = 0;
                            }
                        }

                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    amt=0;
                    showToast(getResources().getString(R.string.serverproblem));

                }
            });

            RequestQueue queue = Volley.newRequestQueue(OpRegistrationActivity.this);
            queue.add(jsonObjReq);
        } catch (JSONException e) {
            e.printStackTrace();
            hideDialog();
            amt = 0;
            showToast(getResources().getString(R.string.serverproblem));

        }


    }


    @Override
    public void onPaymentError(int code, String response) {
        total = 0;
        try {
            Toast.makeText(this, "Payment error please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }


}
