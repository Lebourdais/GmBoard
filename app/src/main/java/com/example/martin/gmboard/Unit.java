package com.example.martin.gmboard;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Unit {
    private String name;
    private int maxHP;
    private int currentHP;
    private int attack;
    private int defense;
    private String notes;
    private Hashtable<String, Integer> stats;
    private boolean pc;


    // To create a new Unit
    public Unit (String pName, int pMaxHP, int pAtk, int pDef, String pNotes, Hashtable<String, Integer> pStats, boolean pPc) {
        this.name = pName;
        this.maxHP = pMaxHP;
        this.currentHP = pMaxHP;
        this.attack = pAtk;
        this.defense = pDef;
        this.notes = pNotes;
        this.stats = pStats;
        this.pc = pPc;
    }

    // To load a previously created Unit
    public Unit (String pName, int pMaxHP, int pCurrentHP, int pAtk, int pDef, String pNotes, Hashtable<String, Integer> pStats, boolean pPC, Context pContext) {
        this(pName, pMaxHP, pAtk, pDef, pNotes, pStats, pPC);
        this.currentHP = pCurrentHP;
    }

    public String getName() { return this.name; }
    public int getAttack() { return this.attack; }
    public int getDefense() { return this.defense; }
    public String getNotes() { return this.notes; }
    public int getMaxHP() { return this.maxHP; }
    public int getCurrentHP() { return this.currentHP; }
    public Hashtable<String, Integer> getStats() { return this.stats; }
    public boolean getPC() { return this.pc; }


    public void loseHP (int amount){
        this.currentHP-=amount;
    }

    public void gainHP (int amount){
        this.currentHP+=amount;
    }

    public void changeAtk (int newAtk){
        this.attack = newAtk;
    }

    public void changeDef (int newDef){
        this.defense = newDef;
    }

    public void editNotes (String newNotes){
        this.notes = newNotes;
    }

    public void editStats (Hashtable<String, Integer> newStats){
        this.stats = newStats;
    }

    public static boolean exists(Context context, String name){

        String response = null;
//        try {
//            response = FileHelper.readUnits(context);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Unit>>(){}.getType();
        ArrayList<Unit> units = gson.fromJson(response, listType);
        for(Unit unit : units){
            Log.d("exists", "is "+name +" equal to "+unit.getName());
            if(unit.getName().equals(name)){
                Log.d("exists", "true");
                return true;
            }
        }
        return false;
    }


}
