package ch.ysdc.mahjongcalculator;

import java.util.Collections;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import ch.ysdc.mahjongcalculator.calculation.Combination;
import ch.ysdc.mahjongcalculator.calculation.Possibility;
import ch.ysdc.mahjongcalculator.manager.FileManager;
import ch.ysdc.mahjongcalculator.model.Tile;
import ch.ysdc.mahjongcalculator.utils.AndroidUtils;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class GameActivity extends SherlockActivity implements
		ImageButton.OnClickListener {

	private static String TAG = "GameActivity";
	private Possibility possibility;

	public static final int MSG_ERR = 0;
	public static final int MSG_END = 1;
	public static final int MSG_EMPTY_END = 2;
	public static final int MSG_INFO = 3;

	protected static ProgressDialog mProgressDialog;

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
		inflater.inflate(R.menu.game_menu, menu);
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
		Log.i(TAG, "onCreate");

	    Bundle extras = getIntent().getExtras();
	    if(getIntent().hasExtra(MainActivity.POSSIBILITY)){
	    	possibility = extras.getParcelable(MainActivity.POSSIBILITY);
	    }
		
		setContentView(R.layout.game);
	}

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

		FileManager fm = new FileManager(getFilesDir());

		// Try to load saved open tiles
		HashMap<String, Integer> ot = fm.readHashMap(MainActivity.POSSIBILITY);
		
		for (String key : ot.keySet()) {
			for (int i = 0; i < ot.get(key); i++) {
				addPlayerTile(key, false);
			}
		}

		// Try to load saved hidden tiles
		HashMap<String, Integer> ht = fm.readHashMap(MainActivity.POSSIBILITY);

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
	 * Called when the user click on an action bar icon
	 ****************************************************************************/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, "optionSelected: " + item.getItemId());
		switch (item.getItemId()) {
		case R.id.game_option_previous:
			return true;
		case R.id.game_option_next:
			return true;
		case R.id.game_option_settings:
			Log.d(TAG, "Enter the settings Option case");
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		case R.id.game_option_about:
			return true;
		case R.id.game_option_exit:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/****************************************************************************
	 * WindSelected, called when the user select a wind
	 ****************************************************************************/
	public void windSelected(View view) {
		// TODO Auto-generated method stub
		Log.d(TAG, "windSelected");

	}

	/****************************************************************************
	 * FlowerSelected, called when the user select a flower
	 ****************************************************************************/
	public void flowerSelected(View view) {
		// TODO Auto-generated method stub
		Log.d(TAG, "flowerSelected");
	}

	/****************************************************************************
	 * SeasonSelected, called when the user select a season
	 ****************************************************************************/
	public void seasonSelected(View view) {
		// TODO Auto-generated method stub
		Log.d(TAG, "seasonSelected");
	}

	/****************************************************************************
	 * onClick, called when the user select a tile
	 ****************************************************************************/
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onClick");
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
	private void addTile(Combination combination) {

		LinearLayout.LayoutParams paramsLO = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		Collections.sort(combination.getTiles());

		for (int i=0; i<combination.getTiles().size(); i++) {
			
			Tile tile = combination.getTiles().get(i);
			
			// Create the tile button to add to the view
			ImageButton imgButton = new ImageButton(this);
			imgButton.setImageResource(getResources().getIdentifier(
					tile.getImg(), "drawable", this.getPackageName()));
			imgButton.setTag(tile.getImg());

			int padding = AndroidUtils.fromDpToPixels(AndroidUtils.COMBINATION_TILE_PADDING,
					getResources().getDisplayMetrics().density);
			
			imgButton.setPadding((i==0?padding:0), padding, (i==(combination.getTiles().size()-1)?padding:0), padding);

			// This test is necessary because of compatibility reason...
			if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				imgButton.setBackgroundDrawable(null);
			} else {
				imgButton.setBackground(null);
			}
			imgButton.setId(tile.getId());
			Log.d(TAG, "new player tile " + (String) imgButton.getTag()
					+ " selected with ID: " + imgButton.getId());

			// Get the player layout
			LinearLayout playerLayout = (LinearLayout) (tile.getIsVisible() ? findViewById(R.id.main_player_open_tiles)
					: findViewById(R.id.main_player_hidden_tiles));

			// Add the tile button to the player list
			playerLayout.addView(imgButton, paramsLO);
		}

	}
}
