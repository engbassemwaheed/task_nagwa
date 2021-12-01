package com.waheed.bassem.nagwa.network.dagger;

import com.waheed.bassem.nagwa.network.DownloadInterface;
import com.waheed.bassem.nagwa.network.FileDownloader;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NetworkModule {

    private final DownloadInterface downloadInterface;

    public NetworkModule(DownloadInterface downloadInterface) {
        this.downloadInterface = downloadInterface;
    }

    @Singleton
    @Provides
    public FileDownloader provideFileDownloader () {
        return new FileDownloader(downloadInterface);
    }

}
