package nl.brouwerijdemolen.borefts2013.gui.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import nl.brouwerijdemolen.borefts2013.api.Beers;
import nl.brouwerijdemolen.borefts2013.api.Brewers;
import nl.brouwerijdemolen.borefts2013.api.GsonRequest;
import nl.brouwerijdemolen.borefts2013.api.Styles;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.mygson.Gson;

@EBean(scope = Scope.Singleton)
public class ApiQueue {

	private RequestQueue requestQueue;
	private ImageLoader imageLoader;
	private Resources resources;
    private final Gson gson = new Gson();
	
	public ApiQueue(Context context) {
		
		requestQueue = Volley.newRequestQueue(context);
		resources = context.getResources();
		
		// Cache at most three screens worth of pixels (at 4 bytes per pixel)
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		final int maxCacheSize = dm.widthPixels * dm.heightPixels * 4 * 3;
		imageLoader = new ImageLoader(requestQueue, new LruBitmapCache(maxCacheSize));
		
	}
	
	public ImageLoader getImageLoader() {
		return imageLoader;
	}

	public void requestBrewers(final Listener<Brewers> listener, final ErrorListener errorListener) {
		requestQueue.add(new GsonRequest<Brewers>(Brewers.BREWERS_URL, Brewers.class, null, listener, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				// Error response; use the offline version instead
				try {
					InputStream in = resources.getAssets().open("json/brewers.json");
					Brewers brewers = gson.fromJson(new InputStreamReader(in, "UTF-8"), Brewers.class);
					listener.onResponse(brewers);
				} catch (IOException e) {
					errorListener.onErrorResponse(new VolleyError("Offline loading of JSON asset file failed: " + e.toString()));
				}
			}
		}));
	}

	public void requestStyles(final Listener<Styles> listener, final ErrorListener errorListener) {
		requestQueue.add(new GsonRequest<Styles>(Styles.STYLES_URL, Styles.class, null, listener, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				// Error response; use the offline version instead
				try {
					InputStream in = resources.getAssets().open("json/styles.json");
					Styles styles = gson.fromJson(new InputStreamReader(in, "UTF-8"), Styles.class);
					listener.onResponse(styles);
				} catch (IOException e) {
					errorListener.onErrorResponse(new VolleyError("Offline loading of JSON asset file failed: " + e.toString()));
				}
			}
		}));
	}

	public void requestBeers(final Listener<Beers> listener, final ErrorListener errorListener) {
		requestQueue.add(new GsonRequest<Beers>(Beers.BEERS_URL, Beers.class, null, listener, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				// Error response; use the offline version instead
				try {
					InputStream in = resources.getAssets().open("json/beers.json");
					Beers beers = gson.fromJson(new InputStreamReader(in, "UTF-8"), Beers.class);
					listener.onResponse(beers);
				} catch (IOException e) {
					errorListener.onErrorResponse(new VolleyError("Offline loading of JSON asset file failed: " + e.toString()));
				}
			}
		}));
	}
	
}
