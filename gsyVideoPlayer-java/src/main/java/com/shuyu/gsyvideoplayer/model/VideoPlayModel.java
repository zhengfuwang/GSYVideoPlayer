package com.shuyu.gsyvideoplayer.model;

/**
 * Created by xiyangyuge on 2017/8/26.
 */

public class VideoPlayModel {

    public String videoId;
    private String videoUrl;
    public String videoSize;

    public VideoPlayModel() {
    }

    public VideoPlayModel(String videoId, String videoUrl) {
        this.videoId = videoId;
        this.videoUrl = videoUrl;
    }

    public VideoPlayModel(String videoId, String videoUrl, String videoSize) {
        this.videoId = videoId;
        this.videoUrl = videoUrl;
        this.videoSize = "0".equals(videoSize) ? null : videoSize;
    }

    public String getVideoUrl() {
        return videoUrl == null ? "" : videoUrl;
    }

    public void setVideoUrl(String url) {
        this.videoUrl = url;
    }
}
