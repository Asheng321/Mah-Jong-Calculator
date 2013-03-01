package ch.ysdc.mahjongcalculator.manager;

import java.util.LinkedList;

import android.content.Context;
import android.util.Log;
import ch.ysdc.mahjongcalculator.R;
import ch.ysdc.mahjongcalculator.model.Combination;
import ch.ysdc.mahjongcalculator.model.Hand;
import ch.ysdc.mahjongcalculator.model.Point;
import ch.ysdc.mahjongcalculator.model.Tile;
import ch.ysdc.mahjongcalculator.model.Validity;

public class ChineseResultManager extends ResultManager {
	private static String TAG = "ChineseResultManager";

	// ALL POINTS
	public static final int PLAYER_PAIR_WIND = 2;
	public static final int GAME_PAIR_WIND = 2;

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
	public static final int ALL_SEASONS = 2;

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

	/****************************************************************************
	 * ChineseResultManager
	 ****************************************************************************/
	public ChineseResultManager(Context c, Hand hand, int roundWind,
			int gameWind) {
		super(c, hand, roundWind, gameWind);
	}

	/****************************************************************************
	 * CalculateResultForHand
	 ****************************************************************************/
	public Hand CalculateResult() {

		this.hand.setPoints(new LinkedList<Point>());
		this.hand.setBonuses(new LinkedList<Point>());

		for (Combination combination : hand.getCombinations()) {
			Log.d(TAG, "Combo: " + combination.getType() + " ");
			switch (combination.getType()) {
			case PAIR:
				if (++pairQuantity > 1) {
					onlyChow = false;
					onlyPong = false;
				}
				calculatePairPoints(combination);
				break;
			case CHOW:
				onlyPong = false;
				calculateChowPoints(combination);
				break;
			case PONG:
				onlyChow = false;
				calculatePongPoints(combination);
				break;
			case KONG:
				onlyChow = false;
				calculateKongPoints(combination);
				break;
			case NONE:
				isPure = false;
				isHidden = false;
				onlyChow = false;
				onlyPong = false;
				isTerminal = false;
				break;
			default:
				throw new RuntimeException("Found a non-typed combination");
			}
		}

		calculateFlowers();

		calculateSeason();

		if (hand.getValidity() == Validity.MAHJONG) {

			setWinnerPoints();

			setWinnerBonuses();
		}
		
		calculateTotal();
		return hand;
	}

	/****************************************************************************
	 * calculateTotal
	 ****************************************************************************/
	private void calculateTotal() {
		int points = 0;
		int bonus = 1;
		
		for(Point p : hand.getPoints()){
			points += p.getPoints();
		}
		
		for(Point p : hand.getBonuses()){
			bonus *= p.getPoints();
		}
		
		hand.setTotalPoints(points);
		hand.setTotalBonuses(bonus);
	}

