package ch.ysdc.mahjongcalculator.model;

import android.os.Parcel;
import android.os.Parcelable;

public enum Validity implements Parcelable{
    MAHJONG,
    VALID,
    INCOMPLETE,
    INVALID,
    ERROR;

    public static final Parcelable.Creator<Validity> CREATOR = new Parcelable.Creator<Validity>() {
    	   
        public Validity createFromParcel(Parcel in) {
            return Validity.values()[in.readInt()];
    	}
 
        public Validity[] newArray(int size) {
    		return new Validity[size];
        }
         
    };
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(ordinal());
		
	}
}