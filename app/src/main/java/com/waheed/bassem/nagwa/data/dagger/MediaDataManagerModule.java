package com.waheed.bassem.nagwa.data.dagger;

import com.waheed.bassem.nagwa.data.MediaDataManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaDataManagerModule {

    @Provides
    @Singleton
    public MediaDataManager provideMediaDataManager () {
        return new MediaDataManager();
    }
}
