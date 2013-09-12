package nl.brouwerijdemolen.borefts2013.gui.fragments;

import java.util.Locale;

import nl.brouwerijdemolen.borefts2013.R;
import nl.brouwerijdemolen.borefts2013.api.Beer;
import nl.brouwerijdemolen.borefts2013.gui.helpers.MolenTypefaceSpan;
import nl.brouwerijdemolen.borefts2013.gui.helpers.NavigationManager;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.FragmentArg;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_beer)
@OptionsMenu(R.menu.fragment_beer)
public class BeerFragment extends SherlockFragment {

	@FragmentArg
	protected Beer beer;
	@ViewById
	protected TextView nameText, brewerText, abvText;
	@ViewById
	protected Button styleButton;
	@ViewById
	protected LinearLayout tagsLayout;

	public BeerFragment() {
		setRetainInstance(true);
		setHasOptionsMenu(true);
	}

	@AfterViews
	protected void init() {
		nameText.setText(MolenTypefaceSpan.makeMolenSpannable(getActivity(), beer.getName()));
		brewerText.setText(beer.getBrewer().getName());
		styleButton.setText(beer.getStyle().getName());
		if (beer.getAbv() >= 0)
			abvText.setText(getString(R.string.info_abvlabel, beer.getAbv()));
		else
			abvText.setVisibility(View.INVISIBLE);
		if (!TextUtils.isEmpty(beer.getTags())) {
			for (String tag : beer.getTags().split(",")) {
				addTagView(tag);
			}
		}
	}

	private void addTagView(String tag) {
		getActivity().getLayoutInflater().inflate(R.layout.widget_label, tagsLayout);
		tag = tag.toUpperCase(Locale.getDefault());
		((TextView) tagsLayout.getChildAt(tagsLayout.getChildCount() - 1)).setText(tag);
	}

	@Click
	protected void styleButtonClicked() {
		((NavigationManager) getActivity()).openStyle(this, beer.getStyle());
	}

	@OptionsItem
	protected void actionLocate() {
		((NavigationManager) getActivity()).openMap(this, MapFragment.BREWER_ID_THRESHOLD + beer.getBrewerId(),
				beer.getBrewer());
	}

	@Click
	protected void ratebeerButtonClicked() {
		if (beer.getRatebeerId() <= 0) {
			Toast.makeText(getActivity(), R.string.error_notcoupled, Toast.LENGTH_LONG).show();
			return;
		}
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.ratebeer.com/b/" + beer.getRatebeerId()
				+ "/")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}

	@Click
	protected void untappdButtonClicked() {
		if (beer.getUntappdId() <= 0) {
			Toast.makeText(getActivity(), R.string.error_notcoupled, Toast.LENGTH_LONG).show();
			return;
		}
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://untappd.com/b/b/" + beer.getUntappdId()))
				.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}

}
