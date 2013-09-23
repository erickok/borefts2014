package nl.brouwerijdemolen.borefts2013.api;

import java.util.List;

public class Styles {

	public static final String STYLES_URL = "http://62.212.65.175/borefts2013/styles.php";

	private List<Style> styles;
	private int revision;

	public List<Style> getStyles() {
		return styles;
	}

	public int getRevision() {
		return revision;
	}

}
