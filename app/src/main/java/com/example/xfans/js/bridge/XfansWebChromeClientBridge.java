package com.example.xfans.js.bridge;

import android.util.Log;
import android.webkit.JsPromptResult;
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
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        Log.d("XfanWebChromeClient",url+":"+ message+":"+  defaultValue+":"+  result);
        result.confirm(call(view, message));
        return true;
    }
    private String call(WebView webView, String jsonStr) {
         return androidApi.callAndroidSync(webView, jsonStr);
    }
}
