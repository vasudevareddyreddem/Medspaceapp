package com.medspaceit.appointments.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.medspaceit.appointments.R;
import com.medspaceit.appointments.activity.SearchTestActivity;
import com.medspaceit.appointments.model.SelectLabTestNamePJ;
import com.medspaceit.appointments.model.TestPJ;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.medspaceit.appointments.activity.SearchTestActivity.selectedTestList;

/**
 * Created by Bhupi on 13-Nov-18.
 */

public class TestListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    ArrayList<TestPJ> testList;

    private List<TestPJ> namesList = null;
    TagsAdapter tagsAdapter;

    public TestListAdapter(Context mContext, ArrayList<TestPJ> namesList) {
        this.mContext = mContext;
        this.namesList = namesList;
        this.testList = new ArrayList();
        selectedTestList = new ArrayList();
        this.testList.addAll(namesList);

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alltestlistlayout, parent, false);
        return new TestHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {


        if (holder instanceof TestHolder) {
            TestHolder acceptholder = (TestHolder) holder;

            acceptholder.tst_cb.setText(namesList.get(position).getTestname());
            acceptholder.tst_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        SelectLabTestNamePJ pj = new SelectLabTestNamePJ(namesList.get(position).getTestname());
                        selectedTestList.add(pj);
                        tagsAdapter = new TagsAdapter((SearchTestActivity) mContext, selectedTestList);
                        SearchTestActivity.selected_tag_view.setAdapter(tagsAdapter);
                        tagsAdapter.notifyDataSetChanged();
                    } else {
                        if (selectedTestList.size() == 0) {
                            SearchTestActivity.tag_view_ll.setVisibility(View.GONE);
                        }

                        selectedTestList.remove(namesList.get(position).getTestname());
                        tagsAdapter = new TagsAdapter((SearchTestActivity) mContext, selectedTestList);
                        SearchTestActivity.selected_tag_view.setAdapter(tagsAdapter);
                        tagsAdapter.notifyDataSetChanged();
                    }






                    if (selectedTestList.size() != 0) {
                        SearchTestActivity.tag_view_ll.setVisibility(View.VISIBLE);
                    } else {
                        if (selectedTestList.size() == 0) {
                            SearchTestActivity.tag_view_ll.setVisibility(View.GONE);
                        }
                    }
                }
            });

            //Toast.makeText(mContext, ""+ SearchTestActivity.selectedTestList.size(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public int getItemCount() {
        return namesList.size();
    }

    public class TestHolder extends RecyclerView.ViewHolder {
        CheckBox tst_cb;

        public TestHolder(View itemView) {
            super(itemView);
            tst_cb = itemView.findViewById(R.id.tst_cb);


        }
    }


    public void filter(String charText) {
        SearchTestActivity.all_test_recycler_view.setVisibility(View.VISIBLE);

        charText = charText.toLowerCase(Locale.getDefault());
        namesList.clear();
        if (charText.length() == 0) {
            //SearchTestActivity.all_test_recycler_view.setVisibility(View.INVISIBLE);

            namesList.addAll(testList);
        } else {
            for (TestPJ wp : testList) {
                if (wp.getTestname().toLowerCase(Locale.getDefault()).contains(charText)) {
                    namesList.add(wp);
                }
//                else if (wp.videoName.toLowerCase(Locale.getDefault()).contains(charText)) {
//                    namesList.add(wp);
//                }
//                else if (wp.searchimagecity_name.toLowerCase(Locale.getDefault()).contains(charText)) {
//                    namesList.add(wp);
//                }
            }
        }
        notifyDataSetChanged();
    }


}

