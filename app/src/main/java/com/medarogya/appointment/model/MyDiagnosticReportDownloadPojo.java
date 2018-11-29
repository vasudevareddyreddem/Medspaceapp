package com.medarogya.appointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Bhupi on 26-Nov-18.
 */

public class MyDiagnosticReportDownloadPojo {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("order_item_id")
    @Expose
    public String orderItemId;
    @SerializedName("reports")
    @Expose
    public List<Report> reports = null;
    @SerializedName("path")
    @Expose
    public String path;
    @SerializedName("message")
    @Expose
    public String message;

    public class Report {

        @SerializedName("test_name")
        @Expose
        public String testName;
        @SerializedName("o_p_t_id")
        @Expose
        public String oPTId;
        @SerializedName("report_file")
        @Expose
        public Object reportFile;

    }
    }
