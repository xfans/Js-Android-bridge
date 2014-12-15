var JsApi = {
    CALLBACK_SUCCESS : {},  // 输出的结果成功时调用的方法
    CALLBACK_FAIL : {},       // 输出的结果失败时调用的方法
    callAndroidSync : function (cmd, args) {//同步调用
        return prompt(cmd,args);
    },
    callAndroidAsync : function (cmd, args, success, fail) {//异步调用
        var key = new String(new Date().getTime()).substring(4,13)
        JsApi.CALLBACK_SUCCESS[key] = success;
        JsApi.CALLBACK_FAIL[key] = fail;
        var doc = document.getElementById("cont");
                doc.innerHTML = key;
        AndroidJsBridge.callback(cmd,args,key)
    },
    jsCallback : function (code,result, key) {
        if(code = 1){
            setTimeout( "JsApi.CALLBACK_SUCCESS['" +key+"']('" + result + "')", 0);
        }else{
            setTimeout( "JsApi.CALLBACK_FAIL['" +key+"']('" + result + "')", 0);
        }
    }
}
