package com.fit2cloud.web.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

public class JsonUtils {

	private static String timeFormat = "yyyy-MM-dd HH:mm";

	public static String fromObject(Object o) {
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		DateJsonValueProcessor p = new DateJsonValueProcessor(timeFormat);
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, p);
		jsonConfig.registerJsonValueProcessor(java.sql.Date.class, p);
		jsonConfig.registerJsonValueProcessor(java.sql.Timestamp.class, p);
		JSONObject jsonObject = JSONObject.fromObject(o, jsonConfig);
		return jsonObject.toString();
	}

	public static String fromArray(Object o) {
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		DateJsonValueProcessor p = new DateJsonValueProcessor(timeFormat);
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, p);
		jsonConfig.registerJsonValueProcessor(java.sql.Date.class, p);
		jsonConfig.registerJsonValueProcessor(java.sql.Timestamp.class, p);
		JSONArray jsonArray = JSONArray.fromObject(o, jsonConfig);
		return "while(1);" + jsonArray.toString();
	}

}
