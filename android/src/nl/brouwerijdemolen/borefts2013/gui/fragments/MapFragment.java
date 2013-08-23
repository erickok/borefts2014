package nl.brouwerijdemolen.borefts2013.gui.fragments;

import java.util.HashMap;
import java.util.Map;

import nl.brouwerijdemolen.borefts2013.R;
import nl.brouwerijdemolen.borefts2013.api.Brewer;
import nl.brouwerijdemolen.borefts2013.api.Brewers;
import nl.brouwerijdemolen.borefts2013.api.GsonRequest;
import nl.brouwerijdemolen.borefts2013.gui.helpers.ApiQueue;
import nl.brouwerijdemolen.borefts2013.gui.helpers.NavigationManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.SparseArray;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.FragmentArg;

@EFragment
public class MapFragment extends com.google.android.gms.maps.SupportMapFragment implements OnInfoWindowClickListener,
		Listener<Brewers>, ErrorListener, OnMarkerClickListener, OnMapClickListener {

	public static final MapElement ELEMENT_TRAINS = new MapElement(0, new LatLng(52.081515d, 4.746145d),
			R.string.map_trains, R.drawable.ic_marker_trains);
	public static final MapElement ELEMENT_ENTRANCE = new MapElement(1, new LatLng(52.084904d, 4.740646d),
			R.string.map_entrance, R.drawable.ic_marker_entrance);
	public static final MapElement ELEMENT_TOILET1 = new MapElement(2, new LatLng(52.084382d, 4.739835d),
			R.string.map_toilet1, R.drawable.ic_marker_toilet);
	public static final MapElement ELEMENT_TOILET2 = new MapElement(3, new LatLng(52.085108d, 4.740736d),
			R.string.map_toilet2, R.drawable.ic_marker_toilet);
	public static final MapElement ELEMENT_TOKENS = new MapElement(4, new LatLng(52.084851d, 4.740432d),
			R.string.map_tokens, R.drawable.ic_marker_tokens);
	public static final MapElement ELEMENT_MILL = new MapElement(5, new LatLng(52.085652d, 4.742069d),
			R.string.map_mill, R.drawable.ic_marker_mill);
	public static final MapElement ELEMENT_FIRSTAID = new MapElement(6, new LatLng(52.084707d, 4.740264d),
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

		// Always centre the map on the festival location in Bodegraven
		// When shown as minimap, no interaction is allowed; the full map screen is started instead
		if (isMinimap) {
			getMap().moveCamera(
					CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
							.target(new LatLng(52.084850d, 4.740050d)).zoom(17.5f).bearing(-46f).build()));
			getMap().getUiSettings().setAllGesturesEnabled(false);
			getMap().getUiSettings().setZoomControlsEnabled(false);
			getMap().setOnMarkerClickListener(this);
			getMap().setOnMapClickListener(this);
		} else {
			getMap().moveCamera(
					CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
							.target(new LatLng(52.084800d, 4.740000d)).zoom(18f).bearing(-47f).build()));
			getMap().setMyLocationEnabled(true);
			getMap().getUiSettings().setCompassEnabled(true);
			getMap().setOnInfoWindowClickListener(this);
		}

		// Load the festival outline
		getMap().addPolygon(
				new PolygonOptions()
						.add(new LatLng(52.084990d, 4.740764d), new LatLng(52.085273d, 4.740357d),
								new LatLng(52.084660d, 4.739418d), new LatLng(52.084531d, 4.739632d),
								new LatLng(52.084406d, 4.739611d), new LatLng(52.084334d, 4.739750d))
						.strokeColor(getResources().getColor(R.color.darkred))
						.fillColor(getResources().getColor(R.color.darkred_half)));

		// Load the POI markers
		elementMarkers = new SparseArray<Marker>(6);
		addPoiMarker(ELEMENT_TRAINS);
		addPoiMarker(ELEMENT_ENTRANCE);
		addPoiMarker(ELEMENT_TOILET1);
		addPoiMarker(ELEMENT_TOILET2);
		addPoiMarker(ELEMENT_TOKENS);
		addPoiMarker(ELEMENT_MILL);
		addPoiMarker(ELEMENT_FIRSTAID);
		if (initFocusId >= 0) {
			focusOnMarker(initFocusId);
		}

		// Load the brewers markers asynchronously
		apiQueue.add(new GsonRequest<Brewers>(Brewers.BREWERS_URL, Brewers.class, null, this, this));

	}

	@Override
	public void onResponse(Brewers brewers) {
		brewerMarkers = new HashMap<Marker, Brewer>();
		for (final Brewer brewer : brewers.getBrewers()) {
			apiQueue.getImageLoader().get(brewer.getLogoFullUrl(), new ImageListener() {

				@Override
				public void onResponse(ImageContainer response, boolean isImmediate) {
					if (response.getBitmap() != null) {
						addBrewerMarker(brewer, response.getBitmap());
					}
				}

				@Override
				public void onErrorResponse(VolleyError error) {
					// Still show the marker, yet the default graphic will be used
					addBrewerMarker(brewer, null);
				}

			});
		}
	}

	/**
	 * Adds a marker to the visible map where some point of interest is located. The marker is cached for later lookup.
	 * @param element The meta data of the marker to show, including the marker resource graphic id
	 */
	protected void addPoiMarker(MapElement element) {
		Marker marker = getMap().addMarker(
				new MarkerOptions().position(element.latLng).title(getString(element.titleResource))
						.icon(BitmapDescriptorFactory.fromResource(element.markerResource)));
		elementMarkers.put(element.focusId, marker);
	}

	/**
	 * Adds a marker to the visible map where a certain brewer is located. The provided bitmap is a logo of the brewer
	 * that was already loaded from the memory cache or internet. The marker is cached for later lookup.
	 * @param brewer The brewer to visualise on the map with a marker
	 * @param bitmap The loaded logo bitmap of this brewer, or null if it was not successfully retreived
	 */
	public void addBrewerMarker(Brewer brewer, Bitmap bitmap) {
		BitmapDescriptor bitmapToUse;
		if (bitmap == null)
			bitmapToUse = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_mask);
		else
			bitmapToUse = BitmapDescriptorFactory.fromBitmap(drawBrewerMarker(bitmap));
		Marker marker = getMap().addMarker(
				new MarkerOptions().position(new LatLng(brewer.getLatitude(), brewer.getLongitude()))
						.title(brewer.getName()).icon(bitmapToUse));
		elementMarkers.put(BREWER_ID_THRESHOLD + brewer.getId(), marker);
		brewerMarkers.put(marker, brewer);
	}

	/**
	 * Draws a bitmap that has the shape and outline of a map marker but the contents of the brewer's logo.
	 * @param brewerLogo The brewer logo as already loaded bitmap
	 * @return A marker bitmap; this is not cached
	 */
	private Bitmap drawBrewerMarker(Bitmap brewerLogo) {
		// TODO Cache the drawn compound bitmap?
		Bitmap mask = BitmapFactory.decodeResource(getResources(), R.drawable.ic_marker_mask);
		Bitmap outline = BitmapFactory.decodeResource(getResources(), R.drawable.ic_marker_outline);
		Bitmap bmp = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
		canvas.drawBitmap(mask, 0, 0, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(brewerLogo, null, new Rect(0, 0, mask.getWidth(), mask.getHeight()), paint);
		paint.setXfermode(null);
		canvas.drawBitmap(outline, 0, 0, paint);
		return bmp;
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		Toast.makeText(getActivity(), R.string.error_nolocations, Toast.LENGTH_LONG).show();
	}

	public void focusOnMarker(int focusId) {
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
		public final int titleResource;
		public final int markerResource;

		public MapElement(int focusId, LatLng latLng, int titleResource, int markerResource) {
			this.focusId = focusId;
			this.latLng = latLng;
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
