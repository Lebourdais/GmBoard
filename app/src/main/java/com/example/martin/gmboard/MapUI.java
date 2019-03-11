package com.example.martin.gmboard;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapUI extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{
    public Map map;
    public PinView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        map=new Map();
        map.unitList=new ArrayList<Unit>();
        map.image = R.drawable.swordcoastmaplowres;
        setContentView(R.layout.activity_map);

        imageView = (PinView)findViewById(R.id.imageMap);
        int[] coordmap={0,0};
        imageView.getLocationOnScreen(coordmap);
        map.xmap = coordmap[0];
        map.ymap = coordmap[1];
        imageView.measure(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        map.wmap=imageView.getMeasuredWidth();
        map.hmap=imageView.getMeasuredHeight();
        RadioGroup radioGroupPin = (RadioGroup) findViewById(R.id.radioGroup_pinType);
        radioGroupPin.setOnCheckedChangeListener(this);
        imageView.setImage(ImageSource.resource(map.image));
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    float x = event.getX();
                    float y = event.getY();
                    if (x>map.xmap && x<(map.xmap+map.wmap) && y>map.ymap && y<(y+map.hmap)){
                        boolean inButton = false;
                        Pin currentclick = null;

                        for(int i=0;i<map.listPins.size();i++){
                            if (x>map.listPins.get(i).getposX()-10 && x<map.listPins.get(i).getposX()+10 &&
                                    y>map.listPins.get(i).getposY()-10 && y<map.listPins.get(i).getposX()+10){
                                inButton=true;
                                currentclick = map.listPins.get(i);
                                String rep = "map is the "+currentclick.getName();
                                Toast.makeText(MapUI.this, rep, Toast.LENGTH_SHORT).show();
                                loadMap(getApplicationContext(),currentclick.getName());
                            }
                        }
                        if (!inButton) {

                            Pin newpin = new Pin(v.getContext(), event.getX(), event.getY(), false);
                            newpin.setName((((EditText) findViewById(R.id.pinName)).getText()).toString());
                            if (map.pinType == 1){
                                newpin.setType(1);
                                try{
                                    FileHelper.saveMap(getApplicationContext(),map);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }
                            if (map.pinType == 2){
                                newpin.setType(2);
                                savePOI();
                            }
                            if (map.pinType == 3){
                                newpin.setType(3);
                                saveUnit(newpin.getName());
                            }
                            map.setCirclePoints(((PinView)v).getCirclePoints());
                            ((PinView)v).touch(event,map.pinType);
                            map.listPins.add(newpin);
                        }
                    }
                }

                return true;
            }
        });
        map.note = "";
        map.name = "";
        map.listPins=new ArrayList<Pin>();
        map.containerView = (ViewGroup) findViewById(R.id.container);
        // inflater .. create the instance once, reuse for all the next view

        ((Button)findViewById(R.id.submit)).setOnClickListener(validateInput);
        ((Button)findViewById(R.id.submitName)).setOnClickListener(validateNameInput);

    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        doOnPinTypeChanged(group, checkedId);
    }

    public void savePOI(){

    }
    public void saveUnit(String name){

    }
    public void loadMap(Context context,String name){
        List<Map> listMap  = FileHelper.getAllMap(context);
        Map newmap = null;
        for(Map m : listMap){
            if (m.getName().equals(name)){
                newmap = m;
            }
        }
        map = newmap;
    }
    private void doOnPinTypeChanged(RadioGroup group, int checkedId) {
        int checkedRadioId = group.getCheckedRadioButtonId();

        if(checkedRadioId== R.id.radioButton_Map) {
            map.pinType = 1;
        } else if(checkedRadioId== R.id.radioButton_POI ) {
            map.pinType = 2;
        } else if(checkedRadioId== R.id.radioButton_Unit) {
            map.pinType = 3;
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
