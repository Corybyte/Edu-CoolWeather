package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

// 天气类
public class Weather {
    // 状态，成功时返回ok
    public String status;

    // 基本数据
    public Basic basic;

    // 空气质量指数
    public AQI aqi;

    // 实时天气
    public Now now;

    // 日常活动的建议
    public Suggestion suggestion;

    // 未来几天天气信息
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
