package com.medarogya.appointment.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.medarogya.appointment.R;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.model.TrackPojo;
import com.medarogya.appointment.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrackOrder extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.your_state_progress_bar_id4)
    StateProgressBar stateProgressBar;
    String phar_id, UID;
    String[] descriptionData = {"Accepted", "Packed", "Dispatched", "Delivered"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);
        ButterKnife.bind(this);

        back.setOnClickListener(this);

        Bundle b = getIntent().getExtras();
        phar_id = b.getString("phar_id");

        if (isConnected()) {
            showDialog();
            getTrackDetails();

        } else {
            showToast(getString(R.string.nointernet));
        }

    }

    private void getTrackDetails() {
        UID = manager.getSingleField(SessionManager.KEY_ID);
        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", UID);
            jsonObject.put("id", phar_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        json = jsonObject.toString();
        JsonObjectRequest jsonObjectRequest = null;
        try {
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ApiUrl.PHARMACYUrl + ApiUrl.order_track, new JSONObject(json), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    hideDialog();
                    Gson gson = new Gson();
                    TrackPojo data = gson.fromJson(response.toString(), TrackPojo.class);
                    if (data.status == 1) {

                        stateProgressBar.setStateDescriptionData(descriptionData);

                        stateProgressBar.setStateSize(25f);
                        stateProgressBar.setStateNumberTextSize(13f);
                        stateProgressBar.setStateLineThickness(8f);

                        stateProgressBar.enableAnimationToCurrentState(true);

                        stateProgressBar.setDescriptionTopSpaceIncrementer(8f);
                        stateProgressBar.setStateDescriptionSize(13f);

                        stateProgressBar.setJustifyMultilineDescription(true);
                        stateProgressBar.setDescriptionLinesSpacing(5f);
                        if (data.statusMsg.equals("0")) {
                            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);

                        } else if (data.statusMsg.equals("1")) {
                            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);

                        } else if (data.statusMsg.equals("2")) {
                            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);

                        } else if (data.statusMsg.equals("3")) {
                            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);

                        }
                    }
                    else {
                        showToast(data.message.toString());
                        stateProgressBar.setVisibility(View.GONE);
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                break;
        }
    }
}
