package ch.ysdc.mahjongcalculator.manager;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.util.Log;
import ch.ysdc.mahjongcalculator.model.Combination;
import ch.ysdc.mahjongcalculator.model.Combination.Type;
import ch.ysdc.mahjongcalculator.model.Possibility;
import ch.ysdc.mahjongcalculator.model.Tile;
import ch.ysdc.mahjongcalculator.model.Validity;

public class CombinationManager {
	
	private static String TAG = "Calculator";
	private List<Possibility> possibilities;
	private boolean hasValidMahjong;
	
	/****************************************************************************
	 * Constructor takes a tiles linked list as parameter
	 * @param tiles the tiles used to find the possibilities
	 ****************************************************************************/
	public CombinationManager(){
		possibilities = new CopyOnWriteArrayList<Possibility>();
	}

	/****************************************************************************
	 * Calculate the different possible combination based on a set of tiles given
	 * in parameter
	 * @param tiles the tiles to use to calculate the possibilities
	 * @return the different possibilities with different combination
	 ****************************************************************************/
	public List<Possibility> getPossibilities(LinkedList<Tile> tiles){
		hasValidMahjong = false;
		
		Possibility.resetCounter();
		possibilities.add(new Possibility(tiles, null));
		
		Log.d(TAG, "Start Calculator");
		
		for(int i=0; i<possibilities.size(); i++){
			Possibility possibility = possibilities.get(i);
			Log.d(TAG, "possibilities (" + i + "/" + possibilities.size() + ")");
			Log.d(TAG, "New possibility: " + possibility.getCombinations().size() + "." + possibility.getPair() + " (unused " + possibility.getUnusedTiles().size() + ")");
			searchPossibility(possibility);
		}
		
		Log.d(TAG, "Possibilities calculated: " + possibilities.size());

		
		for(Possibility p : possibilities){
			Log.d(TAG, "Possibility " + p.getId() + " (" + p.getValidity() + "," + p.getUnusedTiles() + (p.getPair() != null ? "paired" : "no pair"));
			for(Combination c : p.getCombinations()){
				Log.d(TAG, "* " + c);
			}
		}
		return filterValidPossibilities();
		
	}

	/****************************************************************************
	 * Remove the invalid possibilities
	 * @return the filtered possibilities
	 ***************************************************************************/
	private List<Possibility> filterValidPossibilities() {		
		Log.d(TAG, "Filter possibilities. mahjong? " + this.hasValidMahjong);

		Iterator<Possibility> it = possibilities.iterator();
		while(it.hasNext()){
			Possibility p = it.next();

			if (p.getCombinations().size()>4){
				p.setValidity(Validity.INVALID);
			}
			
			if((hasValidMahjong && (p.getValidity() != Validity.MAHJONG)) ||
					(p.getValidity() == Validity.ERROR) ||
					(p.alreadyListed(possibilities))){
				possibilities.remove(p);
			}
		}
		Log.d(TAG, "Filtered possibilities calculated: " + possibilities.size());
		return possibilities;
	}

