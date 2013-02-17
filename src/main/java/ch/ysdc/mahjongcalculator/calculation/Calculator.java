package ch.ysdc.mahjongcalculator.calculation;

import java.util.Iterator;
import java.util.LinkedList;

import android.util.Log;
import ch.ysdc.mahjongcalculator.model.Tile;

public class Calculator {
	
	private static String TAG = "Calculator";
	private LinkedList<Possibility> possibilities;
	
	/****************************************************************************
	 * Constructor takes a tiles linked list as parameter
	 * @param tiles the tiles used to find the possibilities
	 ****************************************************************************/
	public Calculator(){
		possibilities = new LinkedList<Possibility>();
	}

	/****************************************************************************
	 * Calculate the different possible combination based on a set of tiles given
	 * in parameter
	 * @param tiles the tiles to use to calculate the possibilities
	 * @return the different possibilities with different combination
	 ****************************************************************************/
	public LinkedList<Possibility> getPossibilities(LinkedList<Tile> tiles){
		Possibility.resetCounter();
		possibilities.add(new Possibility(tiles, null));
		
		Log.d(TAG, "Start Calculator");
		Iterator<Possibility> itP = possibilities.iterator();
		
		while(itP.hasNext()){
			Possibility possibility = itP.next();
			Log.d(TAG, "Possibility: " + possibility + " (unused " + possibility.getUnusedTiles().size() + ")");
			searchPossibility(possibility);
		}
		
		Log.d(TAG, "Possibilities calculated: " + possibilities.size());
		
		return filterValidPossibilities();
		
	}

	/****************************************************************************
	 * Remove the invalid possibilities
	 * @return the filtered possibilities
	 ***************************************************************************/
	private LinkedList<Possibility> filterValidPossibilities() {		
		Log.d(TAG, "Filter possibilities");

		Iterator<Possibility> it = possibilities.iterator();
		while(it.hasNext()){
			Possibility p = it.next();
			if(!p.isValid()){
				it.remove();
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
			Tile tile = possibility.getUnusedTiles().removeFirst();
			
			Log.d(TAG, "* tile: " + tile + " (stile " + possibility.getUnusedTiles().size() + ")");
			
			LinkedList<Combination> combinations = searchCombination(tile,possibility.getUnusedTiles());
			
			switch(combinations.size()){
			case 0:
				if(possibility.getUnusedTiles().size()==1){
					Tile lastTile = possibility.getUnusedTiles().getFirst();
					if((lastTile.getCategory() == tile.getCategory()) && (lastTile.getNo() == tile.getNo())){
						possibility.setPair(lastTile, tile);
						Log.d(TAG, "* Possibility valid!");
						return;
					}
				}
				possibility.setValid(false);
				Log.d(TAG, "* Possibility invalid!");
				return;
			case 1:
				Log.d(TAG, "* 1 combination");

				if(combinations.getFirst().getTiles().size()==2){
					if(possibility.getPair()!= null){
						possibility.setValid(false);
						Log.d(TAG, "* Possibility invalid, second pair!");
						return;
					}
					possibility.setPair(combinations.getFirst().getTiles().getFirst(), combinations.getFirst().getTiles().getLast());
				}else{
					possibility.addCombinations(combinations.getFirst());
				}
				clearTilesFromUnused(possibility,combinations.getFirst());
				break;
			default:
				Log.d(TAG, "* many combination");
				Combination firstCombination = combinations.removeFirst();
				for(Combination c : combinations){
					addPossibility(possibility,c);
				}
				if(firstCombination.getTiles().size()==2){
					if(possibility.getPair()!= null){
						possibility.setValid(false);
						Log.d(TAG, "* Possibility invalid, second pair!");
						return;
					}
					possibility.setPair(firstCombination.getTiles().getFirst(), firstCombination.getTiles().getLast());
				}else{
					possibility.addCombinations(firstCombination);
				}
				clearTilesFromUnused(possibility,firstCombination);
				break;
			}
			Log.d(TAG, "* End Tile Loop");
		}
		
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

		if(c.getTiles().size()==2){
			if(possibility.getPair()!= null){
				possibility.setValid(false);
				Log.d(TAG, "* Possibility invalid, second pair!");
				return;
			}
			possibility.setPair(c.getTiles().getFirst(), c.getTiles().getLast());
		}else{
			possibility.addCombinations(c);
		}
		clearTilesFromUnused(possibility,c);
		possibilities.addLast(possibility);
		
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
		for(int i=1; i<combination.getTiles().size(); i++){
			Tile t = combination.getTiles().get(i);
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
	private LinkedList<Combination> searchCombination(Tile tile, LinkedList<Tile> unusedTiles) {
		
		LinkedList<Combination> combinations = new LinkedList<Combination>();
		Combination similar = new Combination(tile);
		Tile[] bottomChow = new Tile[2];
		Tile[] middleChow = new Tile[2];
		Tile[] topChow = new Tile[2];
		
		Log.d(TAG, "** searchCombination for tile: " + tile.getImg());
		Iterator<Tile> it = unusedTiles.iterator();
		while(it.hasNext()){
			Tile utile = it.next();
			Log.d(TAG, "** utile: " + utile.getImg() + ")");
			
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
					if(bottomChow[1] == null){
						bottomChow[1] = utile;
						Log.d(TAG, "** bottomChow found (" + utile.getNo() + ")");
					}else if(middleChow[0] == null){
						middleChow[0] = utile;
						Log.d(TAG, "** middleChow found (" + utile.getNo() + ")");
					}
					break;
				case 0:
					similar.addTile(utile);
					Log.d(TAG, "** Similar found (" + utile.getNo() + "," + utile.getCategory() + ")");
					break;
				case 1:
					if(topChow[0] == null){
						topChow[0] = utile;
						Log.d(TAG, "** topChow found (" + utile.getNo() + ")");
					}else if(middleChow[1] == null){
						middleChow[1] = utile;
						Log.d(TAG, "** middleChow found (" + utile.getNo() + ")");
					}
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
			combinations.add(c);
			Log.d(TAG, "** topChow confirmed (" + bottomChow[0].getNo() + bottomChow[1].getNo() + tile.getNo() + ")");
		}
		if((middleChow[0]!=null) && (middleChow[1]!=null)){
			Combination c = new Combination(tile);
			c.addTiles(middleChow);
			combinations.add(c);
			Log.d(TAG, "** middleChow confirmed (" + middleChow[0].getNo() + tile.getNo() + middleChow[1].getNo() + ")");
		}
		if((topChow[0]!=null) && (topChow[1]!=null)){
			Combination c = new Combination(tile);
			c.addTiles(topChow);
			combinations.add(c);
			Log.d(TAG, "** topChow confirmed (" + tile.getNo()+ topChow[0].getNo()  + topChow[1].getNo() + ")");
		}
		switch(similar.getTiles().size()){
			case 2:
				combinations.add(similar);
				Log.d(TAG, "** pair confirmed");
				break;
			case 3:
				combinations.add(similar);
				Log.d(TAG, "** pong confirmed");
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
				Log.d(TAG, "** kong confirmed");
			
		}
		Log.d(TAG, "** searchCombination finished: (" + combinations + ")");
		return combinations;
	}

}

