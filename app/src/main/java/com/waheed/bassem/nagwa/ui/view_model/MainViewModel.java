package com.waheed.bassem.nagwa.ui.view_model;

import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.waheed.bassem.nagwa.R;
import com.waheed.bassem.nagwa.data.MediaDataManager;
import com.waheed.bassem.nagwa.data.MediaItem;
import com.waheed.bassem.nagwa.data.dagger.DaggerMediaDataManagerComponent;
import com.waheed.bassem.nagwa.network.FileDownloader;
import com.waheed.bassem.nagwa.network.DownloadInterface;
import com.waheed.bassem.nagwa.network.dagger.DaggerNetworkComponent;
import com.waheed.bassem.nagwa.network.dagger.NetworkModule;
import com.waheed.bassem.nagwa.ui.ActivityInterface;
import com.waheed.bassem.nagwa.utils.FileOpenUtils;
import com.waheed.bassem.nagwa.utils.NagwaFileUtils;
import com.waheed.bassem.nagwa.utils.NagwaPermissionUtils;

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

        mediaDataManager = DaggerMediaDataManagerComponent.create().getMediaDataManager();

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
            if (NagwaPermissionUtils.checkStoragePermission(context)) {
                handleExisting();
            }
            mediaItemsMutableLiveData.setValue(mediaItems);
        }
    }

    public void downloadFile(AppCompatActivity appCompatActivity, MediaItem mediaItem) {
        if (NagwaPermissionUtils.checkStoragePermission(appCompatActivity)) {
            fileDownloader.downloadFile(appCompatActivity, mediaItem);
        } else {
            requestPermission(appCompatActivity, mediaItem, true);
        }
    }

    public void openFile(Context context, MediaItem mediaItem) {
        Intent intent = FileOpenUtils.getIntent(context, mediaItem);
        if (intent != null) {
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.open_folder)));
        } else {
            activityInterface.showSnackBar(FileOpenUtils.getErrorMessage(context, mediaItem.getExtension()));
        }
    }

    public void deleteFile(AppCompatActivity appCompatActivity, MediaItem mediaItem) {
        if (NagwaPermissionUtils.checkStoragePermission(appCompatActivity)) {
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
                    NagwaPermissionUtils.askForStoragePermission(appCompatActivity);
                } else {
                    handleRejectedPermission();
                }
                break;
        }
    }

    private void handleRejectedPermission() {
        if (pendingMediaItem != null) {
            pendingMediaItem.setNotDownloaded();
            toDownloadMutableLiveData.setValue(mediaItems.indexOf(pendingMediaItem));
        }
        activityInterface.showSnackBar(NagwaPermissionUtils.getDeniedPermissionMessage());
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

    @Override
    public void onError(int networkErrorStringId) {
        activityInterface.showSnackBar(networkErrorStringId);
        activityInterface.notifyDataChanged();
    }

    public void onRequestedPermissionResult(Context context, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Boolean result = NagwaPermissionUtils.isPermissionGranted(requestCode, permissions, grantResults);
        if (result != null) {
            if (result) {
                if (pendingMediaItem != null) {
                    if (pendingIsDownload) {
                        fileDownloader.downloadFile(context, pendingMediaItem);
                    } else {
                        NagwaFileUtils.deleteFile(pendingMediaItem);
                    }
                } else {
                    displayGeneralError();
                }
            } else {
                handleRejectedPermission();
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
        Pair<String, String> descriptionStrings = NagwaPermissionUtils.getDescriptionDialogTexts(appCompatActivity);
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
