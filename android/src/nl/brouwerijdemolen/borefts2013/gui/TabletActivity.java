package nl.brouwerijdemolen.borefts2013.gui;

import nl.brouwerijdemolen.borefts2013.R;
import nl.brouwerijdemolen.borefts2013.api.Beer;
import nl.brouwerijdemolen.borefts2013.api.Brewer;
import nl.brouwerijdemolen.borefts2013.api.Style;
import nl.brouwerijdemolen.borefts2013.gui.fragments.*;
import nl.brouwerijdemolen.borefts2013.gui.helpers.MolenTypefaceSpan;
import nl.brouwerijdemolen.borefts2013.gui.helpers.NavigationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.InstanceState;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.res.BooleanRes;
import com.mapsaurus.paneslayout.PanesActivity;
import com.mapsaurus.paneslayout.PanesSizer.PaneSizer;

@EActivity
@OptionsMenu(R.menu.home)
public class TabletActivity extends PanesActivity implements TabListener, NavigationManager {

	@BooleanRes
	protected boolean fitsThreePanes;
	@InstanceState
	protected String lastUsedTab = "info";
	
	private MapFragment mapFragment = null;
	private InfoFragment infoFragment = null;
	private TwitterFragment twitterFragment = null;
	private BrewersFragment brewersFragment = null;
	private StylesFragment stylesFragment = null;

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
			
        // Set up panes layout and load the first
        setPaneSizer(new ExamplePaneSizer());
        mapFragment = MapFragment_.builder().isMinimap(false).build();
        if (lastUsedTab.equals("info")) {
	        infoFragment = InfoFragment_.builder().build();
	        twitterFragment = TwitterFragment_.builder().build();
	        setMenuFragment(mapFragment);
	        addFragment(mapFragment, infoFragment);
	        if (fitsThreePanes)
	        	addFragment(infoFragment, twitterFragment);
        } else if (lastUsedTab.equals("brewers")) {
	        brewersFragment = BrewersFragment_.builder().build();
	        addFragment(mapFragment, brewersFragment);
	        if (fitsThreePanes)
	        	addFragment(brewersFragment, BeerFragment_.builder().build()); // Will be empty
        } else if (lastUsedTab.equals("styles")) {
        	stylesFragment = StylesFragment_.builder().build();
	        addFragment(mapFragment, stylesFragment);
	        if (fitsThreePanes)
	        	addFragment(stylesFragment, StyleFragment_.builder().build()); // Will be empty
        }
        
    }

	@OptionsItem
	protected void actionSettingsSelected() {
		// TODO: Start settings activity
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
		lastUsedTab = (String) tab.getTag();
		if (tab.getTag().equals("info")) {
			addFragment(mapFragment, infoFragment);
		} else if (tab.getTag().equals("brewers")) {
			if (brewersFragment == null)
				brewersFragment = BrewersFragment_.builder().build();
			addFragment(mapFragment, brewersFragment);
		} else if (tab.getTag().equals("styles")) {
			if (stylesFragment == null)
				stylesFragment = StylesFragment_.builder().build();
			addFragment(mapFragment, stylesFragment);
		}
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
	public void openMap(Fragment baseFragment, int focusId) {
		// Don't add a new fragment, but re-focus
		mapFragment.focusOnMarker(focusId);
	}
	
}
