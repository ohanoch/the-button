package com.example.The_Button;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by 1501858 on 2017/04/22.
 */
public class myWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }


}