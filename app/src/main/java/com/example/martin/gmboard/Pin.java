package com.example.martin.gmboard;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageButton;

public class Pin extends android.support.v7.widget.AppCompatImageButton {

    float xCurr;
    float yCurr;
    String name;
    public Pin(Context context,int x,int y) {
        super(context);
        this.xCurr=x;
        this.yCurr=y;
    }
    public void setposX(float x){this.xCurr=x;}
    public void setposY(float y){this.yCurr=y;}
    public float getposX(){return this.xCurr;}
    public float getposY(){return this.yCurr;}
    public void setName(String name){this.name=name;}
    public void getName(){}
}
