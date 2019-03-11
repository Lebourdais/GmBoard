package com.example.martin.gmboard;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    implements View.OnTouchListener {
    Context context;
    private List<Unit> units;
    private int itemViewType;
    private UnitListCreationListener listener;
    private LinearLayout.LayoutParams params;

    static final int ITEM_TYPE_EDITABLE = 0;
    static final int ITEM_TYPE_QUANTIFIABLE = 1;
    static final int ITEM_TYPE_COMBAT = 2;

    UnitAdapter(Context pContext, int pItemViewType){
        context = pContext;
        this.listener = null;
        itemViewType = pItemViewType;
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15,15,15,15);
        loadDataSet();
    }

    UnitAdapter(Context pContext, int pItemViewType, UnitListCreationListener listener){
        context = pContext;
        this.listener = listener;
        itemViewType = pItemViewType;
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15,15,15,15);
        loadDataSet();
    }

    UnitAdapter(Context pContext, int pItemViewType, UnitList pUnits, UnitListCreationListener listener){
        this.listener = listener;
        context = pContext;
        itemViewType = pItemViewType;
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15,15,15,15);
        if (pUnits != null)
            units = FileHelper.getUnitsFromList(context, pUnits);
        else
            units = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        if (units != null)
            return units.size();
        return 0;
    }

    void loadDataSet(){
        units = FileHelper.getAllUnits(context, false);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // Create a new view
        if(itemViewType == ITEM_TYPE_QUANTIFIABLE){
                View view = inflater.inflate(R.layout.unit_in_list_cell, parent, false);
                return new UnitViewHolderQuantifiable(view);
        } else if (itemViewType == ITEM_TYPE_EDITABLE){
            View view = inflater.inflate(R.layout.list_unit_cell, parent, false);
            return new UnitViewHolderEditable(view);
        } else {
            View view = inflater.inflate(R.layout.combat_layout, parent, false);
            return new UnitViewHolderEditable(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return itemViewType;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Unit unit = units.get(position);
        if(itemViewType == ITEM_TYPE_QUANTIFIABLE){
            ((UnitViewHolderQuantifiable) holder).layout.setLayoutParams(params);
            if( (units.indexOf(unit) != units.lastIndexOf(unit)) && (position != units.indexOf(unit))){
                ((UnitViewHolderQuantifiable) holder).layout.setLayoutParams( new LinearLayout.LayoutParams(0,0));
                ((UnitViewHolderQuantifiable) holder).layout.setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
            }
                int quantity = Collections.frequency(units, unit);
                ((UnitViewHolderQuantifiable) holder).layout.setOnTouchListener(this);
                ((UnitViewHolderQuantifiable) holder).layout.setTag(position);
                ((UnitViewHolderQuantifiable) holder).layout.setOnDragListener(new DragToListListener());
                ((UnitViewHolderQuantifiable) holder).display(unit, quantity);
        } else if (itemViewType == ITEM_TYPE_EDITABLE){
            ((UnitViewHolderEditable)holder).layout.setOnTouchListener(this);
            ((UnitViewHolderEditable)holder).layout.setTag(position);
            ((UnitViewHolderEditable)holder).layout.setOnDragListener(new DragToListListener());
            ((UnitViewHolderEditable)holder).display(unit);
        } else {
            ((UnitViewHolderEditable)holder).layout.setOnTouchListener(this);
            ((UnitViewHolderEditable)holder).layout.setTag(position);
            ((UnitViewHolderEditable)holder).layout.setOnDragListener(new DragToListListener());
            ((UnitViewHolderEditable)holder).display(unit);
        }
    }

    List<Unit> getDataSet(){
            return units;
    }

    private void remove(Unit unit){
            units.remove(unit);
    }

    void add(Unit unit){
            this.units.add(unit);
    }

    void add(int position, Unit unit){
        this.units.add(position, unit);
    }

    void remove(int position){
        units.remove(position);
    }

    void removeAll(int position){
        Unit unitToRemove = units.get(position);
        while (units.remove(unitToRemove));

    }

    DragToListListener getDragInstance() {
            return new DragToListListener();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    v.startDragAndDrop(data, shadowBuilder, v, 0);
                } else {
                    v.startDrag(data, shadowBuilder, v, 0);
                }
                return true;
        }
        return false;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class UnitViewHolderEditable extends RecyclerView.ViewHolder {
        private Context context;
        private LinearLayout layout;
        private TextView name;
        private TextView stats;
        private ExpandableTextView notes;
        private ImageButton edit;
        private ImageButton delete;

        private Unit currentUnit;

        public UnitViewHolderEditable(View itemView) {
            super(itemView);
            context = itemView.getContext();
            layout = itemView.findViewById(R.id.unitInListEditable);
            name =  itemView.findViewById(R.id.name);
            stats = itemView.findViewById(R.id.stats);
            notes = itemView.findViewById(R.id.notes);
            edit = itemView.findViewById(R.id.ImageButtonEdit);
            delete = itemView.findViewById(R.id.ImageButtonDelete);
        }

        public void display(Unit unit){
            currentUnit = unit;
            name.setText(unit.getName());

            String s = "HP : "+unit.getMaxHP()+" | ATK : "+unit.getAttack()+" | DEF : "+unit.getDefense()+"\n" +
                    "STR : "+unit.getStats().get("STR")+
                    " | DEX : "+unit.getStats().get("DEX")+
                    " | CON : "+unit.getStats().get("CON")+
                    " | INT : "+unit.getStats().get("INT")+
                    " | WIS : "+unit.getStats().get("WIS")+
                    " | CHA : "+ unit.getStats().get("CHA");
            stats.setText(s);
            notes.setText(unit.getNotes());

            edit.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent i = new Intent(context, UnitCreationUi.class);
                    i.putExtra("Creation", 0);
                    i.putExtra("Name", currentUnit.getName());
                    context.startActivity(i);
                }
            });

            delete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    try {
                                        FileHelper.removeUnit(context, currentUnit);
                                        UnitAdapter.this.remove(currentUnit);
                                        UnitAdapter.this.notifyDataSetChanged();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(context.getString(R.string.DeletionValidation)+" "+currentUnit.getName()+" ?").setPositiveButton(context.getString(R.string.Yes), dialogClickListener)
                            .setNegativeButton(context.getString(R.string.No), dialogClickListener);

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }
    }

    public class UnitViewHolderQuantifiable extends RecyclerView.ViewHolder{
        private Context context;
        private LinearLayout layout;
        private TextView name;
        private TextView stats;
        private ExpandableTextView notes;
        private EditText number;
        private ImageButton plus;
        private ImageButton minus;

        private Unit currentUnit;

        public UnitViewHolderQuantifiable(View itemView) {
            super(itemView);
            context = itemView.getContext();
            layout = itemView.findViewById(R.id.unitInListQuantifiable);
            name =  itemView.findViewById(R.id.name);
            stats = itemView.findViewById(R.id.stats);
            notes = itemView.findViewById(R.id.notes);
            number = itemView.findViewById(R.id.number);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);
        }

        public void display(Unit unit, int quantity){
            currentUnit = unit;
            name.setText(unit.getName());

            String s = "HP : "+unit.getMaxHP()+" | ATK : "+unit.getAttack()+" | DEF : "+unit.getDefense()+"\n" +
                    "STR : "+unit.getStats().get("STR")+
                    " | DEX : "+unit.getStats().get("DEX")+
                    " | CON : "+unit.getStats().get("CON")+
                    " | INT : "+unit.getStats().get("INT")+
                    " | WIS : "+unit.getStats().get("WIS")+
                    " | CHA : "+ unit.getStats().get("CHA");
            stats.setText(s);
            notes.setText(unit.getNotes());
            number.setText(Integer.toString(quantity));

            plus.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int value = Integer.parseInt(number.getText().toString());
                    if(value < 10){
                        value += 1;
                        UnitAdapter.this.add(currentUnit);
                    }
                    if(value < 0){
                        value = 0;
                    }

                    number.setText(Integer.toString(value));
                }
            });

            minus.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int value = Integer.parseInt(number.getText().toString());
                    if(value > 0){
                        UnitAdapter.this.remove(currentUnit);
                        value -= 1;
                    }
                    if(value < 0)
                        value = 0;
                    number.setText(Integer.toString(value));
                }
            });
        }
    }

    public class UnitViewHolderCombat extends RecyclerView.ViewHolder{
        private Context context;
        private LinearLayout layout;
        private TextView name;
        private TextView stats;
        private ExpandableTextView notes;
        private EditText number;
        private ImageButton plus;
        private ImageButton minus;

        private Unit currentUnit;

        public UnitViewHolderCombat(View itemView) {
            super(itemView);
            context = itemView.getContext();
            name =  itemView.findViewById(R.id.name);
            stats = itemView.findViewById(R.id.stats);
            notes = itemView.findViewById(R.id.notes);
            number = itemView.findViewById(R.id.number);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);
        }

        @SuppressLint("SetTextI18n")
        public void display(Unit unit, int quantity){
            currentUnit = unit;
            name.setText(unit.getName());

            String s = "HP : "+unit.getMaxHP()+" | ATK : "+unit.getAttack()+" | DEF : "+unit.getDefense()+"\n" +
                    "STR : "+unit.getStats().get("STR")+
                    " | DEX : "+unit.getStats().get("DEX")+
                    " | CON : "+unit.getStats().get("CON")+
                    " | INT : "+unit.getStats().get("INT")+
                    " | WIS : "+unit.getStats().get("WIS")+
                    " | CHA : "+ unit.getStats().get("CHA");
            stats.setText(s);
            notes.setText(unit.getNotes());
            number.setText(Integer.toString(quantity));

            plus.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int value = Integer.parseInt(number.getText().toString());
                        value += 1;
                        UnitAdapter.this.add(currentUnit);
                    if(value < 0){
                        value = 0;
                    }
                    number.setText(Integer.toString(value));
                }
            });

            minus.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int value = Integer.parseInt(number.getText().toString());
                    if(value > 0){
                        UnitAdapter.this.remove(currentUnit);
                        value -= 1;
                    }
                    if(value < 0)
                        value = 0;
                    number.setText(Integer.toString(value));
                }
            });
        }
    }
        
    
}
