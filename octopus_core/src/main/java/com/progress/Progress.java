package com.progress;

import java.io.Serializable;

public class Progress implements Serializable {
    private long currentBytes;
    private long totalBytes;

    public  Progress(long currentBytes , long totalBytes){
        this.currentBytes = currentBytes;
        this.totalBytes = totalBytes;
    }


    @Override
    public String toString() {
        return  "Progress{" +
                "currentBytes"+currentBytes+
                "totalBytes"+totalBytes+
                "}";
    }
}
