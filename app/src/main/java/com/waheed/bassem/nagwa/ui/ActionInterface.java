package com.waheed.bassem.nagwa.ui;

import com.waheed.bassem.nagwa.data.MediaItem;

public interface ActionInterface {

    void onDownloadRequested (MediaItem mediaItem);
    void onOpenRequested (MediaItem mediaItem);
    void onDeleteRequested (MediaItem mediaItem);

}
