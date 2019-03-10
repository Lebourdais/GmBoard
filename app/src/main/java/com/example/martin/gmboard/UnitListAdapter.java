package com.example.martin.gmboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class UnitListAdapter  extends RecyclerView.Adapter<UnitListAdapter.UnitListViewHolder> {

    private Context context;
    private List<UnitList> unitLists;

    @Override
    public int getItemCount() {
        return unitLists.size();
    }

    //Constructor
    public UnitListAdapter(Context pContext){
        context = pContext;
        loadDataSet();
    }

    public void loadDataSet(){
        unitLists = FileHelper.getAllUnitLists(context);
        notifyDataSetChanged();
    }
    // Create new views (invoked by the layout manager

    public UnitListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // Create a new view
        View view = inflater.inflate(R.layout.list_listunit_cell, parent, false);

        return new UnitListAdapter.UnitListViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(UnitListViewHolder holder, int position) {
        // get the element from the dataset at this position
        // replace the contents of the view with that element

        UnitList unitList = unitLists.get(position);
        holder.display(unitList);

    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class UnitListViewHolder extends RecyclerView.ViewHolder {
        // each data item is a unit
        // TODO créer plusieurs text view et des boutons pour que tout marche

        private TextView name;
        private Button edit;
        private Button delete;

        private UnitList currentUnitList;


        public UnitListViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            edit = itemView.findViewById(R.id.ButtonEdit);
            delete = itemView.findViewById(R.id.ButtonDelete);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO EDITION DE LISTE
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO destruction DE LISTE
                }
            });
        }

        public void display(UnitList unitList){
            currentUnitList = unitList;
            name.setText(unitList.getName());
        }
    }
}
