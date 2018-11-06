package com.medspaceit.appointments.utils;

import android.app.ProgressDialog;
import android.content.Context;


public class DialogsUtils {

    public static ProgressDialog showProgressDialog(Context context, String text){
         String message=text;
        ProgressDialog m_Dialog = new ProgressDialog(context);
        m_Dialog.setMessage(message);
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.show();
        return m_Dialog;
    }
}
