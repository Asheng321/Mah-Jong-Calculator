package ch.ysdc.mahjongcalculator.utils;

public class AndroidUtils {
	public static int fromDpToPixels(int dp, float density) {
		return (int) (dp * density + 0.5f);
	}
}
