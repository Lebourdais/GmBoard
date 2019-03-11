package com.example.martin.gmboard;

import android.content.Context;
import android.util.AttributeSet;

public class Pin extends android.support.v7.widget.AppCompatImageButton {

    boolean start;
    float xCurr;
    float yCurr;
    int type;
    String name;
    int idElem;

    public Pin(Context context,float x,float y,boolean start) {
        super(context);
        this.xCurr=x;
        this.yCurr=y;
        this.start=start;

    }

    public void setIdElem(int idElem) {
        this.idElem = idElem;
    }

    public int getIdElem() {return idElem;}

    public Pin(Context context){
        super(context);
    }
    public Pin(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }
    public void setposX(float x){this.xCurr=x;}
    public void setposY(float y){this.yCurr=y;}
    public void setType(int x){this.type=x;};
    public int getType(){return this.type;}
    public void setStart(boolean start){this.start=start;}
    public boolean getStart(){return this.start;}
    public float getposX(){return this.xCurr;}
    public float getposY(){return this.yCurr;}
    public void setName(String name){this.name=name;}
    public String getName(){return this.name;}
}
