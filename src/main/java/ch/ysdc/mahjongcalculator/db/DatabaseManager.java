/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ysdc.mahjongcalculator.db;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.util.Log;
import ch.ysdc.mahjongcalculator.model.Hand;
import ch.ysdc.mahjongcalculator.model.Tile;

import com.j256.ormlite.stmt.QueryBuilder;

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
    public List<Tile> getAllTiles() {
        List<Tile> tilesList = null;
        try {
            tilesList = getHelper().getTileDao().queryForAll();
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

    public void updateTile(Tile wishList) {
        try {
            getHelper().getTileDao().update(wishList);
        } catch (SQLException e) {
            Log.e(this.getClass().getName(), "SQLException during updateTile", e);
        }
    }


    /***********************************************************
    HAND METHODS
    ***********************************************************/
    public Hand createHand(Hand h) {
        try {
            getHelper().getHandDao().create(h);
            return h;
        } catch (SQLException e) {
            Log.e(this.getClass().getName(), "SQLException during addTile", e);
        }
        return null;
    }
}