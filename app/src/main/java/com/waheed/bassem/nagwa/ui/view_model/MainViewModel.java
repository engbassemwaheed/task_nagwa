package com.waheed.bassem.nagwa.ui.view_model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.waheed.bassem.nagwa.R;
import com.waheed.bassem.nagwa.data.MediaDataManager;
import com.waheed.bassem.nagwa.data.MediaItem;
import com.waheed.bassem.nagwa.network.FileDownloader;
import com.waheed.bassem.nagwa.network.DownloadInterface;
import com.waheed.bassem.nagwa.network.dagger.DaggerNetworkComponent;
import com.waheed.bassem.nagwa.network.dagger.NetworkModule;
import com.waheed.bassem.nagwa.ui.ActivityInterface;
import com.waheed.bassem.nagwa.utils.NagwaFileUtils;
import com.waheed.bassem.nagwa.utils.NagwaPermissionManager;

import java.util.ArrayList;


public class MainViewModel extends ViewModel implements DownloadInterface {

    private static final int STORAGE_PERMISSION_DIALOG = 1;

    private final MutableLiveData<ArrayList<MediaItem>> mediaItemsMutableLiveData;
    private final MutableLiveData<Pair<Integer, MediaItem>> progressMutableLiveData;
    private final MutableLiveData<Integer> toDownloadMutableLiveData;
    private final MediaDataManager mediaDataManager;
    private final FileDownloader fileDownloader;
    private final ArrayList<MediaItem> mediaItems;
    private ActivityInterface activityInterface;
    private MediaItem pendingMediaItem = null;
    private boolean pendingIsDownload = true;


    public MainViewModel() {
        mediaItemsMutableLiveData = new MutableLiveData<>();
        toDownloadMutableLiveData = new MutableLiveData<>();
        progressMutableLiveData = new MutableLiveData<>();
        mediaDataManager = MediaDataManager.getInstance();
        fileDownloader = DaggerNetworkComponent.builder()
                .networkModule(new NetworkModule(this))
                .build()
                .getFileDownloader();
        mediaItems = new ArrayList<>();
    }

    public void setActivityInterface(ActivityInterface activityInterface) {
        this.activityInterface = activityInterface;
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
            if (NagwaPermissionManager.checkStoragePermission(context)) {
                handleExisting();
            }
            mediaItemsMutableLiveData.setValue(mediaItems);
        }
    }

    public void downloadFile(AppCompatActivity appCompatActivity, MediaItem mediaItem) {
        if (NagwaPermissionManager.checkStoragePermission(appCompatActivity)) {
            fileDownloader.downloadFile(mediaItem);
        } else {
            requestPermission(appCompatActivity, mediaItem, true);
        }
    }

    public void openFile(Context context, MediaItem mediaItem) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(NagwaFileUtils.getDataFile(mediaItem).getAbsolutePath());
        intent.setDataAndType(uri, "*/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "Open folder"));
    }

    public void deleteFile(AppCompatActivity appCompatActivity, MediaItem mediaItem) {
        if (NagwaPermissionManager.checkStoragePermission(appCompatActivity)) {
            NagwaFileUtils.deleteFile(mediaItem);
            mediaItem.setNotDownloaded();
            toDownloadMutableLiveData.setValue(mediaItems.indexOf(mediaItem));
        } else {
            requestPermission(appCompatActivity, mediaItem, false);
        }
    }

    public void onAcceptanceDialogResult(AppCompatActivity appCompatActivity, int code, boolean isAccepted) {
        switch (code) {
            case STORAGE_PERMISSION_DIALOG:
                if (isAccepted) {
                    NagwaPermissionManager.askForStoragePermission(appCompatActivity);
                } else {
                    if (pendingMediaItem != null) {
                        pendingMediaItem.setNotDownloaded();
                        toDownloadMutableLiveData.setValue(mediaItems.indexOf(pendingMediaItem));
                    }
                    activityInterface.showSnackBar(NagwaPermissionManager.getDeniedPermissionMessage());
                }
                break;
        }
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

    public void onRequestedPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Boolean result = NagwaPermissionManager.isPermissionGranted(requestCode, permissions, grantResults);
        if (result != null) {
            if (result) {
                if (pendingMediaItem != null) {
                    if (pendingIsDownload) {
                        fileDownloader.downloadFile(pendingMediaItem);
                    } else {
                        NagwaFileUtils.deleteFile(pendingMediaItem);
                    }
                } else {
                    displayGeneralError();
                }
            } else {
                activityInterface.showSnackBar(NagwaPermissionManager.getDeniedPermissionMessage());
            }
        }
    }

    private void handleExisting() {
        for (MediaItem mediaItem : mediaItems) {
            if (NagwaFileUtils.isExist(mediaItem)) {
                mediaItem.setDownloaded();
            }
        }
    }

    private void requestPermission(AppCompatActivity appCompatActivity, MediaItem mediaItem, boolean isDownload) {
        pendingMediaItem = mediaItem;
        pendingIsDownload = isDownload;
        Pair<String, String> descriptionStrings = NagwaPermissionManager.getDescriptionDialogTexts(appCompatActivity);
        if (activityInterface != null) {
            activityInterface.showAcceptanceDialog(descriptionStrings.first,
                    descriptionStrings.second,
                    STORAGE_PERMISSION_DIALOG);
        }
    }

    private void displayGeneralError() {
        if (activityInterface != null) activityInterface.showSnackBar(R.string.general_error);
    }


}
