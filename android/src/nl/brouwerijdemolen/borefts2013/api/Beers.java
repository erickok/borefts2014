package nl.brouwerijdemolen.borefts2013.api;

import java.util.List;

public class Beers {

	public static final String BEERS_URL = "http://2312.nl/borefts2014/beers.php";

	private List<Beer> beers;
	private int revision;

	public List<Beer> getBeers() {
		return beers;
	}

	public int getRevision() {
		return revision;
	}

}
