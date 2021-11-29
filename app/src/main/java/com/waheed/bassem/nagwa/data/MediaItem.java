package com.waheed.bassem.nagwa.data;

public class MediaItem {

    private final int id;
    private final String type;
    private final String name;
    private final String url;
    private int downloadState;
    private int downloadProgress;
    private String filePath;
    private boolean isExpanded;

    public MediaItem(int id, String type, String name, String url) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.url = url;
        this.downloadState = Constants.DownloadState.NOT_DOWNLOADED;
        this.downloadProgress = 0;
        this.filePath = Constants.JsonConstants.DEFAULT_STRING;
        this.isExpanded = false;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getDownloadState() {
        return downloadState;
    }

    public int getDownloadProgress() {
        return downloadProgress;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setDownloadState(int downloadState) {
        this.downloadState = downloadState;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void invertExpanded() {
        isExpanded = !isExpanded;
    }

    public void setDownloadProgress(int downloadProgress) {
        this.downloadProgress = downloadProgress;
    }
}
