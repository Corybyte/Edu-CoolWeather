package com.coolweather.android.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.coolweather.android.gson.Weather;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

// 天气自动更新服务
public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    // 处理绑定服务的请求.当其它组件(如活动)通过bindServer()方法与服务进行绑定时,会调用此方法.
    // 返回一个IBinder对象,允许客户端与服务进行交互,如果服务不提供绑定服务功能,可以返回null
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    // 用于处理启动服务的请求,当调用startServer()方法时,系统会调用此方法.
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        // 更新天气
        updateWeather();

        // 更新背景图片
        updateBingPic();

        // 获取系统的定时任务
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // 设置定时任务值
        int anHour = 60 * 60 * 1000;                // 1个小时
        long triggrAtTime = SystemClock.elapsedRealtime() + anHour;     // 设置触发时间

        Intent i = new Intent(this, AutoUpdateService.class);
        // 创建一个PendingIntent,用于在其它应用中延迟执行某个操作.
        PendingIntent pi = PendingIntent.getService(this, 0, i,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

        // 取消以前的定时任务
        manager.cancel(pi);
        // 设置一个一次性闹钟,在指定的时间触发
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggrAtTime, pi);

        return super.onStartCommand(intent, flags, startId);
    }

    // 更新天气信息
    private void updateWeather(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);

        if(null != weatherString){
            // 有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            assert weather != null;
            String weatherId = weather.basic.weatherId;

            // 注意更改之前申请的API Key
            String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId
                    + "&key=44fb81b57b7b42f794a0f9547ff812fd";

            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    final String responseText = response.body().string();
                    final Weather weather1 = Utility.handleWeatherResponse(responseText);

                    System.out.println("Weather text: " + responseText);

                    if(null != weather1 && "ok".equals(weather1.status)) {
                        // 缓存有效的Weather对象(实际上缓存的是字符串)
                        SharedPreferences.Editor editor = PreferenceManager
                                .getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weather", responseText);
                        editor.apply();
                    }
                }
            });
        }
    }

    // 更行必应每日一图
    private void updateBingPic(){
        String requestBingPic = "https://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonData = response.body().string();
                String bingPic = Utility.parseImageUrl(jsonData);
                System.out.println("Bing Image URL: " + bingPic);

                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
            }
        });
    }
}