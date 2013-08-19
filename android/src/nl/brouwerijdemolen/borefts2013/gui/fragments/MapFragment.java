package nl.brouwerijdemolen.borefts2013.gui.fragments;

import java.util.HashMap;
import java.util.Map;

import nl.brouwerijdemolen.borefts2013.R;
import nl.brouwerijdemolen.borefts2013.api.Brewer;
import nl.brouwerijdemolen.borefts2013.api.Brewers;
import nl.brouwerijdemolen.borefts2013.api.GsonRequest;
import nl.brouwerijdemolen.borefts2013.gui.helpers.ApiQueue;
import nl.brouwerijdemolen.borefts2013.gui.helpers.NavigationManager;
import android.util.SparseArray;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.FragmentArg;

@EFragment
public class MapFragment extends com.google.android.gms.maps.SupportMapFragment implements OnInfoWindowClickListener,
		Listener<Brewers>, ErrorListener, OnMarkerClickListener, OnMapClickListener {

	public static final MapElement ELEMENT_TRAINS = new MapElement(0, new LatLng(52.081515d, 4.746145d), null,
			R.string.map_trains, R.drawable.ic_marker_trains);
	public static final MapElement ELEMENT_ENTRANCE = new MapElement(1, new LatLng(52.084904d, 4.740646d), null,
			R.string.map_entrance, R.drawable.ic_marker_entrance);
	public static final MapElement ELEMENT_TOILET1 = new MapElement(2, new LatLng(52.084382d, 4.739835d), null,
			R.string.map_toilet1, R.drawable.ic_marker_toilet);
	public static final MapElement ELEMENT_TOILET2 = new MapElement(3, new LatLng(52.085108d, 4.740736d), null,
			R.string.map_toilet2, R.drawable.ic_marker_toilet);
	public static final MapElement ELEMENT_TOKENS = new MapElement(4, new LatLng(52.084851d, 4.740432d), null,
			R.string.map_tokens, R.drawable.ic_marker_tokens);
	public static final MapElement ELEMENT_MILL = new MapElement(5, new LatLng(52.085652d, 4.742069d), null,
			R.string.map_mill, R.drawable.ic_marker_mill);
	public static final MapElement ELEMENT_FIRSTAID = new MapElement(6, new LatLng(52.084707d, 4.740264d), null,
			R.string.map_firstaid, R.drawable.ic_marker_firstaid);
	private static final int BREWER_ID_THRESHOLD = 10;

	private SparseArray<Marker> elementMarkers;
	private Map<Marker, Brewer> brewerMarkers;
	@Bean
	protected ApiQueue apiQueue;
	@FragmentArg
	protected boolean isMinimap = true;
	@FragmentArg
	protected int initFocusId;

	public MapFragment() {
		setRetainInstance(true);
	}

	@AfterViews
	protected void initMap() {

		if (getMap() == null)
			return;

		// Show the user's location, but always center the map on the festival location in Bodegraven
		if (isMinimap) {
			getMap().moveCamera(
					CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(new LatLng(52.085116d,
							4.741019d), 16f)));
			getMap().getUiSettings().setAllGesturesEnabled(false);
			getMap().getUiSettings().setZoomControlsEnabled(false);
			getMap().setOnMarkerClickListener(this);
			getMap().setOnMapClickListener(this);
		} else {
			getMap().moveCamera(
					CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(new LatLng(52.085116d,
							4.741019d), 17f)));
			getMap().setMyLocationEnabled(true);
			getMap().getUiSettings().setCompassEnabled(true);
			getMap().setOnInfoWindowClickListener(this);
		}

		// Load the festival outline
		// TODO
		
		// Load the poi markers
		elementMarkers = new SparseArray<Marker>(6);
		addMarker(ELEMENT_TRAINS);
		addMarker(ELEMENT_ENTRANCE);
		addMarker(ELEMENT_TOILET1);
		addMarker(ELEMENT_TOILET2);
		addMarker(ELEMENT_TOKENS);
		addMarker(ELEMENT_MILL);
		addMarker(ELEMENT_FIRSTAID);
		if (initFocusId >= 0) {
			focusMarker(initFocusId);
		}

		// Load the brewers markers asynchronously
		apiQueue.add(new GsonRequest<Brewers>(Brewers.BREWERS_URL, Brewers.class, null, this, this));

	}

	@Override
	public void onResponse(Brewers brewers) {
		brewerMarkers = new HashMap<Marker, Brewer>();
		for (Brewer brewer : brewers.getBrewers()) {
			Marker marker = addMarker(new MapElement(BREWER_ID_THRESHOLD + brewer.getId(), new LatLng(
					brewer.getLatitude(), brewer.getLongitude()), brewer.getName(), 0, R.drawable.ic_marker_brewer));
			brewerMarkers.put(marker, brewer);
		}
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		Toast.makeText(getActivity(), R.string.error_nolocations, Toast.LENGTH_LONG).show();
	}

	private Marker addMarker(MapElement element) {
		Marker marker = getMap().addMarker(
				new MarkerOptions().position(element.latLng)
						.title(element.title == null ? getString(element.titleResource) : element.title)
						.icon(BitmapDescriptorFactory.fromResource(element.markerResource)));
		elementMarkers.put(element.focusId, marker);
		return marker;
	}

	public void focusMarker(int focusId) {
		Marker marker = elementMarkers.get(focusId);
		if (marker != null)
			marker.showInfoWindow();
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		if (brewerMarkers.containsKey(marker)) {
			((NavigationManager) getActivity()).openBrewer(this, brewerMarkers.get(marker));
		}
	}

	public static class MapElement {

		public final int focusId;
		public final LatLng latLng;
		public final String title;
		public final int titleResource;
		public final int markerResource;

		public MapElement(int focusId, LatLng latLng, String title, int titleResource, int markerResource) {
			this.focusId = focusId;
			this.latLng = latLng;
			this.title = title;
			this.titleResource = titleResource;
			this.markerResource = markerResource;
		}

	}

	@Override
	public void onMapClick(LatLng arg0) {
		if (isMinimap)
			((NavigationManager) getActivity()).openMap(this, -1);
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		if (isMinimap) {
			((NavigationManager) getActivity()).openMap(this, -1);
			return true;
		}
		return false;
	}

}
