package com.example.martin.gmboard;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

public class UnitListCreationUi extends AppCompatActivity implements UnitListCreationListener {

    private Context context;
    RecyclerView unitRecyclerView;
    private RecyclerView unitListRecyclerView;
    private RecyclerView.Adapter unitListAdapter;
    private RecyclerView.Adapter unitAdapter;
    private RecyclerView.Adapter unitInListAdapter;
    private TextInputLayout listName;
    private Button createButton;
    private Button newListButton;
    private boolean edit;
    private boolean flag;
    private UnitList oldUnitList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.unit_list_creation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = getApplicationContext();

        View view = findViewById(R.id.unitListCreationLayout);

        view.setOnTouchListener(new OnSwipeTouchListener(UnitListCreationUi.this) {
            public void onSwipeTop() {
                // DO NOTHING
            }

            public void onSwipeRight() {
                // DO NOTHING
            }

            public void onSwipeLeft() {
                // DO NOTHING
            }

            public void onSwipeBottom() {
                // MAP activity should never be finished
                finish();
            }


        });





        edit = false;
        flag = true;
        final UnitListCreationListener activity = this;
        populateUnitRecyclerView(activity);
        populateUnitListRecyclerView(activity);

        Button newUnitButton = findViewById(R.id.ButtonNewUnit);
        createButton = findViewById(R.id.ButtonCreate);
        listName = findViewById(R.id.TILName);
        newListButton = findViewById(R.id.ButtonNewList);
        final ImageButton su = findViewById(R.id.swapUnits);
        createButton.setVisibility(View.GONE);
        listName.setVisibility(View.GONE);
        unitInListAdapter = new UnitAdapter(context, UnitAdapter.ITEM_TYPE_QUANTIFIABLE, null, activity) ;

        newUnitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UnitListCreationUi.this, UnitCreationUi.class);
                i.putExtra("Creation", 1);
                startActivityForResult(i, 1);
            }
        });

        newListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit = false;
                unitListRecyclerView.setAdapter(unitInListAdapter);
                su.setVisibility(View.GONE);
                swapButtons(true);


            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                su.setVisibility(View.VISIBLE);
                if(!edit) {
                    if (createUnitList()) {

                        unitListRecyclerView.setAdapter(unitListAdapter);
                        ((UnitListAdapter) unitListAdapter).loadDataSet();
                        swapButtons(false);
                    }
                } else {
                    if ( editUnitList()) {

                        unitListRecyclerView.setAdapter(unitListAdapter);
                        ((UnitListAdapter) unitListAdapter).loadDataSet();
                        swapButtons(false);
                    }
                }
            }
        });



        su.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag) {
                    newListButton.setVisibility(View.INVISIBLE);
                    unitRecyclerView.setAdapter(new UnitAdapter(context, UnitAdapter.ITEM_TYPE_EDITABLE, FileHelper.getAllUnits(context, true)));
                    unitRecyclerView.getAdapter().notifyDataSetChanged();
                    unitListRecyclerView.setAdapter(null);
                    flag = false;
                } else {
                    newListButton.setVisibility(View.VISIBLE);
                    unitRecyclerView.setAdapter(new UnitAdapter(context, UnitAdapter.ITEM_TYPE_EDITABLE, activity));
                    unitRecyclerView.getAdapter().notifyDataSetChanged();
                    unitListRecyclerView.setAdapter(new UnitListAdapter(context, UnitListAdapter.ITEM_TYPE_EDITABLE,activity));
                    flag = true;
                }
            }
        });
    }

    public void swapButtons(boolean newList){
        if(newList){
            createButton.setVisibility(View.VISIBLE);
            listName.setVisibility(View.VISIBLE);
            newListButton.setVisibility(View.INVISIBLE);
            unitListRecyclerView.setOnDragListener( ((UnitAdapter )unitInListAdapter).getDragInstance());
            if(oldUnitList != null)
                listName.getEditText().setText(oldUnitList.getName());
        } else {
            newListButton.setVisibility(View.VISIBLE);
            createButton.setVisibility(View.GONE);
            listName.setVisibility(View.GONE);
            unitListAdapter.notifyDataSetChanged();
        }
    }

    public void enableEdition(){
        edit = true;
    }

    public boolean getEdit(){ return edit; }

    @Override
    public void setOldUnitList(UnitList u) {
        oldUnitList = u;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((UnitAdapter)unitAdapter).loadDataSet();
        unitAdapter.notifyDataSetChanged();
    }

    public void populateUnitRecyclerView(UnitListCreationListener activity){
        unitAdapter = new UnitAdapter(context, UnitAdapter.ITEM_TYPE_EDITABLE, activity);
        unitRecyclerView = findViewById(R.id.UnitRecyclerView);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.layout_border));
        unitRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        unitRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        unitRecyclerView.setAdapter(unitAdapter);
        unitRecyclerView.setOnDragListener(((UnitAdapter) unitAdapter).getDragInstance());
    }

    public void populateUnitListRecyclerView(UnitListCreationListener activity){
        unitListAdapter = new UnitListAdapter(context, UnitListAdapter.ITEM_TYPE_EDITABLE,activity);
        unitListRecyclerView = findViewById(R.id.UnitListRecyclerView);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.layout_border));
        unitListRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        unitListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        unitListRecyclerView.setAdapter(unitListAdapter);
    }

    public boolean createUnitList(){
        Log.d("raaa", " in create list ");
        if (UnitList.exists(context, ((EditText) findViewById(R.id.listName)).getText().toString())) {
            Toast.makeText(context, getString(R.string.creationError3), Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(((EditText) findViewById(R.id.listName)).getText().toString().trim())){
            Toast.makeText(context, getString(R.string.creationError4), Toast.LENGTH_LONG).show();
            return false;
        } else {
            UnitList ul = new UnitList(false);
            ul.setUnits( ( (UnitAdapter ) unitListRecyclerView.getAdapter()).getDataSet());
            ul.setName(listName.getEditText().getText().toString());
            try {
                FileHelper.saveUnitList(context, ul);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean editUnitList(){
        Log.d("raaa", " in edit list "+oldUnitList.getName());
        if (TextUtils.isEmpty(((EditText) findViewById(R.id.listName)).getText().toString().trim())){
            Toast.makeText(context, getString(R.string.creationError4), Toast.LENGTH_LONG).show();
            return false;
        } else {
            UnitList ul = new UnitList(false);
            ul.setUnits( ( (UnitAdapter ) unitListRecyclerView.getAdapter()).getDataSet());
            ul.setName(listName.getEditText().getText().toString());
            try {
                FileHelper.modifyUnitList(context, oldUnitList, ul);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }



}
