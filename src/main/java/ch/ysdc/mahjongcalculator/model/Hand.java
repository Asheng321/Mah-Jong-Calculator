/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ysdc.mahjongcalculator.model;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author djohannot
 */
@DatabaseTable
public class Hand {

    public static final String ID_FIELD_NAME = "id";
    public static final String NAME_FIELD_NAME = "name";
    public static final String TILES_FIELD_NAME = "tiles";
    public static final String GAME_FIELD_NAME = "game";
    
    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int id;
    @DatabaseField(columnName = NAME_FIELD_NAME)
    private String name;
    @ForeignCollectionField(columnName = TILES_FIELD_NAME)
    private ForeignCollection<Tile> tiles;
    @DatabaseField(foreign = true, columnName = GAME_FIELD_NAME)
    private Game game;
    
    public Hand(){
    	super();
    }
    public Hand(String name) {
		super();
		this.name = name;
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

	public List<Tile> getTiles() {
        ArrayList<Tile> itemList = new ArrayList<Tile>();
        for (Tile item : tiles) {
            itemList.add(item);
        }
        return itemList;
    }

    public void setTiles(ForeignCollection<Tile> tiles) {
        this.tiles = tiles;
    }
    
    public void addTile(Tile handtile){
		this.tiles.add(handtile);
    }
}
