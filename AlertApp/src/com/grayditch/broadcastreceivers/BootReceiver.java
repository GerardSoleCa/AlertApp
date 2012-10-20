package com.grayditch.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.grayditch.accessibility.AccessibilityServicePostJellyBean;
import com.grayditch.accessibility.AccessibilityServicePreJellyBean;
import com.grayditch.alertapp.R;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			boolean isJellyBean = context.getResources().getBoolean(R.bool.is_jelly_bean);
			if (isJellyBean) {
				context.startService(new Intent(context,
						AccessibilityServicePostJellyBean.class));
			} else {
				context.startService(new Intent(context,
						AccessibilityServicePreJellyBean.class));
			}
		}
	}

}