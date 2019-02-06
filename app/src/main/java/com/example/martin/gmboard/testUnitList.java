package com.example.martin.gmboard;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class testUnitList extends AppCompatActivity {

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.unit_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnCreateUnit = (Button)findViewById(R.id.buttonmagique);


        btnCreateUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    printUnitList();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void printUnitList() throws IOException {

        String fileName = "unitstorage.json";
        File file = new File(context.getFilesDir(), fileName);

        FileReader fileReader = null;
        FileWriter fileWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        String response = null;

        // Si le fichier n'existe pas, on en crée un vide
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

        // On lit le fichier
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
        Log.d("BORDEL", "response : " + response);
        bufferedReader.close();

        // File read

        TextView textView = (TextView)findViewById(R.id.textView2);

        textView.setText(response);
    }



}
