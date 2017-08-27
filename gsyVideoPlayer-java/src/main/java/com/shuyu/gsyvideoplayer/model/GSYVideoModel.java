package com.shuyu.gsyvideoplayer.model;

/**
 * Created by shuyu on 2016/12/20.
 */

public class GSYVideoModel {

    private String mUrl;
    private String mTitle;
    private VideoPlayModel mVideoPlayModel;

    public GSYVideoModel(String url, String title) {
        mUrl = url;
        mTitle = title;
        mVideoPlayModel = new VideoPlayModel("", mUrl);
    }

    public String getUrl() {
        return mVideoPlayModel.getVideoUrl();
    }

    public void setUrl(String url) {
        this.mVideoPlayModel.setVideoUrl(url);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public VideoPlayModel getVideoPlayModel() {
        return mVideoPlayModel;
    }
}
