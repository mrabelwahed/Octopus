package com.core;

import android.os.Build;
import android.os.Message;
import android.util.Log;

import com.status.DownloadStatus;
import com.ui.UIThreadCallback;
import com.ui.UiHandler;
import com.util.Util;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * DownloadTask is used for sending tasks to the thread pool. When a callable is submitted,
 * a Future object is returned, allowing the thread pool manager to stop the task.
 */

public class DownloadTask implements Callable {
    private String url;
    private String destination;
    private UiHandler uiHandler;
    private String fileName;
    private int progress = -1;
    private WeakReference<UIThreadCallback> mUiThreadCallbackWeakReference;
    private long id;
    public DownloadTask(long id, String url, String destination,UIThreadCallback uiThreadCallback) {
        this.id = id;
        this.url = url;
        this.destination = destination;
        this.mUiThreadCallbackWeakReference = new WeakReference<>(uiThreadCallback);
        uiHandler = new UiHandler(mUiThreadCallbackWeakReference);
    }

    public DownloadTask(Builder builder) {
        this.id = builder.id;
        this.url = builder.url;
        this.destination = builder.destination;
        this.mUiThreadCallbackWeakReference = builder.mUiThreadCallbackWeakReference;
        uiHandler = new UiHandler(mUiThreadCallbackWeakReference);
        this.fileName = builder.fileName;
    }


    private boolean downloadFile(String url, String destination) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();

            Response response = client.newCall(request).execute();

            InputStream in = response.body().byteStream();
            FileOutputStream fileOutput =
                    new FileOutputStream(destination);

            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            long total = 0l;
            while ((bufferLength = in.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
            }
            fileOutput.close();
            response.body().close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public void setDestination(String destination){
        this.destination = destination;
    }

    public String getUrl() {
        return url;
    }

    private void updateDownloadProgressBar(){
        Message message = new Message();
        message.what = (int) this.id;
        message.arg1 = progress;
        uiHandler.sendMessage(message);
    }
    @Override
    public Object call() throws Exception {
        Message message = null;
        String status = downloadFile(url, destination) ? DownloadStatus.COMPLETED.name() : DownloadStatus.FAILED.name();
        if (status.equals(DownloadStatus.COMPLETED.name())) {
            message = Util.createMessage(this.id, DownloadStatus.COMPLETED);
        } else if (status.equals(DownloadStatus.FAILED.name())) {
            message = Util.createMessage(this.id, DownloadStatus.FAILED);
        }
        message.arg1 = -1;
        uiHandler.sendMessage(message);
        return null;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public static class Builder {
        private String url;
        private String destination;
        private String fileName;
        private WeakReference<UIThreadCallback> mUiThreadCallbackWeakReference;
        private long id;

        public Builder(String url){
            this.url = url;
        }
        public Builder fileName(String fileName){this.fileName = fileName; return this;}
        public Builder destination(String destinationPath){this.destination = destinationPath; return this;}
        public Builder UiThreadCallback(UIThreadCallback uiThreadCallback){this.mUiThreadCallbackWeakReference = new WeakReference<UIThreadCallback>(uiThreadCallback); return this;}
        public Builder fileId(long id){this.id = id; return this;}
        public DownloadTask build(){
            return new DownloadTask(this);
        }

    }
}
