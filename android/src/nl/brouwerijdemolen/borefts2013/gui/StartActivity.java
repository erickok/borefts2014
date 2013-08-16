package nl.brouwerijdemolen.borefts2013.gui;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.res.BooleanRes;

/**
 * Wrapper activity that simply starts either the phone or tablet version of the main fragments-containing activity.
 * @author Eric Kok <eric@2312.nl>
 */
@EActivity
public class StartActivity extends SherlockFragmentActivity {

	@BooleanRes
	protected boolean isSmallDevice;
	
    @AfterInject
    public void init() {
		if (isSmallDevice)
			PhoneActivity_.intent(this).start();
		else
			TabletActivity_.intent(this).start();
		finish();
	}

}
