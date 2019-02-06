package com.example.martin.gmboard;

import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

public class Unit {
    private String name;
    private int maxHP;
    private int currentHP;
    private int atk;
    private int def;
    private String notes;
    private Hashtable<String, Integer> stats;
    private Context context;


    // To create a new Unit
    public Unit (String pName, int pMaxHP, int pAtk, int pDef, String pNotes, Hashtable<String, Integer> pStats, Context pContext) {
        this.name = pName;
        this.maxHP = pMaxHP;
        this.currentHP = pMaxHP;
        this.atk = pAtk;
        this.def = pDef;
        this.notes = pNotes;
        this.stats = pStats;
        this.context = pContext;
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

    public void editStats (Hashtable<String, Integer> newStats){
        this.stats = newStats;
    }

    public void saveToFile (Context context, String fileName){
        JSONObject newUnit = new JSONObject();
        try {
            newUnit.put("name", name);
            newUnit.put("maxHP", maxHP);
            newUnit.put("currentHP", currentHP);
            newUnit.put("attack", atk);
            newUnit.put("defense", def);
            newUnit.put("notes", notes);
            newUnit.put("stats", stats);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Lit le fichier json existant
        String jsonText = readText(context, R.raw.unitStorage);
        JSONObject jsonRoot = new JSONObject(jsonText);

        JSONArray jsonArray = new JSONArray();

        jsonArray.put(jsonRoot);
        jsonArray.put(newUnit);
    }

    private static String readText(Context context, int resId) throws IOException {
        InputStream is = context.getResources().openRawResource(resId);
        BufferedReader br= new BufferedReader(new InputStreamReader(is));
        StringBuilder sb= new StringBuilder();
        String s = null;
        while((  s = br.readLine())!=null) {
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }

}
