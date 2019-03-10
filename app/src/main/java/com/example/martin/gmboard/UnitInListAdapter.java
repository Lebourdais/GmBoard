package com.example.martin.gmboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

public class UnitInListAdapter extends RecyclerView.Adapter<UnitInListAdapter.UnitInListViewHolder>{

    Context context;
    private String name;
    private List<Unit> units;

    public UnitInListAdapter(){

    }

    @Override
    public int getItemCount() {
        return units.size();
    }

    public UnitInListAdapter(Context pContext){
        context = pContext;
        loadDataSet();
    }

    
    public void loadDataSet(){
        units = FileHelper.getAllUnits(context);
        notifyDataSetChanged();
    }
    // Create new views (invoked by the layout manager)

    public UnitInListAdapter.UnitInListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // Create a new view
        View view =inflater.inflate(R.layout.unit_in_list_cell, parent, false);

        return new UnitInListAdapter.UnitInListViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(UnitInListAdapter.UnitInListViewHolder holder, int position) {
        // get the element from the dataset at this position
        // replace the contents of the view with that element

        Unit unit = units.get(position);
        holder.display(unit);

    }

    public void removeUnit(Unit unit){
        Unit toRemove = null;
        for(Unit u : units){
            if(u.getName().equals(unit.getName())){
                toRemove = u;
            }
        }

        units.remove(toRemove);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class UnitInListViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private TextView name;
        private TextView stats;
        private ExpandableTextView notes;
        private EditText number;
        private ImageButton plus;
        private ImageButton minus;

        private Unit currentUnit;

        public UnitInListViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            name =  itemView.findViewById(R.id.name);
            stats = itemView.findViewById(R.id.stats);
            notes = itemView.findViewById(R.id.notes);
            number = itemView.findViewById(R.id.number);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);

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

            plus.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int value = Integer.parseInt(number.getText().toString());
                    if(value < 10)
                        value += 1;
                    if(value < 0)
                        value = 0;
                    number.setText(Integer.toString(value));
                }
            });

            minus.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int value = Integer.parseInt(number.getText().toString());
                    if(value > 0)
                        value -= 1;
                    if(value < 0)
                        value = 0;
                    number.setText(Integer.toString(value));
                }
            });
        }
    }
}