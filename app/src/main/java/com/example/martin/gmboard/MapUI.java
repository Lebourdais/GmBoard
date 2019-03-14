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


public class MapUI extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    public Map map;
    public Context c;


    public PinView imageView;
    public RecyclerView rv;
    public RadioGroup radioGroupPin;
    public boolean first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.first = true;
        map = new Map();
        c = getApplicationContext();

        map.pinType = 2;
        map.image = R.drawable.swordcoastmaplowres;
        setContentView(R.layout.activity_map);
        imageView = (PinView) findViewById(R.id.imageMap);
        int[] coordmap = {0, 0};
        map.note = "map de départ";
        map.name = "main";
        EditText txt = (((EditText) findViewById(R.id.writeNote)));
        txt.setText(this.map.note);
        ((TextView) findViewById(R.id.Note)).setText(map.note);
        EditText txt2 = (((EditText) findViewById(R.id.editMapName)));
        txt2.setText(map.name);
        ((TextView) findViewById(R.id.MapName)).setText(map.name);
        if (!existMap(c, map.getName())) {
            try {

                FileHelper.saveMap(c, map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        rv = (RecyclerView) findViewById(R.id.list);

        rv.setLayoutManager(new LinearLayoutManager(this));

        rv.setAdapter(new MapAdapter(c, this));
        imageView.getLocationOnScreen(coordmap);
        map.xmap = coordmap[0];
        map.ymap = coordmap[1];
        imageView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        map.wmap = imageView.getMeasuredWidth();
        map.hmap = imageView.getMeasuredHeight();
        radioGroupPin = (RadioGroup) findViewById(R.id.radioGroup_pinType);
        radioGroupPin.setOnCheckedChangeListener(this);
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
                            }
                        }
                        if (!inButton) {

                            Pin newpin = new Pin(v.getContext(), event.getX(), event.getY(), false);
                            newpin.setName((((EditText) findViewById(R.id.pinName)).getText()).toString());
                            if (map.pinType == 1) {
                                newpin.setType(1);
                                Map newmap = new Map(map.getName(),newpin.name);
                                if(!existMap(c,newpin.name)) {
                                    try {
                                        FileHelper.saveMap(c, newmap);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                ((PinView) v).touch(event, 1);
                            }
                            if (map.pinType == 2) {
                                newpin.setType(2);
                                ((PinView) v).touch(event, 2);
                                savePOI();
                            }

                            List temp = map.getListType();
                            map.setCirclePoints(((PinView) v).getCirclePoints());

                            temp.add(map.pinType);
                            map.setListType(temp);

                            map.addListPins(newpin);

                        }
                    }
                }


                return true;
            }
        });




        ((Button) findViewById(R.id.submit)).setOnClickListener(validateInput);


    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        doOnPinTypeChanged(group, checkedId);
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
            Toast.makeText(MapUI.this, "Problème de load", Toast.LENGTH_SHORT).show();
        }
        Log.d("load", "name =" + String.valueOf(map.name));
        Log.d("load", "points =" + String.valueOf(map.circlePoints));
        Log.d("load", "pins =" + String.valueOf(map.listPins));
        Log.d("load", "types =" + String.valueOf(map.getListType()));
        int[] coordmap = {0, 0};
        imageView = (PinView) findViewById(R.id.imageMap);
        imageView.getLocationOnScreen(coordmap);
        this.map.xmap = coordmap[0];
        this.map.ymap = coordmap[1];
        String rep = "map is the " + map.getName();
        Toast.makeText(MapUI.this, rep, Toast.LENGTH_SHORT).show();
        imageView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.map.wmap = imageView.getMeasuredWidth();
        this.map.hmap = imageView.getMeasuredHeight();

        imageView.setCirclePoints(map.getCirclePoints());
        imageView.setTypePoints(map.getListType());
        map.pinType = 2;
        radioGroupPin.check(R.id.radioButton_POI);
        rv = (RecyclerView) findViewById(R.id.list);

        rv.setLayoutManager(new LinearLayoutManager(this));

        rv.setAdapter(new MapAdapter(c, this));
        rv.getAdapter().notifyDataSetChanged();
        EditText txt = (((EditText) findViewById(R.id.writeNote)));
        txt.setText(this.map.note);
        ((TextView) findViewById(R.id.Note)).setText(this.map.note);
        EditText txt2 = (((EditText) findViewById(R.id.editMapName)));
        ((TextView) findViewById(R.id.MapName)).setText(this.map.name);
        txt2.setText(this.map.name);
        first = true;
        refreshmap();

    }

    public void refreshmap() {

        imageView.refresh();

        imageView.requestFocus();

    }

    private void doOnPinTypeChanged(RadioGroup group, int checkedId) {
        int checkedRadioId = group.getCheckedRadioButtonId();

        if (checkedRadioId == R.id.radioButton_Map) {
            map.pinType = 1;
        } else if (checkedRadioId == R.id.radioButton_POI) {
            map.pinType = 2;
        }
        Toast.makeText(MapUI.this, String.valueOf(map.pinType), Toast.LENGTH_SHORT).show();
    }

    protected View.OnClickListener validateInput;

    {
        validateInput = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText txt = (((EditText) findViewById(R.id.writeNote)));
                map.note = txt.getText().toString();
                ((TextView) findViewById(R.id.Note)).setText(map.note);
                EditText txt2 = (((EditText) findViewById(R.id.editMapName)));
                map.name = txt2.getText().toString();
                ((TextView) findViewById(R.id.MapName)).setText(map.name);
                map.listType = imageView.getTypePoints();
                map.circlePoints = imageView.getCirclePoints();


                map.listPins = new ArrayList<>();
                for (int i = 0; i < imageView.getCirclePoints().size() - 1; i++) {
                    map.listPins.add(new Pin(c, imageView.getCirclePoints().get(i).x, imageView.getCirclePoints().get(i).y, false));
                }


                Log.d("save", "name =" + String.valueOf(map.name));
                Log.d("save", "points =" + String.valueOf(map.circlePoints));
                Log.d("save", "pins =" + String.valueOf(map.listPins));
                Log.d("save", "types =" + String.valueOf(map.getListType()));
                try {
                    FileHelper.updateMap(c, map);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                refreshmap();
                Toast.makeText(MapUI.this, "Save Completed", Toast.LENGTH_SHORT).show();
            }
        };
    }

    protected View.OnClickListener validateNameInput;

    {
        validateNameInput = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText txt = (((EditText) findViewById(R.id.editMapName)));
                map.name = txt.getText().toString();
                ((TextView) findViewById(R.id.MapName)).setText(map.name);


            }
        };
    }
}