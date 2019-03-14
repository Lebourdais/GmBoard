package com.example.martin.gmboard;

public class SongInfo {
    private String songName;
    private String songUrl;

    public SongInfo() {
    }

    public SongInfo(String songname, String songUrl) {
        songName = songname;
        this.songUrl = songUrl;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }


    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SongInfo)) return false;
        SongInfo s = (SongInfo) o;
        // Name is unique for each unit
        return songName.equals(s.getSongName()) && songUrl.equals(s.getSongUrl());
    }
}