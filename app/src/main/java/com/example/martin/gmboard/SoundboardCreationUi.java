package com.example.martin.gmboard;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
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
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.io.File;
import java.util.List;


public class SoundboardCreationUi extends AppCompatActivity implements SoundboardListener{

    RecyclerView rvTop;
    RecyclerView rvBot;
    List<SongInfo> top;
    List<SongInfo> bot;
    Context context;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_board);

        if(ContextCompat.checkSelfPermission(SoundboardCreationUi.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(SoundboardCreationUi.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(SoundboardCreationUi.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                ActivityCompat.requestPermissions(SoundboardCreationUi.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        } else {
            context = getApplicationContext();

            DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL);
            itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.layout_border));

            rvTop = findViewById(R.id.topSoundRV);

            rvBot = findViewById(R.id.botSoundRV);

            top = FileHelper.getPlaylist(context, SongAdapter.RV_TOP);
            bot = FileHelper.getPlaylist(context, SongAdapter.RV_BOT);

            rvTop.addItemDecoration(itemDecorator);
            rvTop.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rvTop.setAdapter(new SongAdapter(context, SongAdapter.ITEM_VIEW_TYPE_CREATION, SongAdapter.RV_TOP, top, this));

            rvBot.addItemDecoration(itemDecorator);
            rvBot.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rvBot.setAdapter(new SongAdapter(context, SongAdapter.ITEM_VIEW_TYPE_CREATION, SongAdapter.RV_BOT, bot, this));

            ImageButton addTop = findViewById(R.id.addTop);

            ImageButton addBot = findViewById(R.id.addBot);

            addTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addMusic(SongAdapter.RV_TOP);
                }
            });

            addBot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addMusic(SongAdapter.RV_BOT);
                }
            });

            View view = findViewById(R.id.soundLayout);

            view.setOnTouchListener(new OnSwipeTouchListener(SoundboardCreationUi.this) {
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

    public void addMusic(final int which){

        Log.d("raaa", "Adding music");
        LayoutInflater layoutInflater = getLayoutInflater();


        View pview = layoutInflater.inflate(R.layout.music_choice_layout, null, false);
        final PopupWindow pw = new PopupWindow(pview);
        pw.showAtLocation(this.findViewById(R.id.soundLayout), Gravity.CENTER,0,0);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        pw.update(8,-70,width/4,height/2);
        Log.d("raaa", "Popup should be done");


        final RecyclerView rv = pview.findViewById(R.id.soundChoiceRV);

        DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.layout_border));
        rv.addItemDecoration(itemDecorator);


        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        rv.setAdapter(new SongAdapter(context, SongAdapter.ITEM_VIEW_TYPE_BROWSE, which, this));

        ImageButton cancel = pview.findViewById(R.id.cancelChoice);
        cancel.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                pw.dismiss();
            }
        });

    }


    @Override
    public File getEnv() {
        return Environment.getExternalStorageDirectory();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(SoundboardCreationUi.this,
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
