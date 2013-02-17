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

	// private static final String[] DRAGONS = { "dr", "dg", "dw" };
	// private static final String[] WINDS = { "ve", "vs", "vw", "vn" };
	// private static final String[] FLOWERS = { "ef", "sf", "wf", "nf" };
	// private static final String[] SEASONS = { "es", "ss", "ws", "ns" };

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
			Log.d(TAG, "tile " + entry.getKey());
			for (int i = 0; i < entry.getValue(); i++) {
				Matcher matcher = pattern.matcher(entry.getKey());

				Log.d(TAG, "i: " + "(" + entry.getValue() + ")");

				if (matcher.matches()) {
					// Exception if we can't parse correctly the tile image name
					if (matcher.groupCount() != 3) {
						Log.e(TAG, "Wrong image name " + entry.getKey());
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

		Log.d(TAG, "createTiles end: " + tiles.size());
		return tiles;
	}
	// public static Game initializeGame(Game game) {
	//
	// Log.d(TAG, "Enter initialize");
	//
	// // 36xtile of bamboo
	// for (int i = 1; i <= 9; i++) {
	// for (int j = 0; j < 4; j++) {
	// Tile tile = new Tile(i, "tile_" + i + "b", Tile.Category.BAMBOO);
	// tile.setGame(game);
	// DatabaseManager.getInstance().addTile(tile);
	// DatabaseManager.getInstance().updateGame(game);
	// }
	// }
	//
	// // 36xtile of circle
	// for (int i = 1; i <= 9; i++) {
	// for (int j = 0; j < 4; j++) {
	// Tile tile = new Tile(i, "tile_" + i + "c", Tile.Category.CIRCLE);
	// tile.setGame(game);
	// DatabaseManager.getInstance().addTile(tile);
	// DatabaseManager.getInstance().updateGame(game);
	// }
	// }
	//
	// // 36xtile of character
	// for (int i = 1; i <= 9; i++) {
	// for (int j = 0; j < 4; j++) {
	// Tile tile = new Tile(i, "tile_" + i + "k",
	// Tile.Category.CHARACTER);
	// tile.setGame(game);
	// DatabaseManager.getInstance().addTile(tile);
	// DatabaseManager.getInstance().updateGame(game);
	// }
	// }
	//
	// // 16xtile of wind
	// for (String suffix : WINDS) {
	// for (int j = 0; j < 4; j++) {
	// Tile tile = new Tile(0, "tile_" + suffix, Tile.Category.WIND);
	// tile.setGame(game);
	// DatabaseManager.getInstance().addTile(tile);
	// DatabaseManager.getInstance().updateGame(game);
	// }
	// }
	//
	// // 12xtile of dragon
	// for (String suffix : DRAGONS) {
	// for (int j = 0; j < 4; j++) {
	// Tile tile = new Tile(0, "tile_" + suffix, Tile.Category.DRAGON);
	// tile.setGame(game);
	// DatabaseManager.getInstance().addTile(tile);
	// DatabaseManager.getInstance().updateGame(game);
	// }
	// }
	//
	// // 4xtile of flower
	// for (String suffix : FLOWERS) {
	// Tile tile = new Tile(0, "tile_" + suffix, Tile.Category.FLOWER);
	// tile.setGame(game);
	// DatabaseManager.getInstance().addTile(tile);
	// DatabaseManager.getInstance().updateGame(game);
	// }
	//
	// // 4xtile of season
	// for (String suffix : SEASONS) {
	// Tile tile = new Tile(0, "tile_" + suffix, Tile.Category.SEASON);
	// tile.setGame(game);
	// DatabaseManager.getInstance().addTile(tile);
	// DatabaseManager.getInstance().updateGame(game);
	// }
	// //Create the hand of the game
	// game.setHand(new Hand());
	// DatabaseManager.getInstance().createHand(game.getHand());
	// DatabaseManager.getInstance().updateGame(game);
	//
	// Log.d(TAG, "Leave database initialization");
	// return game;
	// }

}
