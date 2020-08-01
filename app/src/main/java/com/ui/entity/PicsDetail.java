package com.ui.entity;


import android.os.Parcel;
import android.os.Parcelable;

public class PicsDetail implements Parcelable {
	private String newsId;
	private String title;
	private String picUrl;


	public static final Creator<PicsDetail> CREATOR = new Creator<PicsDetail>() {
		@Override
		public PicsDetail createFromParcel(Parcel source) {//该方法用于告诉平台如何从包裹里创建数据类实例
			return new PicsDetail(source);
		}
		@Override
		public PicsDetail[] newArray(int size) {
			return new PicsDetail[size];
		}
	};

	public PicsDetail(String newsId, String title, String picUrl){
		this.newsId = newsId;
		this.title = title;
		this.picUrl = picUrl;
	}

	public PicsDetail(Parcel in){
		this.newsId = in.readString();
		this.title = in.readString();
		this.picUrl = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.getNewsId());
		dest.writeString(this.getTitle());
		dest.writeString(this.getPicUrl());
	}

	public String getNewsId() {
		return newsId;
	}

	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
}


