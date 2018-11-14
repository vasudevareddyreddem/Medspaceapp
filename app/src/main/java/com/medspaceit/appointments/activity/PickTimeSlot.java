package com.medspaceit.appointments.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kofigyan.stateprogressbar.StateProgressBar;
import com.medspaceit.appointments.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_time_slot);
        ButterKnife.bind(this);

        back.setOnClickListener(this);
        btn_next_a.setOnClickListener(this);
        et_select_date.setOnClickListener(this);
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
                Intent i=new Intent(this,PatientDetails.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


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

    public void openSingleChoice(View view) {
    }
}
