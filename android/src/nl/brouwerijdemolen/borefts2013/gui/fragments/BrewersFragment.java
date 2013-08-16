package nl.brouwerijdemolen.borefts2013.gui.fragments;

import java.util.Collections;

import nl.brouwerijdemolen.borefts2013.R;
import nl.brouwerijdemolen.borefts2013.api.Brewers;
import nl.brouwerijdemolen.borefts2013.api.GsonRequest;
import nl.brouwerijdemolen.borefts2013.gui.helpers.ApiQueue;
import nl.brouwerijdemolen.borefts2013.gui.helpers.NavigationManager;
import nl.brouwerijdemolen.borefts2013.gui.lists.BrewerListAdapter;
import android.support.v4.app.Fragment;
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
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_brewers)
public class BrewersFragment extends Fragment implements Listener<Brewers>, ErrorListener, OnItemClickListener {

	@Bean
	protected ApiQueue apiQueue;
	@Bean
	protected BrewerListAdapter brewerListAdapter;
	@ViewById
	protected ListView brewersList;
	@ViewById
	protected TextView errorText;
	@ViewById
	protected ProgressBar loadingProgress;

	public BrewersFragment() {
		setRetainInstance(true);
	}

	public static BrewersFragment newInstance() {
		return new BrewersFragment();
	}

	@AfterViews
	protected void init() {
		brewersList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		brewersList.setOnItemClickListener(this);
		errorText.setOnClickListener(onRetry);
		refreshScreen();
	}

	private void refreshScreen() {
		apiQueue.add(new GsonRequest<Brewers>(Brewers.BREWERS_URL, Brewers.class, null, this, this));
	}

	@Override
	public void onResponse(Brewers brewers) {
		Collections.sort(brewers.getBrewers());
		brewerListAdapter.update(brewers.getBrewers());
		brewersList.setAdapter(brewerListAdapter);
		brewersList.setVisibility(View.VISIBLE);
		errorText.setVisibility(View.GONE);
		loadingProgress.setVisibility(View.GONE);
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		brewersList.setVisibility(View.GONE);
		errorText.setVisibility(View.VISIBLE);
		loadingProgress.setVisibility(View.GONE);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		((NavigationManager) getActivity()).openBrewer(this, brewerListAdapter.getItem(position).getId());
	}
	
	private OnClickListener onRetry = new OnClickListener() {
		@Override
		public void onClick(View v) {
			brewersList.setVisibility(View.GONE);
			errorText.setVisibility(View.GONE);
			loadingProgress.setVisibility(View.VISIBLE);
			refreshScreen();
		}
	};

}
