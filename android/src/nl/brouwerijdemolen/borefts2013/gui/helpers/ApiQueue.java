package nl.brouwerijdemolen.borefts2013.gui.helpers;

import nl.brouwerijdemolen.borefts2013.api.GsonRequest;
import android.content.Context;
import android.util.DisplayMetrics;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.api.Scope;

@EBean(scope = Scope.Singleton)
public class ApiQueue {

	private RequestQueue requestQueue;
	private ImageLoader imageLoader;
	
	public ApiQueue(Context context) {
		
		requestQueue = Volley.newRequestQueue(context);
		
		// Cache at most one screen worth of pixels (at 4 bytes per pixel)
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		final int maxCacheSize = dm.widthPixels * dm.heightPixels * 4 * 3;
		imageLoader = new ImageLoader(requestQueue, new LruBitmapCache(maxCacheSize));
		
	}
	
	public ImageLoader getImageLoader() {
		return imageLoader;
	}

	public void add(GsonRequest<?> gsonRequest) {
		requestQueue.add(gsonRequest);
	}
	
	
}
