package nl.brouwerijdemolen.borefts2013.gui.fragments;

import nl.brouwerijdemolen.borefts2013.R;
import nl.brouwerijdemolen.borefts2013.api.Beer;
import nl.brouwerijdemolen.borefts2013.gui.helpers.NavigationManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.FragmentArg;
import com.googlecode.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_beer)
public class BeerFragment extends Fragment {

	@FragmentArg
	protected Beer beer;
	@ViewById
	protected TextView nameText, brewerText, abvText, oakagedText;
	@ViewById
	protected Button styleButton;
	@ViewById
	protected View buttonbarView, buttondividerView;

	public BeerFragment() {
		setRetainInstance(true);
	}

	@AfterViews
	protected void init() {
		nameText.setText(beer.getName());
		brewerText.setText(beer.getBrewer().getName());
		styleButton.setText(beer.getStyle().getName());
		if (beer.getAbv() >= 0)
			abvText.setText(getString(R.string.info_abv, beer.getAbv()));
		else
			abvText.setVisibility(View.INVISIBLE);
		if (!beer.isOakAged())
			oakagedText.setVisibility(View.INVISIBLE);
		if (beer.getRatebeerId() <= 0 || beer.getUntappdId() <= 0) {
			buttonbarView.setVisibility(View.GONE);
			buttondividerView.setVisibility(View.GONE);
		}
	}

	@Click
	protected void styleButtonClicked() {
		((NavigationManager) getActivity()).openStyle(this, beer.getStyle());
	}

	@Click
	protected void ratebeerButtonClicked() {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.ratebeer.com/b/" + beer.getRatebeerId()
				+ "/")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}

	@Click
	protected void untappdButtonClicked() {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://untappd.com/b/b/" + beer.getUntappdId()))
				.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}

}
