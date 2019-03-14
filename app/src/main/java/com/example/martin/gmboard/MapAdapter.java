package com.example.martin.gmboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MapAdapter extends RecyclerView.Adapter<MapAdapter.MapViewHolder>{
    List<Map> listMap = new ArrayList<>();
    Context context;
    MapUI ui;
    public MapAdapter(Context context,MapUI m){
        listMap = FileHelper.getAllMaps(context);
        ui=m;
        this.context=context;
    }
    @Override
    public int getItemCount() {
        return listMap.size();
    }

    @Override
    public MapViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_map, parent, false);
        return (new MapViewHolder(view));
    }


    @Override
    public void onBindViewHolder( MapAdapter.MapViewHolder holder, int position) {
        Map map = listMap.get(position);
        holder.display(map);
    }

    public class MapViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView description;

        private Map map;

        public MapViewHolder(final View itemView) {
            super(itemView);

            name = ((TextView) itemView.findViewById(R.id.name));
            description = ((TextView) itemView.findViewById(R.id.description));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ui.loadMap(context,name.getText().toString());

                }
            });
        }

        public void display(Map map) {

            this.map = map;
            name.setText(map.name);
            description.setText(map.note);

        }
    }

}
