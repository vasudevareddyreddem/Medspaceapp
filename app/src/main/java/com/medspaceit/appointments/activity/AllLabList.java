package com.medspaceit.appointments.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.medspaceit.appointments.R;
import com.medspaceit.appointments.adapters.AllLabsAdapter;
import com.medspaceit.appointments.adapters.TestListAdapter;
import com.medspaceit.appointments.model.AllLabPJ;
import com.medspaceit.appointments.model.TestPJ;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllLabList extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.selected_test_rcview)
    RecyclerView selected_test_rcview;

    @BindView(R.id.finalLabRecyclerView)
    RecyclerView finalLabRecyclerView;

    ArrayList<AllLabPJ> allLabList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_lab_list);
        ButterKnife.bind(this);

        finalLabRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        selected_test_rcview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        if(isConnected()) {
            getAllTest();}
        else
        {
            showToast("No Internet");
        }

    }

    private void getAllTest() {
        showDialog();
        StringRequest stringRequest=new StringRequest("https://api.myjson.com/bins/jk4mw", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("language");
                    allLabList=new ArrayList();
                    hideDialog();
                    for (int i = 0; i <jsonArray.length() ; i++) {
                        JSONObject jb=jsonArray.getJSONObject(i);
                        String name=jb.getString("name");
                        String image=jb.getString("image");
                        String test=jb.getString("test");
                        String practice=jb.getString("practice");
                        AllLabPJ ap=new AllLabPJ(name,image,test,practice);
                        allLabList.add(ap);

                    }

                    AllLabsAdapter allLabsAdapter=new AllLabsAdapter(AllLabList.this,allLabList);
                    finalLabRecyclerView.setAdapter(allLabsAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
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

        }    }
}
