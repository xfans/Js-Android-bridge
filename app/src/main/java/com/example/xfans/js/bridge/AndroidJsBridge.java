package com.example.xfans.js.bridge;

import android.app.Activity;
import android.util.Log;
import android.webkit.WebView;

import com.example.xfans.js.api.AndroidApi;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by xfans on 2014/12/12.
 */
public class AndroidJsBridge {

    private WebView webView;
    private AndroidApi androidApi;

    public WebView getWebView() {
        return webView;
    }

    public AndroidJsBridge(WebView webView, AndroidApi androidApi) {
        this.webView = webView;
        this.androidApi = androidApi;
    }

    public void callback( String cmd, String agrs, String key){
        Log.d("Api", cmd + ":" + agrs + ":" + key);
        RequestContent requestContent = new RequestContent(cmd, agrs, key, webView);
        ContextQueue.reqMap.put(key,requestContent);
        androidApi.callAndroidAsync(this, requestContent);
    }

    /**
     * @param code 1 success other fail
     * @param result json
     */
    public void jsResult(String code, String result,String key) {
        if(ContextQueue.reqMap.size()>0){
            String js = "javascript: JsApi.jsCallback('" + code + "','" + result + "','" + key + "')";
            runJs(js);
        }else{
            String js = "javascript: JsApi.jsCallback('0','0','0')";
            runJs(js);
        }
    }

    /**
     * run js
     * @param jsStr like:javascript: JsApi.jsCallback('1','{name:"xfans"}','2')"
     */
    private void runJs(String jsStr){
        ((Activity)webView.getContext()).runOnUiThread(new RunJsRunnable(jsStr));
    }

    private class RunJsRunnable implements Runnable{
        private String jsStr;

        RunJsRunnable(String jsStr) {
            this.jsStr = jsStr;
        }

        @Override
        public void run() {
            Log.d("Api", jsStr);
            webView.loadUrl(jsStr);
        }
    }
}
