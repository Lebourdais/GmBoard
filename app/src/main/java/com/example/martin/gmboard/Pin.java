package com.example.martin.gmboard;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class Pin {

    boolean start;
    float xCurr;
    float yCurr;
    int type;
    String name;

    public Pin(Context context,float x,float y,boolean start) {

        this.xCurr=x;
        this.yCurr=y;
        this.start=start;

    }



    public Pin(Context context){

    }
    public Pin(Context context, AttributeSet attributeSet){

    }
    public void setType(int x){this.type=x;};
    public int getType(){return this.type;}
    public void setStart(boolean start){this.start=start;}
    public boolean getStart(){return this.start;}
    public float getposX(){return this.xCurr;}
    public float getposY(){return this.yCurr;}
    public void setName(String name){this.name=name;}
    public String getName(){return this.name;}
}
