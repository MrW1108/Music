package com.example.y.study.myclass;

import org.litepal.crud.DataSupport;

public class MyMusic extends DataSupport{
    private String musicId;
    private String musicName;
    private String singer;
    private String path;
    private int size;
    private int time;
    private String album;

    public MyMusic() {
    }

    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        singer = singer;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public MyMusic(String musicId, String musicName, String singer, String path, int size, int time, String album) {
        this.musicId = musicId;
        this.musicName = musicName;
        this.singer = singer;
        this.path = path;
        this.size = size;
        this.time = time;
        this.album = album;
    }
}
