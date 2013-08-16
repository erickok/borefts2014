package nl.brouwerijdemolen.borefts2013.gui.helpers;

import android.support.v4.app.Fragment;

/**
 * Interface for activities that manage the opening and closing of fragments, that is, the phone and tablet main
 * activities.
 * @author Eric Kok <eric@2312.nl>
 */
public interface NavigationManager {

	/**
	 * Open a brewer details (and beer list) fragment for the given id.
	 * @param baseFragment The fragment that requests the opening of a new brewer fragment
	 * @param brewerId The id of the brewer to show details (and beer list) for
	 */
	void openBrewer(Fragment baseFragment, int brewerId);

	/**
	 * Open a style details (and beer list) fragment for the given id.
	 * @param baseFragment The fragment that requests the opening of a new brewer fragment
	 * @param styleId The id of the style to show details (and beer list) for
	 */
	void openStyle(Fragment baseFragment, int styleId);

	/**
	 * Open a beer details fragment for the given id.
	 * @param baseFragment The fragment that requests the opening of a new beer fragment
	 * @param beerId The id of the beer to show details for
	 */
	void openBeer(Fragment baseFragment, int beerId);

}
