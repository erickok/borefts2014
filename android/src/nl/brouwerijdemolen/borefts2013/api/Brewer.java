package nl.brouwerijdemolen.borefts2013.api;

public class Brewer implements Comparable<Brewer> {

	public static final String BREWER_LOGO_BASE_URL = "http://2312.nl/borefts2013/logos/%s";

	private int id;
	private String code;
	private String logoUrl;
	private String name;
	private String sortName;
	private String city;
	private String country;
	private String description;

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

	public String getLogoFullUrl() {
		return String.format(BREWER_LOGO_BASE_URL, logoUrl);
	}

	@Override
	public int compareTo(Brewer another) {
		return sortName.compareTo(another.getSortName());
	}

}
