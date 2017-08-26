package com.example.gsyvideoplayer.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.example.gsyvideoplayer.utils.Mp4Util;
import com.example.gsyvideoplayer.utils.VideoUtils;
import com.google.gson.Gson;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.model.VideoPlayModel;
import com.shuyu.gsyvideoplayer.utils.NetworkUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 视频Url解析
 * Created by xiyangyuge on 2017/8/8.
 */

public class VideoUrlParserService extends Service {

    private static final String TAG = "VideoUrlParserService";

    private WebView mWebView;
    private boolean loadParseHtmComplete; // 加载解析模板完成

    private String videoType;
    private String videoId;
    private boolean isWifiConnected;

    public static void start(Context mCxt, String videoType, String videoId) {
        if (!NetworkUtils.isConnected(mCxt)) {
            // 网络不可用时直接返回错误提示
            return;
        }
        Intent intent = new Intent(mCxt, VideoUrlParserService.class);
        intent.putExtra("videoType", videoType);
        intent.putExtra("videoId", videoId);
        mCxt.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mWebView = new WebView(this);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setAllowFileAccess(true);
        // 支持 https 内容加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        // 支持访问三方 cookie
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptThirdPartyCookies(mWebView, true);
        }
        mWebView.addJavascriptInterface(new VideoJavascriptInterface(), "JDY");
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.e(TAG, message);
                return super.onJsAlert(view, url, message, result);
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e(TAG, url);
                Map<String, String> params = VideoUtils.getUrlParams(url);
                String fileUrl = params.get("fileurl");
                String vid = params.get("vid");
                if (vid.equals(videoId)) {
                    calculationVideoLength(vid, fileUrl);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loadParseHtmComplete = true;
                // 第一次加载webview时
                if (!TextUtils.isEmpty(videoType) && !TextUtils.isEmpty(videoId)) {
                    parseUrl(videoType, videoId);
                }
            }
        });
        mWebView.loadUrl("file:///android_asset/parse/media.html");
    }

    private class VideoJavascriptInterface {

        @JavascriptInterface
        public void loadFfconcat(String fileContent, final long duration, final long fileSize, final String vid) {
            Log.e(TAG, "fileContent=" + fileContent);
            Observable.just(fileContent)
                    .flatMap(new Function<String, ObservableSource<String>>() {
                        @Override
                        public ObservableSource<String> apply(@NonNull String s) throws Exception {
                            File dir = new File(getExternalCacheDir().getAbsolutePath(), "videoPath");
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }
                            String filePath = dir.getAbsolutePath() + File.separator + vid + ".ffconcat";
                            if (FileIOUtils.writeFileFromString(filePath, s)) {
                                return Observable.just(filePath);
                            }
                            return Observable.error(new FileNotFoundException("videoPath not find"));
                        }
                    })
                    .flatMap(new Function<String, ObservableSource<VideoPlayModel>>() {
                        @Override
                        public ObservableSource<VideoPlayModel> apply(@NonNull String s) throws Exception {
                            return Observable.just(new VideoPlayModel(vid, s, VideoUtils.FormetFileSize(fileSize),
                                    isWifiConnected));
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<VideoPlayModel>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull VideoPlayModel event) {
                            String fileDuration = Mp4Util.getDuration(duration);
                            Log.e(TAG, "fileDuration=" + fileDuration);
                            startPlayVideo(event);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.e(TAG, "优酷视频解析失败");
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }

        @JavascriptInterface
        public void printStr(String str){
            Log.e(TAG, "jdy -> : " + str);
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return super.onStartCommand(intent, flags, startId);
        }
        isWifiConnected = NetworkUtils.isWifiConnected(this);
        videoType = intent.getStringExtra("videoType");
        videoId = intent.getStringExtra("videoId");
        if (loadParseHtmComplete) {
            parseUrl(videoType, videoId);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void parseUrl(String videoType, String videoId) {
        if (TextUtils.isEmpty(videoType) || TextUtils.isEmpty(videoId)) {
            Log.e(TAG, "视频参数异常");
            return;
        }
        Log.e(TAG, "VideoType = " + videoType + "," + "videoId = " + videoId);
        // 网络类型，WIFI = 1, 移动网络 = 2
        int netWorkType = NetworkUtils.isWifiConnected(this) ? 1 : 2;
        mWebView.loadUrl("javascript:getVideoUrl({" +
                "videoType:" + videoType +
                "," +
                "vid:'" + videoId + "'" +
                "," +
                "netWorkType:"+netWorkType +
                "," +
                "timestamp:'" + System.currentTimeMillis() + "'" +
                "," +
                "devid:'" + DeviceUtils.getAndroidID() + "'" +
                "," +
                "api_version:'2.7.2'" +
                "," +
                "platform:'android'" +
                "," +
                "system_version:'" + android.os.Build.VERSION.RELEASE + "'" +
                "})");
    }

    /**
     * 计算视频大小
     * @param vId
     * @param vUrl
     */
    private void calculationVideoLength(final String vId, final String vUrl) {
        // 非移动网络直接播放视频
        if (isWifiConnected) {
            startPlayVideo(new VideoPlayModel(vId, vUrl, isWifiConnected));
            return;
        }
        // 计算视频大小
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> subscribe) throws Exception {
                try {
                    URL url = new URL(vUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(4 * 1000);
                    conn.setInstanceFollowRedirects(false);
                    // 判定是否会进行302重定向
                    if (conn.getResponseCode() == 302) {
                        // 如果会重定向，保存302重定向地址，以及Cookies,然后重新发送请求(模拟请求)
                        String location = conn.getHeaderField("Location");
                        url = new URL(location);
                        conn = (HttpURLConnection) url.openConnection();
                        Log.e(TAG, "跳转地址:" + location);
                    }

                    Map<String, List<String>> headerFields = conn.getHeaderFields();
                    Log.e(TAG, "headerFields:" + new Gson().toJson(headerFields));
                    if(headerFields == null || headerFields.size() == 0) {
                        subscribe.onNext("0");
                    } else {
                        subscribe.onNext(headerFields.get("Content-Length").get(0));
                    }
                    subscribe.onComplete();
                } catch (Exception e) {
                    subscribe.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        String fileSizeStr = VideoUtils.FormetFileSize(Long.parseLong(s));
                        startPlayVideo(new VideoPlayModel(vId, vUrl, fileSizeStr, isWifiConnected));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, e.toString());
                        // 解析时长失败 继续播放
                        startPlayVideo(new VideoPlayModel(vId, vUrl, isWifiConnected));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private static void startPlayVideo(VideoPlayModel event) {
        Observable.just(event)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<VideoPlayModel>() {
                    @Override
                    public void accept(VideoPlayModel videoPlayModel) throws Exception {
                        if (GSYVideoManager.instance().listener() != null) {
                            GSYVideoManager.instance().listener().startWithSetUp(videoPlayModel.videoUrl, false , null);
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.loadUrl("about:blank");
            mWebView.stopLoading();
            mWebView.setWebViewClient(null);
            mWebView.setWebChromeClient(null);
            mWebView.destroy();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void writeToFile(String fileName, String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

}
