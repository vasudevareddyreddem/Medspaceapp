package com.medspaceit.appointments.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.medspaceit.appointments.R;
import com.medspaceit.appointments.apis.ApiUrl;
import com.medspaceit.appointments.model.City;
import com.medspaceit.appointments.model.CityList;
import com.medspaceit.appointments.model.Formatter;
import com.medspaceit.appointments.utils.MessageToast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Lab extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.city_text)
    TextView city_text;

    @BindView(R.id.select_labtest_ll)
    LinearLayout select_labtest_ll;
    @BindView(R.id.select_booklabtest_ll)
    LinearLayout select_booklabtest_ll;
    @BindView(R.id.select_myreport_ll)
    LinearLayout select_myreport_ll;
    @BindView(R.id.checkup_recyclerview)
    RecyclerView checkup_recyclerview;


    List<City> cities = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        select_labtest_ll.setOnClickListener(this);
        select_booklabtest_ll.setOnClickListener(this);
        select_myreport_ll.setOnClickListener(this);
        if (isConnected()) {
            FeatchCity();
        } else
            MessageToast.showToastMethod(this, "No Internet");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.select_myreport_ll:
                Toast.makeText(this, "my report", Toast.LENGTH_SHORT).show();
                break;
            case R.id.select_labtest_ll:
                startActivity(new Intent(this, SelectLabTest.class));
                break;
            case R.id.select_booklabtest_ll:
                Toast.makeText(this, "book lab test", Toast.LENGTH_SHORT).show();
                break;

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
