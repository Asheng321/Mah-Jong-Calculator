package ch.ysdc.mahjongcalculator.manager;

import android.content.Context;
import ch.ysdc.mahjongcalculator.model.Hand;

public abstract class ResultManager {

	protected Hand hand;
	protected int roundWind;
	protected int gameWind;
	protected Context context;
	
	public ResultManager(Context c, Hand h, int rw, int gw){
		super();
		this.context = c;
		this.hand = h;
		this.roundWind = rw;
		this.gameWind = gw;
		
	}
	public abstract Hand CalculateResultForHand();
	
}
