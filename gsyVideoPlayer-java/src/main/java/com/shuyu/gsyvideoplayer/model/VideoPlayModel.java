package com.shuyu.gsyvideoplayer.model;

import android.text.Layout;
import android.text.TextUtils;

/**
 * Created by xiyangyuge on 2017/8/26.
 */

public class VideoPlayModel {

    public String videoId;
    private String videoUrl;
    public long videoSize;
    public String flowHintText; // 流量播放提示文字

    public VideoPlayModel() {
    }

    public VideoPlayModel(String videoId, String videoUrl) {
        this.videoId = videoId;
        this.videoUrl = videoUrl;
    }

    public VideoPlayModel(String videoId, String videoUrl, long videoSize, String flowHintText) {
        this.videoId = videoId;
        this.videoUrl = videoUrl;
        this.videoSize = videoSize;
        this.flowHintText = flowHintText;
    }

    public String getVideoUrl() {
        return videoUrl == null ? "" : videoUrl;
    }

    public void setVideoUrl(String url) {
        this.videoUrl = url;
    }
}
