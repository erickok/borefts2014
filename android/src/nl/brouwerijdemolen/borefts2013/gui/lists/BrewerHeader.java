package nl.brouwerijdemolen.borefts2013.gui.lists;

import nl.brouwerijdemolen.borefts2013.R;
import nl.brouwerijdemolen.borefts2013.api.Brewer;
import nl.brouwerijdemolen.borefts2013.gui.helpers.ApiQueue;
import nl.brouwerijdemolen.borefts2013.gui.helpers.MolenTypefaceSpan;
import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.list_header_brewer)
public class BrewerHeader extends RelativeLayout {

	@Bean
	protected ApiQueue apiQueue;
	@ViewById
	protected NetworkImageView logoImage;
	@ViewById
	protected TextView nameText, originText, descriptionText;
	
	/**
	 * Constructor for this header view. Only for internal use; use the {@link BrewerHeaderedAdapter} class instead.
	 * @param context The activity context to inflate this view into
	 */
	protected BrewerHeader(Context context) {
		super(context);
	}

	protected void update(Brewer brewer) {
		nameText.setText(MolenTypefaceSpan.makeMolenSpannable(getContext(), brewer.getName()));
		originText.setText(getResources().getString(R.string.info_origin, brewer.getCity(), brewer.getCountry()));
		descriptionText.setText(brewer.getDescription());
		logoImage.setImageUrl(brewer.getLogoFullUrl(), apiQueue.getImageLoader());
	}
	
	/**
	 * An adapter that shows a header with {@link Brewer} info and a list of the brewer's beers.
	 */
	public static class BrewerHeaderedAdapter extends MergeAdapter {
	
		public BrewerHeaderedAdapter(Context context, BeerListAdapter beerListAdapter) {
			super();
			addAdapter(new BrewerHeaderAdapter(BrewerHeader_.build(context)));
			addAdapter(beerListAdapter);
		}
		
		/**
		 * Updates the header of this view with brewer name, etc.
		 * @param brewer The brewer to show the header data for
		 */
		public void updateBrewer(Brewer brewer) {
			((BrewerHeader)((BrewerHeaderAdapter)getAdapter(0)).getItem(0)).update(brewer);
		}
		
	}
	
	/**
	 * A wrapper adapter that only has contains a BrewerHeader view. Only for internal use.
	 */
	private static class BrewerHeaderAdapter extends ViewHolderAdapter {

		public BrewerHeaderAdapter(BrewerHeader view) {
			super(view);
			setViewEnabled(false);
		}

	}
	
}
