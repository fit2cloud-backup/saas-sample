package com.fit2cloud.web.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class DateJsonValueProcessor implements JsonValueProcessor {
	private DateFormat dateFormatIn;

	public DateJsonValueProcessor(String datePattern) {
		dateFormatIn = new SimpleDateFormat(datePattern);
		dateFormatIn.setTimeZone(TimeZone.getTimeZone("GMT+8"));
	}

	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return process(value);
	}

	public Object processObjectValue(String key, Object value,
			JsonConfig jsonConfig) {
		return process(value);
	}

	private Object process(Object value) {
		return value == null ? null : dateFormatIn
				.format((java.util.Date) value);
	}
}