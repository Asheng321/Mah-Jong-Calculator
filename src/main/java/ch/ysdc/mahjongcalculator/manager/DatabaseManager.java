/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ysdc.mahjongcalculator.manager;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.util.Log;
import ch.ysdc.mahjongcalculator.model.Hand;
import ch.ysdc.mahjongcalculator.model.Game;
import ch.ysdc.mahjongcalculator.model.Tile;
/**
 *
 * @author djohannot
 */
public class DatabaseManager {

    static private DatabaseManager instance;

    static public void init(Context ctx) {
        if (null == instance) {
            instance = new DatabaseManager(ctx);
        }
    }

    static public DatabaseManager getInstance() {
        return instance;
    }
    private DatabaseHelper helper;

    private DatabaseManager(Context ctx) {
        helper = new DatabaseHelper(ctx);
    }

    private DatabaseHelper getHelper() {
        return helper;
    }

    /***********************************************************
    TILES METHODS
    ***********************************************************/
    public List<Tile> getGameTiles(Game currentGame) {
        List<Tile> tilesList = null;
        try {
            //tilesList = getHelper().getTileDao().queryForAll();
        	tilesList = getHelper().getTileDao().queryForEq(Tile.GAME_FIELD_NAME, currentGame);
        } catch (SQLException e) {
            Log.e(this.getClass().getName(), "SQLException during getAllTiles", e);
        }
        return tilesList;
    }

    public Tile getTileWithId(int tileId) {
        Tile tile = null;
        try {
            tile = getHelper().getTileDao().queryForId(tileId);
        } catch (SQLException e) {
            Log.e(this.getClass().getName(), "SQLException during getTileWithId(" + tileId + ")", e);
        }
        return tile;
    }

    public List<Tile> getTilesWithImageName(String tileImg) {
        try {
        	return getHelper().getTileDao().query(getHelper().getTileDao().queryBuilder().where().eq(Tile.IMG_FIELD_NAME, tileImg).prepare());
        } catch (SQLException e) {
            Log.e(this.getClass().getName(), "SQLException during getTilesWithImageName(" + tileImg + ")", e);
        }
        return null;
    }

    public void addTile(Tile l) {
        try {
            getHelper().getTileDao().create(l);
        } catch (SQLException e) {
            Log.e(this.getClass().getName(), "SQLException during addTile", e);
        }
    }

    public int updateTile(Tile wishList) {
        try {
            return getHelper().getTileDao().update(wishList);
        } catch (SQLException e) {
            Log.e(this.getClass().getName(), "SQLException during updateTile", e);
        }
        return 0;
    }


    /***********************************************************
    HAND METHODS
    ***********************************************************/
    public Hand createHand(Hand h) {
        try {
            getHelper().getHandDao().create(h);
            return h;
        } catch (SQLException e) {
            Log.e(this.getClass().getName(), "SQLException during createHand", e);
        }
        return null;
    }
    public int updateHand(Hand h) {
        try {
            return getHelper().getHandDao().update(h);
        } catch (SQLException e) {
            Log.e(this.getClass().getName(), "SQLException during updateHand", e);
        }
        return 0;
    }
    


    /***********************************************************
    GAME METHODS
    ***********************************************************/

    public List<Game> getCurrentGame(){
        try {
        	return getHelper().getGameDao().query(getHelper().getGameDao().queryBuilder().where().eq(Game.IS_CURRENT_FIELD_NAME, Boolean.valueOf(true)).prepare());
        } catch (SQLException e) {
            Log.e(this.getClass().getName(), "SQLException during create game", e);
        }
        return null;
    }
    
    public Game createGame(Game h) {
        try {
            getHelper().getGameDao().create(h);
            return h;
        } catch (SQLException e) {
            Log.e(this.getClass().getName(), "SQLException during create game", e);
        }
        return null;
    }
    
    public int updateGame(Game h) {
        try {
            return getHelper().getGameDao().update(h);
            
        } catch (SQLException e) {
            Log.e(this.getClass().getName(), "SQLException during update Game", e);
        }
        return 0;
    }
    
    public int deleteGame(Game h) {
        try {
            return getHelper().getGameDao().update(h);
        } catch (SQLException e) {
            Log.e(this.getClass().getName(), "SQLException during delete Game", e);
        }
        return 0;
    }
}