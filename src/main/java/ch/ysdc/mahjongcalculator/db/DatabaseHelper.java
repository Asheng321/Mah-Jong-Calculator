/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ysdc.mahjongcalculator.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import ch.ysdc.mahjongcalculator.model.Hand;
import ch.ysdc.mahjongcalculator.model.HandTile;
import ch.ysdc.mahjongcalculator.model.Tile;
import ch.ysdc.mahjongcalculator.utils.Constants;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	// name of the database file for your application
	private static final String DATABASE_NAME = "MahJongCalculator.sqlite";

	private Context context;
	// any time you make changes to your database objects, you may have to
	// increase the database version
	private static final int DATABASE_VERSION = 1;

	// the DAO object we use to access the SimpleData table
	private Dao<Hand, Integer> handDao = null;
	private Dao<Tile, Integer> tileDao = null;

	public DatabaseHelper(Context c) {
		super(c, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = c;
	}

	@Override
	public void onCreate(SQLiteDatabase database,
			ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, Hand.class);
			TableUtils.createTable(connectionSource, Tile.class);
			TableUtils.createTable(connectionSource, HandTile.class);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			List<String> allSql = new ArrayList<String>();
			switch (oldVersion) {
			case 1:
				try {
					TableUtils.dropTable(connectionSource, Hand.class, false);
					TableUtils.dropTable(connectionSource, Tile.class, false);
					TableUtils.dropTable(connectionSource, HandTile.class, false);
					TableUtils.createTable(connectionSource, Hand.class);
					TableUtils.createTable(connectionSource, Tile.class);
					TableUtils.createTable(connectionSource, HandTile.class);
					
					//update the first run pref
					SharedPreferences mPrefs = context.getSharedPreferences(Constants.appPref, 0); //0 = mode 
				    SharedPreferences.Editor edit = mPrefs.edit();
				    edit.putBoolean(Constants.firstRun, false);
				    edit.commit();
				    
				    
				} catch (java.sql.SQLException e) {
					Log.e(DatabaseHelper.class.getName(), "Can't drop table", e);
					throw new RuntimeException(e);
				}
				// Example on how to hand possible manual change.
				// allSql.add("alter table AdData add column `new_col` VARCHAR");
				// allSql.add("alter table AdData add column `new_col2` VARCHAR");
			}
			for (String sql : allSql) {
				db.execSQL(sql);
			}
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "exception during onUpgrade",
					e);
			throw new RuntimeException(e);
		}

	}

	public Dao<Hand, Integer> getHandDao() {
		if (null == handDao) {
			try {
				handDao = getDao(Hand.class);
			} catch (java.sql.SQLException e) {
				Log.e(this.getClass().getName(),
						"SQLException during getHandDao", e);
			}
		}
		return handDao;
	}

	public Dao<Tile, Integer> getTileDao() {
		if (null == tileDao) {
			try {
				tileDao = getDao(Tile.class);
			} catch (java.sql.SQLException e) {
				Log.e(this.getClass().getName(),
						"SQLException during getTileDao", e);
			}
		}
		return tileDao;
	}
}