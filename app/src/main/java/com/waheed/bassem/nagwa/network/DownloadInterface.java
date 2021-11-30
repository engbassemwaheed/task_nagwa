package com.waheed.bassem.nagwa.network;

import com.waheed.bassem.nagwa.data.MediaItem;

public interface DownloadInterface {

    void onDownloadStarted (MediaItem mediaItem);
    void onDownloadFinished (MediaItem mediaItem);
    void onDownloadProgress(MediaItem mediaItem);
}
