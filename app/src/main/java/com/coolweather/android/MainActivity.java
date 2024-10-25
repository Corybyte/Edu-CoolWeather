package com.coolweather.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // 清除缓存时打开此注释
        /*
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
        */


        // 从SharedPreferences中读取缓存数据
        if(prefs.getString("weather",null) != null){
            // 之前请求过则直接跳到天气信息
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        }
    }
}