	/****************************************************************************
	 * Search possible combination for the given possibility
	 * @param possibility the possibility to analyze
	 ***************************************************************************/
	private void searchPossibility(Possibility possibility){
		
		while(possibility.getUnusedTiles().size() > 0){
			Log.d(TAG, "* Current possibility: " + possibility.getCombinations().size() + "," + possibility.getPair() +  
					   "\n* Tiles " + possibility.displayTiles());
			
			Tile tile = possibility.getUnusedTiles().remove(0);
			
			Log.d(TAG, "* tile (" + tile.getNo() + tile.getCategory() + tile.getId() + ")");
			
			List<Combination> combinations = searchCombination(tile,possibility.getUnusedTiles());
			
			switch(combinations.size()){
			case 0:
				//TODO
				if(tile.getIsVisible()){
					//set validity of the possibility to error
					Log.d(TAG, "* Possibility invalid!");
					possibility.setValidity(Validity.ERROR);
					//stop the loop
					return;
				}
				//set validity of the possibility to invalid
				possibility.setValidity(Validity.INCOMPLETE);
				//Add the tile to the special combination (unusableTile)
				possibility.getUnusedTileCombination().addTile(tile);
				//remove it from the list of unusedTiles
				possibility.getUnusedTiles().remove(tile);
				break;
			case 1:
				Log.d(TAG, "* 1 combination");

				if(combinations.get(0).getType() == Type.PAIR){
					if(possibility.getPair()== null){
						possibility.setPair(combinations.get(0));
					}else{
						possibility.setValidity(Validity.INVALID);
						possibility.addCombinations(combinations.get(0));
					}
				}else{
					possibility.addCombinations(combinations.get(0));
				}
				clearTilesFromUnused(possibility,combinations.get(0));
				break;
			default:
				Log.d(TAG, "* many combinations");
				Combination firstCombination = combinations.remove(0);
				for(Combination c : combinations){
					addPossibility(possibility,c);
				}
				if(firstCombination.getType() == Type.PAIR){
					if(possibility.getPair()== null){
						possibility.setPair(firstCombination);
					}else{
						possibility.setValidity(Validity.INVALID);
						possibility.addCombinations(firstCombination);
					}
				}else{
					possibility.addCombinations(firstCombination);
				}
				clearTilesFromUnused(possibility,firstCombination);
				break;
			}
			Log.d(TAG, "* End Tile Loop: (" + possibility.getCombinations().size() + "," + (possibility.getPair() != null ? "paired" : "unpaired")  + "," + possibility.getUnusedTiles().size() + ")");
		}
		
		if(isWinningPossibility(possibility)){
			this.hasValidMahjong = true;
		}
	}
	
	private boolean isWinningPossibility(Possibility possibility) {
		if(possibility.getValidity() == Validity.VALID){
			possibility.setValidity(Validity.MAHJONG);
			return true;
		}
		return false;
	}

	/****************************************************************************
	 * Add a new possibility to the list by copying another possibility and by 
	 * adding a possibility to it.
	 * @param oldPossibility the possibility used as a model
	 * @param c the combination to add to the new combination
	 ***************************************************************************/
	private void addPossibility(Possibility oldPossibility, Combination c) {

		Log.d(TAG, "*** addPossibility");
		Possibility possibility = new Possibility(oldPossibility);

		
		if(c.getType() == Type.PAIR){
			if(possibility.getPair()== null){
				possibility.setPair(c);
			}else{
				possibility.setValidity(Validity.INVALID);
				possibility.addCombinations(c);
			}
		}else{
			possibility.addCombinations(c);
		}
		clearTilesFromUnused(possibility,c);
		possibilities.add(possibility);
		
	}
	/****************************************************************************
	 * Remove the tiles used in the combination from the unused tiles and add 
	 * the new combination to the possibility
	 * @param possibility the possibility who own the unused tiles
	 * @param combination the new combination to add to the possibility
	 ***************************************************************************/
	private boolean clearTilesFromUnused(Possibility possibility,Combination combination) {
		Log.d(TAG, "## clearTilesFromUnused");
		
		//We don't take the first because it's the parent, who was already removed
		for(Tile t : combination.getTiles()){
			if(!possibility.getUnusedTiles().remove(t)){
				Log.d(TAG, "not found");
			}else{
				Log.d(TAG, "found");
			}
		}
		Log.d(TAG, "## Combination added");
		return true;
	}

