package com.fit2cloud.web.sample.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.fit2cloud.sdk.Fit2CloudClient;
import com.fit2cloud.sdk.Fit2CloudException;
import com.fit2cloud.sdk.model.Server;
import com.fit2cloud.web.infrastructure.Messager;
import com.fit2cloud.web.infrastructure.SampleConfigs;
import com.fit2cloud.web.util.JsonUtils;

@Controller
public class SampleController {

	@Autowired
	private SampleConfigs sampleConfigs;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String sample(Model model) {
		return "module/sample";
	}
	
	@RequestMapping(value = "server/list.json", method = RequestMethod.GET)
	public ModelAndView listServers(Model model, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		try {
			String consumerKey = sampleConfigs.getConsumerKey();
			if("${consumer.key}".equals(consumerKey)) {
				responseJson(response, JsonUtils.fromObject(new Messager(false, "请检查/opt/fit2cloud/saas/fit2cloud.properties文件!")));
				return null;
			}
			Fit2CloudClient client = new Fit2CloudClient(consumerKey, sampleConfigs.getSecretKey(), sampleConfigs.getWebspaceRestApiEndPoint());
			Map<String, Object> map = new HashMap<String, Object>();
			Long multiTenantsClusterId = Long.valueOf(sampleConfigs.getMultiTenantsClusterId());
			Long multiTenantsClusterRoleId = Long.valueOf(sampleConfigs.getMultiTenantsClusterRoleId());
			List<Server> servers = client.getClusterServers(multiTenantsClusterId, multiTenantsClusterRoleId);
			Iterator<Server> iter = servers.iterator();
			while(iter.hasNext()) {
				Server server = iter.next();
				if(server.getVmStatus().equals("Deleted")) {
					iter.remove();
				}
			}
			
			for(Server server : servers) {
				if (server.getVmStatus().equals("Running")){
					StringBuilder sb = new StringBuilder();
					long diff = new Date().getTime() - server.getCreated();
					long diffMinutes = diff / (60 * 1000) % 60;
					long diffHours = diff / (60 * 60 * 1000) % 24;
					long diffDays = diff / (24 * 60 * 60 * 1000);
					if (diffDays>0)sb.append(diffDays).append("天");
					if (diffHours>0)sb.append(diffHours).append("小时");
					sb.append(diffMinutes).append("分钟");
					server.setRunningTime(sb.toString());
				}
			}
			
			map.put("total", servers.size());
			map.put("rows", servers);
			responseJson(response, JsonUtils.fromObject(map));
		} catch (Fit2CloudException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "server/add.json", method = RequestMethod.POST)
	public ModelAndView launchServer(Model model, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		try {
			String consumerKey = sampleConfigs.getConsumerKey();
			if("${consumer.key}".equals(consumerKey)) {
				responseJson(response, JsonUtils.fromObject(new Messager(false, "请检查/opt/fit2cloud/saas/fit2cloud.properties文件!")));
				return null;
			}
			Fit2CloudClient client = new Fit2CloudClient(consumerKey, sampleConfigs.getSecretKey(), sampleConfigs.getWebspaceRestApiEndPoint());
			Long multiTenantsClusterId = Long.valueOf(sampleConfigs.getMultiTenantsClusterId());
			Long multiTenantsClusterRoleId = Long.valueOf(sampleConfigs.getMultiTenantsClusterRoleId());
			long serverId = client.launchServer(multiTenantsClusterId, multiTenantsClusterRoleId);
			responseJson(response, JsonUtils.fromObject(new Messager(true, "添加成功,请等待...")));
		} catch (Fit2CloudException e) {
			responseJson(response, JsonUtils.fromObject(new Messager(false, "添加失败!"+e.getMessage())));
		}
		return null;
	}
	
	@RequestMapping(value = "server/{serverId}/delete.json", method = RequestMethod.POST)
	public ModelAndView terminateServer(Model model, HttpServletRequest request,
			HttpServletResponse response, HttpSession session, @PathVariable long serverId) {
		try {
			String consumerKey = sampleConfigs.getConsumerKey();
			if("${consumer.key}".equals(consumerKey)) {
				responseJson(response, JsonUtils.fromObject(new Messager(false, "请检查/opt/fit2cloud/saas/fit2cloud.properties文件!")));
				return null;
			}
			Fit2CloudClient client = new Fit2CloudClient(consumerKey, sampleConfigs.getSecretKey(), sampleConfigs.getWebspaceRestApiEndPoint());
			Long multiTenantsClusterId = Long.valueOf(sampleConfigs.getMultiTenantsClusterId());
			Long multiTenantsClusterRoleId = Long.valueOf(sampleConfigs.getMultiTenantsClusterRoleId());
			boolean result = client.terminateServer(multiTenantsClusterId, multiTenantsClusterRoleId, serverId);
			if(result) {
				responseJson(response, JsonUtils.fromObject(new Messager(true, "移除成功,请等待...")));
				return null;
			}
		} catch (Fit2CloudException e) {
			responseJson(response, JsonUtils.fromObject(new Messager(false, e.getMessage())));
			return null;
		}
		responseJson(response, JsonUtils.fromObject(new Messager(false, "移除失败!")));
		return null;
	}
	
	protected void responseJson(HttpServletResponse response, String json) {
		response.setContentType("application/json");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.println(json);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
}
