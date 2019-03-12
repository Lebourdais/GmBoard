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


    // defines paint and canvas

    private List<Point> circlePoints;
    private List<Integer> listType;

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

    public List<Integer> getListType() {
        return listType;
    }

    public void setListType(List<Integer> listType) {
        this.listType = listType;
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

    public void setWmap(float wmap) {
        this.wmap = wmap;
    }

    public void setXmap(float xmap) {
        this.xmap = xmap;
    }

    public void setYmap(float ymap) {
        this.ymap = ymap;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public List<Pin> addListPins(Pin p) {
        listPins.add(p);
        return listPins;
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
        listPins=new ArrayList<Pin>();
        circlePoints = new ArrayList<Point>();
        listType = new ArrayList<Integer>();
    }
    public Map(){
        super();
        this.parent_name = "";
        this.name = "main";
        listPins=new ArrayList<Pin>();
        listType = new ArrayList<Integer>();
        circlePoints = new ArrayList<Point>();
    }

        public String getName(){
            return this.name;
        }

}
