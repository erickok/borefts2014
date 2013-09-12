package nl.brouwerijdemolen.borefts2013.gui;

import nl.brouwerijdemolen.borefts2013.R;
import nl.brouwerijdemolen.borefts2013.api.Beer;
import nl.brouwerijdemolen.borefts2013.api.Brewer;
import nl.brouwerijdemolen.borefts2013.api.Style;
import nl.brouwerijdemolen.borefts2013.gui.fragments.*;
import nl.brouwerijdemolen.borefts2013.gui.helpers.MolenTypefaceSpan;
import nl.brouwerijdemolen.borefts2013.gui.helpers.NavigationManager;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;

@EActivity(R.layout.activity_phone)
@OptionsMenu(R.menu.activity_start)
public class PhoneActivity extends SherlockFragmentActivity implements NavigationManager {

	private ViewPager pager;
	private InfoFragment infoFragment = null;
	private BrewersFragment brewersFragment = null;
	private StylesFragment stylesFragment = null;
	private TwitterFragment twitterFragment = null;

	@AfterViews
	protected void init() {

		// Set up an action bar, navigation tabs and view pager
		getSupportActionBar().setTitle(MolenTypefaceSpan.makeMolenSpannable(this, getString(R.string.app_name_short)));
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		getSupportActionBar().addTab(
				getSupportActionBar().newTab().setText(R.string.action_info).setTabListener(tabListener));
		getSupportActionBar().addTab(
				getSupportActionBar().newTab().setText(R.string.action_brewers).setTabListener(tabListener));
		getSupportActionBar().addTab(
				getSupportActionBar().newTab().setText(R.string.action_styles).setTabListener(tabListener));
		getSupportActionBar().addTab(
				getSupportActionBar().newTab().setText(R.string.action_twitter).setTabListener(tabListener));

		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(new TabsPagerAdapter(getSupportFragmentManager()));
		pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				getSupportActionBar().setSelectedNavigationItem(position);
				supportInvalidateOptionsMenu();
			}
		});

	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		menu.findItem(R.id.action_refresh).setVisible(pager.getCurrentItem() == 3);
		return true;
	}
	
	@OptionsItem
	protected void actionRefresh() {
		if (twitterFragment != null)
			twitterFragment.refreshTwitterFeed();
	}

	@OptionsItem
	protected void actionSettings() {
		// TODO: Start settings activity
	}

	@OptionsItem
	protected void actionSendcorrection() {
		Intent startEmail = new Intent(Intent.ACTION_SEND);
		startEmail.setType("message/rfc822");
		startEmail.putExtra(Intent.EXTRA_EMAIL, "borefts2013@2312.nl");
		startEmail.putExtra(Intent.EXTRA_SUBJECT, "Borefts 2013 Android app correction");
		startActivity(startEmail);
	}

	@OptionsItem
	protected void actionAbout() {
		AboutDialog_.builder().build().show(getSupportFragmentManager(), "about");
	}

	private class TabsPagerAdapter extends FragmentPagerAdapter {

		public TabsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				if (infoFragment == null)
					infoFragment = InfoFragment_.builder().build();
				return infoFragment;
			case 1:
				if (brewersFragment == null)
					brewersFragment = BrewersFragment_.builder().build();
				return brewersFragment;
			case 2:
				if (stylesFragment == null)
					stylesFragment = StylesFragment_.builder().build();
				return stylesFragment;
			case 3:
				if (twitterFragment == null)
					twitterFragment = TwitterFragment_.builder().build();
				return twitterFragment;
			}
			return null;
		}
	}

	private TabListener tabListener = new TabListener() {
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// Simply more the view pager to the appropriate tab
			if (pager != null)
				pager.setCurrentItem(tab.getPosition());
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// No need to do anything
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// No need to do anything
		}
	};

	@Override
	public void openBrewer(Fragment baseFragment, Brewer brewer) {
		PhoneContainerActivity_.intent(this).brewer(brewer).start();
	}

	@Override
	public void openStyle(Fragment baseFragment, Style style) {
		PhoneContainerActivity_.intent(this).style(style).start();
	}

	@Override
	public void openBeer(Fragment baseFragment, Beer beer) {
		PhoneContainerActivity_.intent(this).beer(beer).start();
	}

	@Override
	public void openMap(Fragment baseFragment, int focusId, Brewer brewerToOpen) {
		PhoneContainerActivity_.intent(this).focusId(focusId).start();
	}
	
}
