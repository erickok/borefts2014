package nl.brouwerijdemolen.borefts2013.api;

import java.util.List;

public class Brewers {

	public static final String BREWERS_URL = "http://62.212.65.175/borefts2013/brewers.php";

	private List<Brewer> brewers;
	private int revision;

	public List<Brewer> getBrewers() {
		return brewers;
	}

	public int getRevision() {
		return revision;
	}

}
