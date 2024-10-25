package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

// 表：市
public class City extends DataSupport{
    private int id;             // 类ID
    private String cityName;    // 市名称
    private int cityCode;       // 市代号
    private int provinceId;     // 当前市所属省

    // 获取ID
    public int getId() {
        return id;
    }

    // 设置ID
    public void setId(int id) {
        this.id = id;
    }

    // 获取市名称
    public String getCityName() {
        return cityName;
    }

    // 设置市名称
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    // 获取市代号
    public int getCityCode() {
        return cityCode;
    }

    // 设置市代号
    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    // 获取市所属省ID
    public int getProvinceId() {
        return provinceId;
    }

    // 设置市所属省ID
    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
