package com.example.martin.gmboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.view.MotionEvent.ACTION_DOWN;


public class JeuUI extends AppCompatActivity  {
    public Map map;
    public Context c;


    public PinView imageView;
    public boolean first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.first = true;
        c = getApplicationContext();
        map = new Map();

        map.pinType = 2;
        map.image = R.drawable.swordcoastmaplowres;
        setContentView(R.layout.content_jeu);
        imageView = (PinView) findViewById(R.id.imageMapJ);
        int[] coordmap = {0, 0};
        map.note = "map de départ";
        map.name = "main";

        ((TextView) findViewById(R.id.Notemap)).setText(map.note);


        ((TextView) findViewById(R.id.MapNamejeu)).setText(map.name);
        if (!existMap(c, map.getName())) {
            try {

                FileHelper.saveMap(c, map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        imageView.getLocationOnScreen(coordmap);
        map.xmap = coordmap[0];
        map.ymap = coordmap[1];
        imageView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        map.wmap = imageView.getMeasuredWidth();
        map.hmap = imageView.getMeasuredHeight();

        imageView.setImage(ImageSource.resource(map.image));
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int[] coordmap = {0, 0};
                map.xmap = coordmap[0];
                map.ymap = coordmap[1];
                imageView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                map.wmap = imageView.getMeasuredWidth();
                map.hmap = imageView.getMeasuredHeight();
                if (event.getAction() == ACTION_DOWN) {
                    float x = event.getX();
                    float y = event.getY();
                    if (x > map.xmap && x < (map.xmap + map.wmap) && y > map.ymap && y < (y + map.hmap)) {
                        boolean inButton = false;
                        Pin currentclick = null;
                        for (int i = 0; i < map.listPins.size(); i++) {
                            if (x > map.listPins.get(i).getposX() - 10 && x < map.listPins.get(i).getposX() + 10 &&
                                    y > map.listPins.get(i).getposY() - 10 && y < map.listPins.get(i).getposX() + 10) {
                                inButton = true;

                                currentclick = map.listPins.get(i);

                                if (currentclick.getType() == 1)
                                    loadMap(c, currentclick.getName());
                                if (currentclick.getType() == 2)
                                    Toast.makeText(JeuUI.this, "Here will be some notes", Toast.LENGTH_LONG).show();
                            }
                        }


                    }
                }


                return true;
            }
        });
        loadMap(c,"main");
    }

    public void savePOI() {

    }

    public boolean existMap(Context context, String name) {
        List<Map> listMap = FileHelper.getAllMaps(context);
        for (Map m : listMap) {
            Log.d("name", "name = " + m.getName() + " wanted = " + name + " check = " + m.getName().equals(name));
            if (m.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }


    public void loadMap(Context context, String name) {
        List<Map> listMap = FileHelper.getAllMaps(context);
        Log.d("list", String.valueOf(listMap));

        Map newmap = null;
        for (Map m : listMap) {

            if (m.getName().equals(name)) {
                newmap = m;
            }
        }
        if (newmap != null) {
            this.map = newmap;
        } else {
            Toast.makeText(JeuUI.this, "Loading Problem", Toast.LENGTH_SHORT).show();
        }


        int[] coordmap = {0, 0};
        imageView = (PinView) findViewById(R.id.imageMapJ);
        imageView.getLocationOnScreen(coordmap);
        this.map.xmap = coordmap[0];
        this.map.ymap = coordmap[1];
        String rep = "You are on the map : " + map.getName();
        Toast.makeText(JeuUI.this, rep, Toast.LENGTH_SHORT).show();
        imageView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.map.wmap = imageView.getMeasuredWidth();
        this.map.hmap = imageView.getMeasuredHeight();

        imageView.setCirclePoints(map.getCirclePoints());
        imageView.setTypePoints(map.getListType());

        ((TextView) findViewById(R.id.Notemap)).setText(this.map.note);

        ((TextView) findViewById(R.id.MapNamejeu)).setText(this.map.name);

        first = true;
        refreshmap();

    }

    public void refreshmap() {

        imageView.refresh();

        imageView.requestFocus();

    }



}