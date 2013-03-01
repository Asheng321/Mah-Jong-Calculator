package ch.ysdc.mahjongcalculator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;
import ch.ysdc.mahjongcalculator.factory.ResultManagerFactory;
import ch.ysdc.mahjongcalculator.manager.FileManager;
import ch.ysdc.mahjongcalculator.manager.GameManager;
import ch.ysdc.mahjongcalculator.manager.ResultManager;
import ch.ysdc.mahjongcalculator.model.Combination;
import ch.ysdc.mahjongcalculator.model.Hand;
import ch.ysdc.mahjongcalculator.model.Possibility;
import ch.ysdc.mahjongcalculator.model.Tile;
import ch.ysdc.mahjongcalculator.model.Validity;
import ch.ysdc.mahjongcalculator.utils.AndroidUtils;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class GameActivity extends SherlockActivity implements
		ImageButton.OnClickListener {

	private static String TAG = "GameActivity";
	public static final String ACTION_GAME = "ch.ysdc.mahjongcalculator.action.GAME";
	public static final String ACTION_RESULT = "ch.ysdc.mahjongcalculator.action.RESULT";
	public static final String HAND = "ch.ysdc.mahjongcalculator.HAND";
	public static final String WIND = "ch.ysdc.mahjongcalculator.WIND";
	public static final String GAMEWIND = "ch.ysdc.mahjongcalculator.GAMEWIND";
	public static final String ROUNDWIND = "ch.ysdc.mahjongcalculator.ROUNDWIND";
	public static final String FLOW = "ch.ysdc.mahjongcalculator.FLOW";
	public static final String SEAS = "ch.ysdc.mahjongcalculator.SEAS";

	private Hand hand;
	private List<Integer> playerWind;
	private List<Integer> roundWind;
	private List<Integer> gameWind;
	private List<Integer> flowers;
	private List<Integer> seasons;

	public static final int MSG_ERR = 0;
	public static final int MSG_END = 1;
	public static final int MSG_EMPTY_END = 2;
	public static final int MSG_INFO = 3;

	private static Context mContext;
	private static ResultManager resultManager;

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
		// Set the home button as visible
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Log.i(TAG, "onCreate");
		mContext = this;

		Bundle extras = getIntent().getExtras();
		if (getIntent().hasExtra(MainActivity.POSSIBILITY)) {
			Possibility possibility = extras
					.getParcelable(MainActivity.POSSIBILITY);
			if (possibility != null) {
				hand = new Hand(possibility);
				playerWind = new LinkedList<Integer>();
				flowers = new LinkedList<Integer>();
				seasons = new LinkedList<Integer>();
				gameWind = new LinkedList<Integer>();
				roundWind = new LinkedList<Integer>();

				// Set the visibility of the winner layout
				LinearLayout layout = (LinearLayout) findViewById(R.id.game_winner_layout);
				layout.setVisibility(hand.getValidity() == Validity.MAHJONG ? View.VISIBLE
						: View.INVISIBLE);
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
		if (hand == null) {
			// Try to load saved hand
			hand = fm.readHand(MainActivity.POSSIBILITY);
			playerWind = fm.readIntegerList(WIND);
			gameWind = fm.readIntegerList(GAMEWIND);
			roundWind = fm.readIntegerList(ROUNDWIND);
			flowers = fm.readIntegerList(FLOW);
			seasons = fm.readIntegerList(SEAS);

		}
		Log.d(TAG, "nb combo: " + hand.getCombinations().size());
		loadGameParameters();
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
		saveGameParameters();
		FileManager fm = new FileManager(getFilesDir());
		fm.saveHand(hand, MainActivity.POSSIBILITY);
		fm.saveIntegerList(playerWind, WIND);
		fm.saveIntegerList(gameWind, GAMEWIND);
		fm.saveIntegerList(roundWind, ROUNDWIND);
		fm.saveIntegerList(flowers, FLOW);
		fm.saveIntegerList(seasons, SEAS);
		resetView();
	}

	/****************************************************************************
	 * saveGameParameters
	 ****************************************************************************/
	private void saveGameParameters() {

		// Winning tile
		ImageButton imgBtn = (ImageButton) findViewById(R.id.game_lasttile_tile);
		if (((String) imgBtn.getTag()).length() != 0) {
			hand.setWinningTile(GameManager.createTile((String) imgBtn.getTag()));
		}
		// from wall
		CheckBox box = (CheckBox) findViewById(R.id.game_lasttile_box);
		hand.setFromWall(box.isChecked());

		RadioGroup group = (RadioGroup) findViewById(R.id.game_last_choice);

		switch (group.getCheckedRadioButtonId()) {
		case 0:
			break;
		case 1:
			hand.setStealedKong(true);
			break;
		case 2:
			hand.setLastTile(true);
			break;
		case 3:
			hand.setFromHill(true);
			break;
		}

	}
	
	/****************************************************************************
	 * loadGameParameters
	 ****************************************************************************/
	private void loadGameParameters() {

		// Winning tile
		if(hand.getWinningTile() != null){
			ImageButton btn = (ImageButton) findViewById(R.id.game_lasttile_tile);
			btn.setImageResource(getResources().getIdentifier(
					(String) hand.getWinningTile().getImg(), "drawable", this.getPackageName()));
			btn.setTag((String) hand.getWinningTile().getImg());
		}
		// from wall
		CheckBox box = (CheckBox) findViewById(R.id.game_lasttile_box);
		box.setSelected(hand.fromWall());

		//Last tile
		RadioGroup group = (RadioGroup) findViewById(R.id.game_last_choice);
		
		if(hand.stealedKong()){
			group.check(R.id.game_last_kong);
		}else if(hand.lastTile()){
			group.check(R.id.game_last_title);
		}else if(hand.fromHill()){
			group.check(R.id.game_last_detached);
		}else{
			group.check(R.id.game_last_normal);
		}
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
			calculateResult();
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
		if (playerWind.size() > 0) {
			ImageButton btn = (ImageButton) findViewById(playerWind.get(0));
			btn.setBackgroundColor(Color.TRANSPARENT);
		}
		playerWind.clear();
		playerWind.add(view.getId());
		Log.d(TAG, "Player wind Selected: " + view.getTag());
		((ImageButton) view).setBackgroundColor(Color.YELLOW);
	}

	/****************************************************************************
	 * Flower Selected, called when the user select a flower
	 ****************************************************************************/
	public void flowerSelected(View view) {
		if (flowers.contains(view.getId())) {
			view.setBackgroundColor(Color.TRANSPARENT);
			flowers.remove(Integer.valueOf(view.getId()));
		} else {
			view.setBackgroundColor(Color.YELLOW);
			flowers.add(Integer.valueOf(view.getId()));
		}
	}

	/****************************************************************************
	 * SeasonSelected, called when the user select a flower
	 ****************************************************************************/
	public void seasonSelected(View view) {
		if (seasons.contains(view.getId())) {
			view.setBackgroundColor(Color.TRANSPARENT);
			seasons.remove(Integer.valueOf(view.getId()));
		} else {
			view.setBackgroundColor(Color.YELLOW);
			seasons.add(Integer.valueOf(view.getId()));
		}
	}

	/****************************************************************************
	 * roundWindSelected
	 ****************************************************************************/
	public void roundWindSelected(View view) {

		if (roundWind.size() > 0) {
			ImageButton btn = (ImageButton) findViewById(roundWind.get(0));
			btn.setBackgroundColor(Color.TRANSPARENT);
		}
		roundWind.clear();
		roundWind.add(view.getId());
		Log.d(TAG, "Player wind Selected: " + view.getTag());
		((ImageButton) view).setBackgroundColor(Color.YELLOW);
	}

	/****************************************************************************
	 * gameWindSelected
	 ****************************************************************************/
	public void gameWindSelected(View view) {
		if (gameWind.size() > 0) {
			ImageButton btn = (ImageButton) findViewById(gameWind.get(0));
			btn.setBackgroundColor(Color.TRANSPARENT);
		}
		gameWind.clear();
		gameWind.add(view.getId());
		Log.d(TAG, "Player wind Selected: " + view.getTag());
		((ImageButton) view).setBackgroundColor(Color.YELLOW);
	}

	/****************************************************************************
	 * onClick, called when the user select a tile
	 ****************************************************************************/
	@Override
	public void onClick(View view) {

		ImageButton btn = (ImageButton) findViewById(R.id.game_lasttile_tile);
		btn.setImageResource(getResources().getIdentifier(
				(String) view.getTag(), "drawable", this.getPackageName()));
		btn.setTag((String) view.getTag());
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

		for (int i = 0; i < combination.getTiles().size(); i++) {

			Tile tile = combination.getTiles().get(i);

			// Create the tile button to add to the view
			ImageButton imgButton = new ImageButton(this);
			imgButton.setImageResource(getResources().getIdentifier(
					tile.getImg(), "drawable", this.getPackageName()));
			imgButton.setTag(tile.getImg());

			int padding = AndroidUtils.fromDpToPixels(
					AndroidUtils.COMBINATION_TILE_PADDING, getResources()
							.getDisplayMetrics().density);

			imgButton.setPadding((i == 0 ? padding : 0), padding,
					(i == (combination.getTiles().size() - 1) ? padding : 0),
					padding);

			// This test is necessary because of compatibility reason...
			if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				imgButton.setBackgroundDrawable(null);
			} else {
				imgButton.setBackground(null);
			}
			imgButton.setId(tile.getId());
			imgButton.setOnClickListener(this);
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
	 * Called to set the view with the current hand
	 ****************************************************************************/
	private void initializeView() {

		for (Combination combination : hand.getCombinations()) {
			addTile(combination);
		}
		for (Integer i : playerWind) {
			ImageButton btn = (ImageButton) findViewById(i);
			btn.setBackgroundColor(Color.YELLOW);
		}
		for (Integer i : gameWind) {
			ImageButton btn = (ImageButton) findViewById(i);
			btn.setBackgroundColor(Color.YELLOW);
		}
		for (Integer i : roundWind) {
			ImageButton btn = (ImageButton) findViewById(i);
			btn.setBackgroundColor(Color.YELLOW);
		}
		for (Integer i : flowers) {
			ImageButton btn = (ImageButton) findViewById(i);
			btn.setBackgroundColor(Color.YELLOW);
		}
		for (Integer i : seasons) {
			ImageButton btn = (ImageButton) findViewById(i);
			btn.setBackgroundColor(Color.YELLOW);
		}

		// Set the visibility of the winner layout
		LinearLayout layout = (LinearLayout) findViewById(R.id.game_winner_layout);
		layout.setVisibility(hand.getValidity() == Validity.MAHJONG ? View.VISIBLE
				: View.INVISIBLE);
	}

	/****************************************************************************
	 * Clear the view from all it's hand specific content (tiles, etc)
	 ****************************************************************************/
	private void resetView() {

		LinearLayout layout = (LinearLayout) findViewById(R.id.game_player_open_tiles);
		layout.removeAllViews();

		layout = (LinearLayout) findViewById(R.id.game_player_hidden_tiles);
		layout.removeAllViews();

		for (Integer i : playerWind) {
			ImageButton btn = (ImageButton) findViewById(i);
			btn.setBackgroundColor(Color.TRANSPARENT);
		}
		for (Integer i : gameWind) {
			ImageButton btn = (ImageButton) findViewById(i);
			btn.setBackgroundColor(Color.TRANSPARENT);
		}
		for (Integer i : roundWind) {
			ImageButton btn = (ImageButton) findViewById(i);
			btn.setBackgroundColor(Color.TRANSPARENT);
		}
		for (Integer i : flowers) {
			ImageButton btn = (ImageButton) findViewById(i);
			btn.setBackgroundColor(Color.TRANSPARENT);
		}
		for (Integer i : seasons) {
			ImageButton btn = (ImageButton) findViewById(i);
			btn.setBackgroundColor(Color.TRANSPARENT);
		}

		hand = null;
		playerWind = null;
		gameWind = null;
		roundWind = null;
		flowers = null;
		seasons = null;
	}

	private void calculateResult() {
		int gameWindNb = 0;
		int roundWindNb = 0;

		// player wind test
		if (playerWind.size() != 1) {
			Toast.makeText(this, getString(R.string.error_select_playerwind),
					Toast.LENGTH_LONG).show();
			return;
		}
		ImageButton imgBtn = (ImageButton) findViewById(playerWind.get(0));
		hand.setPlayerWind(Integer.valueOf((String) imgBtn.getTag()));

		// game wind test
		if (gameWind.size() != 1) {
			Toast.makeText(this, getString(R.string.error_select_gamewind),
					Toast.LENGTH_LONG).show();
			return;
		}
		imgBtn = (ImageButton) findViewById(gameWind.get(0));
		gameWindNb = Integer.valueOf((String) imgBtn.getTag());

		// round wind test
		if (roundWind.size() != 1) {
			Toast.makeText(this, getString(R.string.error_select_roundwind),
					Toast.LENGTH_LONG).show();
			return;
		}
		imgBtn = (ImageButton) findViewById(roundWind.get(0));
		roundWindNb = Integer.valueOf((String) imgBtn.getTag());

		// flowers
		hand.getFlowers().clear();
		for (Integer id : flowers) {
			imgBtn = (ImageButton) findViewById(id);
			Tile t = GameManager.createTile((String) imgBtn.getTag());
			hand.addFlower(t);
		}

		// season
		hand.getSeasons().clear();
		for (Integer id : seasons) {
			imgBtn = (ImageButton) findViewById(id);
			Tile t = GameManager.createTile((String) imgBtn.getTag());
			hand.addSeasons(t);
		}

		// If winner hand
		if (hand.getValidity() == Validity.MAHJONG) {

			// Winning tile
			imgBtn = (ImageButton) findViewById(R.id.game_lasttile_tile);
			if (((String) imgBtn.getTag()).length() == 0) {
				Toast.makeText(this, getString(R.string.error_select_lasttile),
						Toast.LENGTH_LONG).show();
				return;
			}
			hand.setWinningTile(GameManager.createTile((String) imgBtn.getTag()));

			// from wall
			hand.setFromWall(false);
			CheckBox box = (CheckBox) findViewById(R.id.game_lasttile_box);
			hand.setFromWall(box.isChecked());

			RadioGroup group = (RadioGroup) findViewById(R.id.game_last_choice);

			hand.setStealedKong(false);
			hand.setLastTile(false);
			hand.setFromHill(false);
			
			switch (group.getCheckedRadioButtonId()) {
			case 0:
				break;
			case 1:
				if(box.isChecked()){
					Toast.makeText(this, getString(R.string.error_select_kong),
							Toast.LENGTH_LONG).show();
					return;
				}
				hand.setStealedKong(true);
				break;
			case 2:
				if(!box.isChecked()){
					Toast.makeText(this, getString(R.string.error_select_last),
							Toast.LENGTH_LONG).show();
					return;
				}
				hand.setLastTile(true);
				break;
			case 3:
				if(!box.isChecked()){
					Toast.makeText(this, getString(R.string.error_select_hill),
							Toast.LENGTH_LONG).show();
					return;
				}
				hand.setFromHill(true);
				break;
			}
		}
		// Initialize the progress window
		mProgressDialog = ProgressDialog.show(this,
				getString(R.string.game_result_title),
				getString(R.string.game_result_msg), true);

		resultManager = ResultManagerFactory.getSelectedResultManager(this,
				hand, roundWindNb, gameWindNb);
		// Create the thread that will initialize our DB
		new Thread((new Runnable() {
			@Override
			public void run() {

				hand = resultManager.CalculateResult();

				Message msg = mHandler.obtainMessage(MSG_EMPTY_END, null);
				mHandler.sendMessage(msg);

				Log.d(TAG, "nb points: " + hand.getPoints().size());
				Log.d(TAG, "nb bonus: " + hand.getBonuses().size());

				Log.d(TAG, "nb points: " + hand.getTotalPoints());
				Log.d(TAG, "nb bonus: " + hand.getTotalBonuses());

				// open result page
				Intent intent = new Intent(ACTION_RESULT);
				intent.putExtra(HAND, (Parcelable) hand);
				startActivity(intent);
			}
		})).start();

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
			case MSG_END:
				Toast.makeText(mContext, ((String) msg.obj), Toast.LENGTH_LONG)
						.show();
				if (mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
				}
				break;
			case MSG_EMPTY_END:
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
