package nl.brouwerijdemolen.borefts2013.api;

import android.os.Parcel;
import android.os.Parcelable;

public class Beer implements Comparable<Beer>, Parcelable {

	private int id;
	private String name;
	private int brewerId;
	private int styleId;
	private float abv;
	private boolean oakAged;
	private int ratebeerId;
	
	// These are not provided by the server, but loaded manually and attached with their setters
	private volatile Brewer brewer;
	private volatile Style style;

	protected Beer() {
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public int getBrewerId() {
		return brewerId;
	}

	public int getStyleId() {
		return styleId;
	}

	public float getAbv() {
		return abv;
	}

	public boolean isOakAged() {
		return oakAged;
	}

	public int getRatebeerId() {
		return ratebeerId;
	}
	
	public void setBrewer(Brewer brewer) {
		this.brewer = brewer;
	}
	
	public Brewer getBrewer() {
		return brewer;
	}
	
	public void setStyle(Style style) {
		this.style = style;
	}
	
	public Style getStyle() {
		return style;
	}

	@Override
	public int compareTo(Beer another) {
		return name.compareTo(another.getName());
	}

	public Beer(Parcel in) {
		this.id = in.readInt();
		this.name = in.readString();
		this.brewerId = in.readInt();
		this.styleId = in.readInt();
		this.abv = in.readFloat();
		this.oakAged = in.readInt() == 1;
		this.ratebeerId = in.readInt();
	}
	
    public static final Parcelable.Creator<Beer> CREATOR = new Parcelable.Creator<Beer>() {
    	public Beer createFromParcel(Parcel in) {
    		return new Beer(in);
    	}

		public Beer[] newArray(int size) {
		    return new Beer[size];
		}
    };

	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeInt(brewerId);
		dest.writeInt(styleId);
		dest.writeFloat(abv);
		dest.writeInt(oakAged? 1: 0);
		dest.writeInt(ratebeerId);
	}
	
}
