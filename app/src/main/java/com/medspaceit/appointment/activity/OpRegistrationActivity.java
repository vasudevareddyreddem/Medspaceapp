package com.medspaceit.appointment.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.medspaceit.appointment.R;
import com.medspaceit.appointment.adapters.TagsAdapter;
import com.medspaceit.appointment.apis.ApiUrl;
import com.medspaceit.appointment.model.Appointment;
import com.medspaceit.appointment.model.City;
import com.medspaceit.appointment.model.CityList;
import com.medspaceit.appointment.model.Department;
import com.medspaceit.appointment.model.DepartmentList;
import com.medspaceit.appointment.model.Doctorlist;
import com.medspaceit.appointment.model.Doctorlists;
import com.medspaceit.appointment.model.Formatter;
import com.medspaceit.appointment.model.Hospital;
import com.medspaceit.appointment.model.HospitalList;
import com.medspaceit.appointment.model.RegResult;
import com.medspaceit.appointment.model.Specialist;
import com.medspaceit.appointment.model.Specialists;
import com.medspaceit.appointment.model.TimeSlot;
import com.medspaceit.appointment.model.TimeSlotlists;
import com.medspaceit.appointment.utils.MessageToast;
import com.medspaceit.appointment.utils.SessionManager;

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

public class OpRegistrationActivity extends BaseActivity implements TagsAdapter.OnRemoveListener {
    @BindView(R.id.date_of_reg)
    EditText dateOfReg;

    @BindView(R.id.age)
    EditText edt_age;

    @BindView(R.id.name)
    EditText edt_name;

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
    Button addOp;

    List<City> cities = null;
    List<Department> departments = null;
    List<Specialist> specialists = null;
    List<Doctorlist> doctorlists = null;
    List<Hospital> hospitals = null;
    List<TimeSlot> timeSlots = null;
    String city, dept, spl, doct, time, hospital, hos_id, dept_id, spl_id, doct_id;
    TagsAdapter tagsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_op_registration);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        addOp.setOnClickListener(this);
        dateOfReg.setOnClickListener(this);
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

        if (isConnected()) {
            FeatchCity();
        } else
            MessageToast.showToastMethod(this, "No Internet");


    }

    private void fetchData(int data_type, int position) {
        switch (data_type) {
            case 1:
                city = cities.get(position).getValue();
                city_txt.setText(city);
                FetchHospitals(city);
                dept = null;

                dept_txt.setText("Select Department");
                spl = null;
                spl_txt.setText("Select Speciality");
                specialists = null;
                hospitals = null;
                doctorlists = null;
                departments=null;
                timeSlots=null;
                hos_txt.setText("Select Hospital");
                doct = null;
                doctor_txt.setText("Select Doctor");
                time = null;
                time_txt.setText("Select time");
                checkboxfee.setEnabled(false);

                txt_consultationfee.setVisibility(View.INVISIBLE);
                break;
            case 2:
                hospital = hospitals.get(position).getValue();
                hos_id = hospitals.get(position).getId();
                hos_txt.setText(hospital);
                txt_consultationfee.setText("Consultation fee :" + hospitals.get(position).getConsultationfee());
                FetchDepatments(hos_id);
                departments=null;
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
                break;
            case 3:
                dept = departments.get(position).getValue();
                dept_txt.setText(dept);
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
                break;
            case 4:
                spl = specialists.get(position).getValue();
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

                break;
            case 5:
                doct = doctorlists.get(position).getValue();
                doct_id = doctorlists.get(position).getDoctor_id();
                doctor_txt.setText(doct);
               FetchDoctorTimeSlot(hos_id, doct_id);
                checkboxfee.setEnabled(true);
                timeSlots = null;
                time = null;
                time_txt.setText("Select time");
                break;

            case 6:
                time = timeSlots.get(position).getStime();
                time_txt.setText(time.toString());


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

                    City city = new City();
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


    private void addAppointmentApi() {
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
        String name = edt_name.getText().toString();
        if (name == null || name.isEmpty()) {
            edt_name.setError("Please enter Name");
            return;
        }

        String cardNum = edt_cardNum.getText().toString();
        if (cardNum == null || cardNum.isEmpty()) {
            edt_cardNum.setError("Please enter card number");
            return;
        }
        if (cardNum.length()<14) {
            edt_cardNum.setError("Please enter valid card number");
            return;
        }



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

        appointment.setName(edt_name.getText().toString());
        appointment.setPatientAge(edt_age.getText().toString());
        appointment.setDate(dateOfReg.getText().toString());
        appointment.setTime(time);

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
                if (regResult.getStatus() == 1)

                {
                    Toast.makeText(OpRegistrationActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();

                    finish();
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
                addAppointment();
                break;
            case R.id.date_of_reg:
                showDatePicker();
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

    private void addAppointment() {
        if (isConnected()) {
            addAppointmentApi();
        } else {
            showToast("No Internet");
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
            builder.setCustomTitle(view1)
                    .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            fetchData(data_type, which);
                            data_type = 0;


                        }
                    });
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


    @Override
    public void onRemoveTag(Hospital s) {
        if (out.contains(s))
            out.remove(s);
        if (out.isEmpty()) {
            layout.setVisibility(View.GONE);
        }
    }


    public void showConsultationFee(View view) {
        if (checkboxfee.isChecked()) {
            txt_consultationfee.setVisibility(View.VISIBLE);
            addOp.setBackgroundResource(R.drawable.sign_button_shape);
            addOp.setEnabled(true);
        } else {
            txt_consultationfee.setVisibility(View.INVISIBLE);
            addOp.setBackgroundResource(R.drawable.disable_burtton_shape);
            addOp.setEnabled(false);
        }
    }
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
