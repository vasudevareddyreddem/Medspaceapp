package com.medspaceit.appointments.adapters;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.medspaceit.appointments.R;
import com.medspaceit.appointments.model.DownloadReportPJ;

import java.util.List;

/**
 * Created by Bhupi on 19-Nov-18.
 */

public class MyReportDownloadAdapter extends BaseAdapter
{
    Context context;
    List<DownloadReportPJ> downloadReportList;
    public MyReportDownloadAdapter(Context context, List<DownloadReportPJ> downloadReportList) {
   this.context=context;
   this.downloadReportList=downloadReportList;
    }

    @Override
    public int getCount() {
        return downloadReportList.size();
    }

    @Override
    public Object getItem(int position) {
        return downloadReportList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.myreport_download_layout, parent, false);
        }
        TextView textViewItemName = (TextView)
                convertView.findViewById(R.id.downloadreport);

        textViewItemName.setText(downloadReportList.get(position).getDownloadreport());

        return convertView;
    }
}
