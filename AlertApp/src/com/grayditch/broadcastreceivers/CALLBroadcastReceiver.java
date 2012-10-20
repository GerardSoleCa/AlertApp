package com.grayditch.broadcastreceivers;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.google.gson.Gson;
import com.grayditch.classes.CALL;
import com.grayditch.classes.Message;
import com.grayditch.constants.Const;
import com.grayditch.functions.ARC4;
import com.grayditch.multicast.Client;

public class CALLBroadcastReceiver extends BroadcastReceiver {

	private Gson gson;
	private String user;
	private String pass;

	@Override
	public void onReceive(final Context context, Intent intent) {

		gson = new Gson();

		SharedPreferences pref = context.getSharedPreferences(
				Const.PREFERENCE_NAME, Context.MODE_PRIVATE);

		user = pref.getString(Const.PREF_USER, "");
		pass = pref.getString(Const.PREF_PIN, "");

		if (!(user.equals("") && !(pass.equals("")))) {
			System.out.println("CALL");

			WifiManager wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);

			final MulticastLock multicastLock = wifiManager
					.createMulticastLock("AlertApp");
			multicastLock.acquire();

			// TelephonyManager telephony = (TelephonyManager) context
			// .getSystemService(Context.TELEPHONY_SERVICE);
			// CustomPhoneStateListener customPhoneListener = new
			// CustomPhoneStateListener();
			//
			// telephony.listen(customPhoneListener,
			// PhoneStateListener.LISTEN_CALL_STATE);

			Bundle bundle = intent.getExtras();
			final String phoneNr = bundle.getString("incoming_number");

			Thread t = new Thread(new Runnable() {

				public void run() {
					try {
						try {
							CALL call = new CALL(getContactDisplayNameByNumber(
									context, phoneNr), phoneNr);
							String smsString = gson.toJson(call);
							Message m = new Message(user, Const.CALL,
									ARC4.stringToHex(ARC4.RunRC4(smsString,
											pass)));
							String alert = gson.toJson(m);
							new Client().sendAlert(alert);
							multicastLock.release();
						} catch (NoSuchAlgorithmException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			t.start();
		}
	}

	public String getContactDisplayNameByNumber(Context context, String number) {
		Uri uri = Uri.withAppendedPath(
				ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(number));
		String name = "Unknown";

		ContentResolver contentResolver = context.getContentResolver();
		Cursor contactLookup = contentResolver.query(uri, new String[] {
				BaseColumns._ID, ContactsContract.PhoneLookup.DISPLAY_NAME },
				null, null, null);

		try {
			if (contactLookup != null && contactLookup.getCount() > 0) {
				contactLookup.moveToNext();
				name = contactLookup.getString(contactLookup
						.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
				// String contactId =
				// contactLookup.getString(contactLookup.getColumnIndex(BaseColumns._ID));
			}
		} finally {
			if (contactLookup != null) {
				contactLookup.close();
			}
		}

		return name;
	}

}