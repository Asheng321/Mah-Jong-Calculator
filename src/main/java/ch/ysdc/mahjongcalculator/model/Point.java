package ch.ysdc.mahjongcalculator.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class Point implements Parcelable,Serializable{	

	private static final long serialVersionUID = 290917457285109L;
	
	private String name;
	private int points;
	
	public Point(String n, int p){
		this.name = n;
		this.points = p;
	}

	public Point(Parcel in) {
		this.name = in.readString();
		this.points = in.readInt();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeInt(points);
		
	}


	public static final Parcelable.Creator<Point> CREATOR = new Parcelable.Creator<Point>() {
		public Point createFromParcel(Parcel in) {
			return new Point(in);
		}

		public Point[] newArray(int size) {
			return new Point[size];
		}
	};
}
