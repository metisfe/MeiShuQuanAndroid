package com.metis.meishuquan.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.meishuquan.R;

public class MediaWebActivity extends FragmentActivity {

    private FrameLayout videoview;// 全屏时视频加载view
    private Button videolandport;
    private WebView videowebview;
    private Boolean islandport = true;// true表示此时是竖屏，false表示此时横屏。
    private View xCustomView;
    private xWebChromeClient xwebchromeclient;
    private String url = "http://www.iqiyi.com/v_19rrnqzz0k.html";
    // private String url =
    // "http://test.meishuquan.net/H5/ContentDetial.aspx?ID=5695";
    private WebChromeClient.CustomViewCallback xCustomViewCallback;
    // 计算点击的次数
    private int count = 0;
    // 第一次点击的时间 long型
    private long firClick, secClick;
    // 最后一次点击的时间
    private long lastClick;
    // 第一次点击的button的id
    private int firstId;
    // 网页加载出错进显示页面
    private TextView videoError;
    // 网页加载出错正在刷新
    private TextView videoRefresh;
    private onDoubleClick listClick = new onDoubleClick();
    private GestureDetector gestureScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉应用标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_media_web);
        initwidget();
        initListener();
        videowebview.loadUrl(url);
    }

    private void initListener() {
        // TODO Auto-generated method stub
        // videolandport.setOnClickListener(new Listener());
    }

    @SuppressWarnings("deprecation")
    private void initwidget() {
        // TODO Auto-generated method stub
        videoview = (FrameLayout) findViewById(R.id.video_view);
        videoview.setOnTouchListener(listClick);
        videolandport = (Button) findViewById(R.id.video_landport);
        videowebview = (WebView) findViewById(R.id.video_webview);
        videoError = (TextView) findViewById(R.id.video_error);
        videoRefresh = (TextView) findViewById(R.id.video_refresh);
        videoError.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                videowebview.loadUrl("about:blank");
                videowebview.loadUrl(url);
                videoError.setVisibility(View.GONE);
                videoRefresh.setVisibility(View.VISIBLE);
                videowebview.setVisibility(View.GONE);
            }
        });
        WebSettings ws = videowebview.getSettings();
        /**
         * setAllowFileAccess 启用或禁止WebView访问文件数据 setBlockNetworkImage 是否显示网络图像
         * setBuiltInZoomControls 设置是否支持缩放 setCacheMode 设置缓冲的模式
         * setDefaultFontSize 设置默认的字体大小 setDefaultTextEncodingName 设置在解码时使用的默认编码
         * setFixedFontFamily 设置固定使用的字体 setJavaSciptEnabled 设置是否支持Javascript
         * setLayoutAlgorithm 设置布局方式 setLightTouchEnabled 设置用鼠标激活被选项
         * setSupportZoom 设置是否支持变焦
         * */
        // ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕
        ws.setUseWideViewPort(true);// 可任意比例缩放
        ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        ws.setSavePassword(true);
        ws.setSaveFormData(true);// 保存表单数据
        ws.setJavaScriptEnabled(true);
        // ws.setGeolocationEnabled(true);// 启用地理定位
        ws.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");// 设置定位的数据库路径
        ws.setDomStorageEnabled(true);
        xwebchromeclient = new xWebChromeClient();
        videowebview.setWebChromeClient(xwebchromeclient);
        videowebview.setWebViewClient(new xWebViewClientent());
    }

    class Listener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.video_landport:
                    if (islandport) {
                        Log.i("testwebview", "竖屏切换到横屏");
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        videolandport.setText("全屏不显示该按扭，点击切换竖屏");
                    } else {

                        Log.i("testwebview", "横屏切换到竖屏");
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        videolandport.setText("全屏不显示该按扭，点击切换横屏");
                    }
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (inCustomView()) {
                hideCustomView();
                return true;
            } else {
                // 退出时加载空网址防止退出时还在播放视频
                videowebview.loadUrl("about:blank");
                // mTestWebView.loadData("", "text/html; charset=UTF-8", null);
                MediaWebActivity.this.finish();
                Log.i("testwebview", "===>>>2");
            }
        }
        return true;
    }

    /**
     * 判断是否是全屏
     *
     * @return
     */
    public boolean inCustomView() {
        return (xCustomView != null);
    }

    /**
     * 全屏时按返加键执行退出全屏方法
     */
    public void hideCustomView() {
        xwebchromeclient.onHideCustomView();
    }

    /**
     * 处理Javascript的对话框、网站图标、网站标题以及网页加载进度等
     *
     * @author
     */
    public class xWebChromeClient extends WebChromeClient {
        private Bitmap xdefaltvideo;
        private View xprogressvideo;

        @Override
        // 播放网络视频时全屏会被调用的方法
        public void onShowCustomView(View view,
                                     WebChromeClient.CustomViewCallback callback) {
            if (islandport) {

            } else {

                // ii = "1";
                // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            videowebview.setVisibility(View.GONE);
            // 如果一个视图已经存在，那么立刻终止并新建一个
            if (xCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }

            videoview.addView(view);
            xCustomView = view;
            xCustomViewCallback = callback;
            videoview.setVisibility(View.VISIBLE);
        }

        @Override
        // 视频播放退出全屏会被调用的
        public void onHideCustomView() {

            if (xCustomView == null)// 不是全屏播放状态
                return;

            // Hide the custom view.
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            xCustomView.setVisibility(View.GONE);

            // Remove the custom view from its container.
            videoview.removeView(xCustomView);
            xCustomView = null;
            videoview.setVisibility(View.GONE);
            xCustomViewCallback.onCustomViewHidden();

            videowebview.setVisibility(View.VISIBLE);

            // Log.i(LOGTAG, "set it to webVew");
        }

        // 视频加载添加默认图标
        @Override
        public Bitmap getDefaultVideoPoster() {
            // Log.i(LOGTAG, "here in on getDefaultVideoPoster");
            if (xdefaltvideo == null) {
//                xdefaltvideo = BitmapFactory.decodeResource(getResources(),
//                        R.drawable.videoicon);
            }
            return xdefaltvideo;
        }

        // 视频加载时进程loading
        @Override
        public View getVideoLoadingProgressView() {
            // Log.i(LOGTAG, "here in on getVideoLoadingPregressView");

            if (xprogressvideo == null) {
                LayoutInflater inflater = LayoutInflater
                        .from(MediaWebActivity.this);
                xprogressvideo = inflater.inflate(
                        R.layout.video_loading_progress, null);
            }
            return xprogressvideo;
        }

        // 网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            (MediaWebActivity.this).setTitle(title);
        }

        // @Override
        // //当WebView进度改变时更新窗口进度
        // public void onProgressChanged(WebView view, int newProgress) {
        // (MainActivity.this).getWindow().setFeatureInt(Window.FEATURE_PROGRESS,
        // newProgress*100);
        // }

    }

    /**
     * 处理各种通知、请求等事件
     *
     * @author
     */
    public class xWebViewClientent extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("webviewtest", "shouldOverrideUrlLoading: " + url);
            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            // 这里进行无网络或错误处理，具体可以根据errorCode的值进行判断，做跟详细的处理。
            // view.loadUrl(file:///android_asset/error.html );

            Log.i("onPageStarted", "错误");
            videowebview.setVisibility(View.GONE);
            videoError.setVisibility(View.VISIBLE);
            videoRefresh.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.i("onPageStarted", "onPage               Started" + url);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.i("onPageFinished", "onPageFinished>>>>>>" + url);
            videowebview.setVisibility(View.VISIBLE);
            videoError.setVisibility(View.GONE);
            videoRefresh.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }
    }

    /**
     * 当横竖屏切换时会调用该方法
     *
     * @author
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i("testwebview", "=====<<<  onConfigurationChanged  >>>=====");
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.i("webview", "   现在是横屏1");
            islandport = false;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.i("webview", "   现在是竖屏1");
            islandport = true;
        }
    }

    class onDoubleClick implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (MotionEvent.ACTION_DOWN == event.getAction()) {
                count++;
                if (count == 1) {
                    firClick = System.currentTimeMillis();

                } else if (count == 2) {
                    secClick = System.currentTimeMillis();
                    if (secClick - firClick < 500) {
                        // 双击事件
                        Toast.makeText(MediaWebActivity.this, "双击了屏幕",
                                Toast.LENGTH_LONG).show();
                    } else {

                    }
                    count = 0;
                    firClick = 0;
                    secClick = 0;

                }
            }
            return true;
        }
    }
}
