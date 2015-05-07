package com.metis.meishuquan.fragment.commons;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.metis.meishuquan.R;

/**
 * Created by WJ on 2015/5/7.
 */
public class WebFragment extends Fragment {

    private WebView mWebView = null;
    private WebChromeClient mChromeClient = null;
    private WebViewClient mWebViewClient = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web, null, true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWebView = (WebView)view.findViewById(R.id.web_view);

        mWebView.getSettings().setJavaScriptEnabled(true);

        mChromeClient = new WebChromeClient();
        mWebViewClient = new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Toast.makeText(getActivity(), "onReceivedError", Toast.LENGTH_SHORT).show();
                view.loadData(
                        "<html><body>" +
                                "errorCode=" + errorCode +
                                "\ndescription=" + description +
                                "\nfailingUrl=" + failingUrl +
                                "<b>192</b> points.</body></html>", "text/html", null);
            }
        };

        mWebView.setWebChromeClient(mChromeClient);
        mWebView.setWebViewClient(mWebViewClient);
        mWebView.loadData("<html><body>Loading ... <b>192</b> points.</body></html>", "text/html", null);
    }

    public void loadUrl (String url) {
        mWebView.loadUrl(url);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
    }
}
