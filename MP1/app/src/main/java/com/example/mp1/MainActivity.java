package com.example.mp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    protected Button insert; // the "Insert" button in the GUI
    protected Button check; // the "Check" button in the GUI
    public static final int REQUEST_CODE = 1;
    private boolean isNumberCorrect;
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find the buttons
        insert = (Button) findViewById(R.id.insert);
        check = (Button) findViewById(R.id.check);

        //attach listener to the buttons
        insert.setOnClickListener(insertListener);
        check.setOnClickListener(checkListener);
        //the check button is disabled because no number has been inserted yet
        check.setEnabled(false);
    }

    public View.OnClickListener insertListener = v -> {
        //switch to InsertPhoneNumber activity
        switchToInsertNumber();
    };

    public View.OnClickListener checkListener = v-> {
        if(isNumberCorrect) {
            Intent phoneDialer = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+number));
            startActivity(phoneDialer);
        }else {
            if(number!=null)
                Toast.makeText(MainActivity.this, "Inserted wrong number: "+ number,
                    Toast.LENGTH_LONG).show();
            else{
                Toast.makeText(MainActivity.this, "No number inserted",
                        Toast.LENGTH_LONG).show();
            }
        }
    };

    private void switchToInsertNumber(){
        Intent intent = new Intent(MainActivity.this,InsertPhoneNumber.class);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            //when we return from the InsertPhoneNumber activity we activate the check button
            check.setEnabled(true);
            //get the number
            number = data.getStringExtra("number");

            if (requestCode == REQUEST_CODE  && resultCode  == RESULT_OK) {
                isNumberCorrect=true;
            }else{
                isNumberCorrect=false;
            }
        } catch (Exception ex) {

            Log.e("ERROR: ",ex.toString());
        }


    }

}

