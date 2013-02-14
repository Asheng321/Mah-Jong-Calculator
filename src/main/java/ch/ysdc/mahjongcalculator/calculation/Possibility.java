package ch.ysdc.mahjongcalculator.calculation;

import java.util.LinkedList;
import java.util.List;

import ch.ysdc.mahjongcalculator.model.HandTile;
import ch.ysdc.mahjongcalculator.model.Tile;

public class Possibility {
	private static int counter = 0;
	private int id;
	private boolean isValid;
	private LinkedList<HandTile> unusedTiles;
	private LinkedList<Combination> combinations;
	private Tile[] pair;
	

	/*****************************************
	CONSTRUCTOR
	*****************************************/
	Possibility(Possibility p){
		id = counter++;
		isValid = p.isValid;
		unusedTiles = p.unusedTiles;
		combinations = p.combinations;
		pair = p.pair;
	}
	
	Possibility(List<HandTile> t, List<Combination> c){
		id = counter++;
		isValid = true;
		
		pair = new Tile[2];
		unusedTiles = (t != null ? new LinkedList<HandTile>(t) : new LinkedList<HandTile>());
		combinations = (c != null ? new LinkedList<Combination>(c) : new LinkedList<Combination>());
	}

	/*****************************************
	STATIC COUNTER ACCESSOR
	*****************************************/
	public static void resetCounter(){
		counter = 0;
	}

	/*****************************************
	ADD ELEMENT
	*****************************************/
	public void addCombinations(Combination c){
		combinations.add(c);
	}
	public void addCombinations(LinkedList<Combination> c){
		combinations.addAll(c);
	}
	
	public void addTile(HandTile t){
		unusedTiles.add(t);
	}
	
	public void setPair(Tile t1, Tile t2){
		pair[0] = t1;
		pair[1] = t2;
	}
	/*****************************************
	GETTER AND SETTER 
	*****************************************/

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	
	public LinkedList<HandTile> getUnusedTiles() {
		return unusedTiles;
	}

	public void setUnusedTiles(LinkedList<HandTile> unusedTiles) {
		this.unusedTiles = unusedTiles;
	}

	public LinkedList<Combination> getCombinations() {
		return combinations;
	}

	public void setCombinations(LinkedList<Combination> combinations) {
		this.combinations = combinations;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString(){
		return this.getId() + " (" + this.getUnusedTiles().size() + ", " + this.getCombinations().size() + ")";
	}
	
}
