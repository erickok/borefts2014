package nl.brouwerijdemolen.borefts2013.gui.fragments;

import nl.brouwerijdemolen.borefts2013.R;
import nl.brouwerijdemolen.borefts2013.gui.helpers.MolenTypefaceSpan;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

@EFragment(R.layout.dialog_about)
public class AboutFragment extends SherlockDialogFragment {

	@ViewById
	protected TextView logoText, licensesText;

	public AboutFragment() {
		setRetainInstance(true);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return dialog;
	}
	
	@AfterViews
	protected void init() {
		logoText.setText(MolenTypefaceSpan.makeMolenSpannable(getActivity(), getString(R.string.app_name_short)));
		licensesText.setText(Html.fromHtml(getString(R.string.about_licenses)));
	}

	@Click(R.id.visit2312_button)
	protected void visit2312ButtonClicked() {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://2312.nl"))
				.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}

	@Click
	protected void visitdemolenButtonClicked() {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://http://www.brouwerijdemolen.nl/"))
				.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}

}
