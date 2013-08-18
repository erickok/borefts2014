package nl.brouwerijdemolen.borefts2013.gui.fragments;

import java.util.Collections;
import java.util.List;

import nl.brouwerijdemolen.borefts2013.R;
import nl.brouwerijdemolen.borefts2013.api.Beer;
import nl.brouwerijdemolen.borefts2013.api.Beers;
import nl.brouwerijdemolen.borefts2013.api.Brewer;
import nl.brouwerijdemolen.borefts2013.api.Brewers;
import nl.brouwerijdemolen.borefts2013.api.GsonRequest;
import nl.brouwerijdemolen.borefts2013.api.Style;
import nl.brouwerijdemolen.borefts2013.gui.helpers.ApiQueue;
import nl.brouwerijdemolen.borefts2013.gui.helpers.NavigationManager;
import nl.brouwerijdemolen.borefts2013.gui.lists.BeerListAdapter;
import nl.brouwerijdemolen.borefts2013.gui.lists.StyleHeader.StyleHeaderedAdapter;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.FragmentArg;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_list)
public class StyleFragment extends Fragment implements Listener<Brewers>, ErrorListener, OnItemClickListener {

	@FragmentArg
	protected Style style;
	private SparseArray<Brewer> loadedBrewers;
	
	@Bean
	protected ApiQueue apiQueue;
	@ViewById
	protected ListView theList;
	@ViewById
	protected TextView errorText;
	@ViewById
	protected ProgressBar loadingProgress;
	@Bean
	protected BeerListAdapter beerListAdapter;

	public StyleFragment() {
		setRetainInstance(true);
	}

	@AfterViews
	protected void init() {
		theList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		theList.setOnItemClickListener(this);
		errorText.setOnClickListener(onRetry);
		refreshScreen();
	}

	private void refreshScreen() {
		// Load the styles first, as we will need this info to display every beer's style
		apiQueue.add(new GsonRequest<Brewers>(Brewers.BREWERS_URL, Brewers.class, null, this, this));
	}

	@Override
	public void onResponse(Brewers brewers) {
		// Handle the brewers in a background thread
		handleBrewersResult(brewers.getBrewers());
	}

	@Background
	protected void handleBrewersResult(List<Brewer> brewers) {
		this.loadedBrewers = new SparseArray<Brewer>();
		for (Brewer brewer : brewers) {
			this.loadedBrewers.append(brewer.getId(), brewer);
		}
		// Now we have loaded and sorted the brewers, start (via the UI thread) the loading of this style's beers
		startBeersRequest();
	}

	@UiThread
	protected void startBeersRequest() {
		apiQueue.add(new GsonRequest<Beers>(String.format(Beers.BEERS_BASE_URL, style.getId()), Beers.class, null, new Listener<Beers>() {
			@Override
			public void onResponse(Beers beers) {
				// Beers are loaded now too; sort them and add the style and brewer objects
				List<Beer> beersList = beers.getBeers();
				Collections.sort(beersList);
				for (Beer beer : beersList) {
					beer.setStyle(style);
					beer.setBrewer(loadedBrewers.get(beer.getBrewerId()));
				}
				// Show the beers in the list view
				beerListAdapter.update(beersList, false);
				StyleHeaderedAdapter adapter = new StyleHeaderedAdapter(getActivity(), beerListAdapter);
				adapter.updateStyle(style);
				theList.setAdapter(adapter);
				theList.setVisibility(View.VISIBLE);
				errorText.setVisibility(View.GONE);
				loadingProgress.setVisibility(View.GONE);
			}
		}, this));
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		theList.setVisibility(View.GONE);
		errorText.setVisibility(View.VISIBLE);
		loadingProgress.setVisibility(View.GONE);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		((NavigationManager) getActivity()).openBeer(this, ((Beer) theList.getItemAtPosition(position)));
	}

	private OnClickListener onRetry = new OnClickListener() {
		@Override
		public void onClick(View v) {
			theList.setVisibility(View.GONE);
			errorText.setVisibility(View.GONE);
			loadingProgress.setVisibility(View.VISIBLE);
			refreshScreen();
		}
	};

}
