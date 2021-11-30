package com.waheed.bassem.nagwa.network;

interface ProgressListener {

    void update(long bytesRead, long contentLength, boolean done);

}
