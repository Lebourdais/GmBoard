package com.example.martin.gmboard;

import java.util.Hashtable;

public class Unit {
    private String name;
    private int maxHP;
    private int currentHP;
    private int atk;
    private int def;
    private String notes;
    private Hashtable<String, Integer> stats;

    // To create a new Unit
    public Unit (String pName, int pMaxHP, int pAtk, int pDef, String pNotes, Hashtable<String, Integer> pStats) {
        this.name = pName;
        this.maxHP = pMaxHP;
        this.currentHP = pMaxHP;
        this.atk = pAtk;
        this.def = pDef;
        this.notes = pNotes;
        this.stats = pStats;
    }

    // To load a previously created Unit
    public Unit (String pName, int pMaxHP, int pCurrentHP, int pAtk, int pDef, String pNotes, Hashtable<String, Integer> pStats) {
        this.name = pName;
        this.maxHP = pMaxHP;
        this.currentHP = pCurrentHP;
        this.atk = pAtk;
        this.def = pDef;
        this.notes = pNotes;
        this.stats = pStats;
    }

    public void loseHP (int amount){
        this.currentHP-=amount;
    }

    public void gainHP (int amount){
        this.currentHP+=amount;
    }

    public void changeAtk (int newAtk){
        this.atk = newAtk;
    }

    public void changeDef (int newDef){
        this.def = newDef;
    }

    public void editNotes (String newNotes){
        this.notes = newNotes;
    }

    public void editStats (String newStats){
        this.notes = newStats;
    }

}
