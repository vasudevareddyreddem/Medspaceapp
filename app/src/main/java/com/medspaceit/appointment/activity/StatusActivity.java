package com.medspaceit.appointment.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.medspaceit.appointment.R;
import com.medspaceit.appointment.adapters.StatusAdapter;
import com.medspaceit.appointment.apis.ApiUrl;
import com.medspaceit.appointment.model.AppStatus;
import com.medspaceit.appointment.model.RegResult;
import com.medspaceit.appointment.model.StatusData;
import com.medspaceit.appointment.utils.SessionManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusActivity extends BaseActivity implements StatusAdapter.StatusAction {
    public final static int stausListApi = 5;
    public final static int historyListApi = 10;
    @BindView(R.id.status_list)
    RecyclerView statusList;
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.activity_title)
    TextView title;

    List<AppStatus> appStatuses;

    StatusAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        statusList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        if (getIntent().hasExtra("title")) {
            title.setText(getIntent().getStringExtra("title"));
            fetchAppList(historyListApi);
        } else {

            fetchAppList(stausListApi);
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    private void fetchAppList(final int types) {
        JsonObject object = new JsonObject();
        object.addProperty("a_u_id", manager.getSingleField(SessionManager.KEY_ID));
        Call<StatusData> call = null;
        if (types == stausListApi) {
            call = service.appList(object, ApiUrl.content_type);
        } else if (types == historyListApi) {
            call = service.appHistoryList(object, ApiUrl.content_type);
        }
        showDialog();


        call.enqueue(new Callback<StatusData>() {
            @Override
            public void onResponse(Call<StatusData> call, Response<StatusData> response) {
                hideDialog();
                StatusData data = response.body();

                if (data != null) {


                    if (data.getStatus() == 1) {
                        appStatuses = data.getList();
                        HashMap<String, ArrayList<AppStatus>> map = new HashMap<String, ArrayList<AppStatus>>();
                        for (AppStatus status : appStatuses) {
                            String key = status.getDate().trim();
                            if (!map.containsKey(key)) {
                                ArrayList<AppStatus> statuses = new ArrayList<AppStatus>();
                                map.put(key, statuses);
                            }
                            map.get(key).add(status);
                        }
                        Set<String> sortedKeys = new TreeSet<String>(new Comparator<String>() {
                            @Override
                            public int compare(String o1, String o2) {
                                return o2.compareTo(o1);
                            }
                        });
                        sortedKeys.addAll(map.keySet());
                        ArrayList<AppStatus> a = new ArrayList<AppStatus>();
                        for (String key : sortedKeys) {
                            AppStatus status = new AppStatus();
                            status.setDate(key);
                            status.setView_type(12);
                            a.add(status);
                            a.addAll(map.get(key));
                        }
                        appStatuses = a;
                        adapter = new StatusAdapter(appStatuses, types);
                        adapter.setAction((StatusAdapter.StatusAction) StatusActivity.this);
                        statusList.setAdapter(adapter);
                    } else {
                        showToast(data.getMessage());
                        appStatuses = new ArrayList<AppStatus>();
                        adapter = new StatusAdapter(appStatuses, types);
                        statusList.setAdapter(new StatusAdapter(appStatuses, types));
                    }
                }

            }

            @Override
            public void onFailure(Call<StatusData> call, Throwable t) {
                hideDialog();
            }

        });
    }

    @Override
    public void accept(AppStatus status) {
        apiAccept(status, "1");

    }

    @Override
    public void reject(AppStatus status) {
        apiAccept(status, "0");

    }

    private void apiAccept(AppStatus status, String s) {
        if (isConnected()) {

            JsonObject object = new JsonObject();
            object.addProperty("a_u_id", manager.getSingleField(SessionManager.KEY_ID));
            object.addProperty("b_id", status.getBId());
            object.addProperty("status", s);
            Call<RegResult> call = service.accept(object, ApiUrl.content_type);
            showDialog();
            call.enqueue(new Callback<RegResult>() {
                @Override
                public void onResponse(Call<RegResult> call, Response<RegResult> response) {
                    hideDialog();
                    RegResult result = response.body();
                    showToast(result.getMessage());
                }

                @Override
                public void onFailure(Call<RegResult> call, Throwable t) {
                    hideDialog();
                }
            });

        } else {
            showToast("no Internet");
        }

    }


}
