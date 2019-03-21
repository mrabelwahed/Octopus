package com.ui;

import android.os.Handler;
import android.os.Message;

import com.status.DownloadResult;
import com.status.DownloadStatus;
import com.util.Util;

import java.lang.ref.WeakReference;

public class UiHandler extends Handler {


    WeakReference<UIThreadCallback> mUithUiThreadCallbackWeakReference;

    public UiHandler(WeakReference<UIThreadCallback> mUIUiThreadCallbackWeakReference) {
        this.mUithUiThreadCallbackWeakReference = mUIUiThreadCallbackWeakReference;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        DownloadResult result = new DownloadResult();
        result.setId(msg.what);
        result.setProgress(msg.arg1);
        if(msg.getData() != null) {
            if (msg.getData().getSerializable(Util.MESSAGE_BODY).equals(DownloadStatus.COMPLETED))
                result.setDownloadStatus(DownloadStatus.COMPLETED);
            else if (msg.getData().getSerializable(Util.MESSAGE_BODY).equals(DownloadStatus.FAILED))
                result.setDownloadStatus(DownloadStatus.FAILED);
            else if (msg.getData().getSerializable(Util.MESSAGE_BODY).equals(DownloadStatus.CANCEL_ALL))
                result.setDownloadStatus(DownloadStatus.CANCEL_ALL);
        }
        if (mUithUiThreadCallbackWeakReference != null && mUithUiThreadCallbackWeakReference.get() != null)
            mUithUiThreadCallbackWeakReference.get().publishToUIThread(result);

    }

}
