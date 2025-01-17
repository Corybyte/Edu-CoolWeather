package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

// 未来几天的天气信息
public class Forecast {
    public String date;                 // 预报日期

    @SerializedName("tmp")
    public Temperature temperature;     // 预报气温

    @SerializedName("cond")
    public More more;                   // 更多信息

    public static class Temperature{
        public String max;          // 最高温
        public String min;          // 最低温
    }

    public static class More{
        @SerializedName("txt_d")
        public String info;         // 预测的天气信息
    }
}
