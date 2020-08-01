package com.ui.entity;

import java.io.Serializable;

public class SellerReport implements Serializable {
	private int seller_id;
	private String seller_name;
	private String bn;
	private double today_income;
	private double yesterday_income;
	private double month_income;

	public int getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(int seller_id) {
		this.seller_id = seller_id;
	}

	public String getSeller_name() {
		return seller_name;
	}

	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}

	public String getBn() {
		return bn;
	}

	public void setBn(String bn) {
		this.bn = bn;
	}

	public double getToday_income() {
		return today_income;
	}

	public void setToday_income(double today_income) {
		this.today_income = today_income;
	}

	public double getYesterday_income() {
		return yesterday_income;
	}

	public void setYesterday_income(double yesterday_income) {
		this.yesterday_income = yesterday_income;
	}

	public double getMonth_income() {
		return month_income;
	}

	public void setMonth_income(double month_income) {
		this.month_income = month_income;
	}
}
