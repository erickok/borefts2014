package nl.brouwerijdemolen.borefts2013.api;

import nl.brouwerijdemolen.borefts2013.R;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

public class Beer implements Comparable<Beer>, Parcelable {

	private int id;
	private String name;
	private int brewerId;
	private int styleId;
	private float abv;
	private boolean oakAged;
	private String tags;
	private int ratebeerId;
	private int untappdId;
	private int serving;
	private int color;
	private int body;
	private int bitterness;
	private int sweetness;
	private int acidity;

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

	public String getTags() {
		return tags;
	}

	public int getRatebeerId() {
		return ratebeerId;
	}

	public int getUntappdId() {
		return untappdId;
	}

	public int getServing() {
		return serving;
	}

	public int getColor() {
		return color;
	}

	public int getBody() {
		return body;
	}

	public int getBitterness() {
		return bitterness;
	}

	public int getSweetness() {
		return sweetness;
	}

	public int getAcidity() {
		return acidity;
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

	public int getAbvIndication() {
		if (abv >= 0) {
			return Math.min(Math.max((int) Math.round(abv / 2.2), 1), 5);
		}
		return style.getAbv();
	}

	public int getBitternessIndication() {
		if (bitterness >= 0) {
			return bitterness;
		}
		return style.getBitterness();
	}

	public int getSweetnessIndication() {
		if (sweetness >= 0) {
			return sweetness;
		}
		return style.getSweetness();
	}

	public int getAcidityIndication() {
		if (acidity >= 0) {
			return acidity;
		}
		return style.getAcidity();
	}

	public int getColorIndicationResource(Resources res) {
		int c = style.getColor();
		if (color >= 0) {
			c = color;
		}
		switch (c) {
		case 1:
			return res.getColor(R.color.style_1);
		case 2:
			return res.getColor(R.color.style_2);
		case 3:
			return res.getColor(R.color.style_3);
		case 4:
			return res.getColor(R.color.style_4);
		case 5:
			return res.getColor(R.color.style_5);
		default:
			return res.getColor(R.color.style_unknown);
		}
	}

	public String getServingResource(Resources res) {
		switch (serving) {
		case 1:
			return res.getString(R.string.info_serving_cask);
		case 2:
			return res.getString(R.string.info_serving_bottle);
		default:
			return res.getString(R.string.info_serving_keg);
		}
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
		this.tags = in.readString();
		this.ratebeerId = in.readInt();
		this.untappdId = in.readInt();
		this.serving = in.readInt();
		this.color = in.readInt();
		this.body = in.readInt();
		this.bitterness = in.readInt();
		this.sweetness = in.readInt();
		this.acidity = in.readInt();
		this.brewer = in.readParcelable(Brewer.class.getClassLoader());
		this.style = in.readParcelable(Style.class.getClassLoader());
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
		dest.writeInt(oakAged ? 1 : 0);
		dest.writeString(tags);
		dest.writeInt(ratebeerId);
		dest.writeInt(untappdId);
		dest.writeInt(serving);
		dest.writeInt(color);
		dest.writeInt(body);
		dest.writeInt(bitterness);
		dest.writeInt(sweetness);
		dest.writeInt(acidity);
		dest.writeParcelable(brewer, flags);
		dest.writeParcelable(style, flags);
	}

}
