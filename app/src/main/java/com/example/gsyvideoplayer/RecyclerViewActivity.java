package com.example.gsyvideoplayer;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.util.Log;
import android.view.Window;

import com.example.gsyvideoplayer.adapter.RecyclerBaseAdapter;
import com.example.gsyvideoplayer.adapter.RecyclerNormalAdapter;

import com.example.gsyvideoplayer.holder.RecyclerItemNormalHolder;
import com.example.gsyvideoplayer.model.VideoModel;
import com.example.gsyvideoplayer.service.VideoUrlParserService;
import com.example.gsyvideoplayer.utils.VideoUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RecyclerViewActivity extends AppCompatActivity {


    @BindView(R.id.list_item_recycler)
    RecyclerView videoList;

    LinearLayoutManager linearLayoutManager;

    RecyclerNormalAdapter recyclerBaseAdapter;

    List<VideoModel> dataList = new ArrayList<>();

    boolean mFull = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置一个exit transition
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        ButterKnife.bind(this);

        recyclerBaseAdapter = new RecyclerNormalAdapter(this, dataList);
        linearLayoutManager = new LinearLayoutManager(this);
        videoList.setLayoutManager(linearLayoutManager);
        videoList.setAdapter(recyclerBaseAdapter);
        videoList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int firstVisibleItem, lastVisibleItem;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem   = linearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                //大于0说明有播放
                if (GSYVideoManager.instance().getPlayPosition() >= 0) {
                    //当前播放的位置
                    int position = GSYVideoManager.instance().getPlayPosition();
                    //对应的播放列表TAG
                    if (GSYVideoManager.instance().getPlayTag().equals(RecyclerItemNormalHolder.TAG)
                            && (position < firstVisibleItem || position > lastVisibleItem)) {

                        //如果滑出去了上面和下面就是否，和今日头条一样
                        //是否全屏
                        if(!mFull) {
                            Log.e("--", "--> 释放资源");
                            GSYVideoPlayer.releaseAllVideos();
                            // recyclerNormalAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
        initData();
    }

    private void initData() {

        Observable.create(new ObservableOnSubscribe<ArrayList<VideoModel>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ArrayList<VideoModel>> subscribe) {
                try {
                    InputStream is = getAssets().open("videodata.txt");
                    ArrayList<VideoModel> datas = new Gson().fromJson(VideoUtils.readTextFromInputStream(is),
                            new TypeToken<ArrayList<VideoModel>>(){}.getType());
                    subscribe.onNext(datas);
                    subscribe.onComplete();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscribe.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<VideoModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull ArrayList<VideoModel> videoModels) {
                        resolveData(videoModels);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (newConfig.orientation != ActivityInfo.SCREEN_ORIENTATION_USER) {
            mFull = false;
        } else {
            mFull = true;
        }

    }

    @Override
    public void onBackPressed() {
        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoPlayer.releaseAllVideos();
        // 停止视频地址解析服务
        Intent parserServiceIntent = new Intent(this, VideoUrlParserService.class);
        stopService(parserServiceIntent);
    }

    private void resolveData(ArrayList<VideoModel> videoModels) {
        dataList.addAll(videoModels);
        if (recyclerBaseAdapter != null)
            recyclerBaseAdapter.notifyDataSetChanged();
    }
}
