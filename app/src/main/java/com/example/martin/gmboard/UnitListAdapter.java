package com.example.martin.gmboard;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class UnitListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<UnitList> unitLists;
    protected RecyclerView recyclerView;
    private UnitListCreationListener listener;
    private int itemViewType;
    static final int ITEM_TYPE_EDITABLE = 0;
    static final int ITEM_TYPE_COMBAT = 1;

    @Override
    public int getItemCount() {
        return unitLists.size();
    }

    //Constructor
    public UnitListAdapter(Context pContext, int pItemViewType, UnitListCreationListener listener){
        this.itemViewType = pItemViewType;
        this.listener = listener;
        context = pContext;
        loadDataSet();
    }

    public UnitListAdapter(Context pContext, int pItemViewType){
        this.itemViewType = pItemViewType;
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

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(itemViewType == ITEM_TYPE_EDITABLE){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.list_listunit_cell, parent, false);
            return new UnitListAdapter.UnitListViewHolder(view);
        } else {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.unit_list_combat, parent, false);
            return new UnitListAdapter.UnitListViewHolderCombat(view);
        }

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // get the element from the dataset at this position
        // replace the contents of the view with that element
        UnitList unitList = unitLists.get(position);
        if(itemViewType == ITEM_TYPE_EDITABLE)
            ((UnitListViewHolder)holder).display(unitList);
        else
            ((UnitListViewHolderCombat)holder).display(unitList);

    }

    public void removeUnitList(UnitList unitList){
        unitLists.remove(unitList);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class UnitListViewHolder extends RecyclerView.ViewHolder {
        Context context;
        private TextView name;
        private ImageButton edit;
        private ImageButton delete;

        private UnitList currentUnitList;


        UnitListViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = itemView.findViewById(R.id.name);
            edit = itemView.findViewById(R.id.ButtonEdit);
            delete = itemView.findViewById(R.id.ButtonDelete);

            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/nodesto_caps_condensed_bold.ttf");
            name.setTypeface(typeface);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UnitListCreationListener listener = UnitListAdapter.this.getListener();
                    RecyclerView rv = UnitListAdapter.this.getRecyclerView();
                    listener.enableEdition();
                    listener.setOldUnitList(currentUnitList);
                    listener.swapButtons(true);
                    UnitAdapter ua = new UnitAdapter(context, UnitAdapter.ITEM_TYPE_QUANTIFIABLE, currentUnitList, UnitListAdapter.this.getListener());
                    rv.setAdapter(ua);
                    rv.setOnDragListener(ua.getDragInstance());
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

        void display(UnitList unitList){
            currentUnitList = unitList;
            name.setText(unitList.getName());
        }
    }

    class UnitListViewHolderCombat extends RecyclerView.ViewHolder {
        Context context;
        private TextView name;

        private UnitList currentUnitList;


        public UnitListViewHolderCombat(View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = itemView.findViewById(R.id.name);

            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/nodesto_caps_condensed_bold.ttf");
            name.setTypeface(typeface);

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecyclerView rv = UnitListAdapter.this.getRecyclerView();
                    UnitAdapter ua = new UnitAdapter(context, UnitAdapter.ITEM_TYPE_COMBAT, currentUnitList);
                    rv.setAdapter(ua);
                    rv.setOnDragListener(ua.getDragInstance());
                }
            });
        }

        public void display(UnitList unitList){
            currentUnitList = unitList;
            name.setText(unitList.getName());
        }
    }
}
