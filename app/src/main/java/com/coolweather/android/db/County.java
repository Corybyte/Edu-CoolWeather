package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

// 表：县
public class County extends DataSupport{
    private int id;                     // ID
    private String countyName;          // 县名字
    private String weatherId;           // 县天气对应的ID
    private int cityId;                 // 县所属市ID

    // 获取ID
    public int getId() {
        return id;
    }

    // 设置ID
    public void setId(int id) {
        this.id = id;
    }

    // 获取县名字
    public String getCountyName() {
        return countyName;
    }

    // 设置县名字
    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    // 获取县对应的天气ID
    public String getWeatherId() {
        return weatherId;
    }

    // 设置县对应的天气ID
    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    // 获取当前县所在市ID
    public int getCityId() {
        return cityId;
    }

    // 设置当前县所属市ID
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

}
