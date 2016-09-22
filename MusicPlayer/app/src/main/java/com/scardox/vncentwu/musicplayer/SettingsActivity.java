package com.scardox.vncentwu.musicplayer;

import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    static boolean loopSongList = true;
    static boolean loopCurrentSong = false;
    static int volume = 100;
    AudioManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);
        bar.setTitle("Settings");

        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        volume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.d("volume", "volume: "+ volume);

        SeekBar volumeBar = (SeekBar)findViewById(R.id.volumeBar);
        volumeBar.setMax(am.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeBar.setProgress(volume);

        Switch loopListSwitch = (Switch) findViewById(R.id.loopSongList);
        Switch loopSongSwitch = (Switch) findViewById(R.id.loopCurrentSong);
        loopListSwitch.setChecked(loopSongList);
        loopSongSwitch.setChecked(loopCurrentSong);



    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d("wefiojwef", "" + item.getItemId());
        switch (item.getItemId()) {
            case R.id.miCheck:
                // User chose the "Settings" item, show the app settings UI...
                Switch loopListSwitch = (Switch) findViewById(R.id.loopSongList);
                Switch loopSongSwitch = (Switch) findViewById(R.id.loopCurrentSong);
                SeekBar volumeBar = (SeekBar) findViewById(R.id.volumeBar);
                loopSongList = loopListSwitch.isChecked();
                loopCurrentSong = loopSongSwitch.isChecked();
                volume = volumeBar.getProgress();
                Log.d("volume", "volume: "+ volume);

                am.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);


                setResult(0);
                finish();

                return true;
            case R.id.miCancel:
                setResult(1);

                Log.d("???", "????");
                finish();

                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                Log.d("!!!!", "!!!!");
                return super.onOptionsItemSelected(item);

        }
    }

}
