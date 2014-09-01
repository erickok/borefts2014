package nl.brouwerijdemolen.borefts2013.gui;

import im.delight.apprater.AppRater;
import nl.brouwerijdemolen.borefts2013.R;
import nl.brouwerijdemolen.borefts2013.api.Beer;
import nl.brouwerijdemolen.borefts2013.api.Brewer;
import nl.brouwerijdemolen.borefts2013.api.Style;
import nl.brouwerijdemolen.borefts2013.gui.fragments.AboutDialog_;
import nl.brouwerijdemolen.borefts2013.gui.fragments.BafFragment;
import nl.brouwerijdemolen.borefts2013.gui.fragments.BafFragment_;
import nl.brouwerijdemolen.borefts2013.gui.fragments.BeerFragment_;
import nl.brouwerijdemolen.borefts2013.gui.fragments.BrewerFragment_;
import nl.brouwerijdemolen.borefts2013.gui.fragments.BrewersFragment;
import nl.brouwerijdemolen.borefts2013.gui.fragments.BrewersFragment_;
import nl.brouwerijdemolen.borefts2013.gui.fragments.InfoFragment;
import nl.brouwerijdemolen.borefts2013.gui.fragments.InfoFragment_;
import nl.brouwerijdemolen.borefts2013.gui.fragments.MapFragment;
import nl.brouwerijdemolen.borefts2013.gui.fragments.MapFragment_;
import nl.brouwerijdemolen.borefts2013.gui.fragments.StarredFragment;
import nl.brouwerijdemolen.borefts2013.gui.fragments.StarredFragment_;
import nl.brouwerijdemolen.borefts2013.gui.fragments.StyleFragment_;
import nl.brouwerijdemolen.borefts2013.gui.fragments.StylesFragment;
import nl.brouwerijdemolen.borefts2013.gui.fragments.StylesFragment_;
import nl.brouwerijdemolen.borefts2013.gui.fragments.TwitterFragment;
import nl.brouwerijdemolen.borefts2013.gui.fragments.TwitterFragment_;
import nl.brouwerijdemolen.borefts2013.gui.helpers.MolenTypefaceSpan;
import nl.brouwerijdemolen.borefts2013.gui.helpers.NavigationManager;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.res.BooleanRes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.view.Menu;
import com.mapsaurus.paneslayout.PanesActivity;
import com.mapsaurus.paneslayout.PanesSizer.PaneSizer;

@EActivity
@OptionsMenu(R.menu.activity_start)
public class TabletActivity extends PanesActivity implements TabListener, NavigationManager {

	@BooleanRes
	protected boolean fitsThreePanes;

	private MapFragment mapFragment = null;
	private InfoFragment infoFragment = null;
	private TwitterFragment twitterFragment = null;
	private BrewersFragment brewersFragment = null;
	private StylesFragment stylesFragment = null;
	private StarredFragment starredFragment = null;
	private BafFragment bafFragment = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set up an action bar and navigation tabs
		getSupportActionBar().setTitle(MolenTypefaceSpan.makeMolenSpannable(this, getString(R.string.app_name_short)));
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		getSupportActionBar().addTab(
				getSupportActionBar().newTab().setText(R.string.action_info).setTabListener(this).setTag("info"));
		getSupportActionBar().addTab(
				getSupportActionBar().newTab().setText(R.string.action_brewers).setTabListener(this).setTag("brewers"));
		getSupportActionBar().addTab(
				getSupportActionBar().newTab().setText(R.string.action_styles).setTabListener(this).setTag("styles"));
		if (!fitsThreePanes)
			getSupportActionBar().addTab(
					getSupportActionBar().newTab().setText(R.string.action_twitter).setTabListener(this)
							.setTag("twitter"));
		getSupportActionBar().addTab(
				getSupportActionBar().newTab().setText(R.string.info_stars).setTabListener(this).setTag("stars"));
		getSupportActionBar().addTab(
				getSupportActionBar().newTab().setText(R.string.info_baf).setTabListener(this).setTag("baf"));

		// Set up panes layout and load the first
		setPaneSizer(new ExamplePaneSizer());
		mapFragment = MapFragment_.builder().isMinimap(false).build();
		setMenuFragment(mapFragment);
		infoFragment = InfoFragment_.builder().build();
		addFragment(mapFragment, infoFragment);
		if (fitsThreePanes) {
			twitterFragment = TwitterFragment_.builder().build();
			addFragment(infoFragment, twitterFragment);
		}

