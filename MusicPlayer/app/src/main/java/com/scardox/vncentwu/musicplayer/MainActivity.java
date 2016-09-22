package com.scardox.vncentwu.musicplayer;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    String[] items;
    int[] songs;
    int currentPosition = 0;
    MediaPlayer mp;
    TextView nowSong;
    TextView nextSong;
    boolean playing = true;
    int length = 0;
    ImageButton pauseButton;
    Timer timer;
    TextView elapsedText;
    TextView remainingText;
    SeekBar slider;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        items = getResources().getStringArray(R.array.song_names);
        songs = GetSongResources.getSongResources();



        ArrayAdapter<String> songsAdapter = new ArrayAdapter<String>(this, R.layout.song_item_layout, items);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(songsAdapter);
        int[] colors = {0, 0xFFFF0000, 0}; // red for the example
        listView.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        listView.setDividerHeight(1);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for(int i = 0; i < items.length; i++)
                {
                    View xd = parent.getChildAt(i);
                    if(xd != null)
                        xd.setBackgroundResource(R.color.transparent);
                }
                parent.getChildAt(position).setBackgroundResource(R.color.red);
                currentPosition = position;
                updateSongText();
                playCurrentSong();
                playing = true;
                pauseButton.setImageResource(R.drawable.pause);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);
        bar.setTitle("Scardox Media Player");

        mp = MediaPlayer.create(this, songs[0]);

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp)
        {


            Log.d("wefwe", "Loop list: " + SettingsActivity.loopSongList);
            if(SettingsActivity.loopSongList && currentPosition==songs.length-1)
            {
                currentPosition = 0;
                Log.d("wfew", "going back");
            }
            else if(!SettingsActivity.loopCurrentSong)
                currentPosition++;

            if(currentPosition < songs.length)
                playCurrentSong();
            updateSongText();

        }});

        nowSong = (TextView) findViewById(R.id.nowSongText);
        nextSong = (TextView) findViewById(R.id.nextSongText);
        elapsedText = (TextView) findViewById(R.id.elapsedTime);
        remainingText = (TextView) findViewById(R.id.remainingTime);
        slider = (SeekBar)findViewById(R.id.seekBar);
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                mp.seekTo(slider.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {



            }
        });

        pauseButton = (ImageButton) findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePause();

            }
        });
       ImageButton prevButton = (ImageButton) findViewById(R.id.reverseButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition--;
                if(currentPosition<0)
                    currentPosition = songs.length - 1;
                playCurrentSong();
                updateSongText();
                playing = true;
                pauseButton.setImageResource(R.drawable.pause);

            }
        });
        ImageButton nextButton = (ImageButton) findViewById(R.id.forwardButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition++;
                if(currentPosition>=songs.length)
                    currentPosition = 0;
                playCurrentSong();
                updateSongText();
                playing = true;
                pauseButton.setImageResource(R.drawable.pause);

            }
        });

        playCurrentSong();
        updateSongText();
        getViewByPosition(0, listView).setBackgroundResource(R.color.red);
        //bar.setTitle("Scardox Audio Player");


    }

    //http://stackoverflow.com/questions/24811536/android-listview-get-item-view-by-position
    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d("wefiojwef", "" + item.getItemId());
        switch (item.getItemId()) {
            case R.id.miSettings:
                // User chose the "Settings" item, show the app settings UI...

                Intent intent = new Intent(this, SettingsActivity.class);



                startActivityForResult(intent, 0);

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void togglePause()
    {
        playing = !playing;
        if(playing)
        {
            int length = mp.getCurrentPosition();
            mp.seekTo(length);
            mp.start();
            pauseButton.setImageResource(R.drawable.pause);
        }
        else{
            mp.pause();
            pauseButton.setImageResource(R.drawable.play);
        }

    }


    public void playCurrentSong()
    {
        mp.reset();
        AssetFileDescriptor afd = this.getResources().openRawResourceFd(songs[currentPosition]);
        try {
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.start();

        slider.setMax(mp.getDuration());
        slider.setProgress(0);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mp != null && mp.isPlaying()) {
                            elapsedText.post(new Runnable() {
                                @Override
                                public void run() {
                                    int milli = mp.getCurrentPosition();
                                    slider.setProgress(milli);
                                    int seconds = milli/1000;
                                    int minutes = seconds/60;
                                    seconds = seconds % 60;
                                    elapsedText.setText(String.format("%02d:%02d", minutes, seconds));
                                }
                            });
                            remainingText.post(new Runnable() {
                                @Override
                                public void run() {
                                    int milli = mp.getDuration() - mp.getCurrentPosition();
                                    int seconds = milli/1000;
                                    int minutes = seconds/60;
                                    seconds = seconds % 60;
                                    remainingText.setText(String.format("%02d:%02d", minutes, seconds));
                                }
                            });
                        } else {
                            timer.cancel();
                            timer.purge();
                        }
                    }
                });
            }
        }, 0, 1000);


    }

    public void updateSongText()
    {
        String tempString = "";

        if(currentPosition >= items.length){
            nextSong.setText("");
            nowSong.setText("");
            return;
        }

        if(items[currentPosition].length() > 13)
            nowSong.setText(items[currentPosition].substring(0, 13) + "..");
        else
            nowSong.setText(items[currentPosition]);

        int nextPosition = currentPosition + 1;
        if(nextPosition >= items.length)
            nextPosition = 0;

        if(items[nextPosition].length() > 20)
            nextSong.setText(items[nextPosition].substring(0, 20) + "..");
        else
            nextSong.setText(items[nextPosition]);

        if(nextPosition >= items.length && !SettingsActivity.loopSongList)
            nextSong.setText("");

    }


}
