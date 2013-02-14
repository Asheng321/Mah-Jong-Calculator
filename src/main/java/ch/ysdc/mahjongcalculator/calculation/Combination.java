package ch.ysdc.mahjongcalculator.calculation;

import java.util.Arrays;
import java.util.LinkedList;

import ch.ysdc.mahjongcalculator.model.Tile;

public class Combination {
	private LinkedList<Tile> tiles;

	Combination(Tile tile){
		tiles = new LinkedList<Tile>();
		tiles.add(tile);
	}
	public LinkedList<Tile> getTiles() {
		return tiles;
	}

	public void setTiles(LinkedList<Tile> tiles) {
		this.tiles = tiles;
	}
	
	public void addTile(Tile tile){
		tiles.add(tile);
	}
	
	public void addTiles(Tile[] tileArray){
		tiles.addAll(Arrays.asList(tileArray));
	}

}
