package com.ui.entity;

import java.io.Serializable;

public class DeliveryCorp implements Serializable {
	private String name;
	private int corp_id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCorp_id() {
		return corp_id;
	}

	public void setCorp_id(int corp_id) {
		this.corp_id = corp_id;
	}
}
