package com.example.xfans.js.bridge;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by xfans on 2014/12/13.
 */
public interface XfansActivityInterface {
    abstract void setWebView(XfansWebView webView);
    abstract void setActivity();
    abstract void onActivityResult(int requestCode, int resultCode, Intent data);
}
