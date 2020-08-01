package com.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class News implements Parcelable {
	int tid, setlink, digest;
	String subject, linkurl, pic_url, resume;
	private boolean hasRead = false;


	public static final Creator<News> CREATOR = new Creator<News>() {
		@Override
		public News createFromParcel(Parcel source) {//该方法用于告诉平台如何从包裹里创建数据类实例
			return new News(source);
		}
		@Override
		public News[] newArray(int size) {
			return new News[size];
		}
	};

	public News(int tid, int setlink, int digest, String subject, String linkurl, String pic_url, String resume){
		this.tid = tid;
		this.setlink = setlink;
		this.digest = digest;
		this.subject = subject;
        this.linkurl = linkurl;
        this.pic_url = pic_url;
		this.resume = resume;
	}

	public News(Parcel in){
		this.tid = in.readInt();
		this.setlink = in.readInt();
		this.digest = in.readInt();
		this.subject = in.readString();
        this.linkurl = in.readString();
        this.pic_url = in.readString();
		this.resume = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.getTid());
		dest.writeInt(this.getSetlink());
		dest.writeInt(this.getDigest());
		dest.writeString(this.getSubject());
		dest.writeString(this.getLinkurl());
		dest.writeString(this.getPic_url());
		dest.writeString(this.getResume());
	}


	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}


	public int getSetlink() {
		return setlink;
	}

	public void setSetlink(int setlink) {
		this.setlink = setlink;
	}

	public int getDigest() {
		return digest;
	}

	public void setDigest(int digest) {
		this.digest = digest;
	}


	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}


	public String getLinkurl() {
		return linkurl;
	}

	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}

	public String getPic_url() {
		return pic_url;
	}

	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}
	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	public boolean hasRead() {
		return hasRead;
	}

	public void setRead(boolean hasRead) {
		this.hasRead = hasRead;
	}
}

