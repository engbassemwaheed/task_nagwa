package com.waheed.bassem.nagwa.network;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.waheed.bassem.nagwa.data.MediaItem;
import com.waheed.bassem.nagwa.utils.Utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DownloadClient implements ProgressListener {

    private static final String TAG = "DownloadClient";
    private static final String BASE_URL = "https://www.google.com";
    private static final int MAX_DOWNLOAD_TRIALS = 3;

    private static DownloadClient downloadClient;
    private final DownloadAPI downloadAPI;
    private final ArrayList<MediaItem> mediaItemArrayList;
    private final DownloadInterface downloadInterface;
    private MediaItem mediaItem;

    private DownloadClient(DownloadInterface downloadInterface) {
        this.downloadInterface = downloadInterface;
        mediaItemArrayList = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(buildOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        downloadAPI = retrofit.create(DownloadAPI.class);
    }

    private OkHttpClient buildOkHttpClient() {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(chain -> {
                    okhttp3.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                            .body(new ProgressResponseBody(originalResponse.body(), this))
                            .build();
                })
                .build();
    }

    public static DownloadClient getInstance(DownloadInterface downloadInterface) {
        if (downloadClient == null) {
            downloadClient = new DownloadClient(downloadInterface);
        }
        return downloadClient;
    }

    public void downloadFile(MediaItem mediaItem) {
        mediaItemArrayList.add(mediaItem);
        if (mediaItemArrayList.size() == 1) {
            download();
        }
    }

    private void download() {
        if (mediaItemArrayList.size() > 0) {
            mediaItem = null;
            mediaItem = mediaItemArrayList.get(0);

            mediaItem.setDownloading();
            downloadInterface.onDownloadStarted(mediaItem);

            downloadAPI.downloadFile(mediaItem.getUrl())
                    .retry(throwable -> {
                        mediaItem.incrementDownloadTrials();
                        return (!mediaItem.isDownloaded()) && mediaItem.getDownloadTrials() < MAX_DOWNLOAD_TRIALS;
                    })
                    .flatMap((Response<ResponseBody> response) -> saveFile(response, mediaItem))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<File>() {
                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            Log.e(TAG, "onError e = " + e.getMessage());
                            mediaItem.incrementDownloadTrials();
                            if (mediaItem.getDownloadTrials() >= MAX_DOWNLOAD_TRIALS) {
                                mediaItem.setDownloadingError();
                                downloadInterface.onDownloadFinished(mediaItem);
                                mediaItemArrayList.remove(0);
                            }
                            download();
                        }

                        @Override
                        public void onComplete() {
                            Log.e(TAG, "onComplete");
                            downloadInterface.onDownloadFinished(mediaItem);
                            mediaItemArrayList.remove(0);
                            download();
                        }

                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                        }

                        @Override
                        public void onNext(@NonNull File file) {
                            Log.e(TAG, "onNext");
                            mediaItem.setDownloaded();
                        }
                    });
        } else {
            Log.e(TAG, "download: all downloaded");
        }
    }

    private Observable<File> saveFile(Response<ResponseBody> response, MediaItem internalMediaItem) {
        Log.e(TAG, "saveFile:");
        return Observable.create((ObservableOnSubscribe<File>) emitter -> {
            try {
                File dataFile = Utilities.getDataFile(internalMediaItem);

                BufferedSink sink = Okio.buffer(Okio.sink(dataFile));
                sink.writeAll(response.body().source());
                sink.close();

                emitter.onNext(dataFile);
                emitter.onComplete();
            } catch (IOException e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        });
    }


    @Override
    public void update(long bytesRead, long contentLength, boolean done) {
        float progressFloat = ((float)bytesRead / (float)contentLength) * 100;
        int progressInt = (int) Math.ceil(progressFloat);
        if (progressInt % 5 == 0) {
            if (progressInt < 0) progressInt = -1;
            if (mediaItem!=null && mediaItem.getDownloadProgress() != progressInt) {
                mediaItem.setDownloadProgress(progressInt);
                downloadInterface.onDownloadProgress(mediaItem);
                Log.e(TAG, "update: progress =========================================== " + progressInt);
            }
        }
    }
}
