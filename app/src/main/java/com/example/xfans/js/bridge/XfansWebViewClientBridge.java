package com.example.xfans.js.bridge;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.xfans.js.api.AndroidApi;
import com.example.xfans.js.api.CMD;

/**
 * Created by xfans on 2014/12/13.
 */
public class XfansWebViewClientBridge extends WebViewClient{
    private AndroidApi androidApi;

    public XfansWebViewClientBridge(AndroidApi androidApi) {
        this.androidApi = androidApi;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.d("XfansWebViewClientBridge","shouldOverrideUrlLoading:"+url);
        if(url == null){return false; }
        return androidApi.callUrl(view,url);
    }
}
