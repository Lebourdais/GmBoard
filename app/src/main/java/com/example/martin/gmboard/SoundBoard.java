package com.example.martin.gmboard;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.martin.gmboard.Map;
import com.example.martin.gmboard.OnSwipeTouchListener;

import java.io.IOException;

public class SoundBoard extends AppCompatActivity {
    private float x1,x2;
    static final int MIN_DISTANCE = 150;
    boolean swapped = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_sound_board);
        View view = findViewById(R.id.soundboardcreate);

        view.setOnTouchListener(new OnSwipeTouchListener(SoundBoard.this) {
            public void onSwipeTop() {
                startActivity(new Intent(SoundBoard.this, Map.class));
            }

            public void onSwipeRight() {
                Toast.makeText(SoundBoard.this, "right", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeLeft() {
                Toast.makeText(SoundBoard.this, "left", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeBottom() {
                Toast.makeText(SoundBoard.this, "bottom", Toast.LENGTH_SHORT).show();
            }
        });
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
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){

        // Let ScrollView handle it
        super.dispatchTouchEvent(ev);

        // Otherwise, I'll handle it
        return this.onTouchEvent(ev);
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
