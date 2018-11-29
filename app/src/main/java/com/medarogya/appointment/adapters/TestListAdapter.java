package com.medarogya.appointment.adapters;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.medarogya.appointment.R;
import com.medarogya.appointment.activity.SearchTestActivity;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.model.AllTestListForBook;
import com.medarogya.appointment.model.TestAddToCartPojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * Created by Bhupi on 13-Nov-18.
 */

public class TestListAdapter extends RecyclerView.Adapter<TestListAdapter.TestHolder> {
    SearchTestActivity mContext;
    List<AllTestListForBook.TestName> list = new ArrayList<>();
    List<AllTestListForBook.TestName> searchlist;
    String UID;
    public TestListAdapter(SearchTestActivity mContext, AllTestListForBook data, String UID) {
        this.UID = UID;
        this.mContext = mContext;
        searchlist = new ArrayList<>();
        searchlist.addAll(data.details.testNames);
        list.addAll(data.details.testNames);
        if (list.size() == 0) {
            Toast.makeText(mContext, "No Test List Found...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public TestListAdapter.TestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alltestlistlayout, parent, false);
        return new TestHolder(view);


    }


    @Override
    public void onBindViewHolder(@NonNull TestHolder holder, final int position) {


        holder.txt_test_name.setText(list.get(position).testName);

        holder.txt_test_type.setText("Type: " + list.get(position).testType);
        holder.txt_test_amount.setText("₹" + list.get(position).testAmount);
        holder.txt_test_time.setText("Reports In: " + list.get(position).testDuartion);


        holder.btn_test_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchTestActivity.tag_view_ll.setVisibility(View.VISIBLE);

                mContext.showDialog();

                String json = "";
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("a_u_id",UID);
                    jsonObject.put("l_id", list.get(position).lId);
                } catch (JSONException e) {
                    e.printStackTrace();

                }

                json = jsonObject.toString();
                JsonObjectRequest jsonObjReq = null;

                try {

                    jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                            ApiUrl.DIAGONOSTIC_BASE_URL + ApiUrl.test_addtocart, new JSONObject(json),
                            new com.android.volley.Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    mContext.getWindow().setSoftInputMode(
                                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                                    Log.e("Info ", " Respone" + response.toString());
                                    Gson gson = new Gson();
                                    mContext.hideDialog();
                                    TestAddToCartPojo data = gson.fromJson(response.toString(), TestAddToCartPojo.class);

                                        mContext.showToast(data.message);

                                }
                            }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            mContext.hideDialog();
                        }
                    });
                    RequestQueue queue = Volley.newRequestQueue(mContext);
                    queue.add(jsonObjReq);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
            }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TestHolder extends RecyclerView.ViewHolder {
        TextView txt_test_name, txt_test_type, txt_test_amount, txt_test_time;
        Button btn_test_book;

        public TestHolder(View itemView) {
            super(itemView);
            txt_test_name = itemView.findViewById(R.id.txt_test_name);
            txt_test_type = itemView.findViewById(R.id.txt_test_type);
            txt_test_amount = itemView.findViewById(R.id.txt_test_amount);
            txt_test_time = itemView.findViewById(R.id.txt_test_time);
            btn_test_book = itemView.findViewById(R.id.btn_test_book);


        }
    }


    public void filter(String charText) {
        SearchTestActivity.all_test_recycler_view.setVisibility(View.VISIBLE);

        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length() == 0) {

            list.addAll(searchlist);
        } else {
            for (AllTestListForBook.TestName wp : searchlist) {
                if (wp.testName.toLowerCase(Locale.getDefault()).contains(charText)) {
                    list.add(wp);
                }

            }
        }
        notifyDataSetChanged();
    }


}

