/* 
 * Copyright 2010-2013 Eric Kok et al.
 * 
 * Transdroid is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Transdroid is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Transdroid.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.brouwerijdemolen.borefts2013.gui.lists;

import nl.brouwerijdemolen.borefts2013.R;
import nl.brouwerijdemolen.borefts2013.api.Beer;
import nl.brouwerijdemolen.borefts2013.gui.helpers.ApiQueue;
import nl.brouwerijdemolen.borefts2013.gui.helpers.MolenTypefaceSpan;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;

/**
 * View that represents some {@link Beer} object and displays name, style and a color indication.
 * @author Eric Kok
 */
@EViewGroup(R.layout.list_item_beer)
public class BeerView extends RelativeLayout {

	@Bean
	protected ApiQueue apiQueue;
	@ViewById
	protected View styleView;
	@ViewById
	protected TextView nameText, abvText, stylebrewerText;

	public BeerView(Context context) {
		super(context);
	}

	public void bind(Beer beer, boolean showStyle) {
		nameText.setText(MolenTypefaceSpan.makeMolenSpannable(getContext(), beer.getName()));
		stylebrewerText.setText(showStyle? beer.getStyle().getName(): beer.getBrewer().getName());
		abvText.setText(getResources().getString(R.string.info_abvperc, beer.getAbv()));
		styleView.setBackgroundColor(beer.getStyle().getColorResource(getResources()));
	}

}
