package com.example.martin.gmboard;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class Pin extends android.support.v7.widget.AppCompatImageButton {

    boolean start;
    float xCurr;
    float yCurr;
    String name;
    public Pin(Context context,float x,float y,boolean start) {
        super(context);
        this.xCurr=x;
        this.yCurr=y;
        this.start=start;

    }
    public Pin(Context context){
        super(context);
    }
    public Pin(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }
    public void setposX(float x){this.xCurr=x;}
    public void setposY(float y){this.yCurr=y;}
    public void setStart(boolean start){this.start=start;}
    public boolean getStart(){return this.start;}
    public float getposX(){return this.xCurr;}
    public float getposY(){return this.yCurr;}
    public void setName(String name){this.name=name;}
    public void getName(){}
}
