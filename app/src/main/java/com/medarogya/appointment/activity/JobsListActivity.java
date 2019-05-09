package com.medarogya.appointment.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.medarogya.appointment.R;
import com.medarogya.appointment.adapters.JobListAdapter;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.model.JoblistPojo;
import com.medarogya.appointment.utils.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JobsListActivity extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;
    String UID;
    @BindView(R.id.rcv_joblist)
    RecyclerView rcv_joblist;
    @BindView(R.id.btn_applied_list)
    Button btn_applied_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_list);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        btn_applied_list.setOnClickListener(this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_joblist.setLayoutManager(llm);
        UID = manager.getSingleField(SessionManager.KEY_ID);
        if (isConnected()) {
            showDialog();

            getJobList();

        } else {
            showToast("No Internet");

        }
    }

    private void getJobList() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                ApiUrl.PHARMACYUrl + ApiUrl.joblist,
                response -> {
            hideDialog();
            Gson gson=new Gson();
            JoblistPojo data=gson.fromJson(String.valueOf(response),JoblistPojo.class);
            if(data.status!=0) {
                JobListAdapter adapter = new JobListAdapter(JobsListActivity.this, data, UID);
                rcv_joblist.setAdapter(adapter);
            }
            else {
                showToast(data.message);
            }
        }, error -> {

        });

        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;
                case R.id.btn_applied_list:
                    Intent intent=new Intent(JobsListActivity.this,AppliedListActivity.class);
                    startActivity(intent);
                break;
        }

    }
}
