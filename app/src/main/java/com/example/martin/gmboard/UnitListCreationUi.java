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
import android.view.View;
import android.widget.Button;

import java.util.List;

public class UnitListCreationUi extends AppCompatActivity {
    private RecyclerView unitRecyclerView;
    private RecyclerView unitListRecyclerView;
    private RecyclerView.Adapter unitAdapter;
    private RecyclerView.Adapter unitListAdapter;
    private Context context;
    private List<Unit> units;
    private List<UnitList> unitLists;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.unit_list_creation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = getApplicationContext();

        unitLists = FileHelper.getAllUnitLists(context);

        DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.layout_border));


        // Creation of the right most Recycler view containing a list of all the units available
        unitRecyclerView = findViewById(R.id.UnitRecyclerView);
        unitRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        unitRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        unitAdapter = new UnitAdapter(context);
        unitRecyclerView.setAdapter(unitAdapter);

        // Creation of the left most Recycler view containing a list of all the UnitLists available

        unitListRecyclerView = findViewById(R.id.UnitListRecyclerView);
        unitListRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        unitListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        unitListAdapter = new UnitListAdapter(unitLists);
        unitListRecyclerView.setAdapter(unitListAdapter);

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

}
