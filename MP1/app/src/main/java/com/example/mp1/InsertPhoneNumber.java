package com.example.mp1;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class InsertPhoneNumber extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_phone_number);

        EditText phoneNumber = findViewById(R.id.editPhone);

        //get the intent
        Intent i=getIntent();
        //set as canceled by default
        setResult(RESULT_CANCELED,i);
        phoneNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE || actionId==EditorInfo.IME_NULL ) {
                    //save the phone number,check it and return to MainActivity
                    String number=phoneNumber.getText().toString();

                    Log.v("NUMBER",number);
                    boolean correctFormat;
                    //if the number is empty I avoid doing the checks
                    if(number.length()!=0) {
                        i.putExtra("number", number);
                        Pattern p = Pattern.compile("[0-9]{10}|\\([0-9]{3}\\)\\s?[0-9]{3}-[0-9]{4}");
                        Matcher m = p.matcher(number);

                        correctFormat = m.matches();
                    }else{
                        correctFormat=false;
                    }
                    //REGEX
                    if(correctFormat) {
                        //set the result ok for MainActivity
                        setResult(RESULT_OK, i);
                    }else{
                        //set the result canceled for MainActivity
                        setResult(RESULT_CANCELED, i);
                    }
                    finish();
                    return true;
                }
                return true;
            }
        });

    }
}