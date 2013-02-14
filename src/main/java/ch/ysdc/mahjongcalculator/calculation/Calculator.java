package ch.ysdc.mahjongcalculator.calculation;

import java.util.Iterator;
import java.util.LinkedList;

import android.util.Log;
import ch.ysdc.mahjongcalculator.model.Hand;
import ch.ysdc.mahjongcalculator.model.HandTile;
import ch.ysdc.mahjongcalculator.model.Tile;

public class Calculator {
	
	private static String TAG = "Calculator";
	private LinkedList<Possibility> possibilities;
	
	Calculator(Hand hand){
		possibilities = new LinkedList<Possibility>();
		Possibility.resetCounter();
		possibilities.add(new Possibility(hand.getTiles(), null));
	}
	public LinkedList<Possibility> getPossibilities(Hand hand){
		
		Log.d(TAG, "Start Calculator");
		Iterator<Possibility> itP = possibilities.iterator();
		
		while(itP.hasNext()){
			Possibility possibility = itP.next();
			Log.d(TAG, "Possibility: " + possibility + " (unused " + possibility.getUnusedTiles().size() + ")");
			searchPossibility(possibility);
		}
		
		
		return null;
	}

	private void searchPossibility(Possibility possibility){

		while(possibility.getUnusedTiles().size() > 0){
			HandTile htile = possibility.getUnusedTiles().removeFirst();
			
			Log.d(TAG, "tile: " + htile.getTile() + " (stile " + possibility.getUnusedTiles().size() + ")");
			
			LinkedList<Combination> combinations = searchCombination(htile.getTile(),possibility.getUnusedTiles());
			
			switch(combinations.size()){
			case 0:
				if(possibility.getUnusedTiles().size()==1){
					Tile lastTile = possibility.getUnusedTiles().getFirst().getTile();
					if((lastTile.getCategory() == htile.getTile().getCategory()) && (lastTile.getNo() == htile.getTile().getNo())){
						possibility.setPair(lastTile, htile.getTile());
						Log.d(TAG, "Possibility valid!");
						return;
					}
				}
				possibility.setValid(false);
				Log.d(TAG, "Possibility invalid!");
				return;
			case 1:
				Log.d(TAG, "1 combination");
				clearTilesFromUnused(possibility,combinations.getFirst());
				break;
			default:
				Log.d(TAG, "many combination");
				Combination firstCombination = combinations.removeFirst();
				for(Combination c : combinations){
					addPossibility(possibility,c);
				}
				clearTilesFromUnused(possibility,firstCombination);
				break;
			}
		}
		
	}
	private void addPossibility(Possibility oldPossibility, Combination c) {

		Log.d(TAG, "addPossibility");
		Possibility possibility = new Possibility(oldPossibility);
		clearTilesFromUnused(possibility,c);
		possibilities.addLast(possibility);
		
	}
	private void clearTilesFromUnused(Possibility possibility,Combination combination) {
		possibility.addCombinations(combination);
		for(Tile t : combination.getTiles()){
			if(!possibility.getUnusedTiles().remove(t)){
				Log.d(TAG, "ERROR, REMOVE TILE UNFOUND");
				throw new RuntimeException();
			}
		}
		Log.d(TAG, "Combination added");
		
	}

	private LinkedList<Combination> searchCombination(Tile tile, LinkedList<HandTile> unusedTiles) {
		
		LinkedList<Combination> combinations = new LinkedList<Combination>();
		Combination similar = new Combination(tile);
		Tile[] bottomChow = new Tile[2];
		Tile[] middleChow = new Tile[2];
		Tile[] topChow = new Tile[2];
		
		Log.d(TAG, "searchCombination");
		Iterator<HandTile> it = unusedTiles.iterator();
		while(it.hasNext()){
			HandTile htile = it.next();
			
			if(htile.getTile().getCategory() != tile.getCategory()){
				continue;
			}
			switch(htile.getTile().getNo() - tile.getNo()){
				case -2:
					if(bottomChow[0] == null){
						bottomChow[0] = htile.getTile();
						Log.d(TAG, "bottomChow found (" + htile.getTile().getNo() + ")");
					}
					break;
				case -1:
					if(bottomChow[1] == null){
						bottomChow[1] = htile.getTile();
						Log.d(TAG, "bottomChow found (" + htile.getTile().getNo() + ")");
					}else if(middleChow[0] == null){
						middleChow[0] = htile.getTile();
						Log.d(TAG, "middleChow found (" + htile.getTile().getNo() + ")");
					}
					break;
				case 0:
					similar.addTile(htile.getTile());
					Log.d(TAG, "Similar found (" + similar + ")");
					break;
				case 1:
					if(topChow[0] == null){
						topChow[0] = htile.getTile();
						Log.d(TAG, "topChow found (" + htile.getTile().getNo() + ")");
					}else if(middleChow[1] == null){
						middleChow[1] = htile.getTile();
						Log.d(TAG, "middleChow found (" + htile.getTile().getNo() + ")");
					}
					break;
				case 2:
					if(topChow[1] == null){
						topChow[1] = htile.getTile();
						Log.d(TAG, "topChow found (" + htile.getTile().getNo() + ")");
					}
					break;
					default:
						break;
					
			}
		}
		if((bottomChow[0]!=null) && (bottomChow[1]!=null)){
			Combination c = new Combination(tile);
			c.addTiles(bottomChow);
			combinations.add(c);
			Log.d(TAG, "topChow confirmed (" + bottomChow[0].getNo() + bottomChow[1].getNo() + tile.getNo() + ")");
		}
		if((middleChow[0]!=null) && (middleChow[1]!=null)){
			Combination c = new Combination(tile);
			c.addTiles(middleChow);
			combinations.add(c);
			Log.d(TAG, "middleChow confirmed (" + middleChow[0].getNo() + tile.getNo() + middleChow[1].getNo() + ")");
		}
		if((topChow[0]!=null) && (topChow[1]!=null)){
			Combination c = new Combination(tile);
			c.addTiles(topChow);
			combinations.add(c);
			Log.d(TAG, "topChow confirmed (" + tile.getNo()+ topChow[0].getNo()  + topChow[1].getNo() + ")");
		}
		switch(similar.getTiles().size()){
			case 3:
				combinations.add(similar);
				Log.d(TAG, "pong confirmed");
				break;
			case 4:
				combinations.add(similar);
				Combination c = new Combination(tile);
				for(Tile t : similar.getTiles()){
					if(t.equals(tile)){
						continue;
					}
					c.addTile(t);
					if(c.getTiles().size()==3){
						break;
					}
				}
				combinations.add(c);
				Log.d(TAG, "kong confirmed");
			
		}
		Log.d(TAG, "searchCombination finished: (" + combinations + ")");
		return combinations;
	}

}
