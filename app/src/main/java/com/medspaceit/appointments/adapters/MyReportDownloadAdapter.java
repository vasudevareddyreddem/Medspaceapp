package com.medspaceit.appointments.adapters;

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
import android.widget.TextView;
import android.widget.Toast;

import com.medspaceit.appointments.R;
import com.medspaceit.appointments.activity.MyDiagnosticReportDownload;
import com.medspaceit.appointments.model.MyDiagnosticReportDownloadPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bhupi on 19-Nov-18.
 */

public class MyReportDownloadAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    MyDiagnosticReportDownload context;
        List<MyDiagnosticReportDownloadPojo.Report> downloadReportList=new ArrayList<>();
    MyDiagnosticReportDownloadPojo reportData;
    public MyReportDownloadAdapter(MyDiagnosticReportDownload context, MyDiagnosticReportDownloadPojo reportData) {
    this.context=context;
    this.reportData=reportData;
    downloadReportList.addAll(reportData.reports);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.download_list_card, parent, false);
        return new DownloadHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {


        if (holder instanceof DownloadHolder){
            DownloadHolder acceptholder= (DownloadHolder) holder;
            //acceptholder.tv_download.setText(path+downloadReportList.get(position).getPrescription());
            acceptholder.tv_filename.setText(downloadReportList.get(position).testName);
if(downloadReportList.get(position).reportFile.equals("")||downloadReportList.get(position).reportFile.equals("null"))
{
    acceptholder.tv_download.setVisibility(View.INVISIBLE);
}

acceptholder.tv_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    String file=reportData.path+downloadReportList.get(position).reportFile;


                    String url = file;
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.setDescription("Some descrition");
                    request.setTitle("Your Report");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    }
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "name-of-the-file.ext");

                    // get download service and enqueue file
                    DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    manager.enqueue(request);
                    Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show();


                }});}}



    @Override
    public int getItemCount() {
        return downloadReportList.size();
    }

    public class DownloadHolder extends RecyclerView.ViewHolder{
        TextView tv_download,tv_filename;
        public DownloadHolder(View itemView) {
            super(itemView);
            tv_download= itemView.findViewById(R.id.tv_download);
            tv_filename= itemView.findViewById(R.id.tv_filename);


        }
    }
}


