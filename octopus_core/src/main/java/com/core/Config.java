package com.core;

/**
 * this class will be responsible for setting the configuration
 * for ThreadPoolExecutor in Download Manager
 */

public class Config {
    private int corePoolSize;
    private int maxPoolSize;
    private int keepAliveTime;
    private String localPath;

    public Config(int corePoolSize , int maxPoolSize , int keepAliveTime , String localPath){
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.localPath = localPath;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public int getKeepAliveTime() {
        return keepAliveTime;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public void setKeepAliveTime(int keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

}
