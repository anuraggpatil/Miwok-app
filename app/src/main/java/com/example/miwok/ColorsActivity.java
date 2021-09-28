package com.example.miwok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_colors);

        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Words> words = new ArrayList<Words>();

//        words.add("One");
        words.add(new Words("Red", "Weṭeṭṭi", R.drawable.color_red , R.raw.color_red));
        words.add(new Words("Green", "Chokokki", R.drawable.color_green, R.raw.color_green ));
        words.add(new Words("Brown", "Takaakki" , R.drawable.color_brown, R.raw.color_brown));
        words.add(new Words("Gray", "Topopp", R.drawable.color_gray, R.raw.color_gray ));
        words.add(new Words("Black", "Kululli", R.drawable.color_black , R.raw.color_black));
        words.add(new Words("White", "Kelelli" , R.drawable.color_white, R.raw.color_white));
        words.add(new Words("Dusty Yellow", "Topiisә" , R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        words.add(new Words("Mustard Yellow", "Chiwiiṭә", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));



        WordAdapter adapter = new WordAdapter(this, words, R.color.purple);
        ListView listView = (ListView) findViewById(R.id.colorsList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Words word = words.get(position);

                releaseMediaPlayer();

                mMediaPlayer = MediaPlayer.create(ColorsActivity.this, word.getmSoundResourceID());

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