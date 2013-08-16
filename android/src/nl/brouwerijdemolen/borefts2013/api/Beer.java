package nl.brouwerijdemolen.borefts2013.api;

public class Beer implements Comparable<Beer> {

	public Beer() {
	}

	private String name;
	private int brewerId;
	private int styleId;
	private float abv;
	private boolean oakAged;
	private int ratebeerId;

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

	@Override
	public int compareTo(Beer another) {
		return name.compareTo(another.getName());
	}

}
