package ch.ysdc.mahjongcalculator.calculation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ch.ysdc.mahjongcalculator.model.Tile;

public class Combination {
	private Type type;
	private String representation;
	private LinkedList<Tile> tiles;
	
	public Combination(){
		tiles = new LinkedList<Tile>();
	}
	public Combination(Tile tile){
		tiles = new LinkedList<Tile>();
		tiles.addFirst(tile);
	}
	public LinkedList<Tile> getTiles() {
		return tiles;
	}

	public void setTiles(LinkedList<Tile> tiles) {
		this.tiles = tiles;
	}
	
	public void addTile(Tile tile){
		tiles.addLast(tile);
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
	
	public enum Type {
        PONG,
        CHOW,
        KONG,
        PAIR;
	}
}
