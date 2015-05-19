package com.fit2cloud.web.infrastructure;

public class Messager {
	private boolean success;
	private String msg;

	public Messager(boolean success,String msg) {
		super();
		this.success = success;
		this.msg = msg;
	}

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
