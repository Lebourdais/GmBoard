package com.example.martin.gmboard;


import android.content.Context;

import android.graphics.Point;


import java.util.ArrayList;

import java.util.List;


public class Map{
    float dX;
    float dY;
    String pointJson;
    String pinJson;
    List<Pin> listPins;
    String parent_name;

    // defines paint and canvas

    public List<Point> circlePoints;
    public List<Integer> listType;

    int pinType;
    float xmap;
    float ymap;
    float wmap;

    float hmap;
    String note;
    String name;
    int image;


    public int getImage() {
        return image;
    }

    public List<Integer> getListType() {
        return listType;
    }

    public void setListType(List<Integer> listType) {
        this.listType = listType;
    }



    public List<Point> getCirclePoints() {
        return circlePoints;
    }


    public void setCirclePoints(List<Point> circlePoints) {
        this.circlePoints = circlePoints;
    }



    public void setImage(int image) {
        this.image = image;
    }

    public List<Pin> addListPins(Pin p) {
        listPins.add(p);
        return listPins;
    }



    public void setName(String name) {
        this.name = name;
    }
    public String getName(){
        return this.name;
    }


    public Map(String parent,String nom){

        this.parent_name = parent;
        this.name = nom;
        listPins=new ArrayList<Pin>();
        circlePoints = new ArrayList<Point>();
        listType = new ArrayList<Integer>();
    }
    public Map(){

        parent_name = "main";
        this.name = "main";
        listPins=new ArrayList<Pin>();
        circlePoints = new ArrayList<Point>();
        listType = new ArrayList<Integer>();

    }



}
