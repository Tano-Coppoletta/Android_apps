package com.example.a2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("A2 app", "Programmatic Receiver called into action.");
        boolean isHotel = intent.getBooleanExtra("isHotel",true);
        Intent intent1;
        if(isHotel){
            MainActivity.isHotel = true;
            intent1 = new Intent(context, HotelActivity.class);
        }else{
            MainActivity.isHotel=false;
            intent1 = new Intent(context, AttractionActivity.class);
        }
        context.startActivity(intent1);
    }
}
