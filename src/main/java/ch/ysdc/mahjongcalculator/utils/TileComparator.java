package ch.ysdc.mahjongcalculator.utils;

import java.util.Comparator;
import java.util.HashMap;

public class TileComparator implements Comparator<String> {

    HashMap<String, Integer> tiles;
    public TileComparator(HashMap<String, Integer> base) {
        this.tiles = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
//    	String aCat = a.replace("tile_", "").substring(start);
//    	String shortB = b.replace("tile_", "");
//    	
//    	
//        if (base.get(a) >= tiles.get(b)) {
//            return -1;
//        } else {
//            return 1;
//        } // returning 0 would merge keys
    	return 0;
    }
}


