package com.example.martin.gmboard;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private List<Pair<Unit, Integer>> unitsInList;
    private int itemViewType;
    private UnitListCreationListener listener;
    private LinearLayout.LayoutParams params;

    static final int ITEM_TYPE_EDITABLE = 0;
    static final int ITEM_TYPE_QUANTIFIABLE = 1;
    static final int ITEM_TYPE_COMBAT = 2;

    UnitAdapter(Context pContext, int pItemViewType, UnitList pUnits){
        context = pContext;
        this.listener = null;
        itemViewType = pItemViewType;
        units = FileHelper.getUnitsFromList(context, pUnits);
        unitsInList = new ArrayList<>();

        for(Unit unit : units){
            int quantity = Collections.frequency(units, unit);
            Pair<Unit, Integer> p = new Pair<>(unit, quantity);
            if(!unitsInList.contains(p))
                unitsInList.add(p);
        }
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
        unitsInList = new ArrayList<>();
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15,15,15,15);
        if (pUnits != null)
            units = FileHelper.getUnitsFromList(context, pUnits);
        else
            units = new ArrayList<>();
        for(Unit unit : units){
            int quantity = Collections.frequency(units, unit);
            Pair<Unit, Integer> p = new Pair<>(unit, quantity);
            if(!unitsInList.contains(p))
                unitsInList.add(p);
        }

    }

    @Override
    public int getItemCount() {
        if(itemViewType == ITEM_TYPE_QUANTIFIABLE){
            if(unitsInList != null)
                return unitsInList.size();
        } else {
            if (units != null)
                return units.size();
        }
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
            View view = inflater.inflate(R.layout.unit_in_list_cell, parent, false);
            return new UnitViewHolderCombat(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return itemViewType;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(itemViewType == ITEM_TYPE_QUANTIFIABLE){
            Pair<Unit, Integer> p = unitsInList.get(position);
            ((UnitViewHolderQuantifiable) holder).layout.setOnTouchListener(this);
            ((UnitViewHolderQuantifiable) holder).layout.setTag(position);
            ((UnitViewHolderQuantifiable) holder).layout.setOnDragListener(new DragToListListener());
            ((UnitViewHolderQuantifiable) holder).display(p);
        } else if (itemViewType == ITEM_TYPE_EDITABLE){
            Unit unit = units.get(position);
            ((UnitViewHolderEditable)holder).layout.setOnTouchListener(this);
            ((UnitViewHolderEditable)holder).layout.setTag(position);
            ((UnitViewHolderEditable)holder).layout.setOnDragListener(new DragToListListener());
            ((UnitViewHolderEditable)holder).display(unit);
        } else {
            Unit unit = units.get(position);
            ((UnitViewHolderCombat)holder).layout.setOnTouchListener(this);
            ((UnitViewHolderCombat)holder).layout.setTag(position);
            ((UnitViewHolderCombat)holder).layout.setOnDragListener(new DragToListListener());
            ((UnitViewHolderCombat)holder).display(unit);
        }
    }

    List<Unit> getDataSet(){
            return units;
    }

    List<Pair<Unit, Integer>> getPairs(){
        return unitsInList;
    }

    void remove(int position){
        units.remove(position);

        if(itemViewType == ITEM_TYPE_QUANTIFIABLE) {

            Unit toRemove = unitsInList.get(position).getLeft();
            while (units.remove(toRemove)) ;
            unitsInList.remove(position);
        }
    }

    private void remove(Unit unit){
        units.remove(unit);
        if(itemViewType == ITEM_TYPE_QUANTIFIABLE) {
            Unit toRemove = null;
            for(Pair<Unit, Integer> p : unitsInList){
                if(p.getLeft().getName().equals(unit.getName())){
                    p.setRight(p.getRight()-1);
                    toRemove = p.getLeft();
                }
            }
            while (units.remove(toRemove)) ;
        }
    }

    void add(Unit unit){
        this.units.add(unit);
        if(itemViewType == ITEM_TYPE_QUANTIFIABLE){
            boolean in = false;
            Pair<Unit, Integer> pp = null;
            for(Pair<Unit, Integer> p : unitsInList){
                if(p.getLeft().getName().equals(unit.getName())){
                    pp = p;
                    in = true;
                }
            }
            if(!in)
                unitsInList.add(new Pair<Unit, Integer>(unit, 1));
            else {
                unitsInList.remove(pp);
                pp.setRight(pp.getRight()+1);
                unitsInList.add(pp);
            }
        }
    }

    void add(int position, Unit unit){
        this.units.add(position, unit);
        if(itemViewType == ITEM_TYPE_QUANTIFIABLE){
            boolean in = false;
            Pair<Unit, Integer> pp = null;
            for(Pair<Unit, Integer> p : unitsInList){
                if(p.getLeft().getName().equals(unit.getName())){
                    pp = p;
                    in = true;
                }
            }
            if(!in)
                unitsInList.add(position, new Pair<Unit, Integer>(unit, 1));
            else {
                unitsInList.remove(pp);
                pp.setRight(pp.getRight()+1);
                unitsInList.add(position, pp);
            }
        }
    }

    void add(Pair<Unit, Integer> p){
        unitsInList.add(p);
        for(int i = 0 ; i < p.getRight()+1 ; i++){
            units.add(p.getLeft());
        }
        notifyDataSetChanged();
    }

    void add(int position, Pair<Unit, Integer> p){
        Log.d("raaa", "Ici");
        for(Pair<Unit, Integer> ppppp : unitsInList){
            Log.d("raaa", "Got "+ppppp.getLeft().getName());
        }
            Log.d("raaa", "Removed"+p.getLeft().getName());
            unitsInList.remove(p);
            unitsInList.add(position, p);
            for(int i = 0 ; i < p.getRight()+1 ; i++){
                Log.d("raaa", "Adding "+i+" units");
                units.add(p.getLeft());

        }

    }

    void removeAll(int position){
//        Unit unitToRemove = units.get(position);
//        while (units.remove(unitToRemove));
        if(itemViewType == ITEM_TYPE_QUANTIFIABLE){
            Unit toRemove = unitsInList.get(position).getLeft();
            while (units.remove(toRemove));
            unitsInList.remove(position);
        } else {
            units.remove(position);
        }
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

        UnitViewHolderEditable(View itemView) {
            super(itemView);
            context = itemView.getContext();
            layout = itemView.findViewById(R.id.unitInListEditable);
            name =  itemView.findViewById(R.id.name);
            stats = itemView.findViewById(R.id.stats);
            notes = itemView.findViewById(R.id.notes);
            edit = itemView.findViewById(R.id.ImageButtonEdit);
            delete = itemView.findViewById(R.id.ImageButtonDelete);

            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/nodesto_caps_condensed_bold.ttf");
            name.setTypeface(typeface);
        }

        void display(Unit unit){
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

        private Pair<Unit, Integer> currentPair;
        private Unit currentUnit;
        private int quantity;

        UnitViewHolderQuantifiable(View itemView) {
            super(itemView);
            context = itemView.getContext();
            layout = itemView.findViewById(R.id.unitInListQuantifiable);
            name =  itemView.findViewById(R.id.name);
            stats = itemView.findViewById(R.id.stats);
            notes = itemView.findViewById(R.id.notes);
            number = itemView.findViewById(R.id.number);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);

            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/nodesto_caps_condensed_bold.ttf");
            name.setTypeface(typeface);
        }

        void display(Pair<Unit, Integer> p){
            currentPair = p;
            currentUnit = p.getLeft();
            quantity = p.getRight();

            name.setText(currentUnit.getName());

            String s = "HP : "+currentUnit.getMaxHP()+" | ATK : "+currentUnit.getAttack()+" | DEF : "+currentUnit.getDefense()+"\n" +
                    "STR : "+currentUnit.getStats().get("STR")+
                    " | DEX : "+currentUnit.getStats().get("DEX")+
                    " | CON : "+currentUnit.getStats().get("CON")+
                    " | INT : "+currentUnit.getStats().get("INT")+
                    " | WIS : "+currentUnit.getStats().get("WIS")+
                    " | CHA : "+ currentUnit.getStats().get("CHA");
            stats.setText(s);
            notes.setText(currentUnit.getNotes());
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
            layout = itemView.findViewById(R.id.unitInListQuantifiable);
            name =  itemView.findViewById(R.id.name);
            stats = itemView.findViewById(R.id.stats);
            notes = itemView.findViewById(R.id.notes);
            number = itemView.findViewById(R.id.number);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);

            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/nodesto_caps_condensed_bold.ttf");
            name.setTypeface(typeface);
        }

        @SuppressLint("SetTextI18n")
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
            number.setText(Integer.toString(unit.getCurrentHP()));

            plus.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int value = Integer.parseInt(number.getText().toString());
                    if(value < currentUnit.getMaxHP())
                        value += 1;
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
                        value -= 1;
                    }
                    if(value < 0)
                        value = 0;
                    number.setText(Integer.toString(value));
                    if(value == 0)
                        UnitAdapter.this.remove(currentUnit);
                        UnitAdapter.this.notifyDataSetChanged();


                }
            });
        }
    }
        
    
}