	/****************************************************************************
	 * KONG
	 ****************************************************************************/
	private void calculateKongPoints(Combination combination) {
		Tile t = combination.getTiles().get(0);
		switch (t.getCategory()) {
		case WIND:
			checkIfHidden(t);
			// Calculate combo points
			if (t.getIsVisible()) {
				hand.getPoints().add(
						new Point(context
								.getString(R.string.point_kong_visible_wind),
								WIND_VISIBLE_KONG, combination,false));
			} else {
				hand.getPoints().add(
						new Point(context
								.getString(R.string.point_kong_hidden_wind),
								WIND_HIDDEN_KONG, combination,false));
			}
			// Calculate Bonuses
			if (t.getNo() == hand.getPlayerWind()) {
				hand.getBonuses().add(
						new Point(context
								.getString(R.string.point_kong_wind_player),
								PLAYER_COMBO_WIND, combination,true));
			} else if (t.getNo() == roundWind) {
				hand.getBonuses().add(
						new Point(context
								.getString(R.string.point_kong_wind_round),
								ROUND_COMBO_WIND, combination,true));
			} else if (t.getNo() == gameWind) {
				hand.getBonuses().add(
						new Point(context
								.getString(R.string.point_kong_wind_game),
								GAME_COMBO_WIND, combination,true));
			}
			break;
		case DRAGON:
			checkIfHidden(t);
			// Calculate combo points
			if (t.getIsVisible()) {
				hand.getPoints().add(
						new Point(context
								.getString(R.string.point_kong_visible_dragon),
								DRAGON_VISIBLE_KONG, combination,false));
			} else {
				hand.getPoints().add(
						new Point(context
								.getString(R.string.point_kong_hidden_dragon),
								DRAGON_HIDDEN_KONG, combination,false));
			}
			// Calculate Bonuses
			hand.getBonuses().add(
					new Point(context.getString(R.string.point_kong_dragon),
							DRAGONS_COMBO_COMBO, combination,true));
			break;
		case CHARACTER:
		case CIRCLE:
		case BAMBOO:
			checkIfPure(t);
			checkIfHidden(t);

			// Calculate combo points and use the result to know if we
			// must count more points
			if (checkIfTerminal(t)) {
				// it's a 1 or 9
				if (t.getIsVisible()) {
					// it's visible
					hand.getPoints()
							.add(new Point(
									context.getString(R.string.point_kong_visible_terminal),
									TERMINAL_VISIBLE_KONG, combination,false));
				} else {
					// it's hidden
					hand.getPoints()
							.add(new Point(
									context.getString(R.string.point_kong_hidden_terminal),
									TERMINAL_HIDDEN_KONG, combination,false));
				}
			} else {
				// it's a 2-8
				if (t.getIsVisible()) {
					// it's visible
					hand.getPoints()
							.add(new Point(
									context.getString(R.string.point_kong_visible_common),
									SIMPLE_VISIBLE_KONG, combination,false));
				} else {
					// it's hidden
					hand.getPoints()
							.add(new Point(
									context.getString(R.string.point_kong_hidden_common),
									SIMPLE_HIDDEN_KONG, combination,false));
				}
			}
			break;
		default:
			Log.e(TAG, "seasons in pong?!?...: " + combination);
			break;
		}
	}

	/****************************************************************************
	 * PONG
	 ****************************************************************************/
	private void calculatePongPoints(Combination combination) {
		Tile t = combination.getTiles().get(0);
		switch (t.getCategory()) {
		case WIND:
			checkIfHidden(t);
			// Calculate combo points
			if (t.getIsVisible()) {
				hand.getPoints().add(
						new Point(context
								.getString(R.string.point_pong_visible_wind),
								WIND_VISIBLE_PONG, combination,false));
			} else {
				hand.getPoints().add(
						new Point(context
								.getString(R.string.point_pong_hidden_wind),
								WIND_HIDDEN_PONG, combination,false));
			}
			// Calculate Bonuses
			if (t.getNo() == hand.getPlayerWind()) {
				hand.getBonuses().add(
						new Point(context
								.getString(R.string.point_pong_wind_player),
								PLAYER_COMBO_WIND, combination,true));
			} else if (t.getNo() == roundWind) {
				hand.getBonuses().add(
						new Point(context
								.getString(R.string.point_pong_wind_round),
								ROUND_COMBO_WIND, combination,true));
			} else if (t.getNo() == gameWind) {
				hand.getBonuses().add(
						new Point(context
								.getString(R.string.point_pong_wind_game),
								GAME_COMBO_WIND, combination,true));
			}
			break;
		case DRAGON:
			checkIfHidden(t);
			// Calculate combo points
			if (t.getIsVisible()) {
				hand.getPoints().add(
						new Point(context
								.getString(R.string.point_pong_visible_dragon),
								DRAGON_VISIBLE_PONG, combination,false));
			} else {
				hand.getPoints().add(
						new Point(context
								.getString(R.string.point_pong_hidden_dragon),
								DRAGON_HIDDEN_PONG, combination,false));
			}
			// Calculate Bonuses
			hand.getBonuses().add(
					new Point(context.getString(R.string.point_pong_dragon),
							DRAGONS_COMBO_COMBO, combination,true));
			break;
		case CHARACTER:
		case CIRCLE:
		case BAMBOO:
			checkIfPure(t);
			checkIfHidden(t);

			// Calculate combo points and use the result to know if we
			// must count more points
			if (checkIfTerminal(t)) {
				// it's a 1 or 9
				if (t.getIsVisible()) {
					// it's visible
					hand.getPoints()
							.add(new Point(
									context.getString(R.string.point_pong_visible_terminal),
									TERMINAL_VISIBLE_PONG, combination,false));
				} else {
					// it's hidden
					hand.getPoints()
							.add(new Point(
									context.getString(R.string.point_pong_hidden_terminal),
									TERMINAL_HIDDEN_PONG, combination,false));
				}
			} else {
				// it's a 2-8
				if (t.getIsVisible()) {
					// it's visible
					hand.getPoints()
							.add(new Point(
									context.getString(R.string.point_pong_visible_common),
									SIMPLE_VISIBLE_PONG, combination,false));
				} else {
					// it's hidden
					hand.getPoints()
							.add(new Point(
									context.getString(R.string.point_pong_hidden_common),
									SIMPLE_HIDDEN_PONG, combination,false));
				}
			}
			break;
		default:
			Log.e(TAG, "seasons in pong?!?...: " + combination);
			break;
		}
	}

