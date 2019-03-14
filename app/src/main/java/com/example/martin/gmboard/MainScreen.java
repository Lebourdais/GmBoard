package com.example.martin.gmboard;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/nodesto_caps_condensed_bold.ttf");
        TextView tv = findViewById(R.id.titleView);
        tv.setTypeface(typeface);
        Button btncreate = (Button)findViewById(R.id.buttonCreate);
        Button btnplay = findViewById(R.id.buttonPlay);


        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreen.this, MapUI.class));
            }
        });

        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreen.this, JeuUI.class));
            }
        });





        // NE PAS DECOMMENTER POUR LA PRESENTATION
//        int screenSize = getResources().getConfiguration().screenLayout &
//                Configuration.SCREENLAYOUT_SIZE_MASK;
//
//        switch(screenSize) {
//            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
//            case Configuration.SCREENLAYOUT_SIZE_SMALL:
//                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case DialogInterface.BUTTON_NEUTRAL:
//                                //finish();
//                        }
//                    }
//                };
//
//                Context context = getApplicationContext();
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setMessage(getString(R.string.screenTooSmall)).setNeutralButton(context.getString(R.string.quit), dialogClickListener);
//
//                AlertDialog alert = builder.create();
//                alert.show();
//        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
