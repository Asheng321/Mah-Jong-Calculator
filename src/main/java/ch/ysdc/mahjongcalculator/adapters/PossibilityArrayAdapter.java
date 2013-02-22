package ch.ysdc.mahjongcalculator.adapters;
import java.util.Collections;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import ch.ysdc.mahjongcalculator.PossibilitiesActivity;
import ch.ysdc.mahjongcalculator.R;
import ch.ysdc.mahjongcalculator.model.Combination;
import ch.ysdc.mahjongcalculator.model.Possibility;
import ch.ysdc.mahjongcalculator.model.Tile;
import ch.ysdc.mahjongcalculator.utils.AndroidUtils;


public class PossibilityArrayAdapter extends ArrayAdapter<Possibility>{
	  private final PossibilitiesActivity context;
	  private final Possibility[] values;
		private static String TAG = "PossibilityArrayAdapter";

	  public PossibilityArrayAdapter(PossibilitiesActivity context, Possibility[] values) {
	    super(context, R.layout.possibilitycell, values);
	    this.context = context;
	    this.values = values;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
		  Log.d(TAG, "getView: " + position);
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.possibilitycell, parent, false);

	    for(Combination c : values[position].getCombinations()){
	    	addCombination(c, rowView);
	    }
	    if(values[position].getPair()!=null){
	    	addCombination(values[position].getPair(), rowView);
	    }
	    if(values[position].getUnusedTileCombination()!=null){
	    	addCombination(values[position].getUnusedTileCombination(), rowView);
	    }


		//Set the tag of the selection icon
		ImageButton selection = (ImageButton)rowView.findViewById(R.id.possibilities_selection);
		if(position == context.getSelectedItem()){
			selection.setImageResource(context.getResources().getIdentifier(
					"selected", "drawable", context.getPackageName()));
		}else{
			selection.setImageResource(context.getResources().getIdentifier(
					"nonselected", "drawable", context.getPackageName()));
		}
		selection.setTag(position);
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

			for (int i=0; i<combination.getTiles().size(); i++) {
				
				Tile tile = combination.getTiles().get(i);
				
				// Create the tile button to add to the view
				ImageButton imgButton = new ImageButton(context);
				imgButton.setImageResource(context.getResources().getIdentifier(
						tile.getImg(), "drawable", context.getPackageName()));
				imgButton.setTag(tile.getImg());

				int padding = AndroidUtils.fromDpToPixels(AndroidUtils.COMBINATION_TILE_PADDING,
						context.getResources().getDisplayMetrics().density);
				
				imgButton.setPadding((i==0?padding:0), padding, (i==(combination.getTiles().size()-1)?padding:0), padding);

				// This test is necessary because of compatibility reason...
				if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
					imgButton.setBackgroundDrawable(null);
				} else {
					imgButton.setBackground(null);
				}
				imgButton.setId(tile.getId());
				
				// Get the player layout
				LinearLayout playerLayout = (LinearLayout) (tile.getIsVisible() ? rowView.findViewById(R.id.possibilities_player_open_tiles)
						: rowView.findViewById(R.id.possibilities_player_hidden_tiles));

				// Add the tile button to the player list
				playerLayout.addView(imgButton, paramsLO);
			}
		}
	} 