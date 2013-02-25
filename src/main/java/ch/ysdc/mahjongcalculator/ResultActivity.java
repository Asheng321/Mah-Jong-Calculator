/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ysdc.mahjongcalculator;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import ch.ysdc.mahjongcalculator.adapters.PointArrayAdapter;
import ch.ysdc.mahjongcalculator.manager.FileManager;
import ch.ysdc.mahjongcalculator.model.Hand;
import ch.ysdc.mahjongcalculator.model.Point;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;


/**
 *
 * @author djohannot
 */
public class ResultActivity  extends SherlockActivity  implements
OnItemClickListener {

    private static String TAG = "resultActivity";

    private Hand hand;
	private ListView listView;
	private PointArrayAdapter adapter;
	
	/****************************************************************************
	 * onCreateOptionsMenu
	 ****************************************************************************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.result, menu);
		return true;
	}
	
	/****************************************************************************
	 * onCreate
	 ****************************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Show the Up button in the action bar.
		this.listView = (ListView) findViewById(R.id.result_list);

		Bundle extras = getIntent().getExtras();
		if (getIntent().hasExtra(GameActivity.HAND)) {
			hand = extras.getParcelable(GameActivity.HAND);
			getIntent().removeExtra(GameActivity.HAND);
			initializeList();
		}
		Log.d(TAG, "onCreate, hand: "
				+ (hand != null ? hand : "null"));
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

		if (hand == null) {
			Log.d(TAG, "was null");
			// Try to load saved hand
			hand = fm.readHand(GameActivity.HAND);
			initializeList();
			adapter.notifyDataSetChanged();
		}
		Log.d(TAG, "onStart, hand: "
				+ (hand != null ? hand : "null"));
	}

	/****************************************************************************
	 * onStop
	 ****************************************************************************/
	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG, "onStop, hand: "
				+ (hand != null ? hand : "null"));

		FileManager fm = new FileManager(getFilesDir());
		fm.saveHand(hand, GameActivity.HAND);
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

		Log.d(TAG, "points: " + hand.getPoints().size());
		LinkedList<Point> tmp = new LinkedList<Point>(hand.getPoints());
		tmp.addAll(hand.getBonuses());
		
		Collections.sort(tmp, new InnerComparator());
		adapter = new PointArrayAdapter(this,
				tmp.toArray(new Point[tmp.size()]));
		// Assign adapter to ListView
		listView.setAdapter(adapter);
		listView.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		listView.setOnItemClickListener(this);

		((TextView)findViewById(R.id.result_point)).setText(hand.getTotalPoints() + "" + getString(R.string.point_suffix));
		((TextView)findViewById(R.id.result_bonus)).setText(getString(R.string.bonus_prefix) + "" + hand.getTotalBonuses());
		((TextView)findViewById(R.id.result_total)).setText((hand.getTotalPoints() * hand.getTotalBonuses()) + "" + getString(R.string.point_suffix));
	}


	/****************************************************************************
	 * onOptionsItemSelected
	 ****************************************************************************/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, "optionSelected: " + item.getItemId());
		switch (item.getItemId()) {
		
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/****************************************************************************
	 * Listener methods
	 ****************************************************************************/
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Log.d(TAG, "row selected");
		
	}

	public class InnerComparator implements Comparator<Point> {

		public int compare(Point a, Point b) {
			return b.compareTo(a);
		}
	}
}

