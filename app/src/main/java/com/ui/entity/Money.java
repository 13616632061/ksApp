package com.ui.entity;

import java.io.Serializable;

public class Money implements Serializable {
	private String type;
	private String mtime;
	private String message;
	private double money;
	private int status;
	private String bank_id;
	private String bank_name;
	private double service;

	public double getService() {
		return service;
	}

	public void setService(double service) {
		this.service = service;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMtime() {
		return mtime;
	}

	public void setMtime(String mtime) {
		this.mtime = mtime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusStr() {
		if (status == 1) {
			return "审核中";
		} else if(status == 2) {
			return "未通过";
		} else {
			return (type.equals("withdraw")) ? "提现成功" : "充值成功";
		}
	}

	public String getBank_id() {
		return bank_id;
	}

	public void setBank_id(String bank_id) {
		this.bank_id = bank_id;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
}
