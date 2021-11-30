package com.waheed.bassem.nagwa.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Pair;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.waheed.bassem.nagwa.data.MediaDataManager;
import com.waheed.bassem.nagwa.data.MediaItem;
import com.waheed.bassem.nagwa.network.DownloadClient;
import com.waheed.bassem.nagwa.network.DownloadInterface;
import com.waheed.bassem.nagwa.utils.Utilities;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MainViewModel extends ViewModel implements DownloadInterface {

    private final MutableLiveData<ArrayList<MediaItem>> mediaItemsMutableLiveData;
    private final MutableLiveData<Pair<Integer, MediaItem>> progressMutableLiveData;
    private final MutableLiveData<Integer> toDownloadMutableLiveData;
    private final MediaDataManager mediaDataManager;
    private final DownloadClient downloadClient;
    private final ArrayList<MediaItem> mediaItems;


    public MainViewModel() {
        mediaItemsMutableLiveData = new MutableLiveData<>();
        toDownloadMutableLiveData = new MutableLiveData<>();
        progressMutableLiveData = new MutableLiveData<>();
        mediaDataManager = MediaDataManager.getInstance();
        downloadClient = DownloadClient.getInstance(this);
        mediaItems = new ArrayList<>();
    }

    public MutableLiveData<ArrayList<MediaItem>> getMediaItemsMutableLiveData() {
        return mediaItemsMutableLiveData;
    }

    public MutableLiveData<Integer> getToDownloadMutableLiveData() {
        return toDownloadMutableLiveData;
    }

    public MutableLiveData<Pair<Integer, MediaItem>> getProgressMutableLiveData() {
        return progressMutableLiveData;
    }

    public void getMediaItems(Context context) {
        if (mediaItems.size() == 0) {
            mediaItems.addAll(mediaDataManager.getMediaItems(context));
            mediaItemsMutableLiveData.setValue(mediaItems);
        }
    }

    public void downloadFile(MediaItem mediaItem) {
        downloadClient.downloadFile(mediaItem);
    }

    @Override
    public void onDownloadStarted(MediaItem mediaItem) {
        toDownloadMutableLiveData.setValue(mediaItems.indexOf(mediaItem));
    }

    @Override
    public void onDownloadFinished(MediaItem mediaItem) {
        toDownloadMutableLiveData.setValue(mediaItems.indexOf(mediaItem));
    }

    @Override
    public void onDownloadProgress(MediaItem mediaItem) {
        progressMutableLiveData.postValue(new Pair<>(mediaItems.indexOf(mediaItem), mediaItem));
    }

    public void openFile(Context context, MediaItem mediaItem) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(Utilities.getDataFile(mediaItem).getAbsolutePath());
        intent.setDataAndType(uri, "*/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "Open folder"));
    }
}
