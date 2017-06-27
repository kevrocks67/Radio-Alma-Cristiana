package com.radioalmacristiana.radioalmacristiana;

import android.content.BroadcastReceiver;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ShareActionProvider;

public class MainActivity extends Activity {


    Button startButton;
    Button stopButton;
    static Context context;
    boolean isPlaying;
    Intent streamService;
    SharedPreferences prefs;


  /*  // handler for received data from service
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MyIntentService.BROADCAST_ACTION_BAZ)) {
                final String param = intent.getStringExtra(EXTRA_PARAM_B);
                // do something
            }
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        getPrefs();
        streamService = new Intent(MainActivity.this, StreamService.class);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startService(streamService);
                startButton.setEnabled(false);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                stopService(streamService);
                startButton.setEnabled(true);

            }
        });
    }


    public void getPrefs() {
        isPlaying = prefs.getBoolean("isPlaying", false);
        if (isPlaying) startButton.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem shareItem = (MenuItem) menu.findItem(R.id.action_share);

        ShareActionProvider mShare = (ShareActionProvider)shareItem.getActionProvider();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Estoy escuchando a Radio Alma Cristiana. Descarga nuestra aplicacion(https://play.google.com/store/apps/details?id=com.radioalmacristiana&hl=en)");

        mShare.setShareIntent(shareIntent);
        return true;
    }

}
