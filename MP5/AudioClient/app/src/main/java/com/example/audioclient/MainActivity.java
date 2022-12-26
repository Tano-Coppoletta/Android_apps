package com.example.audioclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    protected static final String TAG = "AudioClient";
    static boolean mIsBound = false;
    private static MusicPlayerService mMusicPlayerService;
    private static Button startServiceButton;
    static Button playMusicButton;
    static Button stopMusicButton;
    static Button resumeMusicButton;
    static Button pauseMusicButton;
    static Button stopServiceButton;
    static boolean isStarted = false;
    private static Spinner spinner;
    private static final String END_OF_SONG = "edu.uic.cs478.fall22.endOfSong";

    private BroadcastReceiver myReceiver1;
    private IntentFilter myFilter1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up the broadcast receiver, used to know when a song finish
        myFilter1 = new IntentFilter(END_OF_SONG);
        myReceiver1 = new MyReceiver() ;
        registerReceiver(myReceiver1, myFilter1);


        spinner = findViewById(R.id.song_names);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.song_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //button used to start the service
        startServiceButton = findViewById(R.id.start_service_button);
        //set the onclicklistener for the start service button
        startServiceButton.setOnClickListener(new View.OnClickListener() {
            //start the service
            @Override
            public void onClick(View view) {

                if(!isStarted){
                   startService(getApplicationContext());
                }else{

                    Log.i(TAG,"SERVICE alredy running!");
                }
            }
        });

        playMusicButton = findViewById(R.id.play_button);
        stopMusicButton = findViewById(R.id.stop_button);
        resumeMusicButton = findViewById(R.id.resume_button);
        stopServiceButton = findViewById(R.id.stop_service_button);
        pauseMusicButton = findViewById(R.id.pause_button);

        //set the other button enabled or disabled based on the service, if started => enabled
        playMusicButton.setEnabled(isStarted);
        stopMusicButton.setEnabled(isStarted);
        resumeMusicButton.setEnabled(isStarted);
        stopMusicButton.setEnabled(isStarted);
        pauseMusicButton.setEnabled(isStarted);
        stopServiceButton.setEnabled(isStarted);

        //set the onClickListener for the buttons
        playMusicButton.setOnClickListener(new View.OnClickListener() {
            //when the user clicks play, bind the service, the music is started in onServiceConnected
            @Override
            public void onClick(View view) {
                try {
                    checkBindingAndBind();
                    stopMusicButton.setEnabled(true);
                    pauseMusicButton.setEnabled(true);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        stopMusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopMusicAndUnbind();
                if(mIsBound){
                    unbindService(mConnection);
                    mIsBound=false;
                }
            }
        });

        pauseMusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                mMusicPlayerService.pauseMusic();
                pauseMusicButton.setEnabled(false);
                resumeMusicButton.setEnabled(true);
                stopMusicButton.setEnabled(true);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        resumeMusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mMusicPlayerService.resumeMusic();
                    pauseMusicButton.setEnabled(true);
                    resumeMusicButton.setEnabled(false);

                } catch (RemoteException e) {
                e.printStackTrace();
            }
            }
        });

        stopServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                // Set the message show for the Alert time
                builder.setMessage("Do you want to stop the service?");

                // Set Alert Title
                builder.setTitle("Alert !");

                // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
                builder.setCancelable(false);

                // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    Intent i = new Intent(MusicPlayerService.class.getName());

                    ResolveInfo info = getPackageManager().resolveService(i, PackageManager.MATCH_ALL);

                    i.setComponent(new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name));

                    //unbind the service if it's bound
                    if(mIsBound){
                        try {
                            //stop the music
                            mMusicPlayerService.stopMusic();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        unbindService(mConnection);
                        mIsBound=false;
                    }
                    //disable the buttons
                    playMusicButton.setEnabled(false);
                    pauseMusicButton.setEnabled(false);
                    resumeMusicButton.setEnabled(false);
                    stopMusicButton.setEnabled(false);
                    stopServiceButton.setEnabled(false);
                    isStarted=false;
                    stopService(i);
                });

                // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    // If user click no then dialog box is canceled.
                    dialog.cancel();
                });

                // Create the Alert dialog
                AlertDialog alertDialog = builder.create();
                // Show the Alert Dialog box
                alertDialog.show();

            }
        });
    }

    protected static void stopMusicAndUnbind(){
        try {

            mMusicPlayerService.stopMusic();
            stopMusicButton.setEnabled(false);
            pauseMusicButton.setEnabled(false);
            resumeMusicButton.setEnabled(false);
            //unbind from the service

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    protected void checkBindingAndBind() throws RemoteException {
        //bind the service
        if(mIsBound){
            mMusicPlayerService.stopMusic();
            unbindService(mConnection);
            mIsBound=false;
        }

        boolean b ;
        Intent i = new Intent(MusicPlayerService.class.getName());

        ResolveInfo info = getPackageManager().resolveService(i, PackageManager.MATCH_ALL);

        i.setComponent(new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name));

        b = bindService(i, this.mConnection, Context.BIND_AUTO_CREATE);
        if (b) {
            Log.i(TAG, "bindService() succeeded!");
        } else {
            Log.i(TAG, "bindService() failed!");
        }

    }

    protected static void startService(Context ctx){
        Intent i = new Intent(MusicPlayerService.class.getName());

        ResolveInfo info = ctx.getPackageManager().resolveService(i, PackageManager.MATCH_ALL);

        i.setComponent(new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ctx.startForegroundService(i);
            isStarted=true;
            playMusicButton.setEnabled(true);

            stopServiceButton.setEnabled(true);
        }
    }


    static final ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder iservice) {

            mMusicPlayerService = MusicPlayerService.Stub.asInterface(iservice);
            mIsBound = true;
            try {
                if(!mMusicPlayerService.isStarted()) {
                    startService(startServiceButton.getContext());
                }

                mMusicPlayerService.startMusic(spinner.getSelectedItemPosition());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            //service killed (e.g by os)
            isStarted=false;
            playMusicButton.setEnabled(isStarted);
            stopMusicButton.setEnabled(isStarted);
            resumeMusicButton.setEnabled(isStarted);
            stopMusicButton.setEnabled(isStarted);
            pauseMusicButton.setEnabled(isStarted);
            stopServiceButton.setEnabled(isStarted);

            mMusicPlayerService = null;
            mIsBound = false;

        }
    };


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}