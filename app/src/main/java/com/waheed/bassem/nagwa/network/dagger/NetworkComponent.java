package com.waheed.bassem.nagwa.network.dagger;

import com.waheed.bassem.nagwa.network.DownloadAPI;
import com.waheed.bassem.nagwa.network.FileDownloader;
import com.waheed.bassem.nagwa.network.ProgressListener;
import com.waheed.bassem.nagwa.network.ProgressListenerModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Provides;

@Singleton
@Component(modules = {NetworkModule.class})
public interface NetworkComponent {

    DownloadAPI getDownloadAPI ();

}
