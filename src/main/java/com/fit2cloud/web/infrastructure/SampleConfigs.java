package com.fit2cloud.web.infrastructure;


public class SampleConfigs {

	private String webspaceRestApiEndPoint;
	private String consumerKey;
	private String secretKey;
	private String multiTenantsClusterId;
	private String multiTenantsClusterRoleId;

	public String getWebspaceRestApiEndPoint() {
		return webspaceRestApiEndPoint;
	}

	public void setWebspaceRestApiEndPoint(String webspaceRestApiEndPoint) {
		this.webspaceRestApiEndPoint = webspaceRestApiEndPoint;
	}

	public String getConsumerKey() {
		return consumerKey;
	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getMultiTenantsClusterId() {
		return multiTenantsClusterId;
	}

	public void setMultiTenantsClusterId(String multiTenantsClusterId) {
		this.multiTenantsClusterId = multiTenantsClusterId;
	}

	public String getMultiTenantsClusterRoleId() {
		return multiTenantsClusterRoleId;
	}

	public void setMultiTenantsClusterRoleId(String multiTenantsClusterRoleId) {
		this.multiTenantsClusterRoleId = multiTenantsClusterRoleId;
	}
}
