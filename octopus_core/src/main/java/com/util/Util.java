package com.util;

import android.os.Bundle;
import android.os.Message;

import com.status.DownloadStatus;

public class Util {

    public static  final String  LOG_TAG ="Background Thread";
    public static final String MESSAGE_BODY ="MESSAGE_BODY";
    public static  final String EMPTY_MESSAGE ="<EMPTY_MESSAGE>";
    public  static final  int MESSAGE_ID =1;


    public static Message createMessage(long id , DownloadStatus data){
        Bundle bundle = new Bundle();
        bundle.putSerializable(MESSAGE_BODY,data);
        Message message = new Message();
        message.what = (int)id;
        message.setData(bundle);
        return message;
    }

    public static String getFileExtension(String fileName){
        String extension = "";

        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (i > p) {
            extension = fileName.substring(i+1);
        }
       return extension;
    }

}
