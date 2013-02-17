/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ysdc.mahjongcalculator.model;

import java.util.regex.Pattern;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/*
 *
 * @author djohannot
 */
@DatabaseTable
public class Tile {

    public static final String ID_FIELD_NAME = "id";
    public static final String NO_FIELD_NAME = "no";
    public static final String IMG_FIELD_NAME = "img";
    public static final String CATEGORY_FIELD_NAME = "category";
    public static final String HAND_FIELD_NAME = "hand";
    public static final String ISVISIBLE_TILE_FIELD_NAME = "isVisible";
    public static final String GAME_FIELD_NAME = "game";
    
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

    @DatabaseField(foreign = true, columnName = HAND_FIELD_NAME)
    private Hand hand;
    @DatabaseField(foreign = true, columnName = GAME_FIELD_NAME)
    private Game game;
    
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
    public Tile(String n, String i, Category c){
    	super();
    	if (Pattern.matches("[0-9]{1}",n)) {
    		this.no = Integer.valueOf(n);
    	}else{
    		this.no = 0;
    	}
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


    public Hand getHand() {
		return hand;
	}

	public void setHand(Hand hand) {
		this.hand = hand;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	@Override
    public String toString() {
        return "Tile (" + id + ") " + no + category + " " + (isVisible ? "Hidden" : "Hand" + " #" + (hand!=null?"handy":"-"));
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

        BAMBOO("b"),
        CHARACTER("k"),
        CIRCLE("c"),
        WIND("w"),
        DRAGON("d"),
        FLOWER("f"),
        SEASON("s");
        
        private String value;
        
        Category(String v){
        	value = v;
        }

        @Override
        public String toString() {
            return value;
        }
        

        public static Category fromValue(String v) {
          if (v != null) {
            for (Category c : Category.values()) {
              if (v.equalsIgnoreCase(c.value)) {
                return c;
              }
            }
          }
          throw new IllegalArgumentException("No constant with value " + v + " found");
        }
	}
}