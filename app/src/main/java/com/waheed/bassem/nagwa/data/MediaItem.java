package com.waheed.bassem.nagwa.data;

import com.waheed.bassem.nagwa.data.Constants.DownloadState;

public class MediaItem {

    private int id;
    private String type;
    private String name;
    private String url;
    private int downloadState;
    private String filePath;

    public MediaItem(int id, String type, String name, String url) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.url = url;
        this.downloadState = DownloadState.NOT_DOWNLOADED;
        this.filePath = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(int downloadState) {
        this.downloadState = downloadState;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
