/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ysdc.mahjongcalculator.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author djohannot
 */
@DatabaseTable
public class Hand implements Parcelable, Serializable{

	private static final long serialVersionUID = 362476378791796419L;
	public static final String ID_FIELD_NAME = "id";
    public static final String NAME_FIELD_NAME = "name";
    public static final String VALIDITY_FIELD_NAME = "validity";
    public static final String COMBO_FIELD_NAME = "combinations";
    
    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int id;
    @DatabaseField(columnName = NAME_FIELD_NAME)
    private String name;
    @DatabaseField(columnName = VALIDITY_FIELD_NAME)
    private Validity validity;
    @ForeignCollectionField(columnName = COMBO_FIELD_NAME)
    private ForeignCollection<Combination> storedCombinations;
    //TODO: add the necessary persistence annotations
	private List<Point> points;
	private List<Point> bonuses;
	private int playerWind;
	private List<Tile> flowers;
	private List<Tile> seasons;
	
	//TODO: temp solution, to avoid the fucking foreing collection
    private List<Combination> combinations;
    
    public Hand(){
    	super();
    	combinations = new LinkedList<Combination>();
    	validity = Validity.VALID;
		points = new LinkedList<Point>();
		bonuses = new LinkedList<Point>();
		playerWind = -1;
		flowers = new LinkedList<Tile>();
		seasons = new LinkedList<Tile>();
    }
    public Hand(String name) {
		super();
		this.name = name;
    	combinations = new LinkedList<Combination>();
    	validity = Validity.VALID;
		points = new LinkedList<Point>();
		bonuses = new LinkedList<Point>();
		playerWind = -1;
		flowers = new LinkedList<Tile>();
		seasons = new LinkedList<Tile>();
	}

	public Hand(Parcel in) {
		id = in.readInt();
		name = in.readString();
		combinations = new LinkedList<Combination>();
		in.readTypedList(combinations, Combination.CREATOR);
		validity = in.readParcelable(Validity.class.getClassLoader());
		points = new LinkedList<Point>();
		in.readTypedList(points, Point.CREATOR);
		bonuses = new LinkedList<Point>();
		in.readTypedList(bonuses, Point.CREATOR);

		playerWind = in.readInt();
		flowers = new LinkedList<Tile>();
		in.readTypedList(flowers, Tile.CREATOR);
		seasons = new LinkedList<Tile>();
		in.readTypedList(seasons, Tile.CREATOR);
	}
	public Hand(Possibility possibility) {
		validity = possibility.getValidity();
		combinations = possibility.getCombinations();
		if(possibility.getPair() != null){
			combinations.add(possibility.getPair());
		}
		if(possibility.getUnusedTileCombination() != null){
			combinations.add(possibility.getUnusedTileCombination());
		}
		points = new LinkedList<Point>();
		bonuses = new LinkedList<Point>();
		playerWind = -1;
		flowers = new LinkedList<Tile>();
		seasons = new LinkedList<Tile>();
	}
	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
    public Validity getValidity() {
		return validity;
	}
	public void setValidity(Validity validity) {
		this.validity = validity;
	}
	public ForeignCollection<Combination> getStoredCombinations() {
		return storedCombinations;
	}
	public void setStoredCombinations(
			ForeignCollection<Combination> storedCombinations) {
		this.storedCombinations = storedCombinations;
	}
	public List<Combination> getCombinations() {
		return combinations;
	}
	public void setCombinations(List<Combination> combinations) {
		this.combinations = combinations;
	}
	public void addCombination(Combination c){
		this.combinations.add(c);
    }
	
	public List<Point> getPoints() {
		return points;
	}
	public void setPoints(List<Point> points) {
		this.points = points;
	}
	public List<Point> getBonuses() {
		return bonuses;
	}
	public void setBonuses(List<Point> bonuses) {
		this.bonuses = bonuses;
	}
	
	
	
	public int getPlayerWind() {
		return playerWind;
	}
	public void setPlayerWind(int playerWind) {
		this.playerWind = playerWind;
	}
	public List<Tile> getFlowers() {
		return flowers;
	}
	public void setFlowers(List<Tile> flowers) {
		this.flowers = flowers;
	}
	public List<Tile> getSeasons() {
		return seasons;
	}
	public void setSeasons(List<Tile> seasons) {
		this.seasons = seasons;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {		
		out.writeInt(id);
		out.writeString(name);
		out.writeTypedList(combinations);
		out.writeParcelable(validity, flags);
		out.writeTypedList(points);
		out.writeTypedList(bonuses);
		out.writeInt(playerWind);
		out.writeTypedList(flowers);
		out.writeTypedList(seasons);
	}
	
	public static final Parcelable.Creator<Hand> CREATOR = new Parcelable.Creator<Hand>() {
        public Hand createFromParcel(Parcel in) {
            return new Hand(in);
        }

        public Hand[] newArray(int size) {
            return new Hand[size];
        }
    };
}
