package com.xfans.jsbridge.bridge;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by xfans on 2014/12/13.
 */
public class XfansWebView extends WebView {
    private Activity activity;
    Method sendMessageMethod;
    Object webViewCore;
    boolean initFailed = false;;

    public XfansWebView(Context context) {
        super(context);
        this.initReflection();
    }

    public XfansWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initReflection();
    }

    public XfansWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.initReflection();
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @SuppressWarnings("rawtypes")
    private void initReflection() {
        Object webViewObject = this;
        Class webViewClass = WebView.class;
        try {
            Field f = webViewClass.getDeclaredField("mProvider");
            f.setAccessible(true);
            webViewObject = f.get(this);
            webViewClass = webViewObject.getClass();
        } catch (Throwable e) {
            // mProvider is only required on newer Android releases.
        }

        try {
            Field f = webViewClass.getDeclaredField("mWebViewCore");
            f.setAccessible(true);
            webViewCore = f.get(webViewObject);
            if (webViewCore != null) {
                sendMessageMethod = webViewCore.getClass().getDeclaredMethod("sendMessage", Message.class);
                sendMessageMethod.setAccessible(true);
            }
        } catch (Throwable e) {
            initFailed = true;
            Log.e("XfansWebView", "PrivateApiBridgeMode failed to find the expected APIs.", e);
        }
    }

    /**
     * 默认加载js方式
     * @param url
     */
    @Override
    public void loadUrl(String url) {
        Log.d("XfansWebView","url");
        super.loadUrl(url);
    }

    /**
     * 反射方式加载js
     * @param jsStr
     */
    public void loadUrlReflection(String jsStr) {
        Log.d("XfansWebView","loadUrlReflection:"+jsStr);
        if (sendMessageMethod == null && !initFailed) {
            initReflection();
        }
        Message execJsMessage = Message.obtain(null, 194, jsStr);//194?
        // webViewCore is lazily initialized, and so may not be available right away.
        if (sendMessageMethod != null) {
            try {
                sendMessageMethod.invoke(webViewCore, execJsMessage);
            } catch (Throwable e) {
                Log.e("XfansWebView", "Reflection message bridge failed.", e);
            }
        }
    }
    /**
     * 4.4新方法
     * @param jsStr
     */
    @SuppressLint("NewApi")
    public void evaJsForKitkat(String jsStr) {
        evaluateJavascript(jsStr,new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                Log.d("AndroidJsBridge", "onReceiveValue:" + s);
            }
        });
    }
}
