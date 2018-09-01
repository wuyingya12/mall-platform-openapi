package com.car.mall.openapi.utils;

import java.lang.reflect.Method;

public class ConvertUtil {


    public static void main(String[] args) {
        Object form=new Object();
        convertSetName(form,"bean","resData");
    }


    public static void convertSetName(Object from,String from_temp,String to_temp){

        Method[] fromMethods = from.getClass().getDeclaredMethods();
        Method fromMethod = null, toMethod = null;
        String fromMethodName = null, toMethodName = null;

        for (int i = 0; i < fromMethods.length; i++) {
            fromMethod = fromMethods[i];
            fromMethodName = fromMethod.getName();
            if (!fromMethodName.contains("get"))
            {
                continue;
            }
            toMethodName = "set" + fromMethodName.substring(3);
            String res = String.format("%s.%s(%s.%s());",to_temp,toMethodName,from_temp,fromMethodName);
            System.out.println(res);
        }
    }
}
