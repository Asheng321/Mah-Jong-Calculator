package ch.ysdc.mahjongcalculator.calculation;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import ch.ysdc.mahjongcalculator.model.Tile;

public class Combination implements Parcelable{
	private Type type;
	private String representation;
	private List<Tile> tiles;
	
	public Combination(){
		tiles = new LinkedList<Tile>();
	}
	public Combination(Parcel in) {
		// TODO Auto-generated constructor stub
		//Combination.class.getClassLoader()
		type = in.readParcelable(Type.class.getClassLoader());
		representation = in.readString();
		tiles = Arrays.asList((Tile[])in.readParcelableArray(Tile.class.getClassLoader()));
	}
	public List<Tile> getTiles() {
		return tiles;
	}

	public void setTiles(LinkedList<Tile> tiles) {
		this.tiles = tiles;
	}
	
	public void addTile(Tile tile){
		tiles.add(tile);
	}
	
	public void addTiles(Tile[] tileArray){
		tiles.addAll(Arrays.asList(tileArray));
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("Combination(");
		for(Tile tile : tiles){
			sb.append(tile.getNo() + "" + tile.getCategory() + tile.getId() + ",");
		}
		return sb.substring(0, sb.length()-1) + ")";
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}

	public String getRepresentation() {
		return representation;
	}
	public void setRepresentation() {
		switch(this.type){
		case PAIR:
		case PONG:
		case KONG:
			this.representation = tiles.get(0).getNo() + "" + tiles.get(0).getCategory() + (tiles.get(0).getIsVisible() ? "v" : "h");  
			break;
		case CHOW:
			this.representation = new String();
			Collections.sort(tiles);
			for(Tile t : tiles){
				this.representation += t.getNo();
			}
			this.representation += tiles.get(0).getCategory() + (tiles.get(0).getIsVisible() ? "v" : "h");
			break;
		default:
			this.representation = new String();
			break;
		}
	}
	
	public enum Type implements Parcelable{
        PONG,
        CHOW,
        KONG,
        PAIR;

        public static final Parcelable.Creator<Type> CREATOR = new Parcelable.Creator<Type>() {
        	   
            public Type createFromParcel(Parcel in) {
                return Type.values()[in.readInt()];
        	}
     
            public Type[] newArray(int size) {
        		return new Type[size];
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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeParcelable(type, flags);
		out.writeString(representation);
		out.writeParcelableArray(tiles.toArray(new Tile[tiles.size()]), flags);		
	}
	
	public static final Parcelable.Creator<Combination> CREATOR = new Parcelable.Creator<Combination>() {
        public Combination createFromParcel(Parcel in) {
            return new Combination(in);
        }

        public Combination[] newArray(int size) {
            return new Combination[size];
        }
    };
}
