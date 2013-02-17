package ch.ysdc.mahjongcalculator.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

public class Game {

    public static final String ID_FIELD_NAME = "id";
    public static final String IS_CURRENT_FIELD_NAME = "isCurrent";
    public static final String NAME_FIELD_NAME = "name";
    public static final String TILES_FIELD_NAME = "tiles";
    public static final String HANDS_FIELD_NAME = "hand";
    
    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int id;
    @DatabaseField(columnName = NAME_FIELD_NAME)
    private String name;
    @DatabaseField(columnName = IS_CURRENT_FIELD_NAME)
    private Boolean isCurrent;
    @ForeignCollectionField(columnName = TILES_FIELD_NAME)
    private ForeignCollection<Tile> tiles;

    @DatabaseField(foreign = true, columnName = HANDS_FIELD_NAME)
    private Hand hand;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public Boolean getIsCurrent() {
		return isCurrent;
	}
	public void setIsCurrent(Boolean isCurrent) {
		this.isCurrent = isCurrent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ForeignCollection<Tile> getTiles() {
		return tiles;
	}
	public void setTiles(ForeignCollection<Tile> tiles) {
		this.tiles = tiles;
	}
	public Hand getHand() {
		return hand;
	}
	public void setHand(Hand h) {
		this.hand = h;
	}

	public void addTile(Tile t){
		this.tiles.add(t);
	}
    
}
