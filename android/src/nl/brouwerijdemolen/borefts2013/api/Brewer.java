package nl.brouwerijdemolen.borefts2013.api;

import android.os.Parcel;
import android.os.Parcelable;

public class Brewer implements Comparable<Brewer>, Parcelable {

	public static final String BREWER_LOGO_BASE_URL = "http://2312.nl/borefts2013/logos/%s";

	private int id;
	private String code;
	private String logoUrl;
	private String name;
	private String sortName;
	private String city;
	private String country;
	private String description;
	private float latitude;
	private float longitude;

	protected Brewer() {
	}
	
	public int getId() {
		return id;
	}
	
	public String getCode() {
		return code;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public String getName() {
		return name;
	}

	public String getSortName() {
		return sortName;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public String getDescription() {
		return description;
	}
	
	public float getLatitude() {
		return latitude;
	}
	
	public float getLongitude() {
		return longitude;
	}

	public String getLogoFullUrl() {
		return String.format(BREWER_LOGO_BASE_URL, logoUrl);
	}

	@Override
	public int compareTo(Brewer another) {
		return sortName.compareTo(another.getSortName());
	}

	public Brewer(Parcel in) {
		this.id = in.readInt();
		this.code = in.readString();
		this.logoUrl = in.readString();
		this.name = in.readString();
		this.sortName = in.readString();
		this.city = in.readString();
		this.country = in.readString();
		this.description = in.readString();
		this.latitude = in.readFloat();
		this.longitude = in.readFloat();
	}
	
    public static final Parcelable.Creator<Brewer> CREATOR = new Parcelable.Creator<Brewer>() {
    	public Brewer createFromParcel(Parcel in) {
    		return new Brewer(in);
    	}

		public Brewer[] newArray(int size) {
		    return new Brewer[size];
		}
    };

	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(code);
		dest.writeString(logoUrl);
		dest.writeString(name);
		dest.writeString(sortName);
		dest.writeString(city);
		dest.writeString(country);
		dest.writeString(description);
		dest.writeFloat(latitude);
		dest.writeFloat(longitude);
	}
	
}
