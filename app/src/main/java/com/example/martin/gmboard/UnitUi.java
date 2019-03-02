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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.Hashtable;

public class UnitUi extends AppCompatActivity {

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.content_unit_creation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnCreateUnit = (Button)findViewById(R.id.UnitCreationConfirm);


        btnCreateUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Unit.exists(context, ((EditText)findViewById(R.id.unitName)).getText().toString()))
                    Toast.makeText(context, getString(R.string.creationError), Toast.LENGTH_LONG).show();
                else if (
                        TextUtils.isEmpty( ((EditText)findViewById(R.id.unitMaxHP)).getText().toString().trim() ) ||
                        TextUtils.isEmpty( ((EditText)findViewById(R.id.unitAttack)).getText().toString().trim() ) ||
                        TextUtils.isEmpty( ((EditText)findViewById(R.id.unitDefense)).getText().toString().trim() ) ||
                        TextUtils.isEmpty( ((EditText)findViewById(R.id.unitStrength)).getText().toString().trim() ) ||
                        TextUtils.isEmpty( ((EditText)findViewById(R.id.unitDexterity)).getText().toString().trim() ) ||
                        TextUtils.isEmpty( ((EditText)findViewById(R.id.unitConstitution)).getText().toString().trim() ) ||
                        TextUtils.isEmpty( ((EditText)findViewById(R.id.unitIntelligence)).getText().toString().trim() ) ||
                        TextUtils.isEmpty( ((EditText)findViewById(R.id.unitWisdom)).getText().toString().trim() )  ||
                        TextUtils.isEmpty( ((EditText)findViewById(R.id.unitCharisma)).getText().toString().trim() )
                ) {
                    Toast.makeText(context, getString(R.string.creationError2), Toast.LENGTH_LONG).show();
                } else{
                    createNewUnit(v);
                    startActivity(new Intent(UnitUi.this, MainScreen.class));
                }
            }
        });

    }

    public void createNewUnit(View v) {

        String name = ((EditText)findViewById(R.id.unitName)).getText().toString();
        String notes = ((EditText)findViewById(R.id.unitNotes)).getText().toString();
        int maxHP = Integer.parseInt(((EditText)findViewById(R.id.unitMaxHP)).getText().toString());
        int attack = Integer.parseInt(((EditText)findViewById(R.id.unitAttack)).getText().toString());
        int defense = Integer.parseInt(((EditText)findViewById(R.id.unitDefense)).getText().toString());
        final int strength = Integer.parseInt(((EditText)findViewById(R.id.unitStrength)).getText().toString());
        final int dexterity = Integer.parseInt(((EditText)findViewById(R.id.unitDexterity)).getText().toString());
        final int constitution = Integer.parseInt(((EditText)findViewById(R.id.unitConstitution)).getText().toString());
        final int intelligence = Integer.parseInt(((EditText)findViewById(R.id.unitIntelligence)).getText().toString());
        final int wisdom = Integer.parseInt(((EditText)findViewById(R.id.unitWisdom)).getText().toString());
        final int charisma = Integer.parseInt(((EditText)findViewById(R.id.unitCharisma)).getText().toString());
        boolean pc = ((CheckBox) findViewById(R.id.checkBoxPC)).isChecked();

        Hashtable stats = new Hashtable<String, Integer>() {{ put("STR",strength);
                                                            put("DEX", dexterity);
                                                            put("CON", constitution);
                                                            put("INT", intelligence);
                                                            put("WIS", wisdom);
                                                            put("CHA", charisma);}};

        Unit unit = new Unit(name, maxHP, attack, defense, notes, stats, pc);
        try {
            FileHelper.saveUnit(context, unit);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public TextView printUnit(Unit u){


        return new TextView(context);
    }



}
