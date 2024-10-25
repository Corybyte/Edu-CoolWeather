package com.coolweather.android;

import org.junit.Test;
import java.lang.reflect.Field;

import com.coolweather.android.db.City;
import com.coolweather.android.db.County;
import com.coolweather.android.db.Province;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class EntityTest {
    @Test
    public  void test_db_City(){
        // 获取类的Class对象
        Class<?> clazz = City.class;
        // 获取类的所有成员变量
        System.out.println("Class City:");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println("成员变量: " + field.getName() + ", 数据类型: " + field.getType());
        }
    }

    @Test
    public  void test_db_County(){
        // 获取类的Class对象
        Class<?> clazz = County.class;
        // 获取类的所有成员变量
        System.out.println("Class County:");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println("成员变量: " + field.getName() + ", 数据类型: " + field.getType());
        }
    }

    @Test
    public  void test_db_Province(){
        // 获取类的Class对象
        Class<?> clazz = Province.class;
        // 获取类的所有成员变量
        System.out.println("Class Province:");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println("成员变量: " + field.getName() + ", 数据类型: " + field.getType());
        }
    }
}