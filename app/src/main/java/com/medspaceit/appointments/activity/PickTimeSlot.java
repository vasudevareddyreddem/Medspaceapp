package com.medspaceit.appointments.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.medspaceit.appointments.R;
import com.medspaceit.appointments.apis.ApiUrl;
import com.medspaceit.appointments.model.Formatter;
import com.medspaceit.appointments.model.Hospital;
import com.medspaceit.appointments.model.MyTimeList;
import com.medspaceit.appointments.model.TimePickerList;
import com.medspaceit.appointments.model.TimePickerList;
import com.medspaceit.appointments.model.TimeSlot;

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

public class PickTimeSlot extends BaseActivity {
    @BindView(R.id.et_select_date)
    EditText et_select_date;

    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.txt_time)
    TextView txt_time;

    @BindView(R.id.btn_next_a)
    Button btn_next_a;

    @BindView(R.id.your_state_progress_bar_id1)
    StateProgressBar your_state_progress_bar_id1;

    List<MyTimeList> timeSlots = null;

    String lab_id, utime,passAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_time_slot);
        ButterKnife.bind(this);

        back.setOnClickListener(this);
        btn_next_a.setOnClickListener(this);
        et_select_date.setOnClickListener(this);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        passAmount = b.getString("passAmount");
        if (isConnected()) {
            FetchTimeSlot(lab_id);
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
            case R.id.et_select_date:
                showDatePicker();

                break;
            case R.id.btn_next_a:
                if (et_select_date.getText().toString().equals("")) {
                    showToast("Please select date");
                } else if (txt_time.getText().toString().equals("Select Time")) {
                    showToast("Please select time");
                } else {
                    Intent i = new Intent(this, PatientDetails.class);
                    i.putExtra("udate", et_select_date.getText().toString());
                    i.putExtra("utime", txt_time.getText().toString());
                    i.putExtra("passAmount", passAmount);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


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

        DatePickerDialog mDatePicker = new DatePickerDialog(PickTimeSlot.this, new DatePickerDialog.OnDateSetListener() {
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

                    et_select_date.setText(sDate);
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

    private void FetchTimeSlot(String lab_id) {

        JsonObject object = new JsonObject();
        object.addProperty("a_id", 0);


        Call<TimePickerList> call = servicedg.getList(object, ApiUrl.content_type);

        showDialog();
        call.enqueue(new Callback<TimePickerList>() {
            @Override
            public void onResponse(Call<TimePickerList> call, Response<TimePickerList> response) {
                hideDialog();

                if (!response.isSuccessful()) {
                    showToast("Server side error");
                    return;
                }
                TimePickerList detps = response.body();

                if (detps != null) {

                    if (detps.status == 1) {
                        timeSlots = detps.list;
                    } else {

                        showToast(detps.getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<TimePickerList> call, Throwable t) {
                hideDialog();
            }

        });
    }

    public void openSingleChoice(View view) {
        List list = null;
        String title = null;

        list = timeSlots;
        title = "Select Time";


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
                            fetchData(which);


                        }
                    });
            builder.create().show();
        }
    }

    private void fetchData(int position) {
        utime = timeSlots.get(position).getStime();
        txt_time.setText(utime.toString());

    }

    private String[] getStringArray(List formatters) {
        String[] strings = new String[formatters.size()];
        for (int i = 0; i < formatters.size(); i++) {
            strings[i] = ((Formatter) formatters.get(i)).getValue();
        }
        return strings;

    }
}
