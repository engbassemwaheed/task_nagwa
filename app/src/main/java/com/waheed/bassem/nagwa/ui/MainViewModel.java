package com.waheed.bassem.nagwa.ui;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.waheed.bassem.nagwa.data.MediaDataManager;
import com.waheed.bassem.nagwa.data.MediaItem;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<MediaItem>> mediaItemsMutableLiveData;
    private final MediaDataManager mediaDataManager;

    public MainViewModel() {
        mediaItemsMutableLiveData = new MutableLiveData<>();
        mediaDataManager = MediaDataManager.getInstance();
    }

    public MutableLiveData<ArrayList<MediaItem>> getMediaItemsMutableLiveData() {
        return mediaItemsMutableLiveData;
    }

    public void getMediaItems (Context context) {
        mediaItemsMutableLiveData.setValue(mediaDataManager.getMediaItems(context));
    }
}
