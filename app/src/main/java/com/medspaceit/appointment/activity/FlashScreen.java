package com.medspaceit.appointment.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.medspaceit.appointment.R;
import com.medspaceit.appointment.utils.MessageToast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlashScreen extends BaseActivity {
    String TAG=this.getClass().getName();

    @BindView(R.id.tryagain)
    TextView triagain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_flash_screen);
        ButterKnife.bind(this);
        triagain.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.notifications_admin_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        // Handle possible data accompanying notification message.
        // [START handle_data_extras]



        startAhandler();


    }

    private boolean isFromFirbase(){
        if (getIntent().getExtras() != null)
            if(getIntent().getExtras().containsKey("from"))
                return true;

        return false;
    }
    private void startAhandler() {
        showDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideDialog();
                if(!isConnected()){
                    MessageToast.showToastMethod(FlashScreen.this,"No Internet");
                       triagain.setVisibility(View.VISIBLE);
                    return;
                }

                triagain.setVisibility(View.GONE);
                if(manager.isLoggedIn())
                {
                   if(isFromFirbase()){
                       startActivity(new Intent(FlashScreen.this,StatusActivity.class));
                   } else {
                       startActivity(new Intent(FlashScreen.this,HomeActivity.class));
                   }

                }
                else
                    startActivity(new Intent(FlashScreen.this,SignInActivity.class));

                finish();
            }
        },2000);

    }

    @Override
    public void onClick(View v) {
        startAhandler();
    }


}
