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
            acceptholder.tv_download.setText(path+downloadList.get(position).getPrescription());
            acceptholder.tv_filename.setText(downloadList.get(position).getCreated_at());

            acceptholder.tv_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    String file=path+downloadList.get(position).getPrescription();
// execute this when the downloader must be fired
//                    final DownloadTask downloadTask = new DownloadTask(context);
//                    downloadTask.execute(file);

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


    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream("/sdcard/file_name.extension");

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.dismiss();
            if (result != null)
                Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();
        }

    }
}


