package com.coolweather.android.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

// OkHttp工具类
public class HttpUtil {
    // 使用OkHttp创建一个访问服务器数据的工具类
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();                       // 创建OkHttpClient对象
        Request request = new Request.Builder().url(address).build();   // 解析请求地址
        client.newCall(request).enqueue(callback);                      // 注册用于处理服务器相应的回调
    }
}
