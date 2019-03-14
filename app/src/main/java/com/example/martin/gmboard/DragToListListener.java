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
                final int unitCombatRV = R.id.combatRV;
                Log.d("raaa",
                        "viewId : "+viewId+
                        " llItem : "+llItem+
                        " unitRV : "+unitRV+
                        " unitListRV : "+unitListRV+
                        " unitCombat : "+unitCombatRV);
                switch (viewId) {
                    case llItem:
                    case unitRV:
                    case unitListRV:
                    case unitCombatRV:
                    default:
                        Log.d("raaa",
                                "viewId : "+viewId);
                        RecyclerView target;
                        switch (viewId) {
                            case unitCombatRV:
                                target = v.getRootView().findViewById(unitCombatRV);
                                break;
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

                            Log.d("raaa", "INITIAL Source : "+adapterSource.getItemViewType(positionSource)+" target : "+adapterTarget.getItemViewType(positionTarget));


                            if( (adapterSource.getItemViewType(positionSource) == UnitAdapter.ITEM_TYPE_QUANTIFIABLE)
                                    && (adapterTarget.getItemViewType(positionTarget) == UnitAdapter.ITEM_TYPE_QUANTIFIABLE)  ) {
                                Log.d("raaa", "Source : Quantifiable target : Quantifiable");
                                Pair<Unit, Integer> pair = adapterSource.getPairs().get(positionSource);
                                adapterSource.remove(positionSource);
                                if (positionTarget >= 0) {
                                    Log.d("raaa", "Adding pair at : "+positionTarget);
                                    adapterTarget.add(positionTarget, pair);
                                } else {
                                    Log.d("raaa", "Adding pair at the end");
                                    adapterTarget.add(pair);

                                }

                                adapterTarget.notifyDataSetChanged();

                                // FROm the list of All units to the UnitList
                            } else if(adapterSource.getItemViewType(positionSource) == adapterTarget.getItemViewType(positionTarget)){
                                Log.d("raaa", "is equal : Source : "+adapterSource.getItemViewType(positionSource)+" target : "+adapterTarget.getItemViewType(positionTarget));
                                adapterSource.remove(positionSource);
                                adapterSource.notifyDataSetChanged();
                                if (positionTarget >= 0) {
                                    Log.d("raaa", "Adding at : "+positionTarget);
                                    adapterTarget.add(positionTarget, unit);
                                } else {
                                    Log.d("raaa", "Adding at the end");
                                    adapterTarget.add(unit);
                                }
                            // From A UnitList to not a unit list, we remove it from the list
                            }
                            if( (adapterSource.getItemViewType(positionSource) == UnitAdapter.ITEM_TYPE_QUANTIFIABLE)
                                    && (adapterTarget.getItemViewType(positionTarget) != UnitAdapter.ITEM_TYPE_QUANTIFIABLE)  ) {
                                Log.d("raaa", "Source : Quantifiable target : Not Quantifiable");
                                adapterSource.remove(positionSource);
                                adapterSource.notifyDataSetChanged();
                             // FROm the list of All units to the UnitList
                            }

                            if ( (adapterSource.getItemViewType(positionSource)== UnitAdapter.ITEM_TYPE_EDITABLE)
                                    && (adapterTarget.getItemViewType(positionTarget) == UnitAdapter.ITEM_TYPE_QUANTIFIABLE)){
                                Log.d("raaa", "Source : Editable target : Quantifiable");
                                if (positionTarget >= 0) {
                                    Log.d("raaa", "Adding at : "+positionTarget);
                                    adapterTarget.add(positionTarget, unit);
                                } else {
                                    Log.d("raaa", "Adding at the end");
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