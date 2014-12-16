Js-Android-bridge
=================
js 调用android native方法。

如：

1.js调用android相机，拍照成功后异步返回图片路径。

2. url定义跳转Activity的路径，从html页面跳转到Activity。

3. 打电话，发短信等。

参考了部分开源项目，Cordova等。

------------------

js调用native方式：

1. prompt方式：
  
  <code>
    callAndroidSync : function (cmd, args) {//同步调用 prompt 方式
        return prompt(cmd,args);
    }
  </code>



Js-Android bridge
