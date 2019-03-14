package com.example.martin.gmboard;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class SoundboardPlayUi extends AppCompatActivity implements SoundboardListener{

    RecyclerView rvTop;
    RecyclerView rvBot;
    List<SongInfo> top;
    List<SongInfo> bot;
    Context context;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_layout);

        if(ContextCompat.checkSelfPermission(SoundboardPlayUi.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(SoundboardPlayUi.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(SoundboardPlayUi.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                ActivityCompat.requestPermissions(SoundboardPlayUi.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        } else {
            View view = findViewById(R.id.soundboardPlay);

            view.setOnTouchListener(new OnSwipeTouchListener(SoundboardPlayUi.this) {
                public void onSwipeTop() {
                    // MAP activity should never be finished
                    finish();
                }

                public void onSwipeRight() {
                    // DO NOTHING
                }

                public void onSwipeLeft() {
                    // DO NOTHING
                }

                public void onSwipeBottom() {
                    // DO NOTHING
                }


            });

            context = getApplicationContext();

            DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL);
            itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.layout_border));

            rvTop = findViewById(R.id.topSoundRV);

            rvBot = findViewById(R.id.botSoundRV);

            top = FileHelper.getPlaylist(context, SongAdapter.RV_TOP);
            bot = FileHelper.getPlaylist(context, SongAdapter.RV_BOT);

            rvTop.addItemDecoration(itemDecorator);
            rvTop.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rvTop.setAdapter(new SongAdapter(context, SongAdapter.ITEM_VIEW_TYPE_PLAY, SongAdapter.RV_TOP, top, this));

            rvBot.addItemDecoration(itemDecorator);
            rvBot.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rvBot.setAdapter(new SongAdapter(context, SongAdapter.ITEM_VIEW_TYPE_PLAY, SongAdapter.RV_BOT, bot, this));


        }
    }

    @Override
    public RecyclerView getRvTop() {
        return rvTop;
    }

    @Override
    public RecyclerView getRvBot() {
        return rvBot;
    }


    @Override
    public File getEnv() {
        return Environment.getExternalStorageDirectory();
    }

    @Override
    public void addMusic(int which) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(SoundboardPlayUi.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }

        }
    }
}
