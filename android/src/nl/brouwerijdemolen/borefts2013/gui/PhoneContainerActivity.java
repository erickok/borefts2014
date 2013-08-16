package nl.brouwerijdemolen.borefts2013.gui;

import nl.brouwerijdemolen.borefts2013.R;
import nl.brouwerijdemolen.borefts2013.gui.fragments.BeerFragment_;
import nl.brouwerijdemolen.borefts2013.gui.fragments.BrewerFragment_;
import nl.brouwerijdemolen.borefts2013.gui.fragments.StyleFragment_;
import nl.brouwerijdemolen.borefts2013.gui.helpers.NavigationManager;
import android.support.v4.app.Fragment;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;

/**
 * A wrapper activity that can open details screens for brewers, beers and styles by supplying the appropriate Extra.
 * The actual content is handled by the normal fragment for whatever content was requested.
 * @author Eric Kok <eric@2312.nl>
 */
@EActivity(R.layout.activity_empty)
public class PhoneContainerActivity extends SherlockFragmentActivity implements NavigationManager {

	@Extra
	protected Integer brewerId = null;
	@Extra
	protected Integer styleId = null;
	@Extra
	protected Integer beerId = null;

	@AfterViews
	protected void openFragment() {
		
		// Get the fragment to open based on the supplied Extra
		Fragment fragment = null;
		if (brewerId != null) {
			fragment = BrewerFragment_.builder().brewerId(brewerId).build();
		} else if (brewerId != null) {
			fragment = StyleFragment_.builder().styleId(styleId).build();
		} else if (brewerId != null) {
			fragment = BeerFragment_.builder().beerId(beerId).build();
		}
		if (fragment == null) {
			throw new IllegalArgumentException("Don't know which fragment to open, since no Extra was specified.");
		}
		
		// Replace the activity contents with the new fragment
		getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
		
	}

	@Override
	public void openBrewer(Fragment baseFragment, int brewerId) {
		PhoneContainerActivity_.intent(this).brewerId(brewerId).start();
	}

	@Override
	public void openStyle(Fragment baseFragment, int styleId) {
		PhoneContainerActivity_.intent(this).styleId(styleId).start();
	}

	@Override
	public void openBeer(Fragment baseFragment, int beerId) {
		PhoneContainerActivity_.intent(this).beerId(beerId).start();
	}
	
}
