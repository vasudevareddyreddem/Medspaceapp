package com.medarogya.appointment.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medarogya.appointment.R;
import com.medarogya.appointment.activity.JobsListActivity;
import com.medarogya.appointment.activity.UploadResumeForJob;
import com.medarogya.appointment.model.JoblistPojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JobListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    JoblistPojo data;
    JobsListActivity context;
    String UID;
    List<JoblistPojo.List> joblist = new ArrayList<>();

    public JobListAdapter(JobsListActivity context, JoblistPojo data, String UID) {
        this.data = data;
        this.UID = UID;
        this.context = context;
      //  Collections.reverse(data.list);
        joblist.addAll(data.list);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.joblist_layout, parent, false);
        return new JobListViewHpolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof JobListViewHpolder) {
            JobListViewHpolder viewHpolder = (JobListViewHpolder) holder;
            viewHpolder.tv_joblist_category.setText(joblist.get(position).category);
            viewHpolder.tv_joblist_postby.setText("Posted by "+joblist.get(position).postedby+", ");
            viewHpolder.tv_joblist_df_time.setText(joblist.get(position).dfTime);
            viewHpolder.tv_joblist_title.setText(joblist.get(position).title);
           viewHpolder.tv_joblist_district.setText(joblist.get(position).district);
            viewHpolder.tv_joblist_exp.setText( joblist.get(position).experience);
           viewHpolder.tv_joblist_description.setText(joblist.get(position).description);
           viewHpolder.tv_joblist_quali.setText(joblist.get(position).qualifications);
           viewHpolder.btn_joblist_apply.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent=new Intent(context, UploadResumeForJob.class);
                   intent.putExtra("post_id",joblist.get(position).jPId);
                   context.startActivity(intent);

               }
           });

             }
    }

    @Override
    public int getItemCount() {
        return joblist.size();
    }

    private class JobListViewHpolder extends RecyclerView.ViewHolder {

        TextView tv_joblist_category, tv_joblist_postby, tv_joblist_df_time, tv_joblist_title, tv_joblist_district,
                tv_joblist_exp,tv_joblist_description,tv_joblist_quali;
        Button btn_joblist_apply;
        JobListViewHpolder(View view) {
            super(view);
            tv_joblist_category = view.findViewById(R.id.tv_joblist_category);
            tv_joblist_postby = view.findViewById(R.id.tv_joblist_postby);
            tv_joblist_df_time = view.findViewById(R.id.tv_joblist_df_time);
            tv_joblist_title = view.findViewById(R.id.tv_joblist_title);
            tv_joblist_district = view.findViewById(R.id.tv_joblist_district);
            tv_joblist_exp = view.findViewById(R.id.tv_joblist_exp);
            tv_joblist_description = view.findViewById(R.id.tv_joblist_description);
            tv_joblist_quali = view.findViewById(R.id.tv_joblist_quali);
            btn_joblist_apply = view.findViewById(R.id.btn_joblist_apply);

        }
    }
}
