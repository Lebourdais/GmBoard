package com.example.martin.gmboard;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;

public class UnitListCreationUi extends AppCompatActivity implements View.OnDragListener {

    private Context context;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.unit_list_creation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = getApplicationContext();


        populateUnitRecyclerView(context);
        populateUnitListRecyclerView(context);


        Button newUnitButton = findViewById(R.id.ButtonNewUnit);
        newUnitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UnitListCreationUi.this, UnitCreationUi.class);
                i.putExtra("Creation", 1);
                startActivity(i);
            }
        });
    }

    public void populateUnitRecyclerView(Context context){

        RecyclerView unitRecyclerView;
        RecyclerView.Adapter unitAdapter;

        unitRecyclerView = findViewById(R.id.UnitRecyclerView);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.layout_border));
        unitRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        unitRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        unitAdapter = new UnitAdapter(context);
        unitRecyclerView.setAdapter(unitAdapter);
    }

    public void populateUnitListRecyclerView(Context context){

        RecyclerView unitListRecyclerView;
        RecyclerView.Adapter unitListAdapter;

        unitListRecyclerView = findViewById(R.id.UnitListRecyclerView);

        DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.layout_border));

        unitListRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        unitListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        unitListAdapter = new UnitListAdapter(context);
        unitListRecyclerView.setAdapter(unitListAdapter);
    }

    public boolean onDrag(View view, DragEvent dragEvent){
        View selectedView = (View) dragEvent.getLocalState();
        RecyclerView rcvSelected = (RecyclerView) view;
        int currentPosition = -1;

        try{

        }
    }

}