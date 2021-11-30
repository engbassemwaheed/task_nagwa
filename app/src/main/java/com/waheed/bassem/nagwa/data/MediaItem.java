package com.waheed.bassem.nagwa.data;

import java.io.File;

public class MediaItem {

    private final int id;
    private final String type;
    private final String name;
    private final String url;
    private int downloadState;
    private int downloadProgress;
    private boolean isExpanded;
    private int downloadTrials;

    public MediaItem(int id, String type, String name, String url) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.url = url;
        this.downloadState = Constants.DownloadState.NOT_DOWNLOADED;
        this.downloadProgress = 0;
        this.isExpanded = false;
        this.downloadTrials = 0;
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

    public boolean isExpanded() {
        return isExpanded;
    }

    public int getDownloadTrials() {
        return downloadTrials;
    }

    public void setDownloadState(int downloadState) {
        this.downloadState = downloadState;
    }

    public void invertExpanded() {
        isExpanded = !isExpanded;
    }

    public void setDownloadProgress(int downloadProgress) {
        this.downloadProgress = downloadProgress;
    }

    public void incrementDownloadTrials() {
        this.downloadTrials++;
    }

    public String getExtension() {
        switch (type) {
            case Constants.FileType.VIDEO:
                return Constants.FileExtension.VIDEO;
            case Constants.FileType.PDF:
                return Constants.FileExtension.PDF;
            default:
                return "";
        }
    }

    public boolean isNotDownloaded() {
        return downloadState == Constants.DownloadState.NOT_DOWNLOADED;
    }

    public boolean isDownloaded() {
        return downloadState == Constants.DownloadState.DOWNLOADED;
    }

    public boolean isDownloadPending() {
        return downloadState == Constants.DownloadState.PENDING_DOWNLOAD;
    }

    public boolean isDownloadError() {
        return downloadState == Constants.DownloadState.ERROR_DOWNLOAD;
    }

    public boolean isDownloading() {
        return downloadState == Constants.DownloadState.DOWNLOADING;
    }

    public void setDownloaded() {
        downloadState = Constants.DownloadState.DOWNLOADED;
    }

    public void setDownloading() {
        downloadState = Constants.DownloadState.DOWNLOADING;
    }

    public void setDownloadingError() {
        downloadState = Constants.DownloadState.ERROR_DOWNLOAD;
    }

    public void setDownloadPending() {
        downloadState = Constants.DownloadState.PENDING_DOWNLOAD;
    }
}
