/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ysdc.mahjongcalculator.db;

import android.util.Log;
import ch.ysdc.mahjongcalculator.model.Tile;

/**
 * 
 * @author djohannot
 */
public class DatabaseInitializator {

	private static String TAG = "DatabaseInitializator";

	private static final String[] DRAGONS = { "dr", "dg", "dw" };
	private static final String[] WINDS = { "ve", "vs", "vw", "vn" };
	private static final String[] FLOWERS = { "ef", "sf", "wf", "nf" };
	private static final String[] SEASONS = { "es", "ss", "ws", "ns" };

	public static void initialize() {

		Log.d(TAG, "Enter initialize");

		// 36xtile of bamboo
		for (int i = 1; i <= 9; i++) {
			for (int j = 0; j < 4; j++) {
				Tile tile = new Tile(i, "tile_" + i + "b", Tile.Category.BAMBOO);
				DatabaseManager.getInstance().addTile(tile);

			}
		}

		// 36xtile of circle
		for (int i = 1; i <= 9; i++) {
			for (int j = 0; j < 4; j++) {
				Tile tile = new Tile(i, "tile_" + i + "c", Tile.Category.CIRCLE);
				DatabaseManager.getInstance().addTile(tile);
			}
		}

		// 36xtile of character
		for (int i = 1; i <= 9; i++) {
			for (int j = 0; j < 4; j++) {
				Tile tile = new Tile(i, "tile_" + i + "k",
						Tile.Category.CHARACTER);
				DatabaseManager.getInstance().addTile(tile);
			}
		}

		// 16xtile of wind
		for (String suffix : WINDS) {
			for (int j = 0; j < 4; j++) {
				Tile tile = new Tile(0, "tile_" + suffix, Tile.Category.WIND);
				DatabaseManager.getInstance().addTile(tile);
			}
		}

		// 12xtile of dragon
		for (String suffix : DRAGONS) {
			for (int j = 0; j < 4; j++) {
				Tile tile = new Tile(0, "tile_" + suffix, Tile.Category.DRAGON);
				DatabaseManager.getInstance().addTile(tile);
			}
		}

		// 4xtile of flower
		for (String suffix : FLOWERS) {
			Tile tile = new Tile(0, "tile_" + suffix, Tile.Category.FLOWER);
			DatabaseManager.getInstance().addTile(tile);
		}

		// 4xtile of season
		for (String suffix : SEASONS) {
			Tile tile = new Tile(0, "tile_" + suffix, Tile.Category.SEASON);
			DatabaseManager.getInstance().addTile(tile);
		}
		Log.d(TAG, "Leave database initialization");
	}

}
