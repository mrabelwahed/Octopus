package com.status;

public interface DownloadListener {
    void onCompleted(DownloadResult result);

    void onFailed(Error error);
}
