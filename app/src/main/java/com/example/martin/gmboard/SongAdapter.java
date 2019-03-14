package com.example.martin.gmboard;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class SongAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static final int ITEM_VIEW_TYPE_CREATION = 0;
    static final int ITEM_VIEW_TYPE_PLAY = 1;
    static final int ITEM_VIEW_TYPE_BROWSE = 2;

    static final int RV_TOP = 0;
    static final int RV_BOT = 1;

    private List<SongInfo> songInfos;
    private List<File> songs;
    private Context context;
    private SoundboardListener listener;
    private int itemViewType;
    private int rvType;
    int selectedPosition = -1;
    RecyclerView recyclerView;


    public SongAdapter(Context context, int itemViewType, int rvType, List<SongInfo> songInfos, SoundboardListener listener) {
        this.context = context;
        this.songInfos = songInfos;
        this.songs = new ArrayList<>();
        this.listener = listener;
        this.rvType = rvType;
        this.itemViewType = itemViewType;
        for(SongInfo si : songInfos){
            songs.add(new File(si.getSongUrl()));
        }    }


    // Should only be used with ITEM_VIEW_TYPE_BROWSE
    public SongAdapter(Context context, int itemViewType, int rvType, SoundboardListener listener){
        this.context = context;
        this.songs = new ArrayList<>();
        this.songs = FileHelper.findSongs(listener.getEnv());
        this.songInfos = new ArrayList<>();
        this.listener = listener;
        this.itemViewType = itemViewType;
        for(File f : songs){
            songInfos.add(new SongInfo(f.getName(), f.getAbsolutePath()));
        }
        this.rvType = rvType;
    }

    public List<SongInfo> getSongInfos() {
        return songInfos;
    }

    public List<File> getSongs() {
        return songs;
    }

    public Context getContext() {
        return context;
    }

    public SoundboardListener getListener() {
        return listener;
    }

    public int getItemViewType() {
        return itemViewType;
    }

    public int getRvType() {
        return rvType;
    }

    public SongInfo getSelectedItem() {
        if(selectedPosition == RecyclerView.NO_POSITION) return null;
        return songInfos.get(selectedPosition);
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void add(SongInfo s){
        songInfos.add(s);
        songs.add(new File(s.getSongUrl()));
    }

    public void add(int position, SongInfo s) {
        songInfos.add(position, s);
        songs.add(position, new File(s.getSongUrl()));
    }

    public void add(File song){
        songInfos.add(new SongInfo(song.getName(), song.getAbsolutePath()));
        songs.add(song);
    }

    public void add(int position, File song) {
        songInfos.add(position, new SongInfo(song.getName(), song.getAbsolutePath()));
        songs.add(position, song);
    }

    public void remove(SongInfo s){
        songInfos.remove(s);
        songs.remove(new File(s.getSongUrl()));
    }

    public void remove(File song){
        songInfos.remove(new SongInfo(song.getName(), song.getAbsolutePath()));
        songs.remove(song);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch(itemViewType){
            case ITEM_VIEW_TYPE_CREATION: {
                View myView = inflater.inflate(R.layout.music_creation_layout, parent, false);
                return new SongHolderCreation(myView);
            }
            case ITEM_VIEW_TYPE_PLAY: {
                View myView = inflater.inflate(R.layout.music_play_layout, parent, false);
                return new SongHolderPlay(myView);
            }
            case ITEM_VIEW_TYPE_BROWSE: {
                View myView = inflater.inflate(R.layout.popup_rv_layout, parent, false);
                return new SongHolderBrowse((myView));
            }

        }
        View myView = LayoutInflater.from(context).inflate(R.layout.music_creation_layout, parent, false);
        return new SongHolderBrowse(myView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder songHolder, final int i) {
        switch(itemViewType) {
            case ITEM_VIEW_TYPE_CREATION: {
                final SongInfo s = songInfos.get(i);
                ((SongHolderCreation)songHolder).display(s);
                break;
            }
            case ITEM_VIEW_TYPE_PLAY: {
                final SongInfo s = songInfos.get(i);
                ((SongHolderPlay)songHolder).display(s);
                break;
            }
            case ITEM_VIEW_TYPE_BROWSE: {
                final SongInfo s = songInfos.get(i);
                ((SongHolderBrowse)songHolder).display(s);
                break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return songInfos.size();
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }


    class SongHolderCreation extends RecyclerView.ViewHolder {
        Context context;
        TextView tvSongTitle;
        ImageButton buttonDelete;
        SongInfo currentSong;

        public SongHolderCreation(View itemView) {
            super(itemView);
            context = itemView.getContext();
            tvSongTitle = itemView.findViewById(R.id.songTitle);
            buttonDelete = itemView.findViewById(R.id.addMusic);

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SongAdapter a = (SongAdapter) recyclerView.getAdapter();
                    a.remove(currentSong);
                    a.notifyDataSetChanged();
                    try {
                        FileHelper.removeSong(context, rvType, currentSong);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        void display(SongInfo s){
            currentSong = s;
            tvSongTitle.setText(s.getSongName());
        }
    }

    class SongHolderPlay extends RecyclerView.ViewHolder {
        Context context;
        TextView tvSongTitle;
        ImageButton buttonPlay;
        ImageButton buttonPause;
        ImageButton buttonStop;
        SongInfo currentSong;
        MediaPlayer mp;
        int pausePosition;

        public SongHolderPlay(View itemView) {
            super(itemView);
            context = itemView.getContext();
            tvSongTitle = itemView.findViewById(R.id.songTitle);
            buttonPlay = itemView.findViewById(R.id.playMusic);
            buttonPause = itemView.findViewById(R.id.pauseMusic);
            buttonStop = itemView.findViewById(R.id.stopMusic);

            buttonPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mp == null){
                        mp = MediaPlayer.create(context, Uri.parse(currentSong.getSongUrl()));
                        mp.start();
                    } else if(!mp.isPlaying()){
                        mp.seekTo(pausePosition);
                        mp.start();
                    }
                }
            });

            buttonPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mp != null){
                        mp.pause();
                        pausePosition = mp.getCurrentPosition();
                    }
                }
            });

            buttonStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mp != null){
                        mp.stop();
                        mp=null;
                    }
                }
            });

        }

        void display(SongInfo s){
            currentSong = s;
            tvSongTitle.setText(s.getSongName());
        }
    }

    class SongHolderBrowse extends RecyclerView.ViewHolder /*implements View.OnClickListener*/{
        private Context context;
        private TextView tvSongTitle;
        private ImageButton btValidate;
        private SongInfo currentSong;

        public SongHolderBrowse(View itemView) {
            super(itemView);
            context = itemView.getContext();
            tvSongTitle = itemView.findViewById(R.id.songName);

            btValidate = itemView.findViewById(R.id.validateButtonSong);
            btValidate.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {

                    if(rvType == SongAdapter.RV_TOP){
                        RecyclerView rv = listener.getRvTop();
                        SongAdapter a = (SongAdapter) rv.getAdapter();
                        a.add(currentSong);
                        a.notifyDataSetChanged();
                        try {
                            FileHelper.savePlaylist(context, SongAdapter.RV_TOP, currentSong );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                    RecyclerView rv = listener.getRvBot();
                    SongAdapter a = (SongAdapter) rv.getAdapter();
                    a.add(currentSong);
                    a.notifyDataSetChanged();
                        try {
                            FileHelper.savePlaylist(context, SongAdapter.RV_BOT, currentSong );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        }

        void display(SongInfo s){
            currentSong = s;
            tvSongTitle.setText(s.getSongName());
        }


    }
}


