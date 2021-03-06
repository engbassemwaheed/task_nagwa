package com.waheed.bassem.nagwa.network.dagger;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.waheed.bassem.nagwa.network.DownloadAPI;
import com.waheed.bassem.nagwa.network.ProgressListener;
import com.waheed.bassem.nagwa.network.ProgressResponseBody;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class DownloadAPIModule {

    private static final String BASE_URL = "https://www.google.com";
    private final ProgressListener progressListener;

    public DownloadAPIModule(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    @Singleton
    @Provides
    public Interceptor getNetworkInterceptor() {
        return chain -> {
            Response response = chain.proceed(chain.request());
            return response.newBuilder()
                    .body(new ProgressResponseBody(response.body(), progressListener))
                    .build();
        };
    }

    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient(Interceptor interceptor) {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .build();
    }

    @Singleton
    @Provides
    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Singleton
    @Provides
    public DownloadAPI provideDownloadAPI(Retrofit retrofit) {
        return retrofit.create(DownloadAPI.class);
    }

}
