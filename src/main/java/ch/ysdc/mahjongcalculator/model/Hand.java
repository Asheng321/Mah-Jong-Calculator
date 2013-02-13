/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ysdc.mahjongcalculator.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author djohannot
 */
@DatabaseTable
public class Hand {

    public static final String ID_FIELD_NAME = "id";
    public static final String NAME_FIELD_NAME = "name";
    public static final String HAND_TILE_FIELD_NAME = "tiles";
    
    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int id;
    @DatabaseField(columnName = NAME_FIELD_NAME)
    private String name;
    @ForeignCollectionField(columnName = ID_FIELD_NAME)
    private ForeignCollection<HandTile> tiles;

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

	public List<HandTile> getTiles() {
        ArrayList<HandTile> itemList = new ArrayList<HandTile>();
        for (HandTile item : tiles) {
            itemList.add(item);
        }
        return itemList;
    }

    public void setTiles(ForeignCollection<HandTile> tiles) {
        this.tiles = tiles;
    }
    
    public void addTile(Tile tile){
    	HandTile ht = new HandTile(this,tile);
    	getTiles().add(ht);
    }
}
