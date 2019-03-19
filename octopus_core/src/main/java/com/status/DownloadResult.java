package com.status;

public class DownloadResult {
    private long id;
    private DownloadStatus downloadStatus;
    private String localPath;
    private String url;

    public long getId() {
        return id;
    }

    public DownloadStatus getDownloadStatus() {
        return downloadStatus;
    }

    public String getLocalPath() {
        return localPath;
    }

    public String getUrl() {
        return url;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDownloadStatus(DownloadStatus downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
