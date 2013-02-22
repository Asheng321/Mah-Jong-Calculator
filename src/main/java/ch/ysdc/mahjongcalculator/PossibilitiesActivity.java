package ch.ysdc.mahjongcalculator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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

public class PossibilitiesActivity extends SherlockActivity implements
		OnItemClickListener {

	private static String TAG = "PossibilitiesActivity";
	public static final String ACTION_MULTI = "ch.ysdc.mahjongcalculator.action.MULTI";

	private List<Possibility> possibilities;
	private Integer selectedItem;
	private ListView listView;
	private PossibilityArrayAdapter adapter;

	/****************************************************************************
	 * onCreateOptionsMenu
	 ****************************************************************************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.possibilities_menu, menu);
		return true;
	}
	
	/****************************************************************************
	 * onCreate
	 ****************************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.possibilities);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Show the Up button in the action bar.
		listView = (ListView) findViewById(R.id.possibilities_list);

		Bundle extras = getIntent().getExtras();
		if (getIntent().hasExtra(MainActivity.POSSIBILITIES)) {
			possibilities = extras
					.getParcelableArrayList(MainActivity.POSSIBILITIES);
			getIntent().removeExtra(MainActivity.POSSIBILITIES);
			selectedItem = -1;
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
	@SuppressWarnings("unchecked")
	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "onStart");

		FileManager fm = new FileManager(getFilesDir());

		if (possibilities == null) {
			Log.d(TAG, "was null");
			// Try to load saved hand
			Object[] tmp = fm.readPossibilities(MainActivity.POSSIBILITIES);
			possibilities = (List<Possibility>) tmp[0];
			selectedItem = (Integer) tmp[1];
			Log.d(TAG, "After start,  values: " + selectedItem);
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
		fm.savePossibilities(possibilities, selectedItem,
				MainActivity.POSSIBILITIES);
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

		Collections.sort(possibilities, new InnerComparator());
		adapter = new PossibilityArrayAdapter(this,
				possibilities.toArray(new Possibility[possibilities.size()]));
		// Assign adapter to ListView
		listView.setAdapter(adapter);
		listView.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		listView.setOnItemClickListener(this);
	}


	/****************************************************************************
	 * get the selected item. Used by the adapter to get the selected item 
	 ****************************************************************************/
	public Integer getSelectedItem() {
		return selectedItem;
	}


	/****************************************************************************
	 * Listener methods
	 ****************************************************************************/
	public void rowSelected(View view) {

		Log.d(TAG, view.getTag() + " -> " + selectedItem);
		if ((selectedItem >= 0) && (selectedItem >= listView.getFirstVisiblePosition()) && (selectedItem <= listView.getLastVisiblePosition())){
			// Change old button state
			LinearLayout layout = (LinearLayout) listView
					.getChildAt(selectedItem-listView.getFirstVisiblePosition());
			if (layout != null) {
				ImageButton btn = (ImageButton) layout
						.findViewById(R.id.possibilities_selection);
				btn.setImageResource(getResources().getIdentifier(
						"nonselected", "drawable", this.getPackageName()));
			}
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

		Log.d(TAG, myView.getTag() + " -> " + selectedItem);
		if ((selectedItem >= 0) && (selectedItem >= listView.getFirstVisiblePosition()) && (selectedItem <= listView.getLastVisiblePosition())){
			// Change old button state
			LinearLayout layout = (LinearLayout) listView
					.getChildAt(selectedItem-listView.getFirstVisiblePosition());
			if (layout != null) {
				ImageButton btn = (ImageButton) layout
						.findViewById(R.id.possibilities_selection);
				btn.setImageResource(getResources().getIdentifier(
						"nonselected", "drawable", this.getPackageName()));
			}
		}
		// Set new button state
		ImageButton btn = (ImageButton) myView
				.findViewById(R.id.possibilities_selection);
		btn.setImageResource(getResources().getIdentifier("selected",
				"drawable", this.getPackageName()));
		// Set the selected
		selectedItem = (Integer) myView.getTag();
	}


	/****************************************************************************
	 * onOptionsItemSelected
	 ****************************************************************************/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, "optionSelected: " + item.getItemId());
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		case R.id.possibilities_option_next:
			if(selectedItem == null || selectedItem < 0){
				new AlertDialog.Builder(this)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setTitle(R.string.action_select_possibility_title)
		        .setMessage(R.string.action_select_possibility_msg)
		        .setPositiveButton(R.string.action_yes, null)
		        .show();
			}else{
			 Intent intent3 = new Intent(GameActivity.ACTION_GAME);
			 intent3.putExtra(MainActivity.POSSIBILITY,(Parcelable)possibilities.get(selectedItem));
			 startActivity(intent3);
			}
			return true;
		case R.id.possibilities_option_settings:
			Log.d(TAG, "Enter the settings Option case");
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		case R.id.possibilities_option_about:
			return true;
		case R.id.possibilities_option_exit:
			Intent intent4 = new Intent(Intent.ACTION_MAIN);
			intent4.addCategory(Intent.CATEGORY_HOME);
			intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent4);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public class InnerComparator implements Comparator<Possibility> {

		public int compare(Possibility a, Possibility b) {
			return b.getValue().compareTo(a.getValue());
		}
	}
	
}
