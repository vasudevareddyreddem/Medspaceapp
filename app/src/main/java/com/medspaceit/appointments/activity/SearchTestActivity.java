package com.medspaceit.appointments.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.ArrayRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.medspaceit.appointments.R;
import com.medspaceit.appointments.adapters.TagsAdapter;
import com.medspaceit.appointments.adapters.TestListAdapter;
import com.medspaceit.appointments.apis.ApiUrl;
import com.medspaceit.appointments.model.Hospital;
import com.medspaceit.appointments.model.AllTestListForBook;
import com.medspaceit.appointments.model.SelectLabTestNamePJ;
import com.medspaceit.appointments.model.TestPJ;
import com.medspaceit.appointments.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SearchTestActivity extends BaseActivity implements SearchView.OnQueryTextListener {
    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.searchbar)
    SearchView searchbar;

    @BindView(R.id.btn_submit_test)
    Button btn_submit_test;

    public static RecyclerView all_test_recycler_view;
    public static LinearLayout tag_view_ll;


    TestListAdapter testListAdapter;

    String a_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_test);
        ButterKnife.bind(this);

        Bundle b=getIntent().getExtras();
        a_id=b.getString("a_id");

        all_test_recycler_view=findViewById(R.id.all_test_recycler_view);
        tag_view_ll=findViewById(R.id.tag_view_ll);
        all_test_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        back.setOnClickListener(this);
        btn_submit_test.setOnClickListener(this);
        searchbar.setOnClickListener(this);
        searchbar.setOnQueryTextListener(this);


        if(isConnected()) {
        getAllTest();}
        else
        {
            showToast(getString(R.string.nointernet));
        }

    }

    private void getAllTest() {
        showDialog();
        String json = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("a_id", a_id);
        } catch (JSONException e) {
            e.printStackTrace();

        }

        json = jsonObject.toString();
        JsonObjectRequest jsonObjReq = null;

        try {

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    ApiUrl.DIAGONOSTIC_BASE_URL + ApiUrl.test_list, new JSONObject(json),
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                           // Log.e("Info ", " Respone" + response.toString());
                            Gson gson = new Gson();
                            hideDialog();

                            AllTestListForBook data = gson.fromJson(response.toString(), AllTestListForBook.class);
                            if (data.status == 1) {
                                String UID= manager.getSingleField(SessionManager.KEY_ID);
                                testListAdapter=new TestListAdapter(SearchTestActivity.this,data,UID);
                                all_test_recycler_view.setAdapter(testListAdapter);
                            } else {
                                showToast(data.message);
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideDialog();
                    Log.e("Info", " Error " + error.getMessage());
                }
            });
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjReq);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Info ", "Error  try " + e.getMessage());
        }
    }




    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;
                case R.id.btn_submit_test:
                startActivity(new Intent(this,Cart.class));
                break;

        }
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text1 = newText;
        try {
            testListAdapter.filter(text1);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }


}