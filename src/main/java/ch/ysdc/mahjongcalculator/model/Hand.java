/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ysdc.mahjongcalculator.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author djohannot
 */
@DatabaseTable
public class Hand implements Parcelable, Serializable{

	private static final long serialVersionUID = 362476378791796419L;
	public static final String ID_FIELD_NAME = "id";
    public static final String NAME_FIELD_NAME = "name";
    public static final String VALIDITY_FIELD_NAME = "validity";
    public static final String COMBO_FIELD_NAME = "combinations";
    
    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int id;
    @DatabaseField(columnName = NAME_FIELD_NAME)
    private String name;
    @DatabaseField(columnName = VALIDITY_FIELD_NAME)
    private Validity validity;
    @ForeignCollectionField(columnName = COMBO_FIELD_NAME)
    private ForeignCollection<Combination> storedCombinations;

    private List<Combination> combinations;
    
    public Hand(){
    	super();
    	combinations = new LinkedList<Combination>();
    	validity = Validity.VALID;
    }
    public Hand(String name) {
		super();
		this.name = name;
    	combinations = new LinkedList<Combination>();
    	validity = Validity.VALID;
	}

	public Hand(Parcel in) {
		id = in.readInt();
		name = in.readString();
		combinations = new LinkedList<Combination>();
		in.readTypedList(combinations, Combination.CREATOR);
		validity = in.readParcelable(Validity.class.getClassLoader());
	}
	public Hand(Possibility possibility) {
		validity = possibility.getValidity();
		combinations = possibility.getCombinations();
		if(possibility.getPair() != null){
			combinations.add(possibility.getPair());
		}
		if(possibility.getUnusedTileCombination() != null){
			combinations.add(possibility.getUnusedTileCombination());
		}
	}
	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
    public Validity getValidity() {
		return validity;
	}
	public void setValidity(Validity validity) {
		this.validity = validity;
	}
	public ForeignCollection<Combination> getStoredCombinations() {
		return storedCombinations;
	}
	public void setStoredCombinations(
			ForeignCollection<Combination> storedCombinations) {
		this.storedCombinations = storedCombinations;
	}
	public List<Combination> getCombinations() {
		return combinations;
	}
	public void setCombinations(List<Combination> combinations) {
		this.combinations = combinations;
	}
	public void addCombination(Combination c){
		this.combinations.add(c);
    }
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {		
		out.writeInt(id);
		out.writeString(name);
		out.writeTypedList(combinations);
		out.writeParcelable(validity, flags);
	}
	
	public static final Parcelable.Creator<Hand> CREATOR = new Parcelable.Creator<Hand>() {
        public Hand createFromParcel(Parcel in) {
            return new Hand(in);
        }

        public Hand[] newArray(int size) {
            return new Hand[size];
        }
    };
}
