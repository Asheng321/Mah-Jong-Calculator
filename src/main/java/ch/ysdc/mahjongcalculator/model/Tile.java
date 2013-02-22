/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.ysdc.mahjongcalculator.model;

import java.io.Serializable;
import java.util.regex.Pattern;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/*
 *
 * @author djohannot
 */
@DatabaseTable
public class Tile implements Comparable<Tile>, Parcelable, Serializable {

	private static final long serialVersionUID = 2906435803297285109L;
	private static int counter = 0;
	public static final String ID_FIELD_NAME = "id";
	public static final String NO_FIELD_NAME = "no";
	public static final String IMG_FIELD_NAME = "img";
	public static final String CATEGORY_FIELD_NAME = "category";
	public static final String HAND_FIELD_NAME = "hand";
	public static final String ISVISIBLE_TILE_FIELD_NAME = "isVisible";
	public static final String GAME_FIELD_NAME = "game";

	@DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
	private int id;
	@DatabaseField(columnName = NO_FIELD_NAME)
	private int no;
	@DatabaseField(columnName = IMG_FIELD_NAME)
	private String img;
	@DatabaseField(columnName = ISVISIBLE_TILE_FIELD_NAME)
	private Boolean isVisible;
	@DatabaseField(columnName = CATEGORY_FIELD_NAME)
	private Category category;
	@DatabaseField(foreign = true, columnName = HAND_FIELD_NAME)
	private Hand hand;

	public Tile() {
		super();
		this.id = counter++;
	}

	public Tile(int n, String i, Category c) {
		super();
		this.id = counter++;
		this.no = n;
		this.img = i;
		this.category = c;
		this.isVisible = false;
	}

	public Tile(String n, String i, Category c) {
		super();
		this.id = counter++;
		if (Pattern.matches("[0-9]{1}", n)) {
			this.no = Integer.valueOf(n);
		} else {
			this.no = 0;
		}
		this.img = i;
		this.category = c;
		this.isVisible = false;
	}

	public Tile(Parcel in) {
		id = in.readInt();
		no = in.readInt();
		img = in.readString();
		isVisible = Boolean.parseBoolean(in.readString());
		category = in.readParcelable(Category.class.getClassLoader());
		hand = in.readParcelable(Hand.class.getClassLoader());
	}

	/*****************************************
	 * STATIC COUNTER ACCESSOR
	 *****************************************/
	public static void resetCounter() {
		counter = 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Category getCategory() {
		return category;
	}

	public Boolean getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(Boolean isVisible) {
		this.isVisible = isVisible;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Hand getHand() {
		return hand;
	}

	public void setHand(Hand hand) {
		this.hand = hand;
	}

	@Override
	public String toString() {
		return "Tile ("
				+ id
				+ ") "
				+ no
				+ category
				+ " "
				+ (isVisible ? "Hidden" : "Hand" + " #"
						+ (hand != null ? "handy" : "-"));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		result = prime * result + id;
		result = prime * result + ((img == null) ? 0 : img.hashCode());
		result = prime * result
				+ ((isVisible == null) ? 0 : isVisible.hashCode());
		result = prime * result + no;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tile other = (Tile) obj;
		if (category != other.category)
			return false;
		if (id != other.id)
			return false;
		if (img == null) {
			if (other.img != null)
				return false;
		} else if (!img.equals(other.img))
			return false;
		if (isVisible == null) {
			if (other.isVisible != null)
				return false;
		} else if (!isVisible.equals(other.isVisible))
			return false;
		if (no != other.no)
			return false;
		return true;
	}

	public enum Category implements Parcelable {

		BAMBOO("b"), CHARACTER("k"), CIRCLE("c"), WIND("w"), DRAGON("d"), FLOWER(
				"f"), SEASON("s");

		private String value;

		public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {

			public Category createFromParcel(Parcel in) {
				return Category.values()[in.readInt()];
			}

			public Category[] newArray(int size) {
				return new Category[size];
			}

		};

		Category(String v) {
			value = v;
		}

		@Override
		public String toString() {
			return value;
		}

		public static Category fromValue(String v) {
			if (v != null) {
				for (Category c : Category.values()) {
					if (v.equalsIgnoreCase(c.value)) {
						return c;
					}
				}
			}
			throw new IllegalArgumentException("No constant with value " + v
					+ " found");
		}

		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeInt(ordinal());
		}
	}

	@Override
	public int compareTo(Tile t) {
		return this.img.compareTo(t.img);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(id);
		out.writeInt(no);
		out.writeString(img);
		out.writeString(isVisible.toString());
		out.writeParcelable(category, flags);
		out.writeParcelable(hand, flags);
	}

	public static final Parcelable.Creator<Tile> CREATOR = new Parcelable.Creator<Tile>() {
		public Tile createFromParcel(Parcel in) {
			return new Tile(in);
		}

		public Tile[] newArray(int size) {
			return new Tile[size];
		}
	};
}