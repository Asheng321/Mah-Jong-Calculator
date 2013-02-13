package ch.ysdc.mahjongcalculator.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class HandTile {

    public static final String ID_FIELD_NAME = "id";
    public static final String TILE_FIELD_NAME = "tile";
    public static final String HAND_FIELD_NAME = "hand";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int id;

    @DatabaseField(foreign = true, columnName = HAND_FIELD_NAME)
    private Hand hand;
    @DatabaseField(foreign = true, columnName = TILE_FIELD_NAME)
    private Tile tile;
    
	public HandTile() {
		super();
	}
	
	public HandTile(Hand hand, Tile tile) {
		super();
		this.hand = hand;
		this.tile = tile;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Hand getHand() {
		return hand;
	}
	public void setHand(Hand hand) {
		this.hand = hand;
	}
	public Tile getTile() {
		return tile;
	}
	public void setTile(Tile tile) {
		this.tile = tile;
	}
    
    
}
