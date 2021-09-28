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

public class FamilyActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_family);

        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Words> words = new ArrayList<Words>();

//        words.add("One");
        words.add(new Words("Father", "әpә", R.drawable.family_father , R.raw.family_father));
        words.add(new Words("Mother", "әṭa" , R.drawable.family_mother, R.raw.family_mother));
        words.add(new Words("Son", "Angsi", R.drawable.family_son , R.raw.family_son));
        words.add(new Words("Daughter", "Tune", R.drawable.family_daughter , R.raw.family_daughter));
        words.add(new Words("Older Brother", "Taachi", R.drawable.family_older_brother , R.raw.family_older_brother));
        words.add(new Words("Younger Brother", "Chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother ));
        words.add(new Words("Older Sister", "Tete" , R.drawable.family_older_sister, R.raw.family_older_sister));
        words.add(new Words("Younger Sister", "Kolliti", R.drawable.family_younger_sister , R.raw.family_younger_sister ));
        words.add(new Words("Grandmother", "Ama", R.drawable.family_grandmother, R.raw.family_grandmother ));
        words.add(new Words("Grandfather", "Paapa", R.drawable.family_grandfather , R.raw.family_grandfather));




        WordAdapter adapter = new WordAdapter(this, words, R.color.green);

        ListView listView = (ListView) findViewById(R.id.familyList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Words word = words.get(position);
                releaseMediaPlayer();

                mMediaPlayer = MediaPlayer.create(FamilyActivity.this, word.getmSoundResourceID());

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
