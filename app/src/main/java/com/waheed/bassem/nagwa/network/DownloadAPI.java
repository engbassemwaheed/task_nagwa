package com.waheed.bassem.nagwa.network;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface DownloadAPI {

    @Streaming
    @GET
    Observable<Response<ResponseBody>> downloadFile (@Url String url);

}
