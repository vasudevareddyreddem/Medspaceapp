package com.medspaceit.appointments.model;

import com.android.volley.toolbox.StringRequest;

/**
 * Created by Bhupi on 19-Nov-18.
 */

public class DownloadReportPJ {
    String downloadreport;
    public DownloadReportPJ(String downloadreport) {
        this.downloadreport=downloadreport;
    }

    public String getDownloadreport() {
        return downloadreport;
    }

    public void setDownloadreport(String downloadreport) {
        this.downloadreport = downloadreport;
    }
}
