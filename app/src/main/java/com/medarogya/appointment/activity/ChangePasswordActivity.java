package com.medarogya.appointment.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.medarogya.appointment.R;
import com.medarogya.appointment.apis.ApiUrl;
import com.medarogya.appointment.apis.MyService;
import com.medarogya.appointment.model.PswChange;
import com.medarogya.appointment.model.RegResult;
import com.medarogya.appointment.utils.MessageToast;
import com.medarogya.appointment.utils.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends BaseActivity {

    @BindView(R.id.old_psw)
    EditText edt_old_psw;

    @BindView(R.id.psw)
    EditText edt_psw;

    @BindView(R.id.conform_psw)
    EditText edt_conform_psw;

    @BindView(R.id.update_psw)
    Button btn_update;

    @BindView(R.id.back)
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        btn_update.setOnClickListener(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back(v);
            }
        });
    }

    private void ApiCall(final PswChange pswChange) {
        MyService service=MyService.Factory.create(getApplicationContext());
        Call<RegResult> call=service.pswChange(pswChange, ApiUrl.content_type);
        showDialog();
        call.enqueue(new Callback<RegResult>() {
            @Override
            public void onResponse(Call<RegResult> call, Response<RegResult> response) {
                   hideDialog();
                if(!response.isSuccessful())
                {
                    showToast("Server side error");
                    return;
                }

                RegResult result=response.body();
                MessageToast.showToastMethod(ChangePasswordActivity.this,result.getMessage());
                if(result.getStatus()==1)
                {
                    if (manager.isRemembered())
                        manager.setSingleField(SessionManager.KEY_PASSWORD,pswChange.getConfirmpassword());

                  finish();
                }


            }

            @Override
            public void onFailure(Call<RegResult> call, Throwable t) {
               hideDialog();
            }
        });



    }
    public void back(View view){
        finish();
    }
    @Override
    public void onClick(View v) {

        String oldpsw,psw,conformpsw;

        oldpsw=edt_old_psw.getText().toString();
        if(oldpsw==null&&oldpsw.isEmpty())
        {
            edt_old_psw.setError("fill this field");
            return;
        }
        psw=edt_psw.getText().toString();
        if(psw==null&&psw.isEmpty())
        {
            edt_psw.setError("fill this field");
            return;
        }
        conformpsw=edt_conform_psw.getText().toString();
        if(conformpsw==null&&conformpsw.isEmpty())
        {
            edt_conform_psw.setError("fill this field");
            return;
        }
        PswChange pswChange=new PswChange();
        pswChange.setAUId(manager.getSingleField(SessionManager.KEY_ID));
        pswChange.setOldpassword(edt_old_psw.getText().toString());
        pswChange.setPassword(edt_psw.getText().toString());
        pswChange.setConfirmpassword(edt_conform_psw.getText().toString());
        if(isConnected())
        {
            ApiCall(pswChange);
        }
        else
            MessageToast.showToastMethod(ChangePasswordActivity.this,"No Internet");

    }
}
