package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

// 天气数据基本类
public class Basic {
    @SerializedName("city")
    public String cityName;             // 城市名

    @SerializedName("id")
    public String weatherId;            // 天气编号

    public Update update;               // 更新状态在类

    public static class Update {
        @SerializedName("loc")
        public String updateTime;           // 更新时间
    }
}