		AppRater appRater = new AppRater(this, this.getPackageName());
		appRater.setDaysBeforePrompt(0);
		appRater.setLaunchesBeforePrompt(5);
		appRater.setPhrases(R.string.rate_title, R.string.rate_explanation, R.string.rate_now, R.string.rate_later,
				R.string.rate_never);
		appRater.show();
		
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		boolean twitterShown;
		if (fitsThreePanes)
			twitterShown = getSupportActionBar().getSelectedTab().getTag().equals("info");
		else
			twitterShown = getSupportActionBar().getSelectedTab().getTag().equals("twitter");
		menu.findItem(R.id.action_refresh).setVisible(twitterShown);
		return true;
	}

	@OptionsItem
	protected void actionRefresh() {
		if (twitterFragment != null)
			twitterFragment.refreshTwitterFeed();
	}

	@OptionsItem
	protected void actionSettings() {
	}

	@OptionsItem
	protected void actionSendcorrection() {
		Intent startEmail = new Intent(Intent.ACTION_SEND);
		startEmail.setType("message/rfc822");
		startEmail.putExtra(Intent.EXTRA_EMAIL, new String[] { "borefts2013@2312.nl" });
		startEmail.putExtra(Intent.EXTRA_SUBJECT, "Borefts 2013 Android app correction");
		startActivity(startEmail);
	}

	@OptionsItem
	protected void actionAbout() {
		AboutDialog_.builder().build().show(getSupportFragmentManager(), "about");
	}

	private class ExamplePaneSizer implements PaneSizer {

		@Override
		public int getWidth(int index, int type, int parentWidth, int parentHeight) {
			if (fitsThreePanes)
				return (int) (0.334 * parentWidth);
			return (int) (0.5 * parentWidth);
		}

		@Override
		public int getType(Object o) {
			return 0;
		}

		@Override
		public boolean getFocused(Object o) {
			// Allow certain fragments to steal the focus, even when not on top
			if (o instanceof MapFragment)
				return true;
			return false;
		}

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if (tab.getTag().equals("info")) {
			addFragment(mapFragment, infoFragment);
			if (fitsThreePanes)
				addFragment(infoFragment, twitterFragment);
		} else if (tab.getTag().equals("brewers")) {
			if (brewersFragment == null)
				brewersFragment = BrewersFragment_.builder().build();
			addFragment(mapFragment, brewersFragment);
		} else if (tab.getTag().equals("styles")) {
			if (stylesFragment == null)
				stylesFragment = StylesFragment_.builder().build();
			addFragment(mapFragment, stylesFragment);
		} else if (tab.getTag().equals("twitter")) {
			if (twitterFragment == null)
				twitterFragment = TwitterFragment_.builder().build();
			addFragment(mapFragment, twitterFragment);
		} else if (tab.getTag().equals("stars")) {
			if (starredFragment == null)
				starredFragment = StarredFragment_.builder().build();
			addFragment(mapFragment, starredFragment);
		} else if (tab.getTag().equals("baf")) {
			if (bafFragment == null)
				bafFragment = BafFragment_.builder().build();
			addFragment(mapFragment, bafFragment);
		}
		supportInvalidateOptionsMenu();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// No need to do anything
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		if (tab.getTag().equals("info")) {
			addFragment(mapFragment, infoFragment);
		} else if (tab.getTag().equals("brewers")) {
			addFragment(mapFragment, brewersFragment);
		} else if (tab.getTag().equals("styles")) {
			addFragment(mapFragment, stylesFragment);
		}
	}

	@Override
	public void openBrewer(Fragment baseFragment, Brewer brewer) {
		addFragment(baseFragment, BrewerFragment_.builder().brewer(brewer).build());
	}

	@Override
	public void openStyle(Fragment baseFragment, Style style) {
		addFragment(baseFragment, StyleFragment_.builder().style(style).build());
	}

	@Override
	public void openBeer(Fragment baseFragment, Beer beer) {
		addFragment(baseFragment, BeerFragment_.builder().beer(beer).build());
	}

	@Override
	public void openMap(Fragment baseFragment, int focusId, Brewer brewerToOpen) {
		// Don't add a new fragment, but re-focus
		mapFragment.focusOnMarker(focusId);
		addFragment(baseFragment, BrewerFragment_.builder().brewer(brewerToOpen).build());
	}

}
