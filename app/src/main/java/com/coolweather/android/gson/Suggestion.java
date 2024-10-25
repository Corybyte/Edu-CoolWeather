package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

// 日常活动的建议
public class Suggestion {
    @SerializedName("comf")
    public Comfort comfort;                 // 舒适度

    @SerializedName("cw")
    public CarWash carWash;                 // 洗车建议

    public Sport sport;                     // 运动建议

    public static class Comfort{
        @SerializedName("txt")
        public String info;
    }

    public static class CarWash{
        @SerializedName("txt")
        public String info;
    }

    public static class Sport{
        @SerializedName("txt")
        public String info;
    }
}
