Js-Android-bridge
=================
更新日志：
2015/01/09:修改XfansWebView.java,AndroidJsBridge.java兼容4.4.

js 调用android native方法。

如：

1. js调用android相机，拍照成功后异步返回图片路径。

2. url定义跳转Activity的路径，从html页面跳转到Activity。

3. 打电话，发短信等。

参考了部分开源项目，Cordova等。

该项目使用Gradle构建。

------------------

##js调用native方式：

1. prompt方式：

  js端：

  ```javascript
  callAndroidSync : function (cmd, args) {//同步调用 prompt 方式
      return prompt(cmd,args);
  }
  ```
  
  native端：
  
  ```Java
  @Override
  public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
      Log.d("XfanWebChromeClient",url+":"+ message+":"+  defaultValue+":"+  result);
      return androidApi.callAndroidSync(view, url, message, defaultValue ,result);
  }
  ```
2. JavascriptInterface方式：

  js端：
  
  ```javascript
  AndroidJsBridge.callNative(cmd,args,key)
  ```
  
  Native端：
  
  ```Java
  public void callNative( String cmd, String agrs, String key){
        Log.d("Api", cmd + ":" + agrs + ":" + key);
        RequestContent requestContent = new RequestContent(cmd, agrs, key, webView);
        ContextQueue.reqMap.put(key,requestContent);
        androidApi.callAndroidAsync(this, requestContent);
  }
  ```
  
##Native调用js

  1. prompt方式：

    js端:
    ```javascript
    return prompt(cmd,args)
    ```
    
    Native端：
    ```Java
    result.confirm("callAndroidSync"); 
    ```
    
  2. loadurl方式：
  
    js端：
    ```javascript
    function show(str){
        var doc = document.getElementById("cont");
        doc.innerHTML = str;
    }
    ```
    Native端：
    ```java
    webView.loadUrl("javascript:show('hello')")
    ```
    
    
-------------
##使用该项目：

  修改`AndroidApi.java`,`CMD.java`即可。
  

-------------
##注意

  未处理webview的安全问题。

  [WebView中接口隐患与手机挂马利用](http://drops.wooyun.org/papers/548 "WebView中接口隐患与手机挂马利用")

-------------
##License
    Copyright 2016 xfans

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

Js-Android bridge
