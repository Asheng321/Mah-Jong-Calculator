package ch.ysdc.mahjongcalculator.factory;

import android.content.Context;
import ch.ysdc.mahjongcalculator.manager.ChineseResultManager;
import ch.ysdc.mahjongcalculator.manager.ResultManager;
import ch.ysdc.mahjongcalculator.model.Hand;

public class ResultManagerFactory {

	public static ResultManager getSelectedResultManager(Context c, Hand hand, int roundWind,
	int gameWind){
		//TODO: implements based on the user choice in the settings
		return new ChineseResultManager(c, hand, roundWind, gameWind);
	}
}
