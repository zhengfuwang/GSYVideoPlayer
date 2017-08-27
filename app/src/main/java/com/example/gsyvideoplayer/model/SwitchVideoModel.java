package com.example.gsyvideoplayer.model;

import com.shuyu.gsyvideoplayer.model.VideoPlayModel;

/**
 * Created by shuyu on 2016/12/7.
 */

public class SwitchVideoModel {
    private String url;
    private String name;
    private VideoPlayModel mVideoPlayModel;

    public SwitchVideoModel(String name, String url) {
        this.name = name;
        this.url = url;
        this.mVideoPlayModel = new VideoPlayModel("", url);
    }

    public String getUrl() {
        return mVideoPlayModel.getVideoUrl();
    }

    public void setUrl(String url) {
        this.mVideoPlayModel.setVideoUrl(url);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VideoPlayModel getVideoPlayModel() {
        return mVideoPlayModel;
    }

    @Override
    public String toString() {
        return this.name;
    }
}