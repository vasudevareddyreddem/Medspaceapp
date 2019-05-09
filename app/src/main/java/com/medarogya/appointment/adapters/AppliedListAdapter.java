package com.medarogya.appointment.adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medarogya.appointment.R;
import com.medarogya.appointment.activity.AppliedListActivity;
import com.medarogya.appointment.model.AppliedJoblistPojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppliedListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    AppliedJoblistPojo data;
    AppliedListActivity context;
    String UID;
    List<AppliedJoblistPojo.List> appliedlist = new ArrayList<>();

    public AppliedListAdapter(AppliedListActivity context, AppliedJoblistPojo data, String UID) {
        this.data = data;
        this.UID = UID;
        this.context = context;
        appliedlist.addAll(data.list);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.appliedhistory_layout, parent, false);
        return new OPViewHpolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OPViewHpolder) {
            OPViewHpolder viewHpolder = (OPViewHpolder) holder;
            viewHpolder.tv_applieddate.setText(appliedlist.get(position).createdAt);
            viewHpolder.tv_dist.setText(appliedlist.get(position).district);
            viewHpolder.tv_appliedfor.setText(appliedlist.get(position).category);
            viewHpolder.tv_exp.setText(appliedlist.get(position).experience);
            viewHpolder.tv_quali.setText(appliedlist.get(position).qualifications);
            viewHpolder.tv_name.setText( appliedlist.get(position).name);
viewHpolder.btn_resume_download.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        DownloadResumes(data.path+"/"+data.list.get(position).resume);
    }
});

        }


    }

    @Override
    public int getItemCount() {
        return appliedlist.size();
    }

    private class OPViewHpolder extends RecyclerView.ViewHolder {

        TextView tv_applieddate, tv_dist, tv_appliedfor, tv_exp,tv_quali,tv_name;
        Button btn_resume_download;
        public OPViewHpolder(View view) {
            super(view);
            tv_applieddate = view.findViewById(R.id.tv_applieddate);
            tv_dist = view.findViewById(R.id.tv_dist);
            tv_appliedfor = view.findViewById(R.id.tv_appliedfor);
            tv_exp = view.findViewById(R.id.tv_exp);
            tv_quali = view.findViewById(R.id.tv_quali);
            tv_name = view.findViewById(R.id.tv_name);
            btn_resume_download = view.findViewById(R.id.btn_resume_download);

        }
    }
    private void DownloadResumes(String s) {

        String url = s;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        request.setTitle("Resume");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "name-of-the-file.ext");

        // get download service and enqueue file
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show();

    }

}
