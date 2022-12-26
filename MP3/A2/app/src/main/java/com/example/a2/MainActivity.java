package com.example.a2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static boolean isHotel;
    public static String[] attractionArray;
    public static String[] hotelArray;
    public static String[] hotelUrlArray;
    public static String[] attractionsUrlArray;


    private BroadcastReceiver myReceiver1;
    private IntentFilter myFilter1 ;
    private BroadcastReceiver myReceiver2;
    private IntentFilter myFilter2 ;


    private static final String PERMISSION =
            "edu.uic.cs478.fall22.mp3" ;

    private static final String HOTEL_INTENT = "edu.uic.cs478.fall22.hotel";
    private static final String ATTRACTIONS_INTENT = "edu.uic.cs478.fall22.attractions";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get the string arrays
        attractionArray = getResources().getStringArray(R.array.attractions);
        hotelArray = getResources().getStringArray(R.array.hotels);
        hotelUrlArray = getResources().getStringArray(R.array.hotel_urls);
        attractionsUrlArray = getResources().getStringArray(R.array.attaction_urls);

        //setup broadcast receivers
        myFilter1 = new IntentFilter(HOTEL_INTENT);
        myReceiver1 = new MyReceiver() ;
        registerReceiver(myReceiver1, myFilter1, PERMISSION, null);

        myFilter2 = new IntentFilter(ATTRACTIONS_INTENT);
        myReceiver2 = new MyReceiver() ;
        registerReceiver(myReceiver2, myFilter2, PERMISSION,null);

    }

    // Create Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("MENU","Created!");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    // Process clicks on Options Menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.hotels:
                isHotel=true;
                intent = new Intent(MainActivity.this,HotelActivity.class);
                startActivity(intent);
                return true;
            case R.id.attractions:
                isHotel=false;
                intent = new Intent(MainActivity.this, AttractionActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }



    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(myReceiver1);
        unregisterReceiver(myReceiver2);
    }
}