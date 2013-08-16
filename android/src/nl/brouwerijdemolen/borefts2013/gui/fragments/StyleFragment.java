package nl.brouwerijdemolen.borefts2013.gui.fragments;

import android.support.v4.app.Fragment;

import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.FragmentArg;

@EFragment
public class StyleFragment extends Fragment {

	@FragmentArg
	protected Integer styleId = null;
	
	public StyleFragment() {
		setRetainInstance(true);
	}

}
