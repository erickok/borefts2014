package nl.brouwerijdemolen.borefts2013.gui.fragments;

import nl.brouwerijdemolen.borefts2013.api.Beer;
import android.support.v4.app.Fragment;

import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.FragmentArg;

@EFragment
public class BeerFragment extends Fragment {

	@FragmentArg
	protected Beer beer;
	
	public BeerFragment() {
		setRetainInstance(true);
	}

}
