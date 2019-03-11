package com.example.martin.gmboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;

public class DragToListListener implements View.OnDragListener {

    private boolean isDropped = false;
    Context context;
    DragToListListener() {    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                //DO NOTHING
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                //SET back shadow
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                // REMOVE back shadow
                break;
            case DragEvent.ACTION_DROP:
                isDropped = true;
                int positionTarget = -1;

                View viewSource = (View) event.getLocalState();
                int viewId = v.getId();
                final int llItem = R.id.unitInListEditable;
                final int unitRV = R.id.UnitRecyclerView;
                final int unitListRV = R.id.UnitListRecyclerView;

                switch (viewId) {
                    case llItem:
                    case unitRV:
                    case unitListRV:

                        RecyclerView target;
                        switch (viewId) {
                            case unitRV:
                                target = v.getRootView().findViewById(unitRV);
                                break;
                            case unitListRV:
                                target = v.getRootView().findViewById(unitListRV);
                                break;
                            default:
                                target = (RecyclerView) v.getParent();
                                positionTarget = (int) v.getTag();
                        }
                        if (viewSource != null) {
                            RecyclerView source = (RecyclerView) viewSource.getParent();

                            UnitAdapter adapterSource = (UnitAdapter) source.getAdapter();

                            int positionSource = (int) viewSource.getTag();

                            Unit unit = adapterSource.getDataSet().get(positionSource);

                            UnitAdapter adapterTarget = (UnitAdapter) target.getAdapter();

                            // If the view is from the RV containing the Unit List
                            // Dragging it off the RV removes it from the RV
                            if(adapterSource.getItemViewType(positionSource) == UnitAdapter.ITEM_TYPE_QUANTIFIABLE ){
                                adapterSource.removeAll(positionSource);
                                adapterSource.notifyDataSetChanged();

                            }

                            // If the view comes from the RV containing all the units and is dragged
                            // To an other position in this RV, we remove it
                            if((adapterSource.getItemViewType(positionSource) == UnitAdapter.ITEM_TYPE_EDITABLE
                            && adapterTarget.getItemViewType(positionTarget) == UnitAdapter.ITEM_TYPE_EDITABLE)){
                                adapterSource.remove(positionSource);
                                adapterSource.notifyDataSetChanged();
                            }

                            // If a view comes from the RV containing the Unit List and goes to the RV containing the list of units
                            // We do not add it to the RV containing the list of units

                            Log.d("raaa", "Source : "+adapterSource.getItemViewType(positionSource)+" target : "+adapterTarget.getItemViewType(positionTarget));
                            if( (adapterSource.getItemViewType(positionSource) == UnitAdapter.ITEM_TYPE_QUANTIFIABLE)
                                && (adapterTarget.getItemViewType(positionTarget) == UnitAdapter.ITEM_TYPE_EDITABLE) ){
                               // DO NOTHING
                                Log.d("raaa", "Not add");
                            } else {
                                if (positionTarget >= 0) {
                                    adapterTarget.add(positionTarget, unit);
                                } else {
                                    adapterTarget.add(unit);
                                }
                            }
                            adapterTarget.notifyDataSetChanged();
                        }
                        break;
                }
                break;
        }

        if (!isDropped && event.getLocalState() != null) {
            ((View) event.getLocalState()).setVisibility(View.VISIBLE);
        }
        return true;
    }
}