package com.example.gsyvideoplayer.holder;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.example.gsyvideoplayer.R;
import com.example.gsyvideoplayer.listener.SampleListener;
import com.example.gsyvideoplayer.model.VideoModel;
import com.example.gsyvideoplayer.service.VideoUrlParserService;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guoshuyu on 2017/1/9.
 */

public class RecyclerItemNormalHolder extends RecyclerItemBaseHolder {

    public final static String TAG = "RecyclerView2List";

    protected Context context = null;
    
    @BindView(R.id.video_item_player)
    StandardGSYVideoPlayer gsyVideoPlayer;

    ImageView imageView;

    public RecyclerItemNormalHolder(Context context, View v) {
        super(v);
        this.context = context;
        ButterKnife.bind(this, v);
        imageView = new ImageView(context);
    }

    public void onBind(final int position, final VideoModel videoModel) {

        //增加封面
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (imageView.getParent() != null) {
            ViewGroup viewGroup = (ViewGroup)imageView.getParent();
            viewGroup.removeView(imageView);
        }
        String coverImageUrl = null;
        if (videoModel.imgs != null && videoModel.imgs.size() > 0) {
            coverImageUrl = videoModel.imgs.get(0).small_img;
        }
        Glide.with(imageView.getContext())
                .load(coverImageUrl)
                .placeholder(Color.GRAY)
                .error(Color.GRAY)
                .crossFade()
                .into(imageView);

        gsyVideoPlayer.setThumbImageView(imageView);

        gsyVideoPlayer.setIsTouchWiget(false);
        gsyVideoPlayer.setTitle(videoModel.title);
        //默认缓存路径
        gsyVideoPlayer.setUp(null, true , null);

        //设置返回键
        gsyVideoPlayer.getBackButton().setVisibility(View.GONE);

        //设置全屏按键功能
        gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolveFullBtn(gsyVideoPlayer);
            }
        });
        gsyVideoPlayer.setRotateViewAuto(true);
        gsyVideoPlayer.setLockLand(true);
        gsyVideoPlayer.setPlayTag(TAG);
        gsyVideoPlayer.setShowFullAnimation(true);
        gsyVideoPlayer.setNeedShowWifiTip(true);
        //循环
        gsyVideoPlayer.setLooping(false);
        gsyVideoPlayer.setNeedLockFull(true);
        gsyVideoPlayer.setPlayPosition(position);

        gsyVideoPlayer.setStandardVideoAllCallBack(new SampleListener() {
            @Override
            public boolean onObtainMediaUrl(Object... objects) {
                gsyVideoPlayer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        VideoUrlParserService.start(gsyVideoPlayer.getContext(),
                                videoModel.media_source, videoModel.media_vid);
                    }
                }, 3000);
                return true;
            }
        });
    }

    /**
     * 全屏幕按键处理
     */
    private void resolveFullBtn(final StandardGSYVideoPlayer standardGSYVideoPlayer) {
        if (TextUtils.isEmpty(standardGSYVideoPlayer.getMediaUrl())) {
            return;
        }
        standardGSYVideoPlayer.startWindowFullscreen(context, true, true);
    }

}