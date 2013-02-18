package ch.ysdc.mahjongcalculator.utils;

public class AndroidUtils {
	public static final int DEFAULT_TILE_PADDING = 2;
	public static final int COMBINATION_TILE_PADDING = 5;
	
	public static int fromDpToPixels(int dp, float density) {
		return (int) (dp * density + 0.5f);
	}
}
