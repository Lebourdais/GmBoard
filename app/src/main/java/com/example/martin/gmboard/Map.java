package com.example.martin.gmboard;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Map extends AppCompatActivity implements View.OnTouchListener {
    float dX;
    float dY;
    HashMap pos;
    int lastAction;
    LayoutInflater inflater;
    List<Pin> listPins;
    ViewGroup containerView;
    int hash1;
    int hash2;

    TextView posY;
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            FrameLayout.LayoutParams params1;
            FrameLayout.LayoutParams params2;
            pos = new HashMap<Integer,String>();
            setContentView(R.layout.activity_map);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            listPins=new ArrayList<Pin>();
            inflater = (LayoutInflater)getApplicationContext().getSystemService (Context.LAYOUT_INFLATER_SERVICE);
            posY = (TextView)findViewById(R.id.yposTxt);
            containerView= (ViewGroup) findViewById(R.id.container);
             // inflater .. create the instance once, reuse for all the next view
            Pin pin1 = (Pin) inflater.inflate(R.layout.draggable,null);
            Pin pin2 = (Pin) inflater.inflate(R.layout.draggable,null);

            pin1.setLayoutParams(new ViewGroup.LayoutParams(
                50,
                50));
            pin2.setLayoutParams(new ViewGroup.LayoutParams(
                50,
                50));
            pin1.setY(100);
            pin1.setX(50);
            pin1.setposX(50);
            pin1.setposY(100);
            pin2.setY(200);
            pin2.setX(50);
            pin2.setposX(50);
            pin2.setposY(200);
            pin1.setOnTouchListener(this);
            pin2.setOnTouchListener(this);
            listPins.add(pin1);
            listPins.add(pin2);
            containerView.addView(pin1);
            containerView.addView(pin2);

    }
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    dX = view.getX() - event.getRawX();
                    dY = view.getY() - event.getRawY();

                    lastAction = MotionEvent.ACTION_DOWN;
                    break;

                case MotionEvent.ACTION_MOVE:
                    view.setY(event.getRawY() + dY);
                    view.setX(event.getRawX() + dX);

                    posY.setText(String.valueOf(view.getX())+" / "+String.valueOf(view.getY()));
                    lastAction = MotionEvent.ACTION_MOVE;
                    break;

                case MotionEvent.ACTION_UP:
                    if (lastAction == MotionEvent.ACTION_DOWN)
                        Toast.makeText(Map.this, "Clicked!", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(Map.this, "Laché", Toast.LENGTH_SHORT).show();
                        if (view.hashCode() == hash1 || view.hashCode() == hash2) {
                            Pin newpin = (Pin) inflater.inflate(R.layout.draggable,null);
                            newpin.setLayoutParams(new ViewGroup.LayoutParams(
                                    50,
                                    50));
                            newpin.setOnTouchListener(this);
                            newpin.setX(view.getX());
                            newpin.setY(view.getY());

                            containerView.addView(newpin);
                            listPins.add(newpin);

                            String Yhash = (String) pos.get(view.hashCode());

                            view.setY(((Pin)view).getposY());
                            view.setX(((Pin)view).getposX());
                        }
                    }

                    break;

                default:
                    return false;
            }
            return true;
        }

}
