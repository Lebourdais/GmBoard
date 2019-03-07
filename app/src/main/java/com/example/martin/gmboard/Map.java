package com.example.martin.gmboard;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Map extends AppCompatActivity implements View.OnTouchListener {
    float dX;
    float dY;
    String msg;
    int lastAction;
    LayoutInflater inflater;
    List<Pin> listPins;
    ViewGroup containerView;
    String createPinName;
    boolean pinType;
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

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            FrameLayout.LayoutParams params1;
            FrameLayout.LayoutParams params2;

            setContentView(R.layout.activity_map);
            PinView imageView = (PinView)findViewById(R.id.imageMap);
            imageView.setImage(ImageSource.resource(R.drawable.swordcoastmaplowres));
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN){
                        boolean inButton = false;
                        Pin currentclick = null;
                        float x = event.getX();
                        float y = event.getY();
                        for(int i=0;i<listPins.size();i++){
                            if (x>listPins.get(i).getposX() && x<listPins.get(i).getposX()+50 &&
                                    y>listPins.get(i).getposY() && y<listPins.get(i).getposX()+50){
                                inButton=true;
                                currentclick = listPins.get(i);
                            }
                        }
                        if (!inButton) {

                            Pin newpin = new Pin(v.getContext(), event.getX(), event.getY(), false);
                            callCreateDialog();

                            listPins.add(newpin);
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
            Pin pin1 = (Pin) inflater.inflate(R.layout.draggable,null);
            Pin pin2 = (Pin) inflater.inflate(R.layout.draggable,null);
            ((Button)findViewById(R.id.submit)).setOnClickListener(this.validateInput);
            ((Button)findViewById(R.id.submitName)).setOnClickListener(this.validateNameInput);


//            pin1.setOnLongClickListener(this);
//            pin2.setOnLongClickListener(this);
//            pin1.setOnDragListener(this);
//            pin2.setOnDragListener(this);
//            pin1.setOnTouchListener(this);
//            pin2.setOnTouchListener(this);
            listPins.add(pin1);
            listPins.add(pin2);
            containerView.addView(pin1);
            containerView.addView(pin2);

    }

//        @Override
//        public boolean onLongClick(View v) {
//            ClipData.Item item = new ClipData.Item((CharSequence)v.getTag());
//            String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
//
//            ClipData dragData = new ClipData(v.getTag().toString(),mimeTypes, item);
//            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(listPins.get(1));
//
//            v.startDrag(dragData,myShadow,null,0);
//            return true;
//        }
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                ClipData data = ClipData.newPlainText("", "");
//                Toast.makeText(Map.this, "CLicked", Toast.LENGTH_SHORT).show();
//                Pin img = listPins.get(1);
//                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(img);
//
//                img.startDrag(data, shadowBuilder, img, 0);
//                img.setVisibility(View.INVISIBLE);
//                return true;
//            } else {
//                return false;
//            }
//        }
//        @Override
//        public boolean onDrag(View v, DragEvent event) {
//            switch(event.getAction()) {
//                case DragEvent.ACTION_DRAG_STARTED:
//                    layoutParams = (LinearLayout.LayoutParams)v.getLayoutParams();
//                    Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");
//
//                    // Do nothing
//                    break;
//
//                case DragEvent.ACTION_DRAG_ENTERED:
//                    Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
//                    int x_cord = (int) event.getX();
//                    int y_cord = (int) event.getY();
//                    break;
//
//                case DragEvent.ACTION_DRAG_EXITED :
//                    Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");
//                    x_cord = (int) event.getX();
//                    y_cord = (int) event.getY();
//                    layoutParams.leftMargin = x_cord;
//                    layoutParams.topMargin = y_cord;
//                    v.setLayoutParams(layoutParams);
//                    break;
//
//                case DragEvent.ACTION_DRAG_LOCATION  :
//                    Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
//                    x_cord = (int) event.getX();
//                    y_cord = (int) event.getY();
//                    String str = x_cord+" / "+y_cord;
//                    ((TextView) findViewById(R.id.Note)).setText(str);
//                    break;
//
//                case DragEvent.ACTION_DRAG_ENDED   :
//                    Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED");
//
//                    // Do nothing
//                    break;
//
//                case DragEvent.ACTION_DROP:
//                    Log.d(msg, "ACTION_DROP event");
//
//                    // Do nothing
//                    break;
//                default: break;
//            }
//            return true;
//        }
        private void callCreateDialog()
        {
            final Dialog myDialog = new Dialog(this);
            myDialog.setContentView(R.layout.new_pin);
            myDialog.setCancelable(false);
            final EditText pinName = (EditText) myDialog.findViewById(R.id.writeMapName);
            final Switch sw = (Switch) myDialog.findViewById(R.id.switch9);
            Button button = myDialog.findViewById(R.id.SubmitPin);
            myDialog.show();

            button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    createPinName=pinName.getText().toString();
                    pinType = sw.isChecked();
                    myDialog.cancel();
                }
            });


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
