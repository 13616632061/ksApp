package com.ui.entity;



import android.os.Parcel;
import android.os.Parcelable;

public class SpecPrice implements Parcelable {
	private int cid, num;
	private String logo, itemKey;
	private double money;

	public static final Creator<SpecPrice> CREATOR = new Creator<SpecPrice>() {
		@Override
		public SpecPrice createFromParcel(Parcel source) {//该方法用于告诉平台如何从包裹里创建数据类实例
			return new SpecPrice(source);
		}
		@Override
		public SpecPrice[] newArray(int size) {
			return new SpecPrice[size];
		}
	};

	public SpecPrice(int cid, int num, String logo, String itemKey, double money){
		this.cid = cid;
		this.num = num;
		this.logo = logo;
		this.itemKey = itemKey;
		this.money = money;
	}

	public SpecPrice(Parcel in){
		this.cid = in.readInt();
		this.num = in.readInt();
		this.logo = in.readString();
		this.itemKey = in.readString();
		this.money = in.readDouble();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.getCid());
		dest.writeInt(this.getNum());
		dest.writeString(this.getLogo());
		dest.writeString(this.getItemKey());
		dest.writeDouble(this.getMoney());
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getItemKey() {
		return itemKey;
	}

	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
	}
}
