/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ysdc.mahjongcalculator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import ch.ysdc.mahjongcalculator.calculation.CombinationManager;
import ch.ysdc.mahjongcalculator.calculation.Possibility;
import ch.ysdc.mahjongcalculator.manager.DatabaseManager;
import ch.ysdc.mahjongcalculator.manager.FileManager;
import ch.ysdc.mahjongcalculator.manager.GameManager;
import ch.ysdc.mahjongcalculator.model.Tile;
import ch.ysdc.mahjongcalculator.utils.AndroidUtils;
import ch.ysdc.mahjongcalculator.utils.Constants;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockActivity implements
		ImageButton.OnClickListener {

	private static String TAG = "mainActivity";

	public static final int MSG_ERR = 0;
	public static final int MSG_SINGLE_END = 1;
	public static final int MSG_MULTI_END = 2;
	public static final int MSG_INFO = 3;

    public static final String POSSIBILITY = "ch.ysdc.mahjongcalculator.POSSIBILITY";
	public static final String OPEN_TILES_FILENAME = "ch.ysdc.mahjongcalculator.OPENPLAYERTILES";
	public static final String HIDDEN_TILES_FILENAME = "ch.ysdc.mahjongcalculator.HIDDENPLAYERTILES";

	private static int tileIdGenerator = 0;
	private static int tileCounter;
	private static int tileSelected;

	private static HashMap<String, Integer> openTiles;
	private static HashMap<String, Integer> hiddenTiles;

	private static SharedPreferences mPrefs;
	private static Context mContext;
	protected static ProgressDialog mProgressDialog;
	protected static List<Possibility> possibilities;

	/****************************************************************************
	 * Called automatically to create the action bar
	 * 
	 * @param menu
	 * @return
	 ****************************************************************************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.d(TAG, "onCreateOptionsMenu");
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	/****************************************************************************
	 * Called when the activity is first created.
	 * 
	 * @param savedInstanceState
	 *            If the activity is being re-initialized after previously being
	 *            shut down then this Bundle contains the data it most recently
	 *            supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it
	 *            is null.</b>
	 ****************************************************************************/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Initialization
		setContentView(R.layout.main);
		// Save the context, for the future handler
		mContext = this;
		Log.d(TAG, "onCreate");

		// Init the DB
		DatabaseManager.init(this);

		// playerHand = DatabaseManager.getInstance().createHand(new Hand());
		// Log.d(TAG, "playerHand: " + playerHand);

		firstRunPreferences();
		// if (getFirstRun()) {
		// Log.d(TAG, "onCreate: it's the first run");
		// createGame();
		// Log.d(TAG, "onCreate: init done");
		// } else {
		// List<Game> games = DatabaseManager.getInstance().getCurrentGame();
		//
		// switch(games.size()){
		// case 0:
		// createGame();
		// break;
		// case 1:
		// currentGame = games.get(0);
		// loadCurrentGame();
		// break;
		// default:
		// throw new RuntimeException("More than one current game...");
		// }
		// Log.d(TAG, "onCreate: not the first run");
		// }
	}

	// private void loadCurrentGame() {
	// // TODO Auto-generated method stub
	//
	// }
	// private void createGame() {
	//
	// currentGame = new Game();
	// currentGame.setIsCurrent(true);
	// DatabaseManager.getInstance().createGame(currentGame);
	//
	// // Initialize the progress window
	// mProgressDialog = ProgressDialog.show(this,
	// getString(R.string.main_creategame_title),
	// getString(R.string.main_creategame_message), true);
	//
	// // Create the thread that will initialize our DB
	// new Thread((new Runnable() {
	// @Override
	// public void run() {
	// DatabaseInitializator.initializeGame(currentGame);
	//
	// Message msg = mHandler.obtainMessage(MSG_END,
	// getString(R.string.main_creategame_finish));
	// // sends the message to our handler
	// mHandler.sendMessage(msg);
	// setRunned();
	// }
	// })).start();
	//
	//
	// }

	/****************************************************************************
	 * onPause
	 ****************************************************************************/
	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
	}

	/****************************************************************************
	 * onStart
	 ****************************************************************************/
	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "onStart");

		// Initialize our attributes
		tileSelected = -1;
		tileCounter = 0;
		openTiles = new HashMap<String, Integer>();
		hiddenTiles = new HashMap<String, Integer>();
		updateCounter();

		FileManager fm = new FileManager(getFilesDir());

		// Try to load saved open tiles
		HashMap<String, Integer> ot = fm.readHashMap(OPEN_TILES_FILENAME);
		
		for (String key : ot.keySet()) {
			for (int i = 0; i < ot.get(key); i++) {
				addPlayerTile(key, false);
			}
		}

		// Try to load saved hidden tiles
		HashMap<String, Integer> ht = fm.readHashMap(HIDDEN_TILES_FILENAME);

		for (String key : ht.keySet()) {
			for (int i = 0; i < ht.get(key); i++) {
				addPlayerTile(key, true);
			}
		}
	}

	/****************************************************************************
	 * onStop
	 ****************************************************************************/
	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG, "onStop");

		FileManager fm = new FileManager(getFilesDir());
		fm.saveHashMap(openTiles, OPEN_TILES_FILENAME);
		fm.saveHashMap(hiddenTiles, HIDDEN_TILES_FILENAME);
		
		LinearLayout playerLayout = (LinearLayout) findViewById(R.id.main_player_open_tiles);
		playerLayout.removeAllViews();

		playerLayout = (LinearLayout) findViewById(R.id.main_player_hidden_tiles);
		playerLayout.removeAllViews();
		
		openTiles = null;
		hiddenTiles = null;
	}

	/****************************************************************************
	 * onPause
	 ****************************************************************************/
	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
	}

	/****************************************************************************
	 * onPause
	 ****************************************************************************/
	@Override
	public void onRestart() {
		super.onRestart();
		Log.d(TAG, "onRestart");
	}

	/****************************************************************************
	 * onDestroy
	 ****************************************************************************/
	@Override
	public void onDestroy() {
		super.onDestroy(); // Always call the superclass
		Log.d(TAG, "onDestroy");

		// Stop method tracing that the activity started during onCreate()
		android.os.Debug.stopMethodTracing();
	}

	/****************************************************************************
	 * get if this is the first run
	 * 
	 * @return returns true, if this is the first run
	 ****************************************************************************/
	public boolean getFirstRun() {
		return mPrefs.getBoolean(Constants.firstRun, true);
	}

	/****************************************************************************
	 * store the first run
	 ****************************************************************************/
	public void firstRunDone() {
		Log.d(TAG, "set Runned called");
		SharedPreferences.Editor edit = mPrefs.edit();
		edit.putBoolean(Constants.firstRun, false);
		edit.commit();
	}

	/****************************************************************************
	 * setting up preferences storage
	 ****************************************************************************/
	public void firstRunPreferences() {
		// mContext = this.getApplicationContext();
		// 0 = mode private. only this app can read these preferences
		mPrefs = mContext.getSharedPreferences(Constants.appPref, 0);
	}

	/****************************************************************************
	 * Called when a user click on an available tile (not the player tiles)
	 * 
	 * @param view
	 *            The view who call the method
	 ****************************************************************************/
	public void tileSelected(View view) {
		// Get the image Button resource
		ImageButton tileButton = (ImageButton) findViewById(view.getId());
		Log.d(TAG, "Tile " + (String) tileButton.getTag()
				+ " selected with ID: " + view.getId());

		addPlayerTile((String) tileButton.getTag(), isPlayerHandSelected());
	}

	/****************************************************************************
	 * Called by the @method tileSelected method to add a tile to a player
	 * 
	 * @param tileTag
	 *            the name of the tile to add
	 * @param isPlayerHandSelected
	 *            true if the tile must be add to the hidden list
	 ****************************************************************************/
	@SuppressWarnings("deprecation")
	private void addPlayerTile(String tileTag, boolean isPlayerHandSelected) {

		Integer openTileCount = openTiles.get(tileTag);
		Integer hiddenTileCount = hiddenTiles.get(tileTag);

		Log.d(TAG, (String) tileTag + ": old value is " + openTileCount + "+"
				+ hiddenTileCount);
		// If we don't have the tile yet, we initialize the counter
		if (openTileCount == null) {
			openTileCount = 0;
		}
		if (hiddenTileCount == null) {
			hiddenTileCount = 0;
		}
		// If we already 4th time the tile, we can't have more of it
		if ((openTileCount + hiddenTileCount) == 4) {
			return;
		}
		LinearLayout.LayoutParams paramsLO = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		// Create the tile button to add to the view
		ImageButton imgButton = new ImageButton(this);
		imgButton.setImageResource(getResources().getIdentifier(
				(String) tileTag, "drawable", this.getPackageName()));
		imgButton.setTag((String) tileTag);

		int padding = AndroidUtils.fromDpToPixels(AndroidUtils.DEFAULT_TILE_PADDING,
				getResources().getDisplayMetrics().density);
		imgButton.setPadding(padding, padding, padding, padding);

		// This test is necessary because of compatibility reason...
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			imgButton.setBackgroundDrawable(null);
		} else {
			imgButton.setBackground(null);
		}
		imgButton.setOnClickListener(this);
		imgButton.setId(tileIdGenerator++);
		Log.d(TAG, "new player tile " + (String) imgButton.getTag()
				+ " selected with ID: " + imgButton.getId());

		// Get the player layout
		LinearLayout playerLayout = (LinearLayout) (isPlayerHandSelected ? findViewById(R.id.main_player_hidden_tiles)
				: findViewById(R.id.main_player_open_tiles));

		// Add the tile button to the player list
		playerLayout.addView(imgButton, paramsLO);
		if (isPlayerHandSelected) {
			hiddenTiles.put((String) tileTag, ++hiddenTileCount);
		} else {
			openTiles.put((String) tileTag, ++openTileCount);
		}
		tileCounter++;

		// If a player tile was selected, it's unselected
		if (tileSelected >= 0) {
			ImageButton btn = (ImageButton) findViewById(tileSelected);
			btn.setBackgroundColor(Color.TRANSPARENT);
			tileSelected = -1;
		}
		// We update then the tile counter
		updateCounter();
	}

	/****************************************************************************
	 * Update the sum of tiles added by the user
	 ****************************************************************************/
	public void updateCounter() {

		// Get player tiles count and set the value in the textview
		Resources res = getResources();
		String tilesQuantity = res.getQuantityString(
				R.plurals.playerTileQuantity, tileCounter, tileCounter);
		TextView playersQuantity = (TextView) findViewById(R.id.main_player_tiles);
		playersQuantity.setText(tilesQuantity);
		// update delete button
		Button delete = (Button) findViewById(R.id.main_delete_btn);
		delete.setEnabled(tileSelected >= 0);
	}

	/****************************************************************************
	 * Method called by the delete button. It will delete the selected tile and
	 * update the tile count.
	 * 
	 * @param view
	 *            The view representing the delete button
	 ****************************************************************************/
	public void deleteTile(View view) {
		if (tileSelected >= 0) {
			ImageButton btn = (ImageButton) findViewById(tileSelected);
			Log.d(TAG, "button found: " + btn);

			if (R.id.main_player_hidden_tiles == ((View) btn.getParent())
					.getId()) {
				Integer tileQuantity = hiddenTiles.get(btn.getTag());
				hiddenTiles.put((String) btn.getTag(), --tileQuantity);

			} else {
				Integer tileQuantity = openTiles.get(btn.getTag());
				openTiles.put((String) btn.getTag(), --tileQuantity);

			}
			((ViewGroup) btn.getParent()).removeView(btn);
			tileCounter--;
			tileSelected = -1;
			((ImageButton) btn).setBackgroundColor(Color.TRANSPARENT);
			updateCounter();
		} else {
			Log.e(TAG, "tileSelected is" + tileSelected
					+ ", and delete button enabled...");
		}

	}

	/****************************************************************************
	 * Called when a user click on the player tile.
	 ****************************************************************************/
	@Override
	public void onClick(View v) {
		if (tileSelected >= 0) {
			ImageButton btn = (ImageButton) findViewById(tileSelected);
			btn.setBackgroundColor(Color.TRANSPARENT);
		}
		tileSelected = v.getId();
		Log.d(TAG, "Player Till Selected: " + tileSelected);
		((ImageButton) v).setBackgroundColor(Color.YELLOW);

		updateCounter();
	}

	/****************************************************************************
	 * Called when the user click on an action bar icon
	 ****************************************************************************/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, "optionSelected: " + item.getItemId());
		switch (item.getItemId()) {
		case R.id.main_option_next:
			// Initialize the progress window
			mProgressDialog = ProgressDialog.show(this,
					getString(R.string.main_next_title),
					getString(R.string.main_next_tiles), true);

			// Create the thread that will initialize our DB
			new Thread((new Runnable() {
				@Override
				public void run() {

					Tile.resetCounter();
					// Create the tiles object from the hash map (image name)
					LinkedList<Tile> tiles = GameManager.createTiles(openTiles,
							true);
					tiles.addAll(GameManager.createTiles(hiddenTiles, false));

					// calculate possibilities
					Message msg = mHandler.obtainMessage(MSG_INFO,
							getString(R.string.main_next_possibilities));
					mHandler.sendMessage(msg);

					CombinationManager calculator = new CombinationManager();
					possibilities = calculator.getPossibilities(tiles);
    				
					switch (possibilities.size()) {
					case 0:
						// if no possibility
	    				// create and send the error message to our handler
	    				msg = mHandler.obtainMessage(MSG_ERR,getString(R.string.error_invalid_tiles));
	    				mHandler.sendMessage(msg);
	    				Log.d(TAG, "no possibility");
						break;
					case 1:
						// if only one possibility
	    				// create and send the message to our handler
	    				Log.d(TAG, "1 possibility");
	    				msg = mHandler.obtainMessage(MSG_SINGLE_END,getString(R.string.main_next_single));
	    				mHandler.sendMessage(msg);
	    				//open new game activity
	    				Intent intent3 = new Intent(GameActivity.ACTION_GAME);
	    				intent3.putExtra(POSSIBILITY, possibilities.get(0));
	    				startActivity(intent3);
						break;
					default:
	    				Log.d(TAG, "many possibilities: " + possibilities.size());
	    				msg = mHandler.obtainMessage(MSG_MULTI_END,getString(R.string.main_next_multi));
	    				mHandler.sendMessage(msg);
						break;
					}
				}
			})).start();

			return true;
		case R.id.main_option_settings:
			Log.d(TAG, "Enter the settings Option case");
			Intent intent2 = new Intent(this, SettingsActivity.class);
			startActivity(intent2);
			return true;
		case R.id.main_option_about:
			// Log.d(TAG, "\nDisplay tile with hand");
			// for(Tile t :
			// DatabaseManager.getInstance().getGameTiles(currentGame)){
			// if(t.getHand()!= null){
			// Log.d(TAG, "Tile: " + t);
			// }
			// }
			return true;
		case R.id.main_option_exit:
			// Log.d(TAG, "\nDisplay hand tiles");
			// for(Tile t : currentGame.getTiles()){
			// Log.d(TAG, "Tile: " + t);
			// }
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/****************************************************************************
	 * Return true if the option button selected is the hidden tile
	 ****************************************************************************/
	private boolean isPlayerHandSelected() {
		RadioGroup g = (RadioGroup) findViewById(R.id.main_add_type);
		return (g.getCheckedRadioButtonId() == R.id.main_add_hidden);
	}

	/****************************************************************************
	 * The handler is used to modify the user interface (more precisely the
	 * progress view) while a Thread is running.
	 ****************************************************************************/
	final static Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_INFO:
				if (mProgressDialog.isShowing()) {
					mProgressDialog.setMessage(((String) msg.obj));
				}
				break;
			case MSG_ERR:
				Toast.makeText(mContext, "Error: " + ((String) msg.obj),
						Toast.LENGTH_LONG).show();
				if (mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
				}
				break;
			case MSG_SINGLE_END:
				Toast.makeText(mContext, ((String) msg.obj), Toast.LENGTH_LONG)
						.show();
				if (mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
				}
				break;
			case MSG_MULTI_END:
				if (mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
				}
				break;
			default: // should never happen
				Log.d(TAG, "Oups, handler is in the defautl switch case :/");
				break;
			}
		}
	};
}


