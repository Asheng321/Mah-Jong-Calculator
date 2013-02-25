package ch.ysdc.mahjongcalculator.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class Point implements Parcelable,Serializable,Comparable{	

	private static final long serialVersionUID = 290917457285109L;
	
	private String name;
	private int points;
	private boolean isBonus;
	private Combination combination;
	
	public Point(String n, int p, Combination c,boolean b){
		this.name = n;
		this.points = p;
		this.combination = c;
		this.isBonus = b;
	}

	public Point(Parcel in) {
		this.name = in.readString();
		this.points = in.readInt();
		this.isBonus = Boolean.valueOf(in.readString());
		this.combination = in.readParcelable(Combination.class.getClassLoader());
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

	public Combination getCombination() {
		return combination;
	}

	public void setCombination(Combination combination) {
		this.combination = combination;
	}

	public boolean isBonus() {
		return isBonus;
	}

	public void setBonus(boolean isBonus) {
		this.isBonus = isBonus;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeInt(points);
		dest.writeString(String.valueOf(isBonus));
		dest.writeParcelable(combination, flags);
		
	}


	public static final Parcelable.Creator<Point> CREATOR = new Parcelable.Creator<Point>() {
		public Point createFromParcel(Parcel in) {
			return new Point(in);
		}

		public Point[] newArray(int size) {
			return new Point[size];
		}
	};

	public Integer calculateValue(){
		return (isBonus ? 0 : 100) + points;
	}
	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return this.calculateValue().compareTo(((Point)arg0).calculateValue());
	}
}
