package com.shuyu.gsyvideoplayer.model;

/**
 * Created by xiyangyuge on 2017/8/26.
 */

public class VideoPlayModel {

    public String videoId;
    public String videoUrl;
    public String videoSize;
    public boolean isWifiConnected;

    public VideoPlayModel(String videoId, String videoUrl, boolean isWifiConnected) {
        this.videoId = videoId;
        this.videoUrl = videoUrl;
        this.isWifiConnected = isWifiConnected;
    }

    public VideoPlayModel(String videoId, String videoUrl, String videoSize, boolean isWifiConnected) {
        this.videoId = videoId;
        this.videoUrl = videoUrl;
        this.videoSize = "0".equals(videoSize) ? null : videoSize;
        this.isWifiConnected = isWifiConnected;
    }
}
