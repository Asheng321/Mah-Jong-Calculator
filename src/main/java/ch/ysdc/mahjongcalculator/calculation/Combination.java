package ch.ysdc.mahjongcalculator.calculation;

import java.util.Arrays;
import java.util.LinkedList;

import ch.ysdc.mahjongcalculator.model.Tile;

public class Combination {
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
			sb.append(tile.getNo() + "" + tile.getCategory() + ",");
		}
		return sb.substring(0, sb.length()-1) + ")";
	}

}
