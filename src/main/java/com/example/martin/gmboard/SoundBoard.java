package com.example.martin.gmboard;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class SoundBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_sound_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Button btn_sound_1up = (Button) findViewById(R.id.Button);
        btn_sound_1up.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                playSound(R.raw.up);
            }

        });


        Button btn_sound_coin = (Button) findViewById(R.id.Button2);
        btn_sound_coin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                playSound(R.raw.coin);
            }

        });
        Button btn_sound_miaou = (Button) findViewById(R.id.Button3);
        btn_sound_miaou.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                playSound(R.raw.miaou);
            }

        });
    }
    private MediaPlayer mPlayer = null;

    private void playSound(int resId) {
        if(mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
        mPlayer = MediaPlayer.create(this, resId);
        mPlayer.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
    }


}
