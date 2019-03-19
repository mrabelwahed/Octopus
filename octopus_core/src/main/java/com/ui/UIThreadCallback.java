package com.ui;

import com.status.DownloadResult;

public interface UIThreadCallback {
    void publishToUIThread(DownloadResult result);
}
