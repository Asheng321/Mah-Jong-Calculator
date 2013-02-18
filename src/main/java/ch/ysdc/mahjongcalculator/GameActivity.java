package ch.ysdc.mahjongcalculator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import ch.ysdc.mahjongcalculator.calculation.Possibility;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class GameActivity extends SherlockActivity implements ImageButton.OnClickListener{

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

	}

	/****************************************************************************
	 * onStop
	 ****************************************************************************/
	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG, "onStop");

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
}

