package com.example.xfans.js.bridge;

import android.webkit.WebView;

/**
 * Created by xfans on 2014/12/12.
 */
public class RequestContent {
    private String cmd;
    private String args;
    private String key;
    private String result;
    private WebView webView;

    public RequestContent(String cmd, String args, String key, WebView webView) {
        this.cmd = cmd;
        this.args = args;
        this.key = key;
        this.webView = webView;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public WebView getWebView() {
        return webView;
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }

    public String getCmd() {
        return cmd.toUpperCase();
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}
