package com.example.xfans.js.widget;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.xfans.js.api.AndroidApi;
import com.example.xfans.js.api.CMD;
import com.example.xfans.js.bridge.AndroidJsBridge;
import com.example.xfans.js.bridge.ContextQueue;
import com.example.xfans.js.bridge.RequestContent;
import com.example.xfans.js.bridge.Utils;
import com.example.xfans.js.bridge.XfansActivityInterface;
import com.example.xfans.js.bridge.XfansWebChromeClientBridge;
import com.example.xfans.js.bridge.XfansWebView;
import com.example.xfans.js.bridge.XfansWebViewClientBridge;

/**
 * Created by xfans on 2014/12/12.
 */
public class XfansBaseActivity extends Activity implements XfansActivityInterface {
    private XfansWebView webView;
    private AndroidJsBridge androidJsBridge;

    @Override
    public void setWebView(XfansWebView webView){
        this.webView = webView;
        AndroidApi androidApi = new AndroidApi();
        androidJsBridge = new AndroidJsBridge(webView,androidApi);
        XfansWebChromeClientBridge xfansWebChromeClientBridge = new XfansWebChromeClientBridge(androidApi);
        XfansWebViewClientBridge xfansWebViewClientBridge = new XfansWebViewClientBridge(androidApi);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(androidJsBridge, "AndroidJsBridge");
        webView.setWebChromeClient(xfansWebChromeClientBridge);
        webView.setWebViewClient(xfansWebViewClientBridge);
    }

    @Override
    public void setActivity() {
        webView.setActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(webView == null){
            try {
                throw new Exception("请先在onCreate中调用setWebView");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("XfansBaseActivity","onActivityResult");
        if (resultCode != 0){
            RequestContent requestContent = ContextQueue.reqMap.get(requestCode+"");
            if(requestContent != null){
                String cmdStr = requestContent.getCmd();
                CMD cmd = Utils.getCmd(cmdStr);
                switch (cmd){
                    case CAMERA:
                        String path = requestContent.getResult();
                        androidJsBridge.jsResult("1", path, requestCode + "");
                        break;
                    case PICTURE:
                        Uri imageUri= data.getData();//TODO 解析正确路径
                        androidJsBridge.jsResult("1", imageUri.getPath(), requestCode + "");
                        break;
                    case NULL:
                        break;
                }
            }
        }else{
            androidJsBridge.jsResult("0","0","0");
        }
    }
}
