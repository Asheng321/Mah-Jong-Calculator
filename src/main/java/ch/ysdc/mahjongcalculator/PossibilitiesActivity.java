package ch.ysdc.mahjongcalculator;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import ch.ysdc.mahjongcalculator.adapters.PossibilityArrayAdapter;
import ch.ysdc.mahjongcalculator.manager.FileManager;
import ch.ysdc.mahjongcalculator.model.Possibility;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class PossibilitiesActivity extends SherlockActivity implements OnItemClickListener{

	private static String TAG = "PossibilitiesActivity";
	public static final String ACTION_MULTI = "ch.ysdc.mahjongcalculator.action.MULTI";
	
	private List<Possibility> possibilities;
	private int selectedItem;
	private ListView listView;
	private PossibilityArrayAdapter adapter;

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

			// Intent intent3 = new Intent(GameActivity.ACTION_GAME);
			// intent3.putExtra(MainActivity.POSSIBILITY,
			// (Parcelable)possibilities.get(position));
			// startActivity(intent3);

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
	 * onCreate
	 ****************************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.possibilities);
		// Show the Up button in the action bar.
		listView = (ListView) findViewById(R.id.possibilities_list);

		Bundle extras = getIntent().getExtras();
		if (getIntent().hasExtra(MainActivity.POSSIBILITIES)) {
			possibilities = extras
					.getParcelableArrayList(MainActivity.POSSIBILITIES);
			getIntent().removeExtra(MainActivity.POSSIBILITIES);
			initializeList();
		}
		Log.d(TAG, "onCreate, possibilities: "
				+ (possibilities != null ? possibilities.size() : "null"));
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

		if (possibilities == null) {
			Log.d(TAG, "was null");
			// Try to load saved hand
			possibilities = fm.readPossibilities(MainActivity.POSSIBILITIES);
			initializeList();
			adapter.notifyDataSetChanged();
		}
		Log.d(TAG, "onStart, possibilities: "
				+ (possibilities != null ? possibilities.size() : "null"));
	}

	/****************************************************************************
	 * onStop
	 ****************************************************************************/
	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG, "onStop, possibilities: "
				+ (possibilities != null ? possibilities.size() : "null"));

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

	/****************************************************************************
	 * initializeList
	 ****************************************************************************/
	private void initializeList() {

		adapter = new PossibilityArrayAdapter(this,
				possibilities.toArray(new Possibility[possibilities.size()]));
		// Assign adapter to ListView
		listView.setAdapter(adapter);
		listView.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

		selectedItem = -1;
		listView.setOnItemClickListener(this);
	}

	public void rowSelected(View view) {

		if (selectedItem >= 0) {
			// Change old button state
			LinearLayout layout = (LinearLayout) listView
					.getChildAt(selectedItem);
			ImageButton btn = (ImageButton) layout
					.findViewById(R.id.possibilities_selection);
			btn.setImageResource(getResources().getIdentifier("nonselected",
					"drawable", this.getPackageName()));
		}
		// Set new button state
		((ImageButton) view).setImageResource(getResources().getIdentifier(
				"selected", "drawable", this.getPackageName()));

		// Set the selected
		selectedItem = (Integer) view.getTag();
	}

	@Override
	public void onItemClick(AdapterView<?> myAdapter, View myView,
			int myItemInt, long mylng) {

		if (selectedItem >= 0) {
			// Change old button state
			LinearLayout layout = (LinearLayout) listView
					.getChildAt(selectedItem);
			ImageButton btn = (ImageButton) layout
					.findViewById(R.id.possibilities_selection);
			btn.setImageResource(getResources().getIdentifier("nonselected",
					"drawable", this.getPackageName()));
		}
		// Set new button state
		ImageButton btn = (ImageButton) myView.findViewById(R.id.possibilities_selection);
		btn.setImageResource(getResources().getIdentifier(
				"selected", "drawable", this.getPackageName()));
	}

}
