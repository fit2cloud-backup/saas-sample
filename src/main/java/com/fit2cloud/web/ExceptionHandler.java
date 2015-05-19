package com.fit2cloud.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.fit2cloud.web.infrastructure.Messager;
import com.fit2cloud.web.util.JsonUtils;

public class ExceptionHandler implements HandlerExceptionResolver{

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object arg2, Exception exception) {
		
		response.setStatus(500);
		String msg = exception.toString();
		if (msg.length() > 500){
			msg = msg.substring(0, 499) + "...";
		}
		responseJson(response, JsonUtils.fromObject(new Messager(false,
				msg)));
		return null;
	}
	
	protected void responseJson(HttpServletResponse response, String json) {
		response.setContentType("application/json");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.println(json);
		} catch (IOException e) {
		} finally {
			out.close();
		}
	}

}
