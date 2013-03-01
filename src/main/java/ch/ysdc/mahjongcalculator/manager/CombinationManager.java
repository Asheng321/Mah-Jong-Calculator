package ch.ysdc.mahjongcalculator.manager;

import java.util.Comparator;
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
	 * 
	 * @param tiles
	 *            the tiles used to find the possibilities
	 ****************************************************************************/
	public CombinationManager() {
		possibilities = new CopyOnWriteArrayList<Possibility>();
	}

	/****************************************************************************
	 * Calculate the different possible combination based on a set of tiles
	 * given in parameter
	 * 
	 * @param tiles
	 *            the tiles to use to calculate the possibilities
	 * @return the different possibilities with different combination
	 ****************************************************************************/
	public List<Possibility> getPossibilities(LinkedList<Tile> tiles) {
		hasValidMahjong = false;

		Possibility.resetCounter();
		possibilities.add(new Possibility(tiles, null));

		Log.d(TAG, "Start Calculator");

		for (int i = 0; i < possibilities.size(); i++) {
			Possibility possibility = possibilities.get(i);
			Log.d(TAG, "possibilities (" + i + "/" + possibilities.size() + ")");
			Log.d(TAG,
					"New possibility: " + possibility.getCombinations().size()
							+ "." + possibility.getPair() + " (unused "
							+ possibility.getUnusedTiles().size() + ")");
			searchPossibility(possibility);
		}

		Log.d(TAG, "Possibilities calculated: " + possibilities.size());

		for (Possibility p : possibilities) {
			Log.d(TAG, "Possibility " + p.getId() + " (" + p.getValidity()
					+ "," + p.getUnusedTiles()
					+ (p.getPair() != null ? "paired" : "no pair"));
			for (Combination c : p.getCombinations()) {
				Log.d(TAG, "* " + c);
			}
		}
		return filterValidPossibilities();

	}

	/****************************************************************************
	 * Remove the invalid possibilities
	 * 
	 * @return the filtered possibilities
	 ***************************************************************************/
	private List<Possibility> filterValidPossibilities() {
		Log.d(TAG, "Filter possibilities. mahjong? " + this.hasValidMahjong);

		Iterator<Possibility> it = possibilities.iterator();
		while (it.hasNext()) {
			Possibility p = it.next();

			if ((p.getValidity() != Validity.MAHJONG)
					&& (p.getCombinations().size() > 4)) {
				p.setValidity(Validity.INVALID);
			}

			if ((hasValidMahjong && (p.getValidity() != Validity.MAHJONG))
					|| (p.getValidity() == Validity.ERROR)
					|| (p.alreadyListed(possibilities))) {
				possibilities.remove(p);
			}
		}
		Log.d(TAG, "Filtered possibilities calculated: " + possibilities.size());
		return possibilities;
	}

	/****************************************************************************
	 * Search possible combination for the given possibility
	 * 
	 * @param possibility
	 *            the possibility to analyze
	 ***************************************************************************/
	private void searchPossibility(Possibility possibility) {

		while (possibility.getUnusedTiles().size() > 0) {
			Log.d(TAG,
					"* Current possibility: "
							+ possibility.getCombinations().size() + ","
							+ possibility.getPair() + "\n* Tiles "
							+ possibility.displayTiles());

			Tile tile = possibility.getUnusedTiles().remove(0);

			Log.d(TAG,
					"* tile (" + tile.getNo() + tile.getCategory()
							+ tile.getId() + ")");

			List<Combination> combinations = searchCombination(tile,
					possibility.getUnusedTiles());

			switch (combinations.size()) {
			case 0:
				// TODO
				if (tile.getIsVisible()) {
					// set validity of the possibility to error
					Log.d(TAG, "* Possibility invalid!");
					possibility.setValidity(Validity.ERROR);
					// stop the loop
					return;
				}
				// set validity of the possibility to invalid
				possibility.setValidity(Validity.INCOMPLETE);
				// Add the tile to the special combination (unusableTile)
				possibility.getUnusedTileCombination().addTile(tile);
				// remove it from the list of unusedTiles
				possibility.getUnusedTiles().remove(tile);
				break;
			case 1:
				Log.d(TAG, "* 1 combination");

				if (combinations.get(0).getType() == Type.PAIR) {
					if (possibility.getPair() == null) {
						possibility.setPair(combinations.get(0));
					} else {
						possibility.setValidity(Validity.INVALID);
						possibility.addCombinations(combinations.get(0));
					}
				} else {
					possibility.addCombinations(combinations.get(0));
				}
				clearTilesFromUnused(possibility, combinations.get(0));
				break;
			default:
				Log.d(TAG, "* many combinations");
				Combination firstCombination = combinations.remove(0);
				for (Combination c : combinations) {
					addPossibility(possibility, c);
				}
				if (firstCombination.getType() == Type.PAIR) {
					if (possibility.getPair() == null) {
						possibility.setPair(firstCombination);
					} else {
						possibility.setValidity(Validity.INVALID);
						possibility.addCombinations(firstCombination);
					}
				} else {
					possibility.addCombinations(firstCombination);
				}
				clearTilesFromUnused(possibility, firstCombination);
				break;
			}
			Log.d(TAG,
					"* End Tile Loop: ("
							+ possibility.getCombinations().size()
							+ ","
							+ (possibility.getPair() != null ? "paired"
									: "unpaired") + ","
							+ possibility.getUnusedTiles().size() + ")");
		}

		possibility.getUnusedTileCombination().setType(Combination.Type.NONE);
		if (isWinningPossibility(possibility)) {
			this.hasValidMahjong = true;
		}
	}

	private boolean isWinningPossibility(Possibility possibility) {
		if (((possibility.getValidity() == Validity.VALID)
				&& (possibility.getPair() != null)
				&& (possibility.getCombinations().size() == 4) && (possibility
				.getUnusedTileCombination().getTiles().size() == 0))
				|| isSpecialHand()) {
			possibility.setValidity(Validity.MAHJONG);
			return true;
		}
		return false;
	}

	private boolean isSpecialHand() {
		return false;
	}

	/****************************************************************************
	 * Add a new possibility to the list by copying another possibility and by
	 * adding a possibility to it.
	 * 
	 * @param oldPossibility
	 *            the possibility used as a model
	 * @param c
	 *            the combination to add to the new combination
	 ***************************************************************************/
	private void addPossibility(Possibility oldPossibility, Combination c) {

		Log.d(TAG, "*** addPossibility");
		Possibility possibility = new Possibility(oldPossibility);

		if (c.getType() == Type.PAIR) {
			if (possibility.getPair() == null) {
				possibility.setPair(c);
			} else {
				possibility.setValidity(Validity.INVALID);
				possibility.addCombinations(c);
			}
		} else {
			possibility.addCombinations(c);
		}
		clearTilesFromUnused(possibility, c);
		possibilities.add(possibility);

	}

	/****************************************************************************
	 * Remove the tiles used in the combination from the unused tiles and add
	 * the new combination to the possibility
	 * 
	 * @param possibility
	 *            the possibility who own the unused tiles
	 * @param combination
	 *            the new combination to add to the possibility
	 ***************************************************************************/
	private boolean clearTilesFromUnused(Possibility possibility,
			Combination combination) {
		Log.d(TAG, "## clearTilesFromUnused");

		// We don't take the first because it's the parent, who was already
		// removed
		for (Tile t : combination.getTiles()) {
			if (!possibility.getUnusedTiles().remove(t)) {
				Log.d(TAG, "not found");
			} else {
				Log.d(TAG, "found");
			}
		}
		Log.d(TAG, "## Combination added");
		return true;
	}

	/****************************************************************************
	 * Search in the unusedTiles for tiles that can make a combination with the
	 * tile given in parameter
	 * 
	 * @param tile
	 *            the main tile
	 * @param unusedTiles
	 *            the list of available tiles to make a combination with.
	 * @return the list of all possible combination
	 ***************************************************************************/
	private List<Combination> searchCombination(Tile tile,
			List<Tile> unusedTiles) {

		List<Combination> combinations = new CopyOnWriteArrayList<Combination>();
		Combination similar = new Combination(tile);
		Tile[] bottomChow = new Tile[2];
		Tile[] middleChow = new Tile[2];
		Tile[] topChow = new Tile[2];

		Iterator<Tile> it = unusedTiles.iterator();
		while (it.hasNext()) {
			Tile utile = it.next();

			if (utile.getId() == tile.getId()) {
				continue;
			}

			if (utile.getCategory() != tile.getCategory()) {
				continue;
			}

			if (utile.getIsVisible() != tile.getIsVisible()) {
				continue;
			}
			switch (utile.getNo() - tile.getNo()) {
			case -2:
				if (bottomChow[0] == null) {
					bottomChow[0] = utile;
				}
				break;
			case -1:
				bottomChow[1] = utile;
				middleChow[0] = utile;
				break;
			case 0:
				similar.addTile(utile);
				break;
			case 1:
				topChow[0] = utile;
				middleChow[1] = utile;
				break;
			case 2:
				if (topChow[1] == null) {
					topChow[1] = utile;
				}
				break;
			default:
				break;

			}
		}
		if ((bottomChow[0] != null) && (bottomChow[1] != null)) {
			if (!tile.getCategory().isHonor()) {
				Combination c = new Combination(tile);
				c.addTiles(bottomChow);
				c.setType(Combination.Type.CHOW);
				combinations.add(c);
			}
		}
		if ((middleChow[0] != null) && (middleChow[1] != null)) {
			if (!tile.getCategory().isHonor()) {
				Combination c = new Combination(tile);
				c.addTiles(middleChow);
				c.setType(Combination.Type.CHOW);
				combinations.add(c);
			}
		}
		if ((topChow[0] != null) && (topChow[1] != null)) {
			if (!tile.getCategory().isHonor()) {
				Combination c = new Combination(tile);
				c.addTiles(topChow);
				c.setType(Combination.Type.CHOW);
				combinations.add(c);
			}
		}
		switch (similar.getTiles().size()) {
		case 2:
			similar.setType(Combination.Type.PAIR);
			combinations.add(similar);
			break;
		case 3:
			similar.setType(Combination.Type.PONG);
			combinations.add(similar);
			break;
		case 4:
			similar.setType(Combination.Type.KONG);
			combinations.add(similar);

			Combination cPong = new Combination(tile);
			for (Tile t : similar.getTiles()) {
				if (t.equals(tile)) {
					continue;
				}
				if (cPong.getTiles().size() < 3) {
					cPong.addTile(t);
				}
			}
			cPong.setType(Type.PONG);
			combinations.add(cPong);
			Log.d(TAG, "** kong confirmed");

		}
		Log.d(TAG, "** searchCombination finished: (" + combinations + ")");
		return combinations;
	}
}
