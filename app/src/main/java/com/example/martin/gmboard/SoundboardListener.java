package com.example.martin.gmboard;

import android.support.v7.widget.RecyclerView;

import java.io.File;

public interface SoundboardListener {

    File getEnv();

    void addMusic(int which);

    RecyclerView getRvTop();

    RecyclerView getRvBot();
}
