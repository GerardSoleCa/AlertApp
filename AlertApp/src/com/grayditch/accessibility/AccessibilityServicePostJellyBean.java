package com.grayditch.accessibility;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.view.accessibility.AccessibilityEvent;

import com.google.gson.Gson;
import com.grayditch.classes.Message;
import com.grayditch.constants.Const;
import com.grayditch.functions.ARC4;
import com.grayditch.multicast.Client;

public class AccessibilityServicePostJellyBean extends AccessibilityService {
	private Gson gson;
	private String user;
	private String pass;

	@Override
	public void onCreate() {
		System.out.println("Service Xungo");
		super.onCreate();
	}

	@Override
	protected void onServiceConnected() {
		System.out.println("Conected");

		AccessibilityServiceInfo info = new AccessibilityServiceInfo();

		// Set the type of events that this service wants to listen to. Others
		// won't be passed to this service.
		info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;

		// Set the type of feedback your service will provide.
		info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;

		String[] packagesToListen = { "com.whatsapp" };
		info.packageNames = packagesToListen;

		// Default services are invoked only if no package-specific ones are
		// present
		// for the type of AccessibilityEvent generated. This service *is*
		// application-specific, so the flag isn't necessary. If this was a
		// general-purpose service, it would be worth considering setting the
		// DEFAULT flag.

		// info.flags = AccessibilityServiceInfo.DEFAULT;

		info.notificationTimeout = 100;

		this.setServiceInfo(info);
		System.out.println("Info");
		super.onServiceConnected();
	}

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {

		if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
			final String packageName = String.valueOf(event.getPackageName());
			if (packageName.equals("com.whatsapp")) {
				gson = new Gson();

				SharedPreferences pref = this.getSharedPreferences(
						Const.PREFERENCE_NAME, Context.MODE_PRIVATE);

				user = pref.getString(Const.PREF_USER, "");
				pass = pref.getString(Const.PREF_PIN, "");

				if (!(user.equals("") && !(pass.equals("")))) {

					WifiManager wifiManager = (WifiManager) this
							.getSystemService(Context.WIFI_SERVICE);

					final MulticastLock multicastLock = wifiManager
							.createMulticastLock("AlertApp");
					multicastLock.acquire();

					Thread t = new Thread(new Runnable() {

						public void run() {
							try {

								final Message m = new Message(user,
										Const.WHATSAPP, ARC4.stringToHex(ARC4.RunRC4(Const.NULL,
												pass)));

								String alert = gson.toJson(m);
								new Client().sendAlert(alert);

							} catch (UnknownHostException e) {
							} catch (IOException e) {
							} catch (NoSuchAlgorithmException e) {
							}
						}
					});
					t.start();
				}
			}
		}

	}

	@Override
	public void onInterrupt() {
		// TODO Auto-generated method stub

	}

}
