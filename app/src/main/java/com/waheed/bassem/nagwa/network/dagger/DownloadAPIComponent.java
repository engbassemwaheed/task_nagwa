package com.waheed.bassem.nagwa.network.dagger;

import com.waheed.bassem.nagwa.network.DownloadAPI;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {DownloadAPIModule.class})
public interface DownloadAPIComponent {

    DownloadAPI getDownloadAPI ();

}