	/****************************************************************************
	 * CHOW
	 ****************************************************************************/
	private void calculateChowPoints(Combination combination) {
		Tile t = combination.getTiles().get(1);
		switch (t.getCategory()) {
		case CHARACTER:
		case CIRCLE:
		case BAMBOO:
			checkIfPure(t);
			checkIfHidden(t);
			checkIfTerminal(t);
			break;
		default:
			Log.e(TAG, "Chow of honor...: " + combination);
			break;
		}
	}

	/****************************************************************************
	 * calculate Flowers
	 ****************************************************************************/
	private void calculateFlowers() {
		if (hand.getFlowers().size() > 0) {
			Tile ownFlower = null;
			Combination c = new Combination();

			for (Tile flower : hand.getFlowers()) {
				if (flower.getNo() == hand.getPlayerWind()) {
					ownFlower = flower;
				}
				c.addTile(flower);
			}

			if (hand.getFlowers().size() == 4) {
				hand.getBonuses().add(
						new Point(
								context.getString(R.string.point_all_flowers),
								ALL_FLOWERS, c,true));
			}
			if (ownFlower != null) {
				hand.getBonuses().add(
						new Point(context
								.getString(R.string.point_player_flower),
								PLAYER_FLOWER, new Combination(ownFlower),true));
			}

			hand.getPoints().add(
					new Point(context.getResources().getQuantityString(
							R.plurals.playerFlowerQuantity,
							c.getTiles().size(), c.getTiles().size()),
							FLOWER_OR_SEASON_TILE * c.getTiles().size(), c,false));
		}
	}

	/****************************************************************************
	 * calculate Season
	 ****************************************************************************/
	private void calculateSeason() {
		if (hand.getSeasons().size() > 0) {
			Tile ownSeason = null;
			Combination c = new Combination();

			for (Tile season : hand.getSeasons()) {
				if (season.getNo() == hand.getPlayerWind()) {
					ownSeason = season;
				}
				c.addTile(season);
			}

			if (hand.getSeasons().size() == 4) {
				hand.getBonuses().add(
						new Point(
								context.getString(R.string.point_all_seasons),
								ALL_SEASONS, c,true));
			}
			if (ownSeason != null) {
				hand.getBonuses().add(
						new Point(context
								.getString(R.string.point_player_season),
								PLAYER_SEASON, new Combination(ownSeason),true));
			}

			hand.getPoints().add(
					new Point(context.getResources().getQuantityString(
							R.plurals.playerSeasonQuantity,
							c.getTiles().size(), c.getTiles().size()),
							FLOWER_OR_SEASON_TILE * c.getTiles().size(), c,false));
		}
	}

