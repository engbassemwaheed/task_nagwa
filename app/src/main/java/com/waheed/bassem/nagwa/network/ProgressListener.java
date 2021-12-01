package com.waheed.bassem.nagwa.network;

public interface ProgressListener {

    void update(long bytesRead, long contentLength, boolean done);

}
