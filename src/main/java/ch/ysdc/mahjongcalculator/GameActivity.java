package ch.ysdc.mahjongcalculator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import ch.ysdc.mahjongcalculator.manager.FileManager;
import ch.ysdc.mahjongcalculator.model.Combination;
import ch.ysdc.mahjongcalculator.model.Hand;
import ch.ysdc.mahjongcalculator.model.Possibility;
import ch.ysdc.mahjongcalculator.model.Tile;
import ch.ysdc.mahjongcalculator.utils.AndroidUtils;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;


public class GameActivity extends SherlockActivity implements
		ImageButton.OnClickListener {

	private static String TAG = "GameActivity";
	public static final String ACTION_GAME = "ch.ysdc.mahjongcalculator.action.GAME";
	public static final String WIND = "ch.ysdc.mahjongcalculator.WIND";
	public static final String FLOW_AND_SEAS = "ch.ysdc.mahjongcalculator.flowandseas";
	
	private Hand hand;
	private List<Integer> wind;
	private List<Integer> flowersAndSeasons;

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
		setContentView(R.layout.game);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Log.i(TAG, "onCreate");

	    Bundle extras = getIntent().getExtras();
	    if(getIntent().hasExtra(MainActivity.POSSIBILITY)){
	    	Possibility possibility = extras.getParcelable(MainActivity.POSSIBILITY);
	    	if(possibility != null){
	    		hand = new Hand(possibility);
	    		wind = new LinkedList<Integer>();
	    		flowersAndSeasons = new LinkedList<Integer>();
	    	}
	    	getIntent().removeExtra(MainActivity.POSSIBILITY);
	    }
		
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

		Log.d(TAG, "hand on start" + hand);
		if(hand == null){
			// Try to load saved hand
			hand = fm.readHand(MainActivity.POSSIBILITY);
    		wind = fm.readIntegerList(WIND);
    		flowersAndSeasons = fm.readIntegerList(FLOW_AND_SEAS);
		}
		Log.d(TAG, "nb combo: " + hand.getCombinations().size());
		initializeView();
	}

	/****************************************************************************
	 * onStop
	 ****************************************************************************/
	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG, "onStop");

		Log.d(TAG, "nb combo: " + hand.getCombinations().size());
		FileManager fm = new FileManager(getFilesDir());
		fm.saveHand(hand, MainActivity.POSSIBILITY);
		fm.saveIntegerList(wind, WIND);
		fm.saveIntegerList(flowersAndSeasons, FLOW_AND_SEAS);
		resetView();
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
		case android.R.id.home:
			this.finish();
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
			Intent intent4 = new Intent(Intent.ACTION_MAIN);
			intent4.addCategory(Intent.CATEGORY_HOME);
			intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent4);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/****************************************************************************
	 * WindSelected, called when the user select a wind
	 ****************************************************************************/
	public void windSelected(View view) {
		if (wind.size() > 0) {
			ImageButton btn = (ImageButton) findViewById(wind.get(0));
			btn.setBackgroundColor(Color.TRANSPARENT);
		}
		wind.clear();
		wind.add(view.getId());
		Log.d(TAG, "Player wind Selected: " + view.getTag());
		((ImageButton) view).setBackgroundColor(Color.YELLOW);
	}

	/****************************************************************************
	 * FlowerSelected, called when the user select a flower
	 ****************************************************************************/
	public void flowerOrSeasonSelected(View view) {
		if (flowersAndSeasons.contains(view.getId())) {
			view.setBackgroundColor(Color.TRANSPARENT);
			flowersAndSeasons.remove(Integer.valueOf(view.getId()));
		}else{
			view.setBackgroundColor(Color.YELLOW);
			flowersAndSeasons.add(Integer.valueOf(view.getId()));
		}
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
			LinearLayout playerLayout = (LinearLayout) (tile.getIsVisible() ? findViewById(R.id.game_player_open_tiles)
					: findViewById(R.id.game_player_hidden_tiles));

			// Add the tile button to the player list
			playerLayout.addView(imgButton, paramsLO);
		}
	}

	/****************************************************************************
	 *Called to set the view with the current hand
	 ****************************************************************************/
	private void initializeView() {

		for(Combination combination : hand.getCombinations()){
			addTile(combination);
		}
		for(Integer i : wind){
			ImageButton btn = (ImageButton)findViewById(i);
			btn.setBackgroundColor(Color.YELLOW);
		}
		for(Integer i : flowersAndSeasons){
			ImageButton btn = (ImageButton)findViewById(i);
			btn.setBackgroundColor(Color.YELLOW);
		}
	}

	/****************************************************************************
	 * Clear the view from all it's hand specific content (tiles, etc)
	 ****************************************************************************/
	private void resetView() {
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.game_player_open_tiles);
		layout.removeAllViews();

		layout = (LinearLayout) findViewById(R.id.game_player_hidden_tiles);
		layout.removeAllViews();

		for(Integer i : wind){
			ImageButton btn = (ImageButton)findViewById(i);
			btn.setBackgroundColor(Color.TRANSPARENT);
		}
		for(Integer i : flowersAndSeasons){
			ImageButton btn = (ImageButton)findViewById(i);
			btn.setBackgroundColor(Color.TRANSPARENT);
		}

		hand = null;
		wind = null;
		flowersAndSeasons = null;
	}
}
