package com.ui.entity;

import com.ui.util.SysUtils;

import java.io.Serializable;

public class Shipping implements Serializable {
	private int id;
	private String name;
	private double fee;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public String getFormatName() {
		return this.name + " " + SysUtils.priceFormat(this.fee, true);
	}
}
