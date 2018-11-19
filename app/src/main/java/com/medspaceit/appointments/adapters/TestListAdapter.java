package com.medspaceit.appointments.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.medspaceit.appointments.R;
import com.medspaceit.appointments.activity.SearchTestActivity;
import com.medspaceit.appointments.model.SelectLabTestNamePJ;
import com.medspaceit.appointments.model.TestPJ;
import com.medspaceit.appointments.utils.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.internal.Utils;


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

        final int myPos= position;
        if (holder instanceof TestHolder) {
            TestHolder acceptholder = (TestHolder) holder;

            acceptholder.tst_cb.setText(namesList.get(position).getTestname());
            acceptholder.tst_cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox temp=(CheckBox)v;
                    if(temp.isChecked()){

                        SelectLabTestNamePJ pj = new SelectLabTestNamePJ(namesList.get(position).getTestname());
                        util.positions.add(pj);
                        Log.e("selectedTestList=====",""+util.positions.size()+"   "+namesList.get(position).getTestname());
                        tagsAdapter = new TagsAdapter((SearchTestActivity) mContext, util.positions);
                        SearchTestActivity.selected_tag_view.setAdapter(tagsAdapter);
                        tagsAdapter.notifyDataSetChanged();
                        if (util.positions.size() != 0) {
                            SearchTestActivity.tag_view_ll.setVisibility(View.VISIBLE);
                        } else {
                            if (util.positions.size() == 0) {
                                SearchTestActivity.tag_view_ll.setVisibility(View.GONE);
                            }}
                    }
                    else{
                        try {
                            util.positions.remove(myPos);

                        Log.e("selectedTestList==rm===",""+util.positions.size()+"   "+namesList.get(position).getTestname());


                        if (util.positions.size() == 0) {
                            util.positions.clear();
                            SearchTestActivity.tag_view_ll.setVisibility(View.GONE);
                        }


                            tagsAdapter = new TagsAdapter((SearchTestActivity) mContext, util.positions);
                        SearchTestActivity.selected_tag_view.setAdapter(tagsAdapter);
                        tagsAdapter.notifyDataSetChanged();
                        }catch (Exception e)
                        {Log.e("e.print",e.getMessage());}
                        if (util.positions.size() != 0) {
                        SearchTestActivity.tag_view_ll.setVisibility(View.VISIBLE);
                    } else {
                        if (util.positions.size() == 0) {
                            util.positions.clear();
                            SearchTestActivity.tag_view_ll.setVisibility(View.GONE);
                        }
                    }
                    }
                }
            });
//            acceptholder.tst_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                    if (isChecked) {
//                        SelectLabTestNamePJ pj = new SelectLabTestNamePJ(namesList.get(position).getTestname());
//                        selectedTestList.add(pj);
//                        Log.e("selectedTestList=====",""+selectedTestList.size()+"   "+namesList.get(position).getTestname());
//                        tagsAdapter = new TagsAdapter((SearchTestActivity) mContext, selectedTestList);
//                        SearchTestActivity.selected_tag_view.setAdapter(tagsAdapter);
//                        tagsAdapter.notifyDataSetChanged();
//                    } else {
//                        try{
//                            selectedTestList.remove(namesList.get(position).getTestname());
//                            Log.e("selectedTestList==rm===",""+selectedTestList.size()+"   "+namesList.get(position).getTestname());
//
//                            if (selectedTestList.size() == 0) {
//                            SearchTestActivity.tag_view_ll.setVisibility(View.GONE);
//                        }
//
//
//                            tagsAdapter = new TagsAdapter((SearchTestActivity) mContext, selectedTestList);
//                        SearchTestActivity.selected_tag_view.setAdapter(tagsAdapter);
//                        tagsAdapter.notifyDataSetChanged();
//                    }catch (Exception e)
//                        {}}
//
//
//
//                    if (selectedTestList.size() != 0) {
//                        SearchTestActivity.tag_view_ll.setVisibility(View.VISIBLE);
//                    } else {
//                        if (selectedTestList.size() == 0) {
//                            SearchTestActivity.tag_view_ll.setVisibility(View.GONE);
//                        }
//                    }
//                }
//            });

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

            }
        }
        notifyDataSetChanged();
    }


}

