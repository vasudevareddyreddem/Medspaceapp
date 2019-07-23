package com.medarogya.appointment.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.medarogya.appointment.apis.MyService;
import com.medarogya.appointment.apis.MyServiceDG;
import com.medarogya.appointment.apis.MyServiceNewLogin;
import com.medarogya.appointment.apis.MyServicePharmacy;
import com.medarogya.appointment.utils.DialogsUtils;
import com.medarogya.appointment.utils.NetworkConnection;
import com.medarogya.appointment.utils.SessionManager;

public abstract class BaseActivity extends Activity implements View.OnClickListener{
    private static final String TAG ="FireBase" ;
    SessionManager manager;
NetworkConnection connection;
MyService service;
MyServiceDG servicedg;
MyServicePharmacy serviceph;
MyServiceNewLogin newloginservice;
ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager=new SessionManager(this);
        connection=new NetworkConnection(this);
        service= MyService.Factory.create(getApplicationContext());
        servicedg= MyServiceDG.Factory.create(getApplicationContext());
        serviceph= MyServicePharmacy.Factorys.create(getApplicationContext());
        newloginservice= MyServiceNewLogin.Factoryes.create(getApplicationContext());
    }
    public void showDialog(){
        if(dialog==null){
            dialog=DialogsUtils.showProgressDialog(this,"Loading data..");
            return;
        }
        if(dialog.isShowing())
            return;
        dialog.show();

    }
    public void hideDialog(){
        if(dialog!=null&&dialog.isShowing())
        {
            dialog.dismiss();
            dialog=null;
        }
    }

    public boolean isConnected(){
        return connection.isConnectingToInternet();
    }
    public SessionManager getManager(){
        return manager;
    }
    public void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
    String token=null;
    public String getToken() {
        // Get token
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();
                        manager.storeToken(token);
                        Log.e("======  ", token);
                        // Log and toast
                        //  Toast.makeText(HomeActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });

        return token;
    }
}
