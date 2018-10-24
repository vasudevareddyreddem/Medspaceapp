package com.medspaceit.appointment.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.medspaceit.appointment.R;
import com.medspaceit.appointment.adapters.TagsAdapter;
import com.medspaceit.appointment.apis.ApiUrl;
import com.medspaceit.appointment.model.Appointment;
import com.medspaceit.appointment.model.City;
import com.medspaceit.appointment.model.CityList;
import com.medspaceit.appointment.model.Department;
import com.medspaceit.appointment.model.DepartmentList;
import com.medspaceit.appointment.model.Formatter;
import com.medspaceit.appointment.model.Hospital;
import com.medspaceit.appointment.model.HospitalList;
import com.medspaceit.appointment.model.RegResult;
import com.medspaceit.appointment.model.Specialist;
import com.medspaceit.appointment.model.Specialists;
import com.medspaceit.appointment.model.TimeSlot;
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

public class OpRegistrationActivity extends BaseActivity implements TagsAdapter.OnRemoveListener{
    @BindView(R.id.date_of_reg)
    EditText dateOfReg;

    @BindView(R.id.age)
    EditText edt_age;


    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.city_txt)
    TextView city_txt;

    @BindView(R.id.dept_txt)
    TextView dept_txt;

    @BindView(R.id.spl_txt)
    TextView spl_txt;

    @BindView(R.id.hos_txt)
    TextView hos_txt;

    @BindView(R.id.time_txt)
    TextView time_txt;

    @BindView(R.id.selected_tag_view)
    RecyclerView recyclerView_tags;

    @BindView(R.id.hos_lay)
    RelativeLayout layout;

    @BindView(R.id.btn_add_op)
    Button addOp;
    List<City> cities=null;
    List<Department> departments=null;
    List<Specialist> specialists=null;
    List<Hospital> hospitals=null;
    List<TimeSlot> timeSlots=null;
    String city,dept,spl,time;
    TagsAdapter tagsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_op_registration);
        ButterKnife.bind(this);
          back.setOnClickListener(this);
          addOp.setOnClickListener(this);
        dateOfReg.setOnClickListener(this);
          if(isConnected()){
              FeatchCity();
          }
          else
          MessageToast.showToastMethod(this,"No Internet");

          setTimeSlots();
        recyclerView_tags.setLayoutManager(
                new StaggeredGridLayoutManager(2,
                        StaggeredGridLayoutManager.VERTICAL));
        tagsAdapter=new TagsAdapter(out);
           tagsAdapter.setOnRemoveListener(this);
        recyclerView_tags.setAdapter(tagsAdapter);


    }

    private void setTimeSlots() {
        timeSlots=new ArrayList<TimeSlot>();
//        timeSlots.add(new TimeSlot("12:00 am"));
//        timeSlots.add(new TimeSlot("12:30 am"));
        for(int i=8;i<=22;i++){
            int time=i%12;
            if(i<12){
                String temp = String.format("%02d", i);
                timeSlots.add(new TimeSlot(temp+":00 am"));
                timeSlots.add(new TimeSlot(temp+":30 am"));
            }else if(i==12){
                timeSlots.add(new TimeSlot(12+":00 pm"));
                timeSlots.add(new TimeSlot(12+":30 pm"));
            }else
            {
                String temp = String.format("%02d", time);
                timeSlots.add(new TimeSlot(temp+":00 pm"));
                timeSlots.add(new TimeSlot(temp+":30 pm"));
            }

        }

    }

   private void fetchData(int data_type,int position){
        switch (data_type){
            case 1:
                city=cities.get(position).getValue();
                city_txt.setText(city);
                FetchDepatments(city);
                dept=null;
                dept_txt.setText("Select Department");
                spl=null;
                spl_txt.setText("Select Specialties");
                specialists=null;
                hospitals=null;
                hos_txt.setText("Select Hospitals");
                break;
            case 2:
                dept=departments.get(position).getValue();
                dept_txt.setText(dept);
                FetchSpecialists(dept,city);
                spl=null;
                spl_txt.setText("Select Specialties");
                hospitals=null;
                hos_txt.setText("Select Hospitals");
                break;
            case 3:
                spl=specialists.get(position).getValue();
                spl_txt.setText(spl);
                FetchHospitals(spl,city);
                hos_txt.setText("Select Hospitals");
                break;
            case 0:
                time=timeSlots.get(position).getValue();
                time_txt.setText(time);
                break;

        }

    }
    private void FetchHospitals(String specialist,String  city) {
        JsonObject object=new JsonObject();
        object.addProperty("specialist_name",specialist);
        object.addProperty("city",city);
        Call<HospitalList> call= service.getHospitals(object, ApiUrl.content_type);
        showDialog();
        call.enqueue(new Callback<HospitalList>() {
            @Override
            public void onResponse(Call<HospitalList> call, Response<HospitalList> response) {
                hideDialog();
                if(!response.isSuccessful())
                {
                    showToast("Server side error");
                    return;
                }

                HospitalList detps=response.body();
                if(detps!=null)
                {

                    if(detps.getStatus()==1)
                    {
                        hospitals=detps.getHospital();
                    }else {
                        hos_txt.setText("No Hospitals");
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
    private void FetchSpecialists(String deptname,String city) {
        JsonObject object=new JsonObject();
        object.addProperty("department_name",deptname);
        object.addProperty("city",city);
        Call<Specialists> call= service.getSpecialists(object, ApiUrl.content_type);

        showDialog();
        call.enqueue(new Callback<Specialists>() {
            @Override
            public void onResponse(Call<Specialists> call, Response<Specialists> response) {
                hideDialog();

                if(!response.isSuccessful())
                {
                    showToast("Server side error");
                    return;
                }
                Specialists detps=response.body();

                if(detps!=null)
                {

                    if(detps.getStatus()==1)
                    {
                        specialists=detps.getSpecialist();
                    }else {
                      dept=null;
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

    private void FetchDepatments(String city) {
        JsonObject object=new JsonObject();
        object.addProperty("city",city);
        Call<DepartmentList> call= service.getDepts(object, ApiUrl.content_type);

        showDialog();
        call.enqueue(new Callback<DepartmentList>() {
            @Override
            public void onResponse(Call<DepartmentList> call, Response<DepartmentList> response) {
              hideDialog();

                if(!response.isSuccessful())
                {
                    showToast("Server side error");
                    return;
                }
                DepartmentList detps=response.body();

                if(detps!=null)
                {

                    if(detps.getStatus()==1)
                    {
                          departments=detps.getDepartment();
                        dept=null;
                        dept_txt.setText("Select Department");
                    }
                    else
                        {
                            showToast(detps.getMessage());
                          dept=null;
                          dept_txt.setText("No DePartments");
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
        Call<CityList> call= service.getCitys(new JsonObject(), ApiUrl.content_type);
        showDialog();
        call.enqueue(new Callback<CityList>() {
            @Override
            public void onResponse(Call<CityList> call, Response<CityList> response) {
                hideDialog();
                if(!response.isSuccessful())
                {
                    showToast("Server side error");
                    return;
                }


                CityList cityList=response.body();

                if(cityList!=null) {

                    City city=new City();
                    if(cityList.getStatus()==1)
                    {
                        cities = cityList.getCitys();
                    }else
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
        if(city==null||city.isEmpty()){
            showToast("no city is selected");
            return;
        }
        if(dept==null||dept.isEmpty()){
            showToast("no department is selected");
            return;
        }
        if(spl==null||spl.isEmpty()){
            showToast("no Specialist is selcted");
            return;
        }
        if(out.size()==0){
            showToast("No Hospital selected");
            return;
        }

        String age=edt_age.getText().toString();
        if(age==null||age.isEmpty())
        {
            edt_age.setError("Please enter age");
        return;
        }
        String date=dateOfReg.getText().toString();
        if(date==null||date.isEmpty()){
              showToast("Date not selected");
            return;
        }
        if(time==null||time.isEmpty()){
           showToast("Time is not selected");
           return;
        }


        Appointment appointment=new Appointment();
        appointment.setPatientAge(edt_age.getText().toString());
        appointment.setAUId(manager.getSingleField(SessionManager.KEY_ID));
        appointment.setCity(city);
        appointment.setDepartmentName(dept);
        appointment.setDate(dateOfReg.getText().toString());
        appointment.setSpecialistName(spl);
        appointment.setTime(time);
        appointment.addHos(out);
        Call<RegResult> call= service.addAppointments(appointment, ApiUrl.content_type);
        showDialog();
        call.enqueue(new Callback<RegResult>() {
            @Override
            public void onResponse(Call<RegResult> call, Response<RegResult> response) {
                hideDialog();
                if(!response.isSuccessful())
                {
                    showToast("Server side error");
                    return;
                }


                RegResult regResult=response.body();
                if(regResult.getStatus()==1)
                   finish();
                else
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

        switch (v.getId()){
            case R.id.back:
               finish();
                break;
            case R.id.btn_add_op:
              addAppointment();
                break;
            case R.id.date_of_reg:
                showDatePicker();
                break;

        }
    }



    int mYear,mMonth,mDay;
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
                    Date date=myCalendar.getTime();
                    String sDate=sdf.format(date);
                    dateOfReg.setText(sDate);
                }
           catch (Exception e){
                    e.printStackTrace();
           }
                mDay = selectedday;
                mMonth = selectedmonth;
                mYear = selectedyear;
            }
        }, mYear, mMonth, mDay);
        //mDatePicker.setTitle("Select date");
        mDatePicker.show();
    }
    private void addAppointment() {
        if(isConnected()){
            addAppointmentApi();
        }else {
            showToast("No Internet");
        }

    }
    int data_type=0;
    public void openSingleChoice(View view) {
        List list=null;
        String title=null;
        switch (view.getId()){
            case R.id.city_lay:
                list=cities;
                title="Select City";
                data_type=1;
                break;
            case R.id.dept_lay:
                title="Select Department";
                list=departments;
                data_type=2;
                break;
            case R.id.spl_lay:
                title="Select Specialties";
                list=specialists;
                data_type=3;
                break;
            case R.id.time_lay:
                title="Select time";
                list=timeSlots;
                break;
        }
        if(list!=null) {
            final String[] items = getStringArray(list);
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
            View view1=getLayoutInflater().inflate(R.layout.spinner_item, null);
            TextView mTitle=view1.findViewById(R.id.txt_item);
            mTitle.setText(title);
            builder.setCustomTitle(view1)
                    .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            fetchData(data_type,which);
                            data_type=0;


                        }
                    });
            builder.create().show();
        }
    }
     ArrayList<Hospital> out=new ArrayList<>();
    public void openDialog(View view) {
        if(hospitals!=null) {
            final String[] items = getStringArray(hospitals);
            final boolean[] selected =new boolean[items.length];
            ArrayList<Integer> list=new ArrayList<>();
            if(!out.isEmpty()){
                for(Hospital hospital:out){
                    if(hospitals.contains(hospital)){
                        list.add(hospitals.indexOf(hospital));
                    }
                }
            }
            for(int i=0;i<selected.length;i++){
                if(list.contains(i))
                    selected[i]=true;
                else
                   selected[i]=false;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
            View view1=getLayoutInflater().inflate(R.layout.spinner_item, null);
            TextView mTitle=view1.findViewById(R.id.txt_item);
            mTitle.setText("Select Hospitals");
            builder.setCustomTitle(view1)
                    .setMultiChoiceItems(items, selected, new DialogInterface.OnMultiChoiceClickListener() {
                        public void onClick(DialogInterface dialogInterface, int item, boolean b) {
                            Log.d("Myactivity", String.format("%s: %s", items[item], selected[item]));

                        }
                    });
            builder.setCancelable(true).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    out.clear();
                    for (int i=0;i<selected.length;i++){
                        if(selected[i]){
                            out.add(hospitals.get(i));
                            if(!out.contains(hospitals.get(i)))
                            out.add(hospitals.get(i));
                        }
                    }
                    if(out!=null&&out.size()>0) {
                        layout.setVisibility(View.VISIBLE);
                        tagsAdapter.setList(out);
                    }else {
                        layout.setVisibility(View.GONE);
                        tagsAdapter.setList(out);
                    }
                    dialog.dismiss();
                }
            });

            builder.create().show();
        }
    }

    private String[] getStringArray(List formatters) {
    String[] strings=new String[formatters.size()];
    for (int i=0;i<formatters.size();i++){
        strings[i]=((Formatter)formatters.get(i)).getValue();
    }
    return strings;
    }


    @Override
    public void onRemoveTag(Hospital s) {
       if(out.contains(s))
           out.remove(s);
        if(out.isEmpty()){
            layout.setVisibility(View.GONE);
        }
    }
}
