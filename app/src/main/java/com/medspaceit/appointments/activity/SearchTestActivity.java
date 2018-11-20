package com.medspaceit.appointments.activity;

import android.content.Intent;
import android.support.annotation.ArrayRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.medspaceit.appointments.R;
import com.medspaceit.appointments.adapters.TagsAdapter;
import com.medspaceit.appointments.adapters.TestListAdapter;
import com.medspaceit.appointments.model.Hospital;
import com.medspaceit.appointments.model.SelectLabTestNamePJ;
import com.medspaceit.appointments.model.TestPJ;

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
    public static RecyclerView selected_tag_view;
    public static LinearLayout tag_view_ll;


    ArrayList<TestPJ> testList;
    TestListAdapter testListAdapter;


    public  static TagsAdapter tagsAdapter;
   public static ArrayList<String>tagsAdapterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_test);
        all_test_recycler_view=findViewById(R.id.all_test_recycler_view);
        selected_tag_view=findViewById(R.id.selected_tag_view);
        tag_view_ll=findViewById(R.id.tag_view_ll);
        all_test_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        selected_tag_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        btn_submit_test.setOnClickListener(this);
        searchbar.setOnClickListener(this);
        searchbar.setOnQueryTextListener(this);



        tagsAdapterList=new ArrayList<>();
        tagsAdapter=new TagsAdapter(SearchTestActivity.this,tagsAdapterList);
        selected_tag_view.setAdapter(tagsAdapter);

        tag_view_ll.setVisibility(View.VISIBLE);
        if(isConnected()) {
        getAllTest();}
        else
        {
            showToast("No Internet");
        }

    }

    private void getAllTest() {
        showDialog();
        StringRequest stringRequest=new StringRequest("https://api.myjson.com/bins/c3inq", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("tests");
                    testList=new ArrayList();
                    hideDialog();
                    for (int i = 0; i <jsonArray.length() ; i++) {
                        JSONObject jb=jsonArray.getJSONObject(i);
                        String testname=jb.getString("test_name");
                        TestPJ tp=new TestPJ(testname);
                        testList.add(tp);

                    }

                    testListAdapter=new TestListAdapter(SearchTestActivity.this,testList);
                    all_test_recycler_view.setAdapter(testListAdapter);



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
                case R.id.btn_submit_test:
                startActivity(new Intent(this,AllLabList.class));
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
        testListAdapter.filter(text1);
        return false;
    }


}