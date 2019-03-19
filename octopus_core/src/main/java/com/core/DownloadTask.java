package com.core;

import android.os.Message;

import com.status.DownloadStatus;
import com.ui.UiHandler;
import com.util.Util;

import java.io.FileOutputStream;
import java.io.InputStream;
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

    public DownloadTask(String url, String destination, UiHandler handler) {
        this.url = url;
        this.destination = destination;
        this.uiHandler = handler;
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


    @Override
    public Object call() throws Exception {
        Message message = null;
        String status = downloadFile(url, destination) ? DownloadStatus.COMPLETED.name() : DownloadStatus.FAILED.name();
        if (status.equals(DownloadStatus.COMPLETED.name())) {
            message = Util.createMessage(Thread.currentThread().getId(), DownloadStatus.COMPLETED);
        } else if (status.equals(DownloadStatus.FAILED.name())) {
            message = Util.createMessage(Thread.currentThread().getId(), DownloadStatus.COMPLETED);
        }

        uiHandler.sendMessage(message);
        return null;
    }
}
