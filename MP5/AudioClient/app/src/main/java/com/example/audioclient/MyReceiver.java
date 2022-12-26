package com.example.audioclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("AudioClient", "Programmatic Receiver called into action.");

        MainActivity.stopMusicAndUnbind();
        if (MainActivity.mIsBound) {
            context.unbindService(MainActivity.mConnection);
            MainActivity.mIsBound = false;
        }

    }
}