	/****************************************************************************
	 * Search in the unusedTiles for tiles that can make a combination with the 
	 * tile given in parameter
	 * @param tile the main tile
	 * @param unusedTiles the list of available tiles to make a combination with.
	 * @return the list of all possible combination
	 ***************************************************************************/
	private List<Combination> searchCombination(Tile tile, List<Tile> unusedTiles) {
		
		List<Combination> combinations = new CopyOnWriteArrayList<Combination>();
		Combination similar = new Combination(tile);
		Tile[] bottomChow = new Tile[2];
		Tile[] middleChow = new Tile[2];
		Tile[] topChow = new Tile[2];
		
		Log.d(TAG, "** searchCombination for tile: " + tile.getImg());
		Iterator<Tile> it = unusedTiles.iterator();
		while(it.hasNext()){
			Tile utile = it.next();
			Log.d(TAG, "** utile: " + utile.getImg() + ")");
			

			if(utile.getId()== tile.getId()){
				Log.d(TAG, "** same tile");
				continue;
			}
			
			if(utile.getCategory() != tile.getCategory()){
				Log.d(TAG, "** different category");
				continue;
			}

			if(utile.getIsVisible() != tile.getIsVisible()){
				Log.d(TAG, "** different visibility");
				continue;
			}
			switch(utile.getNo() - tile.getNo()){
				case -2:
					if(bottomChow[0] == null){
						bottomChow[0] = utile;
						Log.d(TAG, "** bottomChow found (" + utile.getNo() + ")");
					}
					break;
				case -1:
					Log.d(TAG, "** bottomChow found (" + utile.getNo() + ")");
					bottomChow[1] = utile;
					Log.d(TAG, "** middleChow found (" + utile.getNo() + ")");
					middleChow[0] = utile;
//					if(bottomChow[1] == null){
//						bottomChow[1] = utile;
//						Log.d(TAG, "** bottomChow found (" + utile.getNo() + ")");
//					}else if(middleChow[0] == null){
//						middleChow[0] = utile;
//						Log.d(TAG, "** middleChow found (" + utile.getNo() + ")");
//					}
					break;
				case 0:
					similar.addTile(utile);
					Log.d(TAG, "** Similar found (" + utile.getNo() + "," + utile.getCategory() + ")");
					break;
				case 1:
					Log.d(TAG, "** topChow found (" + utile.getNo() + ")");
					topChow[0] = utile;
					Log.d(TAG, "** middleChow found (" + utile.getNo() + ")");
					middleChow[1] = utile;
//					if(topChow[0] == null){
//						topChow[0] = utile;
//						Log.d(TAG, "** topChow found (" + utile.getNo() + ")");
//					}else if(middleChow[1] == null){
//						middleChow[1] = utile;
//						Log.d(TAG, "** middleChow found (" + utile.getNo() + ")");
//					}
					break;
				case 2:
					if(topChow[1] == null){
						topChow[1] = utile;
						Log.d(TAG, "** topChow found (" + utile.getNo() + ")");
					}
					break;
					default:
						break;
					
			}
		}
		if((bottomChow[0]!=null) && (bottomChow[1]!=null)){
			Combination c = new Combination(tile);
			c.addTiles(bottomChow);
			c.setType(Combination.Type.CHOW);
			c.setRepresentation();
			combinations.add(c);
			Log.d(TAG, "** topChow confirmed (" + bottomChow[0].getNo() + bottomChow[1].getNo() + tile.getNo() + ")");
		}
		if((middleChow[0]!=null) && (middleChow[1]!=null)){
			Combination c = new Combination(tile);
			c.addTiles(middleChow);
			c.setType(Combination.Type.CHOW);
			c.setRepresentation();
			combinations.add(c);
			Log.d(TAG, "** middleChow confirmed (" + middleChow[0].getNo() + tile.getNo() + middleChow[1].getNo() + ")");
		}
		if((topChow[0]!=null) && (topChow[1]!=null)){
			Combination c = new Combination(tile);
			c.addTiles(topChow);
			c.setType(Combination.Type.CHOW);
			c.setRepresentation();
			combinations.add(c);
			Log.d(TAG, "** topChow confirmed (" + tile.getNo()+ topChow[0].getNo()  + topChow[1].getNo() + ")");
		}
		switch(similar.getTiles().size()){
			case 2:
				similar.setType(Combination.Type.PAIR);
				similar.setRepresentation();
				combinations.add(similar);
				Log.d(TAG, "** pair confirmed");
				break;
			case 3:
				similar.setType(Combination.Type.PONG);
				similar.setRepresentation();
				combinations.add(similar);
				Log.d(TAG, "** pong confirmed");
				break;
			case 4:
				similar.setType(Combination.Type.KONG);
				similar.setRepresentation();
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
				Log.d(TAG, "** kong confirmed");
			
		}
		Log.d(TAG, "** searchCombination finished: (" + combinations + ")");
		return combinations;
	}

}

