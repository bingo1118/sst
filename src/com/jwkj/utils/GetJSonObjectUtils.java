package com.jwkj.utils;

import java.util.List;

import com.google.gson.Gson;

public class GetJSonObjectUtils {
	public static List<Object> getListJson(String json){  
       java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<Object>>() {  
       }.getType();  
       Gson gson = new Gson();
       List<Object> list  = gson.fromJson(json, type);  
       System.out.println("list_gson="+list.size());
       return list;  
	}
}
