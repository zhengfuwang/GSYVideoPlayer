package com.shuyu.gsyvideoplayer.listener;

import com.shuyu.gsyvideoplayer.model.VideoPlayModel;

/**
 * Created by Nathen，参考jiecao结构，在其基础上修改
 * On 2016/04/04 22:13
 */
public interface VideoAllCallBack {

    // 点击了开始播放，url为空时获取播放地址，object[1]是当前所处播放器（全屏或非全屏）
    boolean onObtainMediaUrl(Object... objects);

    //加载成功，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
    void onPrepared(VideoPlayModel videoPlayModel, Object... objects);

    //点击了开始按键播放，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
    void onClickStartIcon(VideoPlayModel videoPlayModel, Object... objects);

    //点击了错误状态下的开始按键，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
    void onClickStartError(VideoPlayModel videoPlayModel, Object... objects);

    //点击了播放状态下的开始按键--->停止，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
    void onClickStop(VideoPlayModel videoPlayModel, Object... objects);

    //点击了全屏播放状态下的开始按键--->停止，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
    void onClickStopFullscreen(VideoPlayModel videoPlayModel, Object... objects);

    //点击了暂停状态下的开始按键--->播放，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
    void onClickResume(VideoPlayModel videoPlayModel, Object... objects);

    //点击了全屏暂停状态下的开始按键--->播放，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
    void onClickResumeFullscreen(VideoPlayModel videoPlayModel, Object... objects);

    //点击了空白弹出seekbar，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
    void onClickSeekbar(VideoPlayModel videoPlayModel, Object... objects);

    //点击了全屏的seekbar，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
    void onClickSeekbarFullscreen(VideoPlayModel videoPlayModel, Object... objects);

    //播放完了，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
    void onAutoComplete(VideoPlayModel videoPlayModel, Object... objects);

    //进去全屏，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
    void onEnterFullscreen(VideoPlayModel videoPlayModel, Object... objects);

    //退出全屏，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
    void onQuitFullscreen(VideoPlayModel videoPlayModel, Object... objects);

    //进入小窗口，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
    void onQuitSmallWidget(VideoPlayModel videoPlayModel, Object... objects);

    //退出小窗口，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
    void onEnterSmallWidget(VideoPlayModel videoPlayModel, Object... objects);

    //触摸调整声音，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
    void onTouchScreenSeekVolume(VideoPlayModel videoPlayModel, Object... objects);

    //触摸调整进度，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
    void onTouchScreenSeekPosition(VideoPlayModel videoPlayModel, Object... objects);

    //触摸调整亮度，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
    void onTouchScreenSeekLight(VideoPlayModel videoPlayModel, Object... objects);

    //播放错误，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
    void onPlayError(VideoPlayModel videoPlayModel, Object... objects);

    //点击了空白区域开始播放，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
    void onClickStartThumb(VideoPlayModel videoPlayModel, Object... objects);

    //点击了播放中的空白区域，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
    void onClickBlank(VideoPlayModel videoPlayModel, Object... objects);

    //点击了全屏播放中的空白区域，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
    void onClickBlankFullscreen(VideoPlayModel videoPlayModel, Object... objects);


}
