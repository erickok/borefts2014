package nl.brouwerijdemolen.borefts2013.gui.fragments;

import java.util.Collections;
import java.util.List;

import nl.brouwerijdemolen.borefts2013.R;
import nl.brouwerijdemolen.borefts2013.api.Beer;
import nl.brouwerijdemolen.borefts2013.api.Beers;
import nl.brouwerijdemolen.borefts2013.api.Brewer;
import nl.brouwerijdemolen.borefts2013.api.GsonRequest;
import nl.brouwerijdemolen.borefts2013.api.Style;
import nl.brouwerijdemolen.borefts2013.api.Styles;
import nl.brouwerijdemolen.borefts2013.gui.helpers.ApiQueue;
import nl.brouwerijdemolen.borefts2013.gui.helpers.NavigationManager;
import nl.brouwerijdemolen.borefts2013.gui.lists.BeerListAdapter;
import nl.brouwerijdemolen.borefts2013.gui.lists.BrewerHeader.BrewerHeaderedAdapter;
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
public class BrewerFragment extends Fragment implements Listener<Styles>, ErrorListener, OnItemClickListener {

	@FragmentArg
	protected Brewer brewer;
	private SparseArray<Style> loadedStyles;

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

	public BrewerFragment() {
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
		apiQueue.add(new GsonRequest<Styles>(Styles.STYLES_URL, Styles.class, null, this, this));
	}

	@Override
	public void onResponse(Styles styles) {
		// Handle the styles in a background thread
		handleStylesResult(styles.getStyles());
	}

	@Background
	protected void handleStylesResult(List<Style> styles) {
		this.loadedStyles = new SparseArray<Style>();
		for (Style style : styles) {
			this.loadedStyles.append(style.getId(), style);
		}
		// Now we have loaded and sorted the styles, start (via the UI thread) the loading of this brewer's beers
		startBeersRequest();
	}

	@UiThread
	protected void startBeersRequest() {
		apiQueue.add(new GsonRequest<Beers>(String.format(Beers.BEERS_BASE_URL, brewer.getCode()), Beers.class, null,
				new Listener<Beers>() {
					@Override
					public void onResponse(Beers beers) {
						// Beers are loaded now too; sort them and add the style and brewer objects
						List<Beer> beersList = beers.getBeers();
						Collections.sort(beersList);
						for (Beer beer : beersList) {
							beer.setStyle(loadedStyles.get(beer.getStyleId()));
							beer.setBrewer(brewer);
						}
						// Show the beers in the list view
						beerListAdapter.update(beersList, true);
						BrewerHeaderedAdapter adapter = new BrewerHeaderedAdapter(getActivity(), beerListAdapter);
						adapter.updateBrewer(brewer);
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
