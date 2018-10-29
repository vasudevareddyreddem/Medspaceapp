package com.medspaceit.appointment.adapters;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.medspaceit.appointment.R;
import com.medspaceit.appointment.activity.MyReportDownload;
import com.medspaceit.appointment.model.AcceptListPJ;
import com.medspaceit.appointment.model.DownloadListPJ;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import butterknife.internal.Utils;

/**
 * Created by Bhupi on 29-Oct-18.
 */

public class DownloadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ProgressDialog mProgressDialog;
    List<DownloadListPJ> downloadList;

    String path;
    public DownloadAdapter(Context context, List<DownloadListPJ> downloadList, String path) {
        this.downloadList=downloadList;
        this.context=context;
        this.path=path;
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
            //acceptholder.tv_download.setText(path+downloadList.get(position).getPrescription());
            acceptholder.tv_filename.setText(downloadList.get(position).getCreated_at());

            acceptholder.tv_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    String file=path+downloadList.get(position).getPrescription();


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
        return downloadList.size();
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


