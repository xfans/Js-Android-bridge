package com.example.xfans.js.bridge;

import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.example.xfans.js.api.AndroidApi;

/**
 * Created by xfans on 2014/12/11.
 * WebChromeClient
 */
public class XfansWebChromeClientBridge extends WebChromeClient {
    private AndroidApi androidApi;

    public XfansWebChromeClientBridge(AndroidApi androidApi) {
        this.androidApi = androidApi;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return androidApi.onJsAlert(view,url,message,result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        Log.d("XfanWebChromeClient",url+":"+ message+":"+  defaultValue+":"+  result);

        return androidApi.callAndroidSync(view, url, message, defaultValue ,result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        return androidApi.onJsConfirm(view, url, message, result);
    }
}
