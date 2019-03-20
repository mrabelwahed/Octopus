package com.core;

import com.status.DownloadStatus;
import com.ui.UIThreadCallback;
import com.ui.UiHandler;
import com.util.Util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * this class will manage all file download operations
 */
public class DownloadManager {
    private final ThreadPoolExecutor downloadThreadPool;
    private final BlockingQueue<Runnable> mDownloadQueue;
//    private WeakReference<UIThreadCallback> mUiThreadCallbackWeakReference;
    private List<Future> mRunningDownloadList = new ArrayList<>();
//    private UiHandler uiHandler;

    public static DownloadManager mDownloadManager;

    private DownloadManager() {
        mDownloadQueue = new LinkedBlockingQueue<>();
        downloadThreadPool = new ThreadPoolExecutor(Constants.CORE_POOL_SIZE
                                                   , Constants.MAX_POOL_SIZE
                                                   , Constants.KEEP_ALIVE_TIME
                                                   , TimeUnit.MILLISECONDS,
                                                        mDownloadQueue);
    }

    public static DownloadManager getInstance() {
        if (mDownloadManager == null)
            mDownloadManager = new DownloadManager();
        return mDownloadManager;
    }


//    public void downloadFile(String url ,String localPath,UIThreadCallback uiThreadCallback) {
//        addDownloadTask(new DownloadTask(url, localPath, uiThreadCallback));
//    }
    public void downloadFile(DownloadTask task){
        addDownloadTask(task);
    }

    private void addDownloadTask(Callable task) {
        mRunningDownloadList.add(downloadThreadPool.submit(task));
    }


//    public void setUiThreadCallback(UIThreadCallback mUiThreadCallback) {
//        this.mUiThreadCallbackWeakReference = new WeakReference<>(mUiThreadCallback);
//        uiHandler = new UiHandler(mUiThreadCallbackWeakReference);
//    }


//    public void cancelAll() {
//        synchronized (this) {
//            mDownloadQueue.clear();
//            for (Future task : mRunningDownloadList) {
//                if (!task.isDone()) {
//                    task.cancel(true);
//                }
//            }
//
//            mRunningDownloadList.clear();
//        }
//
//        uiHandler.sendMessage(Util.createMessage(Util.MESSAGE_ID, DownloadStatus.CANCEL_ALL));
//    }


}
