package com.example.martin.gmboard;

import android.content.Context;
import android.util.JsonWriter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
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

    public String getName() { return this.name; }
    public int getAttack() { return this.atk; }
    public int getDefense() { return this.def; }
    public String getNotes() { return this.notes; }
    public int getMaxHP() { return this.maxHP; }
    public int getCurrentHP() { return this.currentHP; }
    public Hashtable<String, Integer> getStats() { return this.stats; }



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

    public void saveToFile (Context context, String fileName) throws IOException, JSONException {
        Log.d("START", "Entering saveToFile");

        File file = new File(context.getFilesDir(), fileName);

        FileReader fileReader = null;
        FileWriter fileWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        String response = null;

        if(!file.exists()) {
            try {
                file.createNewFile();
                fileWriter = new FileWriter(file.getAbsoluteFile());
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write("{}");
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        StringBuffer output = new StringBuffer();
        try {
            fileReader = new FileReader(file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bufferedReader = new BufferedReader(fileReader);

        String line = "";

        while((line  = bufferedReader.readLine()) != null) {
            output.append(line + "\n");
        }

        response = output.toString();

        bufferedReader.close();

        JSONObject messageDetails = null;
        try {
            messageDetails = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Boolean isUnitExisting = messageDetails.has("Unit");

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

        if(!isUnitExisting) {
            JSONArray newUnitArray = new JSONArray();
            newUnitArray.put(newUnit);
            messageDetails.put("Unit", newUnitArray);
        } else {
            JSONArray unitArray = (JSONArray) messageDetails.get("Unit");
            unitArray.put(newUnit);
        }

        fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fileWriter);
        bw.write(messageDetails.toString());
        bw.close();

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


        public void writeJsonStream(Writer output) throws IOException {
            JsonWriter jsonWriter = new JsonWriter(output);

            jsonWriter.beginObject();// begin root
            // "Unit" : { ... }
            jsonWriter.name("Unit").beginObject();

                jsonWriter.name("name").value(getName());
                jsonWriter.name("attack").value(getAttack());
                jsonWriter.name("defense").value(getDefense());
                jsonWriter.name("maxHP").value(getMaxHP());
                jsonWriter.name("currentHP").value(getCurrentHP());

                Hashtable<String, Integer> s = getStats();

                // "stats": { ... }
                jsonWriter.name("stats").beginObject(); // begin websites
                    jsonWriter.name("strength").value(s.get("strength"));
                    jsonWriter.name("dexterity").value(s.get("dexterity"));
                    jsonWriter.name("constitution").value(s.get("constitution"));
                    jsonWriter.name("intelligence").value(s.get("intelligence"));
                    jsonWriter.name("wisdom").value(s.get("wisdom"));
                    jsonWriter.name("charisma").value(s.get("charisma"));
                jsonWriter.endObject();// end stats

                jsonWriter.name("notes").value(getNotes());
            // end unit
             jsonWriter.endObject();
            // end root
            jsonWriter.endObject();
        }
}
