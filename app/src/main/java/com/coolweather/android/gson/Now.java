package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

// 实时天气
public class Now {
    @SerializedName("tmp")
    public String temperature;          // 当前温度

    @SerializedName("cond")
    public More more;                   // 更多信息

    public static class More{
        @SerializedName("txt")
        public String info;         // 天气信息
    }
}
