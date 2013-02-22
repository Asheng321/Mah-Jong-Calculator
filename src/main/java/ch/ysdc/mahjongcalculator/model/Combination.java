package ch.ysdc.mahjongcalculator.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Combination implements Parcelable,Serializable{

	private static final long serialVersionUID = 7620094355764403955L;
	public static final String ID_FIELD_NAME = "id";
    public static final String TYPE_FIELD_NAME = "type";
    public static final String REPRES_FIELD_NAME = "representation";
    public static final String TILES_FIELD_NAME = "tiles";
    public static final String HAND_FIELD_NAME = "hand";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int id;
    @DatabaseField(columnName = TYPE_FIELD_NAME)
	private Type type;
    @DatabaseField(columnName = REPRES_FIELD_NAME)
	private String representation;
    @ForeignCollectionField(columnName = TILES_FIELD_NAME)
	private ForeignCollection<Tile> storedTiles;
    @DatabaseField(columnName = HAND_FIELD_NAME)
    private Hand hand;

	private List<Tile> tiles;

	public Combination(){
		tiles = new LinkedList<Tile>();
		type = Type.NONE;
	}
	public Combination(Combination c){
		if(c != null){
			this.id = c.id;
			this.type = c.type;
			if(c.representation != null){
				this.representation = new String();
			}
			this.hand = c.hand;
			if(c.tiles != null){
				this.tiles = new CopyOnWriteArrayList<Tile>(c.tiles);
			}else{
				this.tiles = new LinkedList<Tile>();
			}
		}else{
			this.tiles = new LinkedList<Tile>();
		}
	}
	
	public Combination(Tile t){
		tiles = new LinkedList<Tile>();
		type = Type.NONE;
		tiles.add(t);
	}
	public Combination(Parcel in) {
		id = in.readInt();
		type = in.readParcelable(Type.class.getClassLoader());
		representation = in.readString();
		tiles = new LinkedList<Tile>();
		in.readTypedList(tiles, Tile.CREATOR);
		hand = in.readParcelable(Hand.class.getClassLoader());
	}

	public void addTile(Tile tile){
		tiles.add(tile);
	}
	
	public void addTiles(Tile[] tileArray){
		tiles.addAll(Arrays.asList(tileArray));
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("Combination(" + type + "): ");
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public ForeignCollection<Tile> getStoredTiles() {
		return storedTiles;
	}


	public void setStoredTiles(ForeignCollection<Tile> storedTiles) {
		this.storedTiles = storedTiles;
	}



	public Hand getHand() {
		return hand;
	}



	public void setHand(Hand hand) {
		this.hand = hand;
	}



	public List<Tile> getTiles() {
		return tiles;
	}



	public void setTiles(List<Tile> tiles) {
		this.tiles = tiles;
	}



	public String getRepresentation() {
		if(representation == null){
			setRepresentation();
		}
		return representation;
	}
	
	private void setRepresentation() {
		switch(this.type){
		case PAIR:
		case PONG:
		case KONG:
			List<Tile> list = new ArrayList<Tile>(tiles);
			this.representation = list.get(0).getNo() + "" + list.get(0).getCategory() + (list.get(0).getIsVisible() ? "v" : "h");  
			break;
		case CHOW:
			this.representation = new String();
			List<Tile> list2 = new ArrayList<Tile>(tiles);
			Collections.sort(list2);
			for(Tile t : list2){
				this.representation += t.getNo();
			}
			this.representation += list2.get(0).getCategory() + (list2.get(0).getIsVisible() ? "v" : "h");
			break;
		case NONE:
			this.representation = new String();
			List<Tile> list3 = new ArrayList<Tile>(tiles);
			Collections.sort(list3);
			for(Tile t : list3){
				this.representation += t.getNo() + "" + t.getCategory();
			}
			break;
		default:
			this.representation = new String();
			break;
		}
	}
	
	public enum Type implements Parcelable{
        KONG(16),
        PONG(8),
        CHOW(4),
        PAIR(1),
        NONE(0);

        private int value;
        
        Type(int v){
        	this.value = v;
        }
        public static final Parcelable.Creator<Type> CREATOR = new Parcelable.Creator<Type>() {
        	   
            public Type createFromParcel(Parcel in) {
                return Type.values()[in.readInt()];
        	}
     
            public Type[] newArray(int size) {
        		return new Type[size];
            }
             
        };
        
		public int getValue() {
			return value;
		}

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
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(id);
		out.writeParcelable(type, flags);
		out.writeString(representation);
		out.writeTypedList(tiles);	
		out.writeParcelable(hand, flags);
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
