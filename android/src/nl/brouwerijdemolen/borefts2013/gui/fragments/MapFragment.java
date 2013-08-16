package nl.brouwerijdemolen.borefts2013.gui.fragments;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.FragmentArg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@EFragment
public class MapFragment extends com.google.android.gms.maps.SupportMapFragment implements OnMapClickListener, OnMarkerClickListener {

	@FragmentArg
	protected int focusId;
	
	public MapFragment() {
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		if (getMap() != null) {
			initMap(getMap());
		}
		return v;
	}

	private void initMap(GoogleMap map) {
		
		// Show the user's location, but always center the map on the festival location in Bodegraven
		map.setMyLocationEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(false);
		map.getUiSettings().setCompassEnabled(true);
		map.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(
                new LatLng(52.085398d, 4.741655d), 16f)));
		
		// Load the map festival overlay and brewer/poi markers
		loadMapMarkers();
		
		// Enable clicks
		map.setOnMapClickListener(this);
		map.setOnMarkerClickListener(this);
		
	}
	
	@Background
	protected void loadMapMarkers() {
		// TODO Load the map markers
		if (focusId >= 0) {
			// TODO Focus on the supplied map marker
		}
		
	}

	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
