package com.medspaceit.appointments.activity;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.medspaceit.appointments.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectLabTest extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.date_of_reg)
    EditText dateOfReg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_lab_test);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        dateOfReg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;  case R.id.date_of_reg:
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

        DatePickerDialog mDatePicker = new DatePickerDialog(SelectLabTest.this, new DatePickerDialog.OnDateSetListener() {
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

    public void openSingleChoice(View view) {
    }
}
