package com.example.xfans.js.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.WebView;

import com.example.xfans.js.bridge.AndroidJsBridge;
import com.example.xfans.js.bridge.RequestContent;
import com.example.xfans.js.bridge.Utils;

import java.io.File;

/**
 * Created by xfans on 2014/12/12.
 */
public class AndroidApi {

    /**
     * 同步
     * @param webView
     * @param jsonStr
     * @return
     */
    public String callAndroidSync(WebView webView, String jsonStr) {
        return "callAndroidSync";
    }

    public void callUrl(WebView webView, String url){
        Log.d("AndroidApi",url);
        if(url != null){
            Context context = webView.getContext();
            String args = url.substring(url.indexOf(":")+1,url.length());
            String cmdStr = url.substring(0,url.indexOf(":"));
            CMD cmd = Utils.getCmd(cmdStr);
            switch (cmd){
                case GONATIVE:
                    goActivity(context, args);
                    break;
                case TEL:
                    goTel(url, context);
                    break;
                default:
                    break;
            }
        }
    }

     /**
     * 异步
     * @param androidJsBridge
     * @param requestContent
     */
    public void callAndroidAsync(AndroidJsBridge androidJsBridge, RequestContent requestContent) {
        Log.d("AndroidApi","callAndroidAsync:");
        if(requestContent != null && androidJsBridge != null){
            Context context = androidJsBridge.getWebView().getContext();
            String cmdStr = requestContent.getCmd();
            CMD cmd = Utils.getCmd(cmdStr);
            switch (cmd){
                case CAMERA:
                    goCamera(context, requestContent);
                    break;
                case PICTURE:
                    goPicture(context, requestContent);
                    break;

                case NULL:
                    break;
            }
        }
    }

    /**
     * 打电话
     * @param url
     * @param context
     */
    private void goTel(String url, Context context) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }
    /**
     * 跳转页面
     * @param context
     * @param args
     */
    private void goActivity(Context context, String args) {
        Log.d("AndroidApi","goActivity: "+context.getPackageName()+"："+args);
        Intent intent = new Intent();
        intent.setClassName(context,args);
        context.startActivity(intent);
    }

    /**
     * 调用相册
     * @param context
     * @param requestContent
     */
    private void goPicture(Context context, RequestContent requestContent) {
        Log.d("AndroidApi","goPicture");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        ((Activity)context).startActivityForResult(intent, Integer.parseInt(requestContent.getKey()));
    }

    /**
     * 调用相机
     * @param context
     * @param requestContent
     */
    private void goCamera(Context context, RequestContent requestContent) {
        Log.d("AndroidApi","goCamera");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis()+".jpg");
        requestContent.setResult(file.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        ((Activity)context).startActivityForResult(intent, Integer.parseInt(requestContent.getKey()));
    }
}
