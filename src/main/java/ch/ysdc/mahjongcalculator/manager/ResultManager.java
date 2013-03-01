package ch.ysdc.mahjongcalculator.manager;

import android.content.Context;
import android.util.Log;
import ch.ysdc.mahjongcalculator.model.Hand;
import ch.ysdc.mahjongcalculator.model.Tile;
import ch.ysdc.mahjongcalculator.model.Tile.Category;

public abstract class ResultManager {

	private static String TAG = "ResultManager";
	
	protected Hand hand;
	protected int roundWind;
	protected int gameWind;
	protected Context context;
	
	protected Category currentCategory;
	protected boolean isPure;
	protected boolean onlyChow;
	protected boolean onlyPong;
	protected boolean isHidden;
	protected boolean isTerminal;
	protected int pairQuantity;

	/****************************************************************************
	 * Constructor
	 ****************************************************************************/
	public ResultManager(Context c, Hand h, int rw, int gw){
		super();
		this.context = c;
		this.hand = h;
		this.roundWind = rw;
		this.gameWind = gw;

		pairQuantity = 0;
		isPure = true;
		onlyChow = true;
		onlyPong = true;
		isHidden = true;
		isTerminal = true;
		
		currentCategory = null;
	}
	public abstract Hand CalculateResult();


	/****************************************************************************
	 * checkIfTerminal
	 ****************************************************************************/
	protected boolean checkIfTerminal(Tile t){
		if((t.getNo() != 1) && (t.getNo() != 9)){
			isTerminal = false;
			return false;
		}
		return true;
	}

	/****************************************************************************
	 * checkIfHidden
	 ****************************************************************************/
	protected void checkIfHidden(Tile t){
		if(t.getIsVisible()){
			isHidden = false;
		}
	}

	/****************************************************************************
	 * checkIfPure
	 ****************************************************************************/
	protected void checkIfPure(Tile t) {
		if (isPure) {
			Log.d(TAG, "isPure(" + isPure + ")");
			Log.d(TAG, "currentCategory: " + currentCategory + "       challenger: " + t.getCategory());
			if ((currentCategory == null) && (!t.getCategory().isHonor())) {
				currentCategory = t.getCategory();
				Log.d(TAG, "new category: " + currentCategory);
			} else if ((currentCategory != t.getCategory())
					&& (!t.getCategory().isHonor())) {
				isPure = false;
			}
		}
	}
}
