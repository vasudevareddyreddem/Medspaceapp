package com.medspaceit.appointments.model;

/**
 * Created by Bhupi on 29-Oct-18.
 */

public class DownloadListPJ {
    String prescription;
    String created_at;
    public DownloadListPJ(String prescription, String created_at) {
        this.created_at=created_at;
        this.prescription=prescription;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
