package com.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class OrderGoods implements Serializable, Parcelable {
	private float quantity;
	private String name;
	private double price;
	private String formatPrice;
	private String attr;

	protected OrderGoods(Parcel in) {
		quantity = in.readInt();
		name = in.readString();
		price = in.readDouble();
		formatPrice = in.readString();
		attr = in.readString();
	}

	public OrderGoods(float quantity, String name, double price) {
		this.quantity = quantity;
		this.name = name;
		this.price = price;
	}

	public OrderGoods() {
	}

	public static final Creator<OrderGoods> CREATOR = new Creator<OrderGoods>() {
		@Override
		public OrderGoods createFromParcel(Parcel in) {
			return new OrderGoods(in);
		}

		@Override
		public OrderGoods[] newArray(int size) {
			return new OrderGoods[size];
		}
	};

	public float getQuantity() {
		return quantity;
	}

	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getFormatPrice() {
		return formatPrice;
	}

	public void setFormatPrice(String formatPrice) {
		this.formatPrice = formatPrice;
	}

	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public String toString() {
		return "OrderGoods{" +
				"quantity=" + quantity +
				", name='" + name + '\'' +
				", price=" + price +
				", formatPrice='" + formatPrice + '\'' +
				", attr='" + attr + '\'' +
				'}';
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeFloat(quantity);
		dest.writeString(name);
		dest.writeDouble(price);
		dest.writeString(formatPrice);
		dest.writeString(attr);
	}
}
