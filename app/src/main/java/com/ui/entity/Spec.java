package com.ui.entity;

import com.ui.view.TagCloudView;

import java.io.Serializable;
import java.util.ArrayList;

public class Spec implements Serializable {
	private int cid;
	private String title;
	private ArrayList<SpecItem> item;
	private TagCloudView tagView;
	private ArrayList<Integer> validList;

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList<SpecItem> getItem() {
		return item;
	}

	public void setItem(ArrayList<SpecItem> item) {
		this.item = item;
	}

	public TagCloudView getTagView() {
		return tagView;
	}

	public void setTagView(TagCloudView tagView) {
		this.tagView = tagView;
	}

	public ArrayList<Integer> getValidList() {
		return validList;
	}

	public void setValidList(ArrayList<Integer> validList) {
		this.validList = validList;
	}
}
