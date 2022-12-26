package com.example.a1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button hotelsButton;
    private Button attractionsButton;
        private boolean isHotel;
        private static final String HOTEL_INTENT = "edu.uic.cs478.fall22.hotel";
        private static final String ATTRACTIONS_INTENT = "edu.uic.cs478.fall22.attractions";
        private static final String PERMISSION =
                "edu.uic.cs478.fall22.mp3" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hotelsButton = findViewById(R.id.hotels_button);
        attractionsButton = findViewById(R.id.attractions_button);

        hotelsButton.setOnClickListener(
                (view) ->  {
                    isHotel = true;
                    Toast.makeText(MainActivity.this, "Hotels clicked!", Toast.LENGTH_LONG).show() ;
                    checkPermissionAndBroadcast(isHotel);
                } );
        attractionsButton.setOnClickListener(
                (view -> {
                    isHotel = false;
                    Toast.makeText(MainActivity.this, "Attractions clicked!", Toast.LENGTH_LONG).show() ;
                    checkPermissionAndBroadcast(isHotel);
                })
        );
    }

    private void checkPermissionAndBroadcast(boolean isHotel) {
        Intent aIntent;
        if (ActivityCompat.checkSelfPermission(this, PERMISSION)
                == PackageManager.PERMISSION_GRANTED) {
            if(isHotel) {
                aIntent = new Intent(HOTEL_INTENT);
            }else{
                aIntent = new Intent(ATTRACTIONS_INTENT);
            }
            aIntent.putExtra("isHotel",isHotel);
            sendBroadcast(aIntent); ;

        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{PERMISSION}, 0);
        }

    }

    public void onRequestPermissionsResult(int code, @NonNull String[] permissions, @NonNull int[] results) {
        super.onRequestPermissionsResult(code, permissions, results);
        Intent aIntent;
        if (results.length > 0) {
            if (results[0] == PackageManager.PERMISSION_GRANTED) {
                if(isHotel) {
                    aIntent = new Intent(HOTEL_INTENT);
                }else{
                    aIntent = new Intent(ATTRACTIONS_INTENT);
                }
                aIntent.putExtra("isHotel",isHotel);
                sendOrderedBroadcast(aIntent, PERMISSION);
            } else {
                Toast.makeText(this, "No permission", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}