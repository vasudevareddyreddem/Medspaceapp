package com.medspaceit.appointments.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;

import com.medspaceit.appointments.R;

public class FinalScreen extends Activity {
String name,phone,heaith_card_no;
TextView holder_name,phone_no;
EditText card_no,card_No;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_screen);
        Intent intent=getIntent();
        Bundle b=intent.getExtras();
        name=b.getString("name");

        phone=b.getString("phone");
        heaith_card_no=b.getString("heaith_card_no");


        holder_name=findViewById(R.id.holder_name);
        phone_no=findViewById(R.id.phone_no);
        card_no=findViewById(R.id.card_no);
        card_No=findViewById(R.id.card_No);


        card_no.addTextChangedListener(new PhoneNumberFormattingTextWatcher() {
            private boolean backspacingFlag = false;
            private boolean editedFlag = false;
            private int cursorComplement;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                cursorComplement = s.length() - card_no.getSelectionStart();
                if (count > after) {
                    backspacingFlag = true;
                } else {
                    backspacingFlag = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);

            }

            @Override
            public synchronized void afterTextChanged(Editable s) {


                String string = s.toString();
                String phone = string.replaceAll("[^\\d]", "");

                if (!editedFlag) {

                    //example: 999999999 <- 6+ digits already typed
                    // masked: (999) 999-999
                    if (phone.length() >= 12 && !backspacingFlag) {
                        editedFlag = true;
                        String ans = phone.substring(0, 4) + " " + phone.substring(4, 8) + " " + phone.substring(8, 12) ;
                        card_no.setText(ans);
                        card_no.setSelection(card_no.getText().length() - cursorComplement);

                    }
                } else {
                    editedFlag = false;
                }
            }
        });
        card_No.addTextChangedListener(new PhoneNumberFormattingTextWatcher() {
            private boolean backspacingFlag = false;
            private boolean editedFlag = false;
            private int cursorComplement;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                cursorComplement = s.length() - card_No.getSelectionStart();
                if (count > after) {
                    backspacingFlag = true;
                } else {
                    backspacingFlag = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);

            }

            @Override
            public synchronized void afterTextChanged(Editable s) {


                String string = s.toString();
                String phone = string.replaceAll("[^\\d]", "");

                if (!editedFlag) {

                    //example: 999999999 <- 6+ digits already typed
                    // masked: (999) 999-999
                    if (phone.length() >= 12 && !backspacingFlag) {
                        editedFlag = true;
                        String ans = phone.substring(0, 4) + " " + phone.substring(4, 8) + " " + phone.substring(8, 12) ;
                        card_No.setText(ans);
                        card_No.setSelection(card_No.getText().length() - cursorComplement);

                    }
                } else {
                    editedFlag = false;
                }
            }
        });


        card_no.setText(heaith_card_no);
        card_No.setText(heaith_card_no);
        holder_name.setText(name);
        phone_no.setText(phone);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        Get_Health_Card.ghc.finish();

    }
}
