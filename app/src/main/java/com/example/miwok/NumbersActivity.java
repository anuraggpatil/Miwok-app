package com.example.miwok;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.*;


public class NumbersActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;

    private AudioManager mAudioManager;

    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);
                    }
                    else if(focusChange == AudioManager.AUDIOFOCUS_GAIN){
                        mMediaPlayer.start();
                    }
                    else if(focusChange == AudioManager.AUDIOFOCUS_LOSS){
                        releaseMediaPlayer();
                    }
                }
            };

    private MediaPlayer.OnCompletionListener mOncompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbers);

        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Words> words = new ArrayList<Words>();

//        words.add("One");
        words.add(new Words("One", "Lutti", R.drawable.number_one, R.raw.number_one));
        words.add(new Words("Two", "Ottiko", R.drawable.number_two, R.raw.number_two ));
        words.add(new Words("Three", "Tolookosu", R.drawable.number_three, R.raw.number_three ));
        words.add(new Words("Four", "Oyyisa", R.drawable.number_four , R.raw.number_four));
        words.add(new Words("Five", "Massokka", R.drawable.number_five, R.raw.number_five));
        words.add(new Words("Six", "Temokka" , R.drawable.number_six, R.raw.number_six));
        words.add(new Words("Seven", "Kwnwkaku" ,R.drawable.number_seven, R.raw.number_seven));
        words.add(new Words("Eight", "Kawinta" , R.drawable.number_eight, R.raw.number_eight));
        words.add(new Words("Nine", "Wo'e", R.drawable.number_nine , R.raw.number_nine));
        words.add(new Words("Ten", "Na'aacha", R.drawable.number_ten, R.raw.number_nine));




        WordAdapter adapter = new WordAdapter(this, words, R.color.orange);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Words word = words.get(position);
                releaseMediaPlayer();

                mMediaPlayer = MediaPlayer.create(NumbersActivity.this, word.getmSoundResourceID());

                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mMediaPlayer.setOnCompletionListener(mOncompletionListener);
                    // we have audio focus now

                    mMediaPlayer.start();

                    mMediaPlayer.setOnCompletionListener(mOncompletionListener);
                }
            }
        });

        }

    @Override
    protected void onStop() {
        super.onStop();

        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            // Abandon audio focus when playback complete
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}


