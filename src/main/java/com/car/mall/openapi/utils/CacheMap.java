package com.car.mall.openapi.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheMap {
    private static Map<String,String> map = new ConcurrentHashMap();
    private CacheMap(){}
    private static CacheMap instance = new CacheMap();
    public static CacheMap getInstance() {
        return instance ;
    }

    //添加数据
    public static void put(String key,String value){
        map.put(key, value);
    }

    //获得数据
    public static String get(String key){
        return map.get(key);
    }

    //删除数据
    public static void remove(String key){
        map.remove(key);
    }

    //获得数据
    public static Map<String,String> getMap() {
        return map ;
    }

    //清空
    public static void flushAll(){
        map.clear();
    }






}
