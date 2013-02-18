package ch.ysdc.mahjongcalculator.calculation;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import ch.ysdc.mahjongcalculator.model.Combination;
import ch.ysdc.mahjongcalculator.model.Tile;

public class Possibility implements Parcelable {
	private static String TAG = "Possibility";
	
	private static int counter = 0;
	private int id;
	private boolean isValid;
	private List<Tile> unusedTiles;
	private List<Combination> combinations;
	private Combination pair;
	

	/*****************************************
	CONSTRUCTOR
	*****************************************/
	Possibility(Possibility p){
		id = counter++;
		isValid = p.isValid;
		unusedTiles = new CopyOnWriteArrayList<Tile>(p.unusedTiles);
		combinations =  new CopyOnWriteArrayList<Combination>(p.combinations);
		pair =  p.getPair();
	}
	
	Possibility(List<Tile> t, List<Combination> c){
		id = counter++;
		isValid = true;
		
		pair = null;
		unusedTiles = (t != null ? new CopyOnWriteArrayList<Tile>(t) : new CopyOnWriteArrayList<Tile>());
		combinations = (c != null ? new CopyOnWriteArrayList<Combination>(c) : new CopyOnWriteArrayList<Combination>());
	}

	public Possibility(Parcel in) {
		id = in.readInt();
		isValid = Boolean.valueOf(in.readString());
		pair = in.readParcelable(Combination.class.getClassLoader());

		combinations = new LinkedList<Combination>();
		in.readTypedList(combinations, Combination.CREATOR);
	}

	/*****************************************
	STATIC COUNTER ACCESSOR
	*****************************************/
	public static void resetCounter(){
		counter = 0;
	}

	/*****************************************
	ADD ELEMENT
	*****************************************/
	public void addCombinations(Combination c){
		combinations.add(c);
	}
	public void addCombinations(LinkedList<Combination> c){
		combinations.addAll(c);
	}
	
	public void addTile(Tile t){
		unusedTiles.add(t);
	}
	/*****************************************
	GETTER AND SETTER 
	*****************************************/

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	
	public List<Tile> getUnusedTiles() {
		return unusedTiles;
	}

	public void setUnusedTiles(List<Tile> unusedTiles) {
		this.unusedTiles = unusedTiles;
	}

	public List<Combination> getCombinations() {
		return combinations;
	}

	public void setCombinations(List<Combination> combinations) {
		this.combinations = combinations;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Combination getPair() {
		return pair;
	}

	public void setPair(Combination pair) {
		this.pair = pair;
	}

	/*****************************************
	@Override
	*****************************************/
	@Override
	public String toString(){
		return this.getId() + " (" + this.getUnusedTiles().size() + ", " + this.getCombinations().size() + ")";
	}

	public String displayTiles() {
		StringBuffer sb = new StringBuffer("(" + this.getUnusedTiles().size() + ") ");
		for(Tile tile : getUnusedTiles()){
			sb.append(tile.getNo() + "" + tile.getCategory() + tile.getId() + ", ");
		}
		
		return sb.substring(0, sb.length()-1);
	}

	public boolean alreadyListed(List<Possibility> possibilities) {
		Log.d(TAG, "alreadyListed for possibility " + id + "(" + possibilities.size() + ")");
		
		//Compare with all possibilities
		for(Possibility p : possibilities){
			Log.d(TAG, "Compare possibility " + p.getId());
			if(p.getId()==getId()){
				Log.d(TAG, "Same ID");
				continue;
			}
			
			boolean samePossibility = true;
			//Loop through all combination
			for(Combination c : getCombinations()){
				boolean sameCombination = false;
				
				//for each combination of the possibility
				for(Combination cp : p.getCombinations()){
					//if it's the same combination
					if((c.getRepresentation().equals(cp.getRepresentation())) && (c.getType() == cp.getType())){
						//We leave the combination loop
						sameCombination = true;
						break;
					}
				}
				//If the combination was
				if(!sameCombination){
					Log.d(TAG, "unfound combo: " +c.getRepresentation());
					//We set the possibility as a different
					samePossibility = false;
					//we leave the combination loop
					break;
				}
				
			}
			if(samePossibility && (this.samePair(p.getPair()))){
				Log.d(TAG, "samePossibility found");
				return true;
			}
		}
		return false;
	}

	public boolean samePair(Combination pair2) {
		// TODO Auto-generated method stub
		if((pair.getRepresentation().equals(pair2.getRepresentation())) && (pair.getType() == pair2.getType())){
			//We leave the combination loop
			return true;
		}
		return false;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel out, int flag) {
		// TODO Auto-generated method stub
		out.writeInt(id);
		out.writeString(String.valueOf(isValid));
		out.writeParcelable(pair, flag);
		out.writeTypedList(combinations);
	}
	public static final Parcelable.Creator<Possibility> CREATOR = new Parcelable.Creator<Possibility>() {
        public Possibility createFromParcel(Parcel in) {
            return new Possibility(in);
        }

        public Possibility[] newArray(int size) {
            return new Possibility[size];
        }
    };
}
