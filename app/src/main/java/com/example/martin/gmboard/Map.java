package com.example.martin.gmboard;


import android.content.Context;

import android.graphics.Point;

import android.view.ViewGroup;

import java.util.ArrayList;

import java.util.List;


public class Map{
    float dX;
    float dY;

    int lastAction;
    static ArrayList<Unit> unitList;
    List<Pin> listPins;

    ViewGroup containerView;
    // defines paint and canvas

    private List<Point> circlePoints;

    int pinType;
    float xmap;
    float ymap;
    float wmap;
    String parent_name;
    float hmap;
    String note;
    String name;
    int image;

    public float getdX() {
        return dX;
    }

    public float getdY() {
        return dY;
    }

    public float getHmap() {
        return hmap;
    }

    public float getWmap() {
        return wmap;
    }

    public float getXmap() {
        return xmap;
    }

    public float getYmap() {
        return ymap;
    }

    public int getImage() {
        return image;
    }

    public ViewGroup getContainerView() {
        return containerView;
    }

    public int getLastAction() {
        return lastAction;
    }

    public int getPinType() {
        return pinType;
    }

    public List<Point> getCirclePoints() {
        return circlePoints;
    }

    public List<Pin> getListPins() {
        return listPins;
    }

    public void setCirclePoints(List<Point> circlePoints) {
        this.circlePoints = circlePoints;
    }

    public void setdX(float dX) {
        this.dX = dX;
    }

    public void setdY(float dY) {
        this.dY = dY;
    }

    public void setHmap(float hmap) {
        this.hmap = hmap;
    }

    public void setImage(int image) {
        this.image = image;
    }



    public void setListPins(List<Pin> listPins) {
        this.listPins = listPins;
    }

    public void setName(String name) {
        this.name = name;
    }



    public Map(Context context, String parent, String nom){
        super();
        this.parent_name = parent;
        this.name = nom;
    }
    public Map(){
        super();
        this.parent_name = "";
        this.name = "Default";
    }

        public String getName(){
            return this.name;
        }

}
