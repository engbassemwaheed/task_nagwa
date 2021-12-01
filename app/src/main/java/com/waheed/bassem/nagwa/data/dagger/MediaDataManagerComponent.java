package com.waheed.bassem.nagwa.data.dagger;

import com.waheed.bassem.nagwa.data.MediaDataManager;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = MediaDataManagerModule.class)
public interface MediaDataManagerComponent {

    MediaDataManager getMediaDataManager();
}
