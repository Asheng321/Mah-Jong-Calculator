package ch.ysdc.mahjongcalculator;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import ch.ysdc.mahjongcalculator.adapters.PossibilityArrayAdapter;
import ch.ysdc.mahjongcalculator.manager.FileManager;
import ch.ysdc.mahjongcalculator.model.Possibility;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class PossibilitiesActivity extends SherlockListActivity implements OnItemClickListener{

	private static String TAG = "PossibilitiesActivity";
	public static final String ACTION_MULTI = "ch.ysdc.mahjongcalculator.action.MULTI";
	private List<Possibility> possibilities;
	private ListView listView;
	
	/****************************************************************************
	 * onCreateOptionsMenu
	 ****************************************************************************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.possibilities_menu, menu);
		return true;
	}

	/****************************************************************************
	 * onOptionsItemSelected
	 ****************************************************************************/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, "optionSelected: " + item.getItemId());
		switch (item.getItemId()) {
		case R.id.possibilities_option_previous:
			return true;
		case R.id.possibilities_option_next:
			return true;
		case R.id.possibilities_option_settings:
			Log.d(TAG, "Enter the settings Option case");
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		case R.id.possibilities_option_about:
			return true;
		case R.id.possibilities_option_exit:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/****************************************************************************
	 * Method called when a list cell is selected. The method will call 
	 * the game activity with the selected possibility in parameter.
	 ****************************************************************************/
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		Intent intent3 = new Intent(GameActivity.ACTION_GAME);
		intent3.putExtra(MainActivity.POSSIBILITY, possibilities.get(position));
		startActivity(intent3);
		
	}

	/****************************************************************************
	 * onCreate
	 ****************************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.possibilities);
		// Show the Up button in the action bar.
		listView = (ListView) getListView();

	    Bundle extras = getIntent().getExtras();
	    if(getIntent().hasExtra(MainActivity.POSSIBILITIES)){
	    	possibilities = extras.getParcelableArrayList(MainActivity.POSSIBILITIES);
	    	getIntent().removeExtra(MainActivity.POSSIBILITIES);
			PossibilityArrayAdapter adapter = new PossibilityArrayAdapter(this, possibilities.toArray(new Possibility[possibilities.size()]));
			// Assign adapter to ListView
			listView.setAdapter(adapter);
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

		if(possibilities == null){
			// Try to load saved hand
			possibilities = fm.readPossibilities(MainActivity.POSSIBILITIES);
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
		fm.savePossibilities(possibilities, MainActivity.POSSIBILITIES);
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


}
