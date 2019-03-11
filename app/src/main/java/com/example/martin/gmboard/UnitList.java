package com.example.martin.gmboard;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class UnitList {

    private String name;
    private Hashtable<String, Integer> units; // Contains couples (unitName, quantity)
    private boolean pc;


    UnitList(boolean pc){
        if(pc)
            name = "pcList";
        else name = "";
        units = new Hashtable<String, Integer>();
        this.pc = pc;
    }

    public UnitList (List<Unit> pUnits, String pName, boolean pc){
        this.name = pName;
        this.pc = pc;
        units = new Hashtable<String, Integer>();
        if(pUnits != null){
            for(Unit u : pUnits){
                String name = u.getName();
                if(units.containsKey(name)){
                    units.put(name, units.get(name)+1);
                } else {
                    units.put(name, 1);
                }
            }
        }
    }

    public String getName(){ return name; }

    public void setName(String pName){ this.name = pName;}

    void setUnits(List<Unit> pUnits) {
        units.clear();
        if (pUnits != null) {
            for (Unit u : pUnits) {
                String name = u.getName();
                if (units.containsKey(name)) {
                    units.put(name, units.get(name) + 1);
                } else {
                    units.put(name, 1);
                }
            }
        }
    }

    void addUnit(Unit u){
        if(units.containsKey(name)){
            units.put(name, units.get(name)+1);
        } else {
            units.put(name, 1);
        }
    }

    Hashtable<String, Integer> getUnits(){
        return units;
    }

    boolean isPC(){ return this.pc; }

    static boolean exists(Context context, String name){

        String response = null;
        try {
            response = FileHelper.readJsonFile(context, "liststorage.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Type listType = new TypeToken<List<UnitList>>(){}.getType();
        ArrayList<UnitList> unitLists = gson.fromJson(response, listType);
        for(UnitList u : unitLists)
            if (u.getName().equals(name)) return true;
        return false;
    }

}
