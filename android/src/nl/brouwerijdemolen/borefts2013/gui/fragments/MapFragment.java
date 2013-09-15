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
	public static final MapElement ELEMENT_ENTRANCE = new MapElement(1, new LatLng(52.084935d, 4.740653d),
			R.string.map_entrance, R.drawable.ic_marker_entrance);
	public static final MapElement ELEMENT_FTOILET1 = new MapElement(2, new LatLng(52.084742d, 4.740870d),
			R.string.map_ftoilet1, R.drawable.ic_marker_toilet);
	public static final MapElement ELEMENT_FTOILET2 = new MapElement(3, new LatLng(52.085127d, 4.740728d),
			R.string.map_ftoilet2, R.drawable.ic_marker_toilet);
	public static final MapElement ELEMENT_TOKENS = new MapElement(4, new LatLng(52.084919d, 4.740567d),
			R.string.map_tokens, R.drawable.ic_marker_tokens);
	public static final MapElement ELEMENT_MILL = new MapElement(5, new LatLng(52.085711d, 4.742077d),
			R.string.map_mill, R.drawable.ic_marker_mill);
	public static final MapElement ELEMENT_FIRSTAID = new MapElement(6, new LatLng(52.084738d, 4.740854d),
			R.string.map_firstaid, R.drawable.ic_marker_firstaid);
	public static final MapElement ELEMENT_MTOILET1 = new MapElement(7, new LatLng(52.085345d, 4.741973d),
			R.string.map_mtoilet1, R.drawable.ic_marker_toilet);
	public static final MapElement ELEMENT_MTOILET2 = new MapElement(8, new LatLng(52.084771d, 4.740420d),
			R.string.map_mtoilet2, R.drawable.ic_marker_toilet);
	public static final MapElement ELEMENT_MTOILET3 = new MapElement(9, new LatLng(52.085106d, 4.740752d),
			R.string.map_mtoilet3, R.drawable.ic_marker_toilet);
	public static final MapElement ELEMENT_FOODPLAZA = new MapElement(10, new LatLng(52.084692d, 4.740983d),
			R.string.map_foodplaza, R.drawable.ic_marker_entrance);
	public static final int BREWER_ID_THRESHOLD = 100;

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
							.target(new LatLng(52.085207d, 4.740871d)).zoom(17f).bearing(-46f).build()));
			getMap().getUiSettings().setAllGesturesEnabled(false);
			getMap().getUiSettings().setZoomControlsEnabled(false);
			getMap().setOnMarkerClickListener(this);
			getMap().setOnMapClickListener(this);
		} else {
			getMap().moveCamera(
					CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
							.target(new LatLng(52.085041d, 4.741054d)).zoom(18f).build()));
			getMap().setMyLocationEnabled(true);
			getMap().getUiSettings().setCompassEnabled(true);
			getMap().setOnInfoWindowClickListener(this);
		}

		// Load the festival outline
		// Festival area 1
		getMap().addPolygon(
				new PolygonOptions()
						.add(new LatLng(52.084906d, 4.740165d), new LatLng(52.084961d, 4.740251d),
								new LatLng(52.085024d, 4.740243d), new LatLng(52.085169d, 4.740476d),
								new LatLng(52.085009d, 4.740749d), new LatLng(52.084821d, 4.740436d))
						.strokeColor(getResources().getColor(R.color.yellow)).strokeWidth(5f)
						.fillColor(getResources().getColor(R.color.yellow_half)));
		// Bottling building
		getMap().addPolygon(
				new PolygonOptions()
						.add(new LatLng(52.085263d, 4.740358d), new LatLng(52.085113d, 4.740092d),
								new LatLng(52.085134d, 4.740039d), new LatLng(52.08504d, 4.739942d),
								new LatLng(52.084915d, 4.740162d), new LatLng(52.084961d, 4.740251d),
								new LatLng(52.085024d, 4.740243d), new LatLng(52.085190d, 4.740505d))
						.strokeColor(getResources().getColor(R.color.darkred)).strokeWidth(5f)
						.fillColor(getResources().getColor(R.color.darkred_half)));
		// Brewery building
		getMap().addPolygon(
				new PolygonOptions()
						.add(new LatLng(52.085144d, 4.740540d), new LatLng(52.085203d, 4.740631d),
								new LatLng(52.085073d, 4.740849d), new LatLng(52.085019d, 4.740741d))
						.strokeColor(getResources().getColor(R.color.darkred)).strokeWidth(5f)
						.fillColor(getResources().getColor(R.color.darkred_half)));
		// Entrance area
		getMap().addPolygon(
				new PolygonOptions()
						.add(new LatLng(52.084910d, 4.740556d), new LatLng(52.084969d, 4.740648d),
								new LatLng(52.084953d, 4.740698d), new LatLng(52.084892d, 4.740605d))
						.strokeColor(getResources().getColor(R.color.blue)).strokeWidth(5f)
						.fillColor(getResources().getColor(R.color.blue_half)));
		// Food plaza
		getMap().addPolygon(
				new PolygonOptions()
						.add(new LatLng(52.084730d, 4.741184d), new LatLng(52.084798d, 4.741074d),
								new LatLng(52.084668d, 4.740881d), new LatLng(52.084625d, 4.741004d))
						.strokeColor(getResources().getColor(R.color.blue)).strokeWidth(5f)
						.fillColor(getResources().getColor(R.color.blue_half)));
		// Festival area 2
		getMap().addPolygon(
				new PolygonOptions()
						.add(new LatLng(52.084668d, 4.740881d), new LatLng(52.084762d, 4.740768d),
								new LatLng(52.085470d, 4.741804d), new LatLng(52.085343d, 4.742064d),
								new LatLng(52.085279d, 4.741973d), new LatLng(52.085324d, 4.741892d))
						.strokeColor(getResources().getColor(R.color.yellow)).strokeWidth(5f)
						.fillColor(getResources().getColor(R.color.yellow_half)));
		// Mill building
		getMap().addPolygon(
				new PolygonOptions()
						.add(new LatLng(52.085645d, 4.741879d), new LatLng(52.085816d, 4.742056d),
								new LatLng(52.085752d, 4.742251d), new LatLng(52.085571d, 4.742085d))
						.strokeColor(getResources().getColor(R.color.darkred)).strokeWidth(5f)
						.fillColor(getResources().getColor(R.color.darkred_half)));

		// Load the POI markers
		elementMarkers = new SparseArray<Marker>(6);
		addPoiMarker(ELEMENT_TRAINS);
		addPoiMarker(ELEMENT_ENTRANCE);
		addPoiMarker(ELEMENT_FTOILET1);
		addPoiMarker(ELEMENT_FTOILET2);
		addPoiMarker(ELEMENT_TOKENS);
		addPoiMarker(ELEMENT_MILL);
		addPoiMarker(ELEMENT_FIRSTAID);
		addPoiMarker(ELEMENT_MTOILET1);
		addPoiMarker(ELEMENT_MTOILET2);
		addPoiMarker(ELEMENT_MTOILET3);
		addPoiMarker(ELEMENT_FOODPLAZA);
		if (initFocusId >= 0 && initFocusId < BREWER_ID_THRESHOLD) {
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
					if (response.getBitmap() != null && getActivity() != null) {
						addBrewerMarker(brewer, response.getBitmap());
					}
				}

				@Override
				public void onErrorResponse(VolleyError error) {
					// Still show the marker, yet the default graphic will be used
					if (getActivity() != null)
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
						.title(brewer.getShortName()).icon(bitmapToUse));
		// Also open the info window if a focus ID for this brewer was supplied
		if (initFocusId == BREWER_ID_THRESHOLD + brewer.getId())
			marker.showInfoWindow();
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
			((NavigationManager) getActivity()).openMap(this, -1, null);
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		if (isMinimap) {
			((NavigationManager) getActivity()).openMap(this, -1, null);
			return true;
		}
		return false;
	}

}
