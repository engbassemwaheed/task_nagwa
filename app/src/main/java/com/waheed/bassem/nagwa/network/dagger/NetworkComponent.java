package com.waheed.bassem.nagwa.network.dagger;

import com.waheed.bassem.nagwa.network.FileDownloader;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = NetworkModule.class)
public interface NetworkComponent {

    FileDownloader getFileDownloader();

}
