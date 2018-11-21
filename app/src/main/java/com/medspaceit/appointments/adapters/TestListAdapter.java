package com.medspaceit.appointments.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.medspaceit.appointments.R;
import com.medspaceit.appointments.activity.SearchTestActivity;
import com.medspaceit.appointments.model.AllTestListForBook;
import com.medspaceit.appointments.model.MyReportDownloadPoojo;
import com.medspaceit.appointments.model.TestPJ;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.medspaceit.appointments.activity.SearchTestActivity.tag_view_ll;


/**
 * Created by Bhupi on 13-Nov-18.
 */

public class TestListAdapter extends RecyclerView.Adapter<TestListAdapter.TestHolder> {
    Context mContext;
    List<AllTestListForBook.TestName> list = new ArrayList<>();
    List<AllTestListForBook.TestName> searchlist;

    public TestListAdapter(Context mContext, AllTestListForBook data) {
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
        holder.txt_test_type.setText("Type :" + list.get(position).testType);
        holder.txt_test_amount.setText("₹" + list.get(position).testAmount);
        holder.txt_test_time.setText("Time :" + list.get(position).testDuartion);


        holder.btn_test_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchTestActivity.tag_view_ll.setVisibility(View.VISIBLE);

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

