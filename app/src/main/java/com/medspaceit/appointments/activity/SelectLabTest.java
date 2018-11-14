package com.medspaceit.appointments.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.medspaceit.appointments.R;
import com.medspaceit.appointments.apis.ApiUrl;
import com.medspaceit.appointments.model.City;
import com.medspaceit.appointments.model.CityList;
import com.medspaceit.appointments.model.Formatter;
import com.medspaceit.appointments.utils.MessageToast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectLabTest extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.city_text)
    TextView city_text;
    @BindView(R.id.openSearchView)
    LinearLayout openSearchView;
    List<City> cities = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_lab_test);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        openSearchView.setOnClickListener(this);
        if (isConnected()) {
            FeatchCity();
        } else {
            MessageToast.showToastMethod(this, "No Internet");
        }

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;
                case R.id.openSearchView:
            startActivity(new Intent(this,SearchTestActivity.class));
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

                   // dateOfReg.setText(sDate);
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
                String city = cities.get(position).getValue();
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


}
