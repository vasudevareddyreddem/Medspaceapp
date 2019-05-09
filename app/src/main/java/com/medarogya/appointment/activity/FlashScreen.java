package com.medarogya.appointment.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medarogya.appointment.R;
import com.medarogya.appointment.model.Login;
import com.medarogya.appointment.utils.MessageToast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlashScreen extends BaseActivity {


    @BindView(R.id.tryagain)
    TextView triagain;
    @BindView(R.id.ll_no_internet)
    LinearLayout ll_no_internet;
    @BindView(R.id.txt_no_internet)
    TextView txt_no_internet;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.imageView)
    ImageView imageview;

    Handler handler = null;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_flash_screen);
        ButterKnife.bind(this);
        triagain.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
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

    private boolean isFromFirbase() {
        if (getIntent().getExtras() != null)
            if (getIntent().getExtras().containsKey("from"))
                return true;

        return false;
    }

    private void startAhandler() {

        showAnim();
    }

    private void showAnim() {

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageview.setVisibility(View.VISIBLE);
                animation = AnimationUtils.loadAnimation(FlashScreen.this, R.anim.toptocenter);
                imageview.startAnimation(animation);


                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textView.setVisibility(View.VISIBLE);
                        animation = AnimationUtils.loadAnimation(FlashScreen.this, R.anim.fade_in);
                        textView.startAnimation(animation);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if(!isConnected()){
                                    ll_no_internet.setVisibility(View.VISIBLE);
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
                        }, 1500);
                    }
                }, 1000);

            }
        }, 100);


    }

    @Override
    public void onClick(View v) {
        if(!isConnected()){
            triagain.setVisibility(View.VISIBLE);
            animation = AnimationUtils.loadAnimation(FlashScreen.this, R.anim.shrink);
            txt_no_internet.startAnimation(animation);
            return;
        }

        ll_no_internet.setVisibility(View.GONE);
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


}
