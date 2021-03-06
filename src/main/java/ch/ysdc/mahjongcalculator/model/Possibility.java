package ch.ysdc.mahjongcalculator.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Possibility implements Parcelable, Serializable,Comparable {


	private static final long serialVersionUID = 7620094326864403955L;

	private static String TAG = "Possibility";
	
	private static int counter = 0;
	private int id;
	private Validity validity;
	private List<Tile> unusedTiles;
	private List<Combination> combinations;
	private Combination unusedTileCombination;
	private Combination pair;
	private Integer value;
	

	/*****************************************
	CONSTRUCTOR
	*****************************************/
	public Possibility(Possibility p){
		id = counter++;
		validity = p.validity;
		unusedTiles = new CopyOnWriteArrayList<Tile>(p.unusedTiles);
		combinations =  new CopyOnWriteArrayList<Combination>(p.combinations);
		unusedTileCombination = new Combination(p.unusedTileCombination);
		pair =  p.getPair();
	}
	
	public Possibility(List<Tile> t, List<Combination> c){
		id = counter++;
		validity = Validity.VALID;	
		pair = null;
		unusedTiles = (t != null ? new CopyOnWriteArrayList<Tile>(t) : new CopyOnWriteArrayList<Tile>());
		combinations = (c != null ? new CopyOnWriteArrayList<Combination>(c) : new CopyOnWriteArrayList<Combination>());
		unusedTileCombination = new Combination();
	}

	public Possibility(Parcel in) {
		id = in.readInt();
		validity = in.readParcelable(Validity.class.getClassLoader());
		pair = in.readParcelable(Combination.class.getClassLoader());

		combinations = new LinkedList<Combination>();
		in.readTypedList(combinations, Combination.CREATOR);
		unusedTileCombination = in.readParcelable(Combination.class.getClassLoader());
		unusedTiles = new LinkedList<Tile>();
		in.readTypedList(unusedTiles, Tile.CREATOR);
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
	
	
	public List<Tile> getUnusedTiles() {
		return unusedTiles;
	}

	public Combination getUnusedTileCombination() {
		return unusedTileCombination;
	}

	public void setUnusedTileCombination(Combination unusedTileCombination) {
		this.unusedTileCombination = unusedTileCombination;
	}

	public Validity getValidity() {
		return validity;
	}

	public void setValidity(Validity validity) {
		this.validity = validity;
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

	public static int getCounter() {
		return counter;
	}

	public static void setCounter(int counter) {
		Possibility.counter = counter;
	}
	@Override
	public String toString(){
		return this.getId() + " (" + this.getUnusedTiles().size() + ", " + this.getCombinations().size() + ")";
	}


	/*****************************************
	 OWn Method
	*****************************************/
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

			if(p.getId()==getId()){
				Log.d(TAG, "Same ID");
				continue;
			}
			List<Combination> cpList = new CopyOnWriteArrayList<Combination>(p.getCombinations());
			
			boolean samePossibility = true;
			//Loop through all combination
			for(Combination c : this.getCombinations()){
				boolean sameCombination = false;

				//for each combination of the possibility
				for(Combination cp : cpList){

					//if it's the same combination
					if((c.getType() == cp.getType()) && (c.getRepresentation().equals(cp.getRepresentation()))){
						//We leave the combination loop
						sameCombination = true;
						cpList.remove(cp);
						break;
					}
				}
				//If the combination was
				if(!sameCombination){
					//We set the possibility as a different
					samePossibility = false;
					//we leave the combination loop
					break;
				}
				
			}
			if(samePossibility && (this.samePair(p.getPair()))){
				return true;
			}
		}
		return false;
	}

	public Integer getValue(){
		if(value== null){
			value = 0;
			for(Combination c : getCombinations()){
				value += c.getType().getValue();
			}
		}
		return value;
	}
	
	public boolean samePair(Combination pair2) {
		if(pair == null){
			if(pair2 == null){
				return true;
			}
			return false;
		}else if(pair2 == null){
			return false;
		}
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
		out.writeParcelable(validity, flag);
		out.writeParcelable(pair, flag);
		out.writeTypedList(combinations);
		out.writeParcelable(unusedTileCombination, flag);
		out.writeTypedList(unusedTiles);
	}
	public static final Parcelable.Creator<Possibility> CREATOR = new Parcelable.Creator<Possibility>() {
        public Possibility createFromParcel(Parcel in) {
            return new Possibility(in);
        }

        public Possibility[] newArray(int size) {
            return new Possibility[size];
        }
    };


	@Override
	public int compareTo(Object p) {
		int val = getValue().compareTo(((Possibility)p).getValue());
		if(val == 0){
			return Integer.valueOf(this.getId()).compareTo(Integer.valueOf(((Possibility)p).getId()));
		}
		return val;
	}
}
