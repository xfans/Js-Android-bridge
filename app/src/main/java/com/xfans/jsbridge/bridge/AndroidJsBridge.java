package com.xfans.jsbridge.bridge;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.xfans.jsbridge.api.AndroidApi;

/**
 * Created by xfans on 2014/12/12.
 */
public class AndroidJsBridge {

    private XfansWebView webView;
    private AndroidApi androidApi;

    public WebView getWebView() {
        return webView;
    }

    public AndroidJsBridge(XfansWebView webView, AndroidApi androidApi) {
        this.webView = webView;
        this.androidApi = androidApi;
    }
    @JavascriptInterface
    public void callNative( String cmd, String agrs, String key){
        Log.d("AndroidJsBridge", cmd + ":" + agrs + ":" + key);
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
            String js = "('" + code + "','" + result + "','" + key + "')";
            runJs(js);
        }else{
            String js = "('0','0','0')";
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
            Log.d("AndroidJsBridge", jsStr);
            loadUrlForVersion(jsStr);
        }
    }
    private void loadUrlForVersion(String jsStr) {
        //fix bugs: 1. loadUrl may hide keyboard when your focus in a input. 2. loadUrl cannot be called too often.
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.HONEYCOMB_MR2) {
            webView.loadUrl("javascript: JsApi.jsCallback"+jsStr);
        } else if(Build.VERSION.SDK_INT<Build.VERSION_CODES.KITKAT){//android 3.2 以上的使用反射方式加载js
            webView.loadUrlReflection("javascript: JsApi.jsCallback"+jsStr);
        }else{//兼容4.4
            webView.evaJsForKitkat("JsApi.jsCallback"+jsStr);
        }
    }

}
