package com.example.xfans.js.bridge;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by xfans on 2014/12/13.
 */
public class XfansWebView extends WebView {
    private Activity activity;

    public XfansWebView(Context context) {
        super(context);
    }

    public XfansWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XfansWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
