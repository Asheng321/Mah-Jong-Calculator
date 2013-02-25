package ch.ysdc.mahjongcalculator.adapters;

import java.util.Collections;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import ch.ysdc.mahjongcalculator.R;
import ch.ysdc.mahjongcalculator.ResultActivity;
import ch.ysdc.mahjongcalculator.model.Combination;
import ch.ysdc.mahjongcalculator.model.Point;
import ch.ysdc.mahjongcalculator.model.Tile;
import ch.ysdc.mahjongcalculator.utils.AndroidUtils;

public class PointArrayAdapter extends ArrayAdapter<Point> {
	private final ResultActivity context;
	private final Point[] values;
	private static String TAG = "PointArrayAdapter";

	public PointArrayAdapter(ResultActivity context, Point[] values) {
		super(context, R.layout.pointcell, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "getView: " + position);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.pointcell, parent, false);

		if(values[position].getCombination() != null){
			addCombination(values[position].getCombination(), rowView);
		}
		
		TextView info = (TextView) rowView.findViewById(R.id.result_point_info);
		info.setText(values[position].getName());

		TextView total = (TextView) rowView.findViewById(R.id.result_point_total);
		total.setText((values[position].isBonus() ? context.getString(R.string.bonus_prefix) + values[position].getPoints() : values[position].getPoints() + context.getString(R.string.point_suffix)));
		
		rowView.setTag(position);

		return rowView;
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
	private void addCombination(Combination combination, View rowView) {

		LinearLayout.LayoutParams paramsLO = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		Collections.sort(combination.getTiles());

		for (int i = 0; i < combination.getTiles().size(); i++) {

			Tile tile = combination.getTiles().get(i);

			// Create the tile button to add to the view
			ImageButton imgButton = new ImageButton(context);
			imgButton.setImageResource(context.getResources().getIdentifier(
					tile.getImg(), "drawable", context.getPackageName()));
			imgButton.setTag(tile.getImg());

			int padding = AndroidUtils.fromDpToPixels(
					AndroidUtils.COMBINATION_TILE_PADDING, context
							.getResources().getDisplayMetrics().density);

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

			// Get the player layout
			LinearLayout playerLayout = (LinearLayout) rowView
					.findViewById(R.id.result_point_tiles);

			// Add the tile button to the player list
			playerLayout.addView(imgButton, paramsLO);
		}
	}
}