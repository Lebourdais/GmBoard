package com.example.martin.gmboard;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

public class CombatUi extends AppCompatActivity {

    public RecyclerView units;
    private Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.combat_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();

        units = findViewById(R.id.combatRV);

        units.setAdapter(new UnitListAdapter(context, UnitListAdapter.ITEM_TYPE_COMBAT));

        DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.layout_border));
        units.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        units.setLayoutManager(new LinearLayoutManager(this));
        LinearLayout layout = findViewById(R.id.combatLayout);

        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                units.setAdapter(new UnitListAdapter(context, UnitListAdapter.ITEM_TYPE_COMBAT));
                return false;
            }
        });
    }





}
