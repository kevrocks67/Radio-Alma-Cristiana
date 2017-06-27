package com.radioalmacristiana.radioalmacristiana;

/**
 * Created by jaime7571 on 2/14/2016.
 */
import java.io.IOException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;

public class StreamService extends Service  {
    private static final String TAG = "StreamService";
    MediaPlayer mp;
    boolean isPlaying;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Notification n;
    NotificationManager notificationManager;
    // Change this int to some number specifically for this app
    int notifId = 7571;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");



        // Set up the buffering notification
        notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(NOTIFICATION_SERVICE);
        Context context = getApplicationContext();

        String notifMessage = context.getResources().getString(R.string.buffering);

        Intent nIntent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, nIntent, 0);

        // Init the SharedPreferences and Editor
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = prefs.edit();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Radio Alma Cristiana")
                        .setContentText(notifMessage);
// Sets an ID for the notification
        int mNotificationId = 001;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());





        // It's very important that you put the IP/URL of your ShoutCast stream here

        String url = "http://zenorad.io:10830/";
        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mp.setDataSource(url);
            mp.prepare();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "SecurityException");
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "IllegalStateException");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "IOException");
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        Log.d(TAG, "onStart");
        mp.start();
        // Set the isPlaying preference to true
        editor.putBoolean("isPlaying", true);
        editor.commit();

        Context context = getApplicationContext();
        String notifMessage = context.getResources().getString(R.string.now_playing);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Radio Alma Cristiana")
                        .setContentText(notifMessage);

        //  n.flags = Notification.FLAG_NO_CLEAR;

        Intent nIntent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, nIntent, 0);


    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        mp.stop();
        mp.release();
        mp = null;
        editor.putBoolean("isPlaying", false);
        editor.commit();
        notificationManager.cancel(001);
    }


}
