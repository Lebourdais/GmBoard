package com.example.martin.gmboard;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.Hashtable;

public class UnitCreationUi extends AppCompatActivity {

    private static Context context;
    private Unit oldUnit; // Only used when editing a unit
    private EditText nameET;
    private EditText maxHPET;
    private EditText atk;
    private EditText def;
    private EditText str;
    private EditText dex;
    private EditText con;
    private EditText intel;
    private EditText wis;
    private EditText cha;
    private EditText notesET;
    private CheckBox pcB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.content_unit_creation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // findViewById is heavy, we are doing it here instead of in the creation function to avoid having to do it twice if we're actually modifying a unit
        // (Which'd have to be done in the method to reprint the stats of the unit and the method to save them)
        nameET = findViewById(R.id.unitName);
        notesET = findViewById(R.id.unitNotes);
        maxHPET =findViewById(R.id.unitMaxHP);
        atk = findViewById(R.id.unitAttack);
        def = findViewById(R.id.unitDefense);
        str = findViewById(R.id.unitStrength);
        dex = findViewById(R.id.unitDexterity);
        con = findViewById(R.id.unitConstitution);
        intel = findViewById(R.id.unitIntelligence);
        wis = findViewById(R.id.unitWisdom);
        cha = findViewById(R.id.unitCharisma);
        pcB = findViewById(R.id.checkBoxPC);
        Button btnCreateUnit = findViewById(R.id.UnitCreationConfirm);

        int callingActivity = getIntent().getIntExtra("Creation", 0);
        switch (callingActivity) {
            case 0:
                // Creation false
                oldUnit = FileHelper.getUnitByName(context, getIntent().getStringExtra("Name"));
                loadUnit();
                btnCreateUnit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editUnit(v);
                    }
                });
                break;
            case 1:
                btnCreateUnit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createNewUnit(v);
                    }
                });
        }
    }

    public void createNewUnit(View v) {
        if(Unit.exists(context, ((EditText)findViewById(R.id.unitName)).getText().toString()))
            Toast.makeText(context, getString(R.string.creationError), Toast.LENGTH_LONG).show();
        else if ( !checkMandatoryFields() ){
            Toast.makeText(context, getString(R.string.creationError2), Toast.LENGTH_LONG).show();
        } else {

            String name = nameET.getText().toString();
            String notes = notesET.getText().toString();
            int maxHP = Integer.parseInt(maxHPET.getText().toString());
            int attack = Integer.parseInt(atk.getText().toString());
            int defense = Integer.parseInt(def.getText().toString());
            final int strength = Integer.parseInt(str.getText().toString());
            final int dexterity = Integer.parseInt(dex.getText().toString());
            final int constitution = Integer.parseInt(con.getText().toString());
            final int intelligence = Integer.parseInt(intel.getText().toString());
            final int wisdom = Integer.parseInt(wis.getText().toString());
            final int charisma = Integer.parseInt(cha.getText().toString());
            boolean pc = pcB.isChecked();

            Hashtable stats = new Hashtable<String, Integer>() {{
                put("STR", strength);
                put("DEX", dexterity);
                put("CON", constitution);
                put("INT", intelligence);
                put("WIS", wisdom);
                put("CHA", charisma);
            }};

            Unit unit = new Unit(name, maxHP, attack, defense, notes, stats, pc);
            try {
                FileHelper.saveUnit(context, unit);
                Intent intent = new Intent();
                intent.putExtra("OK", 1);
                setResult(1, intent);
                finish();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void editUnit(View v){
        if ( !checkMandatoryFields() ){
            Toast.makeText(context, getString(R.string.creationError2), Toast.LENGTH_LONG).show();
        } else {
            String name = nameET.getText().toString();
            String notes = notesET.getText().toString();
            int maxHP = Integer.parseInt(maxHPET.getText().toString());
            int attack = Integer.parseInt(atk.getText().toString());
            int defense = Integer.parseInt(def.getText().toString());
            final int strength = Integer.parseInt(str.getText().toString());
            final int dexterity = Integer.parseInt(dex.getText().toString());
            final int constitution = Integer.parseInt(con.getText().toString());
            final int intelligence = Integer.parseInt(intel.getText().toString());
            final int wisdom = Integer.parseInt(wis.getText().toString());
            final int charisma = Integer.parseInt(cha.getText().toString());
            boolean pc = pcB.isChecked();

            Hashtable stats = new Hashtable<String, Integer>() {{
                put("STR", strength);
                put("DEX", dexterity);
                put("CON", constitution);
                put("INT", intelligence);
                put("WIS", wisdom);
                put("CHA", charisma);
            }};

            Unit newUnit = new Unit(name, maxHP, attack, defense, notes, stats, pc);


            try {
                FileHelper.modifyUnit(context, oldUnit, newUnit);
                finish();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //Returns true if no mandatory field is empty
    private boolean checkMandatoryFields(){
        return !(TextUtils.isEmpty( ((EditText)findViewById(R.id.unitMaxHP)).getText().toString().trim() ) ||
                TextUtils.isEmpty( ((EditText)findViewById(R.id.unitAttack)).getText().toString().trim() ) ||
                TextUtils.isEmpty( ((EditText)findViewById(R.id.unitDefense)).getText().toString().trim() ) ||
                TextUtils.isEmpty( ((EditText)findViewById(R.id.unitStrength)).getText().toString().trim() ) ||
                TextUtils.isEmpty( ((EditText)findViewById(R.id.unitDexterity)).getText().toString().trim() ) ||
                TextUtils.isEmpty( ((EditText)findViewById(R.id.unitConstitution)).getText().toString().trim() ) ||
                TextUtils.isEmpty( ((EditText)findViewById(R.id.unitIntelligence)).getText().toString().trim() ) ||
                TextUtils.isEmpty( ((EditText)findViewById(R.id.unitWisdom)).getText().toString().trim() )  ||
                TextUtils.isEmpty( ((EditText)findViewById(R.id.unitCharisma)).getText().toString().trim()));
    }

    //
    private void loadUnit(){
        Hashtable<String, Integer> stats = oldUnit.getStats();
        nameET.setText(oldUnit.getName());
        notesET.setText(oldUnit.getNotes());
        maxHPET.setText(Integer.toString(oldUnit.getMaxHP()));
        atk.setText(Integer.toString(oldUnit.getAttack()));
        def.setText(Integer.toString(oldUnit.getDefense()));
        str.setText(Integer.toString(stats.get("STR")));
        dex.setText(Integer.toString(stats.get("DEX")));
        con.setText(Integer.toString(stats.get("CON")));
        intel.setText(Integer.toString(stats.get("INT")));
        wis.setText(Integer.toString(stats.get("WIS")));
        cha.setText(Integer.toString(stats.get("CHA")));
        if (oldUnit.isPC())
            pcB.setChecked(true);
    }

}
