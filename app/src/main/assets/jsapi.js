var JsApi = {
    callback_success : {},  // 输出的结果成功时调用的方法
    callback_fail : {},       // 输出的结果失败时调用的方法
    callAndroidSync : function (cmd, args) {//同步调用 prompt 方式
        return prompt(cmd,args);
    },
    callAndroidAsync : function (cmd, args, success, fail) {//异步调用 addJavascriptInterface方式
        var key = new String(new Date().getTime()).substring(4,13)
        JsApi.callback_success[key] = success;
        JsApi.callback_fail[key] = fail;
        var doc = document.getElementById("cont");
                doc.innerHTML = key;
        AndroidJsBridge.callNative(cmd,args,key)
    },
    jsCallback : function (code,result, key) {
        if(code = 1){ //1 seccuss
            setTimeout( "JsApi.callback_success['" +key+"']('" + result + "')", 0);
        }else{
            setTimeout( "JsApi.callback_fail['" +key+"']('" + result + "')", 0);
        }
    }
}
