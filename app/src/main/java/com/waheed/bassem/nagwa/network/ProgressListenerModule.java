package com.waheed.bassem.nagwa.network;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ProgressListenerModule {

    @Binds
    abstract ProgressListener bindProgressListener(FileDownloader fileDownloader);

}
