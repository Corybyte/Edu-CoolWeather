package com.coolweather.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.coolweather.android.db.City;
import com.coolweather.android.db.County;
import com.coolweather.android.db.Province;
import com.coolweather.android.module.XProgressBar;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

// 选择区域类
public class ChooseAreaFragment extends Fragment {
    // 行政不同的级别
    public static final int LEVEL_PROVINCE = 0;         // 省
    public static final int LEVEL_CITY = 1;             // 市
    public static final int LEVEL_COUNTY = 2;           // 县

    // 进度对话框
    private XProgressBar progressBar;
    // 文本组件
    private TextView titleView;
    // 回退按钮
    private Button backButton;
    // 列表组件
    private ListView listView;

    // 数组数据适配器
    private ArrayAdapter<String> adapter;

    // 数据
    private final List<String> dataList = new ArrayList<>();

    // 省数据
    private List<Province> provinceList;
    // 市数据
    private List<City> cityList;
    // 县数据
    private List<County> countyList;

    // 当前选中的省
    private Province selectProvince;
    // 当前选中的市
    private City selectCity;
    // 当前选中的级别
    private int currentLevel;

    // 视图创建时
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.choose_area, container, false);

        // 获取相关控件的实例
        titleView = view.findViewById(R.id.title_text);
        backButton = view.findViewById(R.id.back_button);
        listView = view.findViewById(R.id.list_view);

        // 初始化适配器
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, dataList);
        // 绑定到控件
        listView.setAdapter(adapter);

        return view;
    }

    // 视图已创建时
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 设置ListView的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(LEVEL_PROVINCE == currentLevel){
                    // 为省行政时

                    // 获取当前选择的省
                    selectProvince = provinceList.get(i);
                    // 查询当前省下面的所有市
                    queryCities();
                }else if(LEVEL_CITY == currentLevel){
                    // 为市行政级别时

                    // 获取当前选择的讪市
                    selectCity = cityList.get(i);
                    // 查询当前市下面的所有县
                    queryCounties();
                }else if(LEVEL_COUNTY == currentLevel){
                    // 为县级别时

                    String weatherId = countyList.get(i).getWeatherId();

                    // 判断碎片位置
                    if(getActivity() instanceof WeatherActivity){
                        // 该碎片在WeatherActivity中,只需要刷新该活动
                        WeatherActivity activity = (WeatherActivity) getActivity();
                        activity.drawerLayout.closeDrawers();
                        activity.swipeRefreshLayout.setRefreshing(true);
                        activity.requestWeather(weatherId);
                    }else {

                        // 准备传参
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                        // 向intent传入Weather Id
                        intent.putExtra("weather_id", weatherId);

                        startActivity(intent);
                        requireActivity().finish();
                    }
                }
            }
        });

        // 设置Button的点击事件
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(LEVEL_COUNTY == currentLevel){
                    // 如果当前选择的是县，则返回到市行政级别
                    queryCities();
                }else if(LEVEL_CITY == currentLevel){
                    // 如果当前选择的是行政市，则返回到省行政级别
                    queryProvinces();
                }
            }
        });

        // 默认加载省列表
        queryProvinces();
    }

    // 查询全国所有的省，优先从数据训库中，如果没有则到服务器上查询
    private void queryProvinces(){
        // 设置标题
        titleView.setText("中国");
        // 隐藏回退按钮
        backButton.setVisibility(View.GONE);

        // 调用LitePal查询接口从数据训上读取省级数据
        provinceList = DataSupport.findAll(Province.class);

        // 如果不为空，则显示到界面上，否则从服务器上获取数据
        if(provinceList.size() > 0){
            // 清空数据队列
            dataList.clear();

            // 迭代省数据，取名字放到数据队列
            for(Province province : provinceList){
                dataList.add(province.getProvinceName());
            }

            // 通知数据有改变
            adapter.notifyDataSetChanged();
            // 滚到条滚到到最上，即顶部
            listView.setSelection(0);
            // 当前选中的行政级别
            currentLevel = LEVEL_PROVINCE;
        }else {
            // 请求地址
            String address = "http://guolin.tech/api/china";

            // 从服务器上查询
            queryFromServer(address, "province");
        }
    }

    // 查询选中省的所有市，优先从数据训库中，如果没有则到服务器上查询
    private void queryCities(){
        // 设置标题
        titleView.setText(selectProvince.getProvinceName());
        // 隐藏回退按钮
        backButton.setVisibility(View.VISIBLE);

        // 调用LitePal查询接口从数据训上读取市级数据
        cityList = DataSupport.where("provinceId = ?", String.valueOf(selectProvince.getId())).find(City.class);

        // 如果不为空，则显示到界面上，否则从服务器上获取数据
        if(cityList.size() > 0){
            // 清空数据队列
            dataList.clear();

            // 迭代市数据，取名字放到数据队列
            for(City city : cityList){
                dataList.add(city.getCityName());
            }

            // 通知数据有改变
            adapter.notifyDataSetChanged();
            // 滚到条滚到到最上，即顶部
            listView.setSelection(0);
            // 当前选中的行政级别
            currentLevel = LEVEL_CITY;
        }else {
            // 获取当前选中的省代号
            int provinceCode = selectProvince.getProvinceCode();
            // 请求地址
            String address = "http://guolin.tech/api/china/" + provinceCode;

            // 从服务器上查询
            queryFromServer(address, "city");
        }
    }

    // 查询选中市的所有县，优先从数据训库中，如果没有则到服务器上查询
    private void queryCounties(){
        // 设置标题
        titleView.setText(selectCity.getCityName());
        // 隐藏回退按钮
        backButton.setVisibility(View.VISIBLE);

        // 调用LitePal查询接口从数据训上读取县级数据
        countyList = DataSupport.where("cityId = ?", String.valueOf(selectCity.getId())).find(County.class);

        // 如果不为空，则显示到界面上，否则从服务器上获取数据
        if(countyList.size() > 0){
            // 清空数据队列
            dataList.clear();

            // 迭代县数据，取名字放到数据队列
            for(County county : countyList){
                dataList.add(county.getCountyName());
            }

            // 通知数据有改变
            adapter.notifyDataSetChanged();
            // 滚到条滚到到最上，即顶部
            listView.setSelection(0);
            // 当前选中的行政级别
            currentLevel = LEVEL_COUNTY;
        }else {
            // 获取当前选中的省代号
            int provinceCode = selectProvince.getProvinceCode();
            // 获取当前选中的市代号
            int cityCode = selectCity.getCityCode();
            // 请求地址
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;

            // 从服务器上查询
            queryFromServer(address, "county");
        }
    }

    // 根据传入的地址和类型从服务器查询省市县数据
    private void queryFromServer(String address, final String type){
        // 显示进度对话框
        showProgressDialog();

        HttpUtil.sendOkHttpRequest(address, new Callback() {    // 向服务器发送请求，callback导包选择okhttp3
            // 当加载失败时
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                requireActivity().runOnUiThread(() -> {
                    // 关闭进度对话框
                    closeProgressDialog();
                    // 显示错误信息
                    Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                });
            }

            // 当加载成功并有数据响应时
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // 响应数据
                assert response.body() != null;
                String responseText = response.body().string();
                // 结果
                boolean result = false;

                if("province".equals(type)){
                    // 省级别类型
                    result = Utility.handleProvinceResponse(responseText);
                }else if("city".equals(type)){
                    // 市级别类型
                    result = Utility.handleCityResponse(responseText, selectProvince.getId());
                }else if("county".equals(type)){
                    // 县级别类型
                    result = Utility.handleCountResponse(responseText, selectCity.getId());
                }

                // 当处理结果正确时
                if(result){
                    // 从子线程切换到UI主线程
                    requireActivity().runOnUiThread(() -> {
                        // 关闭进度对话框
                        closeProgressDialog();

                        if("province".equals(type)){
                            // 重新加载省数据
                            queryProvinces();
                        }else if("city".equals(type)){
                            // 重新加载市数据
                            queryCities();
                        }else {
                            // 重新加载县数据
                            queryCounties();
                        }
                    });
                }
            }
        });

    }

    // 显示进度对话框
    private void showProgressDialog(){
        if(null == progressBar){
            // 创建对话框
            progressBar = new XProgressBar(requireActivity());
            progressBar.setText("正在加载...");
            progressBar.setCanceledOnTouchOutside(false);
        }

        progressBar.show();
    }

    // 关闭进度对话框
    private void closeProgressDialog(){
        if(null != progressBar){
            progressBar.dismiss();
        }
    }
}
