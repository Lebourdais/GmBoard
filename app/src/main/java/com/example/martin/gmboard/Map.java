package com.example.martin.gmboard;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.DragEvent;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Map extends AppCompatActivity implements View.OnTouchListener,RadioGroup.OnCheckedChangeListener {
    float dX;
    float dY;
    String msg;
    int lastAction;
    LayoutInflater inflater;
    static int mapid = 0;
    static int poiid = 0;
    static ArrayList<Unit> unitList;
    List<Pin> listPins;

    ViewGroup containerView;
    private final int paintColor = Color.BLACK;
    // defines paint and canvas

    private List<Point> circlePoints;
    String createPinName;
    int pinType;
    float xmap;
    float ymap;
    float wmap;
    float hmap;
    String note;
    String name;
    private android.widget.LinearLayout.LayoutParams layoutParams;
    TextView posY;
    protected View.OnClickListener validateInput;

    {
        validateInput = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               EditText txt = (((EditText) findViewById(R.id.writeNote)));
               note = txt.getText().toString();
               ((TextView) findViewById(R.id.Note)).setText(note);
            }
        };
    }
    protected View.OnClickListener validateNameInput;

    {
        validateNameInput = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText txt = (((EditText) findViewById(R.id.editMapName)));
                name = txt.getText().toString();
                ((TextView) findViewById(R.id.MapName)).setText(name);
            }
        };
    }

    public Map(Context context, Map parent, String nom){

    }
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            FrameLayout.LayoutParams params1;
            FrameLayout.LayoutParams params2;
            unitList=new ArrayList<Unit>();
            setContentView(R.layout.activity_map);
            PinView imageView = (PinView)findViewById(R.id.imageMap);
            int[] coordmap={0,0};
            imageView.getLocationOnScreen(coordmap);
            xmap = coordmap[0];
            ymap = coordmap[1];
            imageView.measure(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            wmap=imageView.getMeasuredWidth();
            hmap=imageView.getMeasuredHeight();
            RadioGroup radioGroupPin = (RadioGroup) findViewById(R.id.radioGroup_pinType);
            radioGroupPin.setOnCheckedChangeListener(this);
            imageView.setImage(ImageSource.resource(R.drawable.swordcoastmaplowres));
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_DOWN){
                        float x = event.getX();
                        float y = event.getY();
                        if (x>xmap && x<(xmap+wmap) && y>ymap && y<(y+hmap)){
                            boolean inButton = false;
                            Pin currentclick = null;

                            for(int i=0;i<listPins.size();i++){
                                if (x>listPins.get(i).getposX()-10 && x<listPins.get(i).getposX()+10 &&
                                        y>listPins.get(i).getposY()-10 && y<listPins.get(i).getposX()+10){
                                    inButton=true;
                                    currentclick = listPins.get(i);
                                    String rep = "This is the "+currentclick.getName();
                                    Toast.makeText(Map.this, rep, Toast.LENGTH_SHORT).show();

                                }
                            }
                            if (!inButton) {

                                Pin newpin = new Pin(v.getContext(), event.getX(), event.getY(), false);
                                newpin.setName((((EditText) findViewById(R.id.pinName)).getText()).toString());
                                if (pinType == 1){
                                   newpin.setType(1);
                                   saveMap(newpin.getName());
                                }
                                if (pinType == 2){
                                    newpin.setType(2);
                                    savePOI();
                                }
                                if (pinType == 3){
                                    newpin.setType(3);
                                    saveUnit(newpin.getName());
                                }
                                ((PinView)v).touch(event,pinType);
                                listPins.add(newpin);
                            }
                        }
                    }

                    return true;
                }
            });
            this.note = "";
            this.name = "";
            listPins=new ArrayList<Pin>();
            inflater = (LayoutInflater)getApplicationContext().getSystemService (Context.LAYOUT_INFLATER_SERVICE);
            containerView = (ViewGroup) findViewById(R.id.container);
             // inflater .. create the instance once, reuse for all the next view

            ((Button)findViewById(R.id.submit)).setOnClickListener(this.validateInput);
            ((Button)findViewById(R.id.submitName)).setOnClickListener(this.validateNameInput);

    }
        public String getName(){
            return this.name;
        }
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            doOnPinTypeChanged(group, checkedId);
        }
        public void saveMap(String name){
           Map newmap= new Map(this.getApplicationContext(),this,name);
            //TODO Save object
        }
        public void savePOI(){

        }
        public void saveUnit(String name){

        }
        public void loadMap(String name){
            List<Map> listMap; //TODO Get the object
            Map newmap ;
            for(Map m : listMap){
                if (m.getName().equals(name)){
                    newmap = m;
                }
            }
            this= newmap;
            
        }
        private void doOnPinTypeChanged(RadioGroup group, int checkedId) {
            int checkedRadioId = group.getCheckedRadioButtonId();

            if(checkedRadioId== R.id.radioButton_Map) {
                pinType = 1;
            } else if(checkedRadioId== R.id.radioButton_POI ) {
                pinType = 2;
            } else if(checkedRadioId== R.id.radioButton_Unit) {
                pinType = 3;
            }
            Toast.makeText(Map.this, String.valueOf(pinType), Toast.LENGTH_SHORT).show();
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
                    view.setY((int) (event.getRawY() + dY));
                    view.setX((int) (event.getRawX() + dX));

                    lastAction = MotionEvent.ACTION_MOVE;
                    String str = view.getX()+" / "+view.getY();
                    ((TextView) findViewById(R.id.Note)).setText(str);
                    break;

                case MotionEvent.ACTION_UP:
                    if (lastAction == MotionEvent.ACTION_DOWN) {

                        String str2 = view.getX() + " / " + view.getY();
                        Toast.makeText(Map.this, str2, Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(Map.this, "Laché", Toast.LENGTH_SHORT).show();
                        if (((Pin)view).getStart()) {
                            Pin newpin = (Pin) inflater.inflate(R.layout.draggable,null);
                            newpin.setLayoutParams(new ViewGroup.LayoutParams(
                                    50,
                                    50));
                            newpin.setOnTouchListener(this);
                            newpin.setX(((Pin)view).getX());
                            newpin.setStart(false);
                            newpin.setY(((Pin)view).getY());

                            containerView.addView(newpin);
                            listPins.add(newpin);

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
