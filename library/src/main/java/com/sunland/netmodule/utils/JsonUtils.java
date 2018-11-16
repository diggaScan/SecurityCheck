package com.sunland.netmodule.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * Created by LSJ on 2017/11/3.
 */

public class JsonUtils {

    public static <T> T fromJson(String json, Class<T> obj) {
        T retObj = null;

        Gson gsonObj = new Gson();
        try {
            retObj = gsonObj.fromJson(json, obj);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return retObj;
    }

    public static <T> T fromJsonType(String json, Type type) {
        T retObj = null;
        Gson gsonObj = new Gson();
        try {
            retObj = gsonObj.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return retObj;
    }

    public static <T> String toJson(T obj) {
        String gsStr = "";
        Gson gsObj = new Gson();
        try {
            gsStr = gsObj.toJson(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gsStr;
    }
}
