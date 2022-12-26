package com.example.clipserver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.audioclient.MusicPlayerService;

import java.util.ArrayList;
import java.util.Arrays;

public class MusicPlayer extends Service {
    private final String TAG = "MusicPlayerService";
    private static final int NOTIFICATION_ID = 1;
    private MediaPlayer mPlayer;
    private boolean isStarted = false;
    ArrayList<Integer> songs = new ArrayList<>(Arrays.asList( R.raw.track1, R.raw.track2, R.raw.track3, R.raw.track4, R.raw.track5));
    private static final String CHANNEL_ID = "Music player style" ;
    private static final String END_OF_SONG = "edu.uic.cs478.fall22.endOfSong";

    @Override
    public void onCreate() {

        super.onCreate();
        Log.i(TAG, "Servive was created!") ;
        //create a notification that the user can use to get back to the client
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.createNotificationChannel();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        CharSequence name = "Music player notification";
        String description = "The channel for music player notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel ;
            channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification ;

        final Intent notificationIntent = new Intent(getApplicationContext(),
                MusicPlayer.class);

        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_IMMUTABLE) ;

        notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setOngoing(true).setContentTitle("Music Playing")
                .setContentText("Click to Access Music Player")
                .setTicker("Music is playing!")
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_launcher_background, "Show service", pendingIntent)
                .build();


        // Put this Service in a foreground state, so it won't
        // readily be killed by the system
        startForeground(NOTIFICATION_ID, notification);
        // Don't automatically restart this Service if it is killed
        isStarted=true;
        return START_NOT_STICKY;
    }

    private final MusicPlayerService.Stub mBinder = new MusicPlayerService.Stub() {

        @Override
        public void startMusic(int id) {
            if(mPlayer!=null){
                mPlayer=null;
            }
            mPlayer = MediaPlayer.create(MusicPlayer.this, songs.get(id));
            mPlayer.setLooping(false);
            mPlayer.start();

            // Stop Service when music has finished playing
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {

                    //send a broadcast to the client so that he can unbind
                    Intent aIntent = new Intent(END_OF_SONG);
                    sendBroadcast(aIntent);
                }
            });
        }

        @Override
        public void stopMusic(){
            mPlayer.stop();
            mPlayer=null;
        }
        @Override
        public void pauseMusic(){
            mPlayer.pause();
        }
        @Override
        public void resumeMusic(){
            mPlayer.start();
        }
        @Override
        public boolean isStarted(){
            return isStarted;
        }


    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {

        if (null != mPlayer) {

            mPlayer.stop();
            mPlayer.release();

        }
    }
}
