package nl.brouwerijdemolen.borefts2013.gui.fragments;

import nl.brouwerijdemolen.borefts2013.R;
import nl.brouwerijdemolen.borefts2013.gui.helpers.NavigationManager;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_info)
public class InfoFragment extends Fragment {

	public InfoFragment() {
		setRetainInstance(true);
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// HACK: Force the map view to be completely removed, to overcome fragment reloading issues
		// See http://stackoverflow.com/q/14083950/243165
		MapFragment_ f = (MapFragment_) getFragmentManager().findFragmentById(R.id.minimap);
		if (f != null)
			getFragmentManager().beginTransaction().remove(f).commit();
	}
	
	@Click
	protected void timesButtonClicked() {
		// Try to start the calendar application
		Intent intent = new Intent(Intent.ACTION_EDIT);  
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra("title", getString(R.string.app_name));
		intent.putExtra("eventLocation", getString(R.string.info_address));
		intent.putExtra("beginTime", 1380301200); // 27 sep 2013 12:00
		intent.putExtra("endTime", 1380337200); // 27 sep 2013 22:00
		intent.putExtra("endTime", 1380337200); // 27 sep 2013 22:00
		intent.putExtra("rrule", "FREQ=DAILY;COUNT=2");
		if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
			startActivity(intent);
		} else {
			Toast.makeText(getActivity(), R.string.error_nocalendar, Toast.LENGTH_LONG).show();
		}

	}

	@Click
	protected void getmoreButtonClicked() {
		((NavigationManager) getActivity()).openMap(this, MapFragment_.ELEMENT_TOKENS.focusId);
	}
	
}
