/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ysdc.mahjongcalculator.manager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;
import ch.ysdc.mahjongcalculator.model.Tile;

/**
 * 
 * @author djohannot
 */
public class GameManager {

	private static String TAG = "GameManager";

	private static final String TILE_IMG_REGEX = "(tile_)(\\w{1})(\\w{1})";

	/****************************************************************************
	 * Generate tiles based on the image name given in parameter.
	 * 
	 * @param playerTiles
	 *            the hidden tiles list selected by the user
	 * @param isVisible
	 *            true if the tiles are visible
	 * @return Linked list of tiles
	 ****************************************************************************/
	public static LinkedList<Tile> createTiles(
			HashMap<String, Integer> playerTiles, boolean isVisible) {
		Log.d(TAG, "createTiles");

		LinkedList<Tile> tiles = new LinkedList<Tile>();
		Pattern pattern = Pattern.compile(TILE_IMG_REGEX);

		// Loop through the hidden tiles
		for (Map.Entry<String, Integer> entry : playerTiles.entrySet()) {

			for (int i = 0; i < entry.getValue(); i++) {
				Matcher matcher = pattern.matcher(entry.getKey());

				if (matcher.matches()) {
					// Exception if we can't parse correctly the tile image name
					if (matcher.groupCount() != 3) {
						throw new RuntimeException("Wrong image name "
								+ entry.getKey());
					}

					// Create the tile and set the properties
					Tile tile = new Tile(Integer.valueOf(matcher.group(2)),
							entry.getKey(), Tile.Category.fromValue(matcher
									.group(3)));
					tile.setIsVisible(isVisible);
					tiles.add(tile);
				}
			}
		}

		return tiles;
	}

	public static Tile createTile(String img) {
		Pattern pattern = Pattern.compile(TILE_IMG_REGEX);
		Tile tile = null;
		Matcher matcher = pattern.matcher(img);

		if (matcher.matches()) {
			// Exception if we can't parse correctly the tile image name
			if (matcher.groupCount() != 3) {
				throw new RuntimeException("Wrong image name " + img);
			}

			// Create the tile and set the properties
			tile = new Tile(Integer.valueOf(matcher.group(2)), img,
					Tile.Category.fromValue(matcher.group(3)));
		}
		
		return tile;
	}
}
