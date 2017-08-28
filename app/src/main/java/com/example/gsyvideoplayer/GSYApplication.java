package com.example.gsyvideoplayer;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.model.VideoOptionModel;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;

import java.util.ArrayList;
import java.util.List;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

//import com.squareup.leakcanary.LeakCanary;

/**
 * Created by shuyu on 2016/11/11.
 */

public class GSYApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            //return;
        //}
        //LeakCanary.install(this);
        //GSYVideoType.enableMediaCodec();

        List<VideoOptionModel> videoOptionModels = new ArrayList<>();
        videoOptionModels.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "protocol_whitelist",
                "rtmp,concat,ffconcat,file,subfile,http,https,tls,rtp,tcp,udp,crypto"));
        videoOptionModels.add(new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "safe", 0));
        GSYVideoManager.instance().setOptionModelList(videoOptionModels);

        //GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL);
        //GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);
        //GSYVideoType.setRenderType(GSYVideoType.SUFRACE);

        Utils.init(this);
    }
}
