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
public class Tile {

    public static final String ID_FIELD_NAME = "id";
    public static final String NO_FIELD_NAME = "no";
    public static final String IMG_FIELD_NAME = "img";
    public static final String CATEGORY_FIELD_NAME = "category";
    public static final String HAND_TILE_FIELD_NAME = "hands";
    public static final String ISVISIBLE_TILE_FIELD_NAME = "isVisible";
    
    @DatabaseField(generatedId = true,columnName = ID_FIELD_NAME)
    private int id;
    @DatabaseField(columnName = NO_FIELD_NAME)
    private int no;
    @DatabaseField(columnName = IMG_FIELD_NAME)
    private String img;
    @DatabaseField(columnName = ISVISIBLE_TILE_FIELD_NAME)
    private Boolean isVisible;
    @DatabaseField(columnName = CATEGORY_FIELD_NAME)
    private Category category;
    
    @ForeignCollectionField(columnName = ID_FIELD_NAME)
    private ForeignCollection<HandTile> hands;

    public Tile(){
        super();
    }
    
    public Tile(int n, String i, Category c){
    	super();
        this.no = n;
        this.img = i;
        this.category = c;
        this.isVisible = false;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Category getCategory() {
        return category;
    }

    public Boolean getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(Boolean isVisible) {
		this.isVisible = isVisible;
	}

	public void setCategory(Category category) {
        this.category = category;
    }

	public List<HandTile> getHands() {
        ArrayList<HandTile> itemList = new ArrayList<HandTile>();
        for (HandTile hand : hands) {
            itemList.add(hand);
        }
        return itemList;
    }

    public void setHands(ForeignCollection<HandTile> hands) {
        this.hands = hands;
    }

    @Override
    public String toString() {
        return "Tile (" + id + ") " + no + category + " " + (isVisible ? "Hidden" : "Hand");
    }
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + no;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tile other = (Tile) obj;
		if (no != other.no)
			return false;
		return true;
	}

	public enum Category {

        BAMBOO("B"),
        CHARACTER("K"),
        CIRCLE("C"),
        WIND("W"),
        DRAGON("D"),
        FLOWER("F"),
        SEASON("S");
        
        private String value;
        
        Category(String v){
        	value = v;
        }

        @Override
        public String toString() {
            return value;
        }
    };
    
    

}