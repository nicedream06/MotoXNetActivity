package org.cptl.motoxnetactivity;

import android.content.res.XResources;
import android.graphics.drawable.Drawable;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;

public class MotoXNetActivity implements IXposedHookInitPackageResources {

	@Override
	public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
		if (!resparam.packageName.equals("com.android.systemui"))
			return;

		//For Lollipop, just one switch has to be flipped.
		if (android.os.Build.VERSION.SDK_INT>=21) {
			resparam.res.setReplacement("com.android.systemui", "bool", "config_enable_activity_on_wide_statusbar_icons", true);
		}
		
		//KitKat makes it a bit more work.
		else {
			//Easy part - just change the boolean value to enable activity indicators.
			//resparam.res.setReplacement("com.android.systemui", "bool", "config_disable_wide_activity_statusbar_icons", false);

			/*
			 * If we just enable the indicators, the wifi icon bounces around in the statusbar whenever the network is active.
			 * This is because the indicator arrow icons defined in SystemUI are wider than the signal icon (48px vs 39px).
			 * To fix this, we will use different icons already included in SystemUI by getting their identifiers and then replacing the wide ones.
			 */
			final int moto_stat_sys_wifi_in = resparam.res.getIdentifier("zz_moto_stat_sys_wifi_in", "drawable", "com.android.systemui");
			final int moto_stat_sys_wifi_out = resparam.res.getIdentifier("zz_moto_stat_sys_wifi_out", "drawable", "com.android.systemui");
			final int moto_stat_sys_wifi_inout = resparam.res.getIdentifier("zz_moto_stat_sys_wifi_inout", "drawable", "com.android.systemui");

			resparam.res.setReplacement("com.android.systemui", "drawable",	"zz_moto_stat_sys_wifi_in_wide",
					new XResources.DrawableLoader() {
						@Override
						public Drawable newDrawable(XResources res, int id)	throws Throwable {
							return res.getDrawable(moto_stat_sys_wifi_in);
						}
					});

			resparam.res.setReplacement("com.android.systemui", "drawable",	"zz_moto_stat_sys_wifi_out_wide",
					new XResources.DrawableLoader() {
						@Override
						public Drawable newDrawable(XResources res, int id)	throws Throwable {
							return res.getDrawable(moto_stat_sys_wifi_out);
						}
					});

			resparam.res.setReplacement("com.android.systemui", "drawable",	"zz_moto_stat_sys_wifi_inout_wide",
					new XResources.DrawableLoader() {
						@Override
						public Drawable newDrawable(XResources res, int id)	throws Throwable {
							return res.getDrawable(moto_stat_sys_wifi_inout);
						}
					});
	
		}
			
		
	}

}
