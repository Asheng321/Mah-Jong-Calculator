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
import ch.ysdc.mahjongcalculator.calculation.Possibility;

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
    public static final String COMBO_FIELD_NAME = "combinations";
    
    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int id;
    @DatabaseField(columnName = NAME_FIELD_NAME)
    private String name;
    @ForeignCollectionField(columnName = COMBO_FIELD_NAME)
    private ForeignCollection<Combination> storedCombinations;

    private List<Combination> combinations;
    
    public Hand(){
    	super();
    	combinations = new LinkedList<Combination>();
    }
    public Hand(String name) {
		super();
		this.name = name;
    	combinations = new LinkedList<Combination>();
	}

	public Hand(Parcel in) {
		id = in.readInt();
		name = in.readString();
		combinations = new LinkedList<Combination>();
		in.readTypedList(combinations, Combination.CREATOR);
	}
	public Hand(Possibility possibility) {
		combinations = possibility.getCombinations();
		combinations.add(possibility.getPair());
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
