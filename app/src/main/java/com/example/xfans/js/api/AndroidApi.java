package com.example.xfans.js.api;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
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
     * @param url
     *@param message
     * @param jsonStr
     * @param result  @return
     */
    public boolean callAndroidSync(WebView webView, String url, String message, String jsonStr, JsPromptResult result) {
        result.confirm("callAndroidSync");
        return true;
    }

    /**
     * url调用
     * @param webView
     * @param url
     */
    public boolean callUrl(WebView webView, String url){
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
                case GEO:
                    getGeo(url, context);
                    break;
                case MAILTO:
                    goMailTo(url,context);
                    break;
                case SMS:
                    goSms(url, context);
                    break;
                case MARKET:
                    goMarket(url, context);
                    break;
                default:
                    goOther(url, context);
                    break;
            }
        }
        return true;
    }

    private void goOther(String url, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    private void goMarket(String url, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    private void goSms(String url, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Get address
        String address = null;
        int parmIndex = url.indexOf('?');
        if (parmIndex == -1) {
            address = url.substring(4);
        }
        else {
            address = url.substring(4, parmIndex);

            // If body, then set sms body
            Uri uri = Uri.parse(url);
            String query = uri.getQuery();
            if (query != null) {
                if (query.startsWith("body=")) {
                    intent.putExtra("sms_body", query.substring(5));
                }
            }
        }
        intent.setData(Uri.parse("sms:" + address));
        intent.putExtra("address", address);
        intent.setType("vnd.android-dir/mms-sms");
        context.startActivity(intent);
    }

    private void goMailTo(String url, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    private void getGeo(String url, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
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

    /**
     * onJsAlert
     * @param view
     * @param url
     * @param message
     * @param result
     * @return
     */
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        Context context = view.getContext();
        AlertDialog.Builder dlg = new AlertDialog.Builder(context);
        dlg.setMessage(message);
        dlg.setTitle("Alert");
        //Don't let alerts break the back button
        dlg.setCancelable(true);
        dlg.setPositiveButton(android.R.string.ok,
                new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
        dlg.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        result.cancel();
                    }
                });
        dlg.setOnKeyListener(new DialogInterface.OnKeyListener() {
            //DO NOTHING
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                {
                    result.confirm();
                    return false;
                }
                else
                    return true;
            }
        });
        dlg.create();
        dlg.show();
        return true;
    }

    /**
     * onJsConfirm
     * @param view
     * @param url
     * @param message
     * @param result
     * @return
     */
    public boolean onJsConfirm(WebView view, String url, String message,final JsResult result) {
        Context context = view.getContext();
        AlertDialog.Builder dlg = new AlertDialog.Builder(context);
        dlg.setMessage(message);
        dlg.setTitle("Confirm");
        dlg.setCancelable(true);
        dlg.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
        dlg.setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                });
        dlg.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        result.cancel();
                    }
                });
        dlg.setOnKeyListener(new DialogInterface.OnKeyListener() {
            //DO NOTHING
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                {
                    result.cancel();
                    return false;
                }
                else
                    return true;
            }
        });
        dlg.create();
        dlg.show();
        return true;
    }
}
