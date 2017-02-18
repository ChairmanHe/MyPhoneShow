package com.hjt.phoneshow.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class JsonUtil {
	private static JsonUtil instance;
	
	private Gson gson;
	
	private JsonUtil(){
		gson=new Gson();
	}
	
	public static JsonUtil getJsonUtil(){
		if(instance==null){
			instance=new JsonUtil();
		}
		return instance;
	}
	/**
	 * transform a String to a Java bean
	 * <p>
	 * ex: Person is a java bean, Str is the String will be transformed to
	 * Person.
	 * <p>
	 * user can call this method like:
	 * <p>
	 * Person p=JsonUtil.getJsonUtil().fromJson(str,Person.class);
	 */
	public <T> T fromJson(String jsonData, Class<T> clz) {
		// TODO Auto-generated method stub
		return gson.fromJson(jsonData, clz);
	}

	/**
	 * transform a String to a List,to get Type ,user should use new
	 * TypeToken<List<T>>(){}.getType()
	 * <p>
	 * ex: Person is a java bean, Str is the String will be transformed to
	 * Person. user can call this method like:
	 * <p>
	 * List<Person> ps=JsonUtil.getJsonUtil().fromJson(str,new
	 * TypeToken<List<Person>>(){}.getType());
	 * </p>
	 */
	public <T> T fromJson(String jsonData, Type type) {
		// TODO Auto-generated method stub
		return gson.fromJson(jsonData, type);

	}

	/**
	 * call this method to transform a Object to String
	 * <p>
	 * the Object can be a java bean or a List
	 */
	public String toJson(Object object) {
		// TODO Auto-generated method stub
		return gson.toJson(object);
	}
}
