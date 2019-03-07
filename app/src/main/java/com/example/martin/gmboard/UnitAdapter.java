package com.example.martin.gmboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

public class UnitAdapter extends RecyclerView.Adapter<UnitAdapter.UnitViewHolder>{
    Context context;
    private List<Unit> units;

    @Override
    public int getItemCount() {
        return units.size();
    }

    public UnitAdapter(Context pContext){
        context = pContext;
        loadDataSet();
    }

    public void loadDataSet(){
        units = FileHelper.getAllUnits(context);
        notifyDataSetChanged();
    }
    // Create new views (invoked by the layout manager

    public UnitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // Create a new view
        View view =inflater.inflate(R.layout.list_unit_cell, parent, false);

        return new UnitViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(UnitViewHolder holder, int position) {
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
    public class UnitViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private TextView name;
        private TextView stats;
        private ExpandableTextView notes;
        private ImageButton edit;
        private ImageButton delete;

        private Unit currentUnit;

        public UnitViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            name =  itemView.findViewById(R.id.name);
//            stats = itemView.findViewById(R.id.stats);
//            notes = itemView.findViewById(R.id.notes);
//            edit = itemView.findViewById(R.id.ImageButtonEdit);
//            delete = itemView.findViewById(R.id.ImageButtonDelete);

        }

        public void display(Unit unit){
            currentUnit = unit;
            name.setText(unit.getName());
            Hashtable<String, Integer> htStats= unit.getStats();

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
                    loadDataSet();
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
                                        UnitAdapter.this.removeUnit(currentUnit);
                                        UnitAdapter.this.notifyDataSetChanged();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
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

}
