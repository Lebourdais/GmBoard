package com.example.martin.gmboard;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class UnitListAdapter  extends RecyclerView.Adapter<UnitListAdapter.UnitListViewHolder> {

    private Context context;
    private List<UnitList> unitLists;
    protected RecyclerView recyclerView;
    private UnitListCreationListener listener;

    @Override
    public int getItemCount() {
        return unitLists.size();
    }

    //Constructor
    public UnitListAdapter(Context pContext, UnitListCreationListener listener){
        this.listener = listener;
        context = pContext;
        loadDataSet();
    }
    public UnitListAdapter(Context pContext){
        this.listener = null;
        context = pContext;
        loadDataSet();
    }

    public UnitListCreationListener getListener(){return listener;}

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
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

    public void removeUnitList(UnitList unitList){
        unitLists.remove(unitList);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class UnitListViewHolder extends RecyclerView.ViewHolder {
        Context context;
        private TextView name;
        private Button edit;
        private Button delete;

        private UnitList currentUnitList;


        public UnitListViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = itemView.findViewById(R.id.name);
            edit = itemView.findViewById(R.id.ButtonEdit);
            delete = itemView.findViewById(R.id.ButtonDelete);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UnitListCreationListener listener = UnitListAdapter.this.getListener();
                    RecyclerView rv = UnitListAdapter.this.getRecyclerView();
                    listener.enableEdition();
                    listener.setOldUnitList(currentUnitList);
                    listener.swapButtons(true);
                    rv.setAdapter(new UnitAdapter(context, UnitAdapter.ITEM_TYPE_QUANTIFIABLE, currentUnitList, UnitListAdapter.this.getListener()));
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
                                        FileHelper.removeUnitList(context, currentUnitList);
                                        UnitListAdapter.this.removeUnitList(currentUnitList);
                                        UnitListAdapter.this.notifyDataSetChanged();
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
                    builder.setMessage(context.getString(R.string.DeletionValidation)+" "+currentUnitList.getName()+" ?").setPositiveButton(context.getString(R.string.Yes), dialogClickListener)
                            .setNegativeButton(context.getString(R.string.No), dialogClickListener);

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }

        public void display(UnitList unitList){
            currentUnitList = unitList;
            name.setText(unitList.getName());
        }
    }
}
