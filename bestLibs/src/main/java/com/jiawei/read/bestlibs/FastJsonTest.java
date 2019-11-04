package com.jiawei.read.bestlibs;

import com.alibaba.fastjson.JSON;

import java.util.Map;

public class FastJsonTest  {

    public static void main(String[] args){
        changeStrToMap();
    }

    //JSON 字符串转map对象
    public static void changeStrToMap(){
        String jsonStr="{\"key1\":\"value1\",\"key2\":\"value2\"}";
        System.out.println(jsonStr);

        Map<String,String> jsonMap= (Map<String, String>) JSON.parse(jsonStr);
        for (Map.Entry<String,String> map:jsonMap.entrySet()){
            System.out.println("map.key:"+map.getKey()+",map.value:"+map.getValue());
        }

    }

}
