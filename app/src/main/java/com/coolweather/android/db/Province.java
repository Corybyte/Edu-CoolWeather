package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

// 表：省
public class Province extends DataSupport{
    private int id;                 // 标识符
    private String provinceName;    // 省名字
    private int provinceCode;       // 省代号

    // 获取ID
    public int getId(){
        return id;
    }

    // 设置ID
    public void setId(int id){
        this.id = id;
    }

    // 获取省名字
    public String getProvinceName(){
        return provinceName;
    }

    // 设置省名字
    public void setProvinceName(String provinceName){
        this.provinceName = provinceName;
    }

    // 获取省代号
    public int getProvinceCode(){
        return provinceCode;
    }

    // 设置省代号
    public void setProvinceCode(int provinceCode){
        this.provinceCode = provinceCode;
    }
}
