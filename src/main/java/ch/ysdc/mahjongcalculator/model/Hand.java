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

import ch.ysdc.mahjongcalculator.model.Tile.Category;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 
 * @author djohannot
 */
@DatabaseTable
public class Hand implements Parcelable, Serializable {

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
	// TODO: add the necessary persistence annotations
	private List<Point> points;
	private List<Point> bonuses;
	private int playerWind;
	private List<Tile> flowers;
	private List<Tile> seasons;

	private Tile winningTile;
	private boolean fromWall;
	private boolean fromHill;
	private boolean stealedKong;
	private boolean isLastTile;
	
	private int totalPoints;
	private int totalBonuses;

	// TODO: temp solution, to avoid the fucking foreing collection
	private List<Combination> combinations;

	public Hand() {
		super();
		combinations = new LinkedList<Combination>();
		validity = Validity.VALID;
		points = new LinkedList<Point>();
		bonuses = new LinkedList<Point>();
		playerWind = -1;
		flowers = new LinkedList<Tile>();
		seasons = new LinkedList<Tile>();
		winningTile = null;
		fromWall = false;
		fromHill = false;
		stealedKong = false;
		isLastTile = false;
		totalPoints = 0;
		totalBonuses = 1;
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
		winningTile = null;
		fromWall = false;
		fromHill = false;
		stealedKong = false;
		isLastTile = false;
		totalPoints = 0;
		totalBonuses = 1;
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
		winningTile = in.readParcelable(Tile.class.getClassLoader());
		fromWall = Boolean.valueOf(in.readString());
		fromHill = Boolean.valueOf(in.readString());
		stealedKong = Boolean.valueOf(in.readString());
		isLastTile = Boolean.valueOf(in.readString());
		totalPoints = in.readInt();
		totalBonuses = in.readInt();
	}

	public Hand(Possibility possibility) {
		validity = possibility.getValidity();
		combinations = possibility.getCombinations();

		if ((possibility.getPair() != null) && (possibility.getPair().getTiles().size() > 0)) {
			combinations.add(possibility.getPair());
		}
		if ((possibility.getUnusedTileCombination() != null) && (possibility.getUnusedTileCombination().getTiles().size() > 0)) {
			combinations.add(possibility.getUnusedTileCombination());
		}
		points = new LinkedList<Point>();
		bonuses = new LinkedList<Point>();
		playerWind = -1;
		flowers = new LinkedList<Tile>();
		seasons = new LinkedList<Tile>();
		winningTile = null;
		fromWall = false;
		fromHill = false;
		stealedKong = false;
		isLastTile = false;
		totalPoints = 0;
		totalBonuses = 1;
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

	public void addCombination(Combination c) {
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

	public Tile getWinningTile() {
		return winningTile;
	}

	public boolean isFromWall() {
		return fromWall;
	}

	public boolean isFromHill() {
		return fromHill;
	}

	public boolean isStealedKong() {
		return stealedKong;
	}

	public boolean isLastTile() {
		return isLastTile;
	}

	public void setWinningTile(Tile winningTile) {
		this.winningTile = winningTile;
	}

	public void setFromWall(boolean fromWall) {
		this.fromWall = fromWall;
	}

	public void setFromHill(boolean fromHill) {
		this.fromHill = fromHill;
	}

	public void setStealedKong(boolean stealedKong) {
		this.stealedKong = stealedKong;
	}

	public void setLastTile(boolean isLastTile) {
		this.isLastTile = isLastTile;
	}
	
	public void addFlower(Tile t){
		this.flowers.add(t);
	}
	
	public void addSeasons(Tile t){
		this.seasons.add(t);
	}

	public int getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}

	public int getTotalBonuses() {
		return totalBonuses;
	}

	public void setTotalBonuses(int totalBonuses) {
		this.totalBonuses = totalBonuses;
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
		out.writeParcelable(winningTile, flags);
		out.writeString(String.valueOf(fromWall));
		out.writeString(String.valueOf(fromHill));
		out.writeString(String.valueOf(stealedKong));
		out.writeString(String.valueOf(isLastTile));
		out.writeInt(totalPoints);
		out.writeInt(totalBonuses);
	}

	public static final Parcelable.Creator<Hand> CREATOR = new Parcelable.Creator<Hand>() {
		public Hand createFromParcel(Parcel in) {
			return new Hand(in);
		}

		public Hand[] newArray(int size) {
			return new Hand[size];
		}
	};

	public boolean fromWall() {
		return (fromWall);
	}

	public boolean lastTile() {
		return (isLastTile && (fromWall || fromHill));
	}

	public boolean fromHill() {
		return fromHill;
	}

	public boolean stealedKong() {
		return stealedKong;
	}

	public boolean called() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean prunier() {
		if(winningTile == null){
			return false;
		}
		return (fromWall || fromHill) && (winningTile.getNo() == 5)
				&& (winningTile.getCategory() == Category.CIRCLE);
	}
}