	/****************************************************************************
	 * PAIR
	 ****************************************************************************/
	private void calculatePairPoints(Combination combination) {
		Tile t = combination.getTiles().get(0);
		switch (t.getCategory()) {
		case WIND:
			checkIfHidden(t);
			if (t.getNo() == hand.getPlayerWind()) {
				hand.getPoints().add(
						new Point(context
								.getString(R.string.point_pair_wind_player),
								PLAYER_PAIR_WIND, combination,false));
			} else if (t.getNo() == roundWind) {
				hand.getPoints().add(
						new Point(context
								.getString(R.string.point_pair_wind_round),
								ROUND_PAIR_WIND, combination,false));
			} else if ((t.getNo() == gameWind) && (!!t.getIsVisible())) {
				hand.getPoints().add(
						new Point(context
								.getString(R.string.point_pair_wind_game),
								GAME_PAIR_WIND, combination,false));
			}
			break;
		case DRAGON:
			checkIfHidden(t);
			if (!!t.getIsVisible()) {
				hand.getPoints().add(
						new Point(
								context.getString(R.string.point_pair_dragon),
								DRAGON_PAIR, combination,false));
			}
			break;
		case CHARACTER:
		case CIRCLE:
		case BAMBOO:
			checkIfPure(t);
			checkIfHidden(t);
			checkIfTerminal(t);
			break;
		default:
			Log.e(TAG, "seasons in pair?!?...: " + combination);
			break;
		}

	}

	public void setWinnerPoints() {

		// add mahjong points
		hand.getPoints().add(
				new Point(context.getString(R.string.point_mahjong),
						DID_MAHJONG, null,false));

		if (onlyChow) {
			// only chows
			hand.getPoints().add(
					new Point(context.getString(R.string.point_only_chow),
							ONLY_CHOW, null,false));
		}
		// winning tile from wall
		if (hand.fromWall()) {
			hand.getPoints().add(
					new Point(context.getString(R.string.point_from_wall),
							FROM_WALL, null,false));
		}
		// TODO: not implemented
		// if(hand.isServed()){
		// hand.getPoints().add(context
		// .getString(R.string.point_served),
		// SERVED, null);
		// }
		// finish with 5 dot from wall
		if (hand.prunier()) {
			hand.getPoints().add(
					new Point(context.getString(R.string.point_prunier),
							FIVE_CIRCLE, null,false));
		}
	}

	public void setWinnerBonuses() {

		// no chow in the hand
		if (onlyPong) {
			hand.getBonuses().add(
					new Point(context.getString(R.string.point_only_pong),
							NO_CHOW, null,true));
		}

		// TODO: not implemented
		// main appelante
//		if (hand.called()) {
//			hand.getBonuses().add(
//					new Point(context.getString(R.string.point_calling_hand),
//							CALLING_HAND, null));
//		}

		// only one kind of till
		if (isPure) {
			hand.getBonuses().add(
					new Point(context.getString(R.string.point_pure_hand),
							PUR_HAND, null,true));
		}

		// only one kind of till
		if (hand.stealedKong()) {
			hand.getBonuses().add(
					new Point(context.getString(R.string.point_steal_kong),
							STEAL_KONG, null,true));
		}

		// only one kind of till
		if (hand.fromHill()) {
			hand.getBonuses().add(
					new Point(context.getString(R.string.point_hill),
							FROM_HILL, null,true));
		}

		// only one kind of till
		if (isHidden) {
			hand.getBonuses().add(
					new Point(context.getString(R.string.point_hidden),
							FULL_HIDDEN, null,true));
		}

		// only one kind of till
		if (isTerminal) {
			hand.getBonuses().add(
					new Point(context.getString(R.string.point_terminal),
							ONE_NINE_HONNOR, null,true));
		}
		// only one kind of till
		if (hand.lastTile()) {
			hand.getBonuses().add(
					new Point(context.getString(R.string.point_last_tile),
							LAST_TILE, null,true));
		}
	}
}
