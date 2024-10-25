package com.coolweather.android.util;

import android.text.TextUtils;

import com.coolweather.android.db.City;
import com.coolweather.android.db.County;
import com.coolweather.android.db.Province;
import com.coolweather.android.gson.Weather;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// 工具类
public class Utility {
    // 解析和处理服务器返回的省级数据
    public static boolean handleProvinceResponse(String response){
        // 数据不为空时
        if(!TextUtils.isEmpty(response)){
            try {
                // 解析成一个JSON数组
                JSONArray allProvinces = new JSONArray(response);

                // 循环解析省数据
                for(int i = 0; i < allProvinces.length(); ++i){
                    // 获取一个JSON对象
                    JSONObject provinceObject = allProvinces.getJSONObject(i);

                    Province province = new Province();

                    // 将JSON对象中的name字段赋值给ProvinceName
                    province.setProvinceName(provinceObject.getString("name"));
                    // 将JSON对象中的id字段赋值给ProvinceCode
                    province.setProvinceCode(provinceObject.getInt("id"));
                    // 保存到数据库
                    province.save();
                }

                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        return false;
    }

    // 解析和处理服务器返回的市级数据
    public static boolean handleCityResponse(String response, int provinceId){
        // 如果数据不为空
        if(!TextUtils.isEmpty(response)){
            try {
                // 解析成一个JSON数组
                JSONArray allCity = new JSONArray(response);

                // 循环解析市数据
                for(int i = 0; i < allCity.length(); ++i){
                    // 获取一个JSON对象
                    JSONObject provinceObject = allCity.getJSONObject(i);

                    City city = new City();

                    // 将JSON对象中的name字段赋值给CityName
                    city.setCityName(provinceObject.getString("name"));
                    // 将JSON对象中的id字段赋值给CityCode
                    city.setCityCode(provinceObject.getInt("id"));
                    // 设置市所属省ID
                    city.setProvinceId(provinceId);

                    // 保存到数据库
                    city.save();
                }

                return true;

            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        return false;
    }

    // 解析和处理服务器返回的县级数据
    public static boolean handleCountResponse(String response, int cityId){
        // 如果数据不为空
        if(!TextUtils.isEmpty(response)){
            try {
                // 解析成一个JSON数组
                JSONArray allCity = new JSONArray(response);

                // 循环解析县数据
                for(int i = 0; i < allCity.length(); ++i){
                    // 获取一个JSON对象
                    JSONObject provinceObject = allCity.getJSONObject(i);

                    County county = new County();

                    // 将JSON对象中的name字段赋值给CountyName
                    county.setCountyName(provinceObject.getString("name"));
                    // 将JSON对象中的weather_id字段赋值给WeatherId
                    county.setWeatherId(provinceObject.getString("weather_id"));
                    // 设置市所属市ID
                    county.setCityId(cityId);

                    // 保存到数据库
                    county.save();
                }

                return true;

            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        return false;
    }

    // 将返回的Json数据解析成Weather实体类
    public static Weather handleWeatherResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();

            // 将JSON数据解析成Weather对象
            return new Gson().fromJson(weatherContent, Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    // 解析必应获取图片时数据格式
    public static String parseImageUrl(String jsonData) {
        JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();

        String imageUrl = jsonObject.getAsJsonArray("images")
                .get(0).getAsJsonObject()
                .get("url").getAsString();

        return "https://www.bing.com" + imageUrl;
    }
}