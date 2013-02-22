package ch.ysdc.mahjongcalculator.manager;

import java.util.LinkedList;

import android.content.Context;

import ch.ysdc.mahjongcalculator.R;
import ch.ysdc.mahjongcalculator.model.Combination;
import ch.ysdc.mahjongcalculator.model.Hand;
import ch.ysdc.mahjongcalculator.model.Point;
import ch.ysdc.mahjongcalculator.model.Tile;

public class ChineseResultManager extends ResultManager {

	// ALL POINTS
	public static final int VISIBLE_PLAYER_PAIR_WIND = 2;
	public static final int HIDDEN_PLAYER_PAIR_WIND = 2;

	public static final int VISIBLE_GAME_PAIR_WIND = 2;
	public static final int HIDDEN_GAME_PAIR_WIND = 2;

	public static final int ROUND_PAIR_WIND = 2;
	public static final int DRAGON_PAIR = 2;

	public static final int SIMPLE_VISIBLE_PONG = 2;
	public static final int SIMPLE_HIDDEN_PONG = 4;
	public static final int TERMINAL_VISIBLE_PONG = 4;
	public static final int TERMINAL_HIDDEN_PONG = 8;
	public static final int WIND_VISIBLE_PONG = 4;
	public static final int WIND_HIDDEN_PONG = 8;
	public static final int DRAGON_VISIBLE_PONG = 8;
	public static final int DRAGON_HIDDEN_PONG = 16;

	public static final int SIMPLE_VISIBLE_KONG = 8;
	public static final int SIMPLE_HIDDEN_KONG = 16;
	public static final int TERMINAL_VISIBLE_KONG = 16;
	public static final int TERMINAL_HIDDEN_KONG = 32;
	public static final int WIND_VISIBLE_KONG = 16;
	public static final int WIND_HIDDEN_KONG = 32;
	public static final int DRAGON_VISIBLE_KONG = 32;
	public static final int DRAGON_HIDDEN_KONG = 64;

	public static final int FLOWER_OR_SEASON_TILE = 4;

	// ALL MULTI
	public static final int PLAYER_COMBO_WIND = 2;
	public static final int GAME_COMBO_WIND = 2;
	public static final int ROUND_COMBO_WIND = 2;
	public static final int DRAGONS_COMBO_COMBO = 2;
	public static final int PLAYER_FLOWER = 2;
	public static final int PLAYER_SEASON = 2;
	public static final int ALL_FLOWERS = 2;
	public static final int ALL_SEASON = 2;

	// WINNER POINTS
	public static final int DID_MAHJONG = 20;
	public static final int ONLY_CHOW = 10;
	public static final int FROM_WALL = 2;
	public static final int SERVED = 100;
	public static final int FIVE_CIRCLE = 100;

	// WINNER MULTI
	public static final int NO_CHOW = 2;
	public static final int CALLING_HAND = 2;
	public static final int PUR_HAND = 8;
	public static final int STEAL_KONG = 2;
	// PRENDRE UNE PIECE DE LA PARTIE DETACHEE
	public static final int FROM_HILL = 2;
	public static final int FULL_HIDDEN = 2;
	public static final int ONE_NINE_HONNOR = 2;
	public static final int LAST_TILE = 2;


	public ChineseResultManager(Context c, Hand hand, int roundWind, int gameWind){
		super(c,hand,roundWind,gameWind);
	}
	public Hand CalculateResultForHand() {
		
		this.hand.setPoints(new LinkedList<Point>());
		this.hand.setBonuses(new LinkedList<Point>());

		for (Combination combination : hand.getCombinations()) {
			switch (combination.getType()) {
			case PAIR:
				calculatePairPoints(combination);
				break;
			case CHOW:
				calculateChowPoints(combination);
				break;
			case PONG:
				calculatePongPoints(combination);
				break;
			case KONG:
				calculateKongPoints(combination);
				break;
			case NONE:
				break;
			default:
				throw new RuntimeException("Found a non-typed combination");
			}
		}
		return hand;
	}

	private void calculateKongPoints(Combination combination) {
		// TODO Auto-generated method stub

	}

	private void calculatePongPoints(Combination combination) {
		// TODO Auto-generated method stub

	}

	private void calculateChowPoints(Combination combination) {
		//chow gives no points
	}

	private void calculatePairPoints(Combination combination) {
		Tile t = combination.getTiles().get(0);
		switch (t.getCategory()) {
		case WIND:
			if(t.getNo() == hand.getPlayerWind()){
				hand.getPoints().add(new Point(context.getString(R.string.point_pair_wind_player),2));
			}else if(t.getNo() == roundWind){
				hand.getPoints().add(new Point(context.getString(R.string.point_pair_wind_round),2));
			}else if((t.getNo() == gameWind) && (!!t.getIsVisible())){
				hand.getPoints().add(new Point(context.getString(R.string.point_pair_wind_game),2));
			}
			break;
		case DRAGON:
			if(!!t.getIsVisible()){
				hand.getPoints().add(new Point(context.getString(R.string.point_pair_dragon),2));
			}
			break;
		default:
			break;
		}

	}

}
