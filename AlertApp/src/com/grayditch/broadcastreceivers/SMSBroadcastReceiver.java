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
import android.telephony.SmsMessage;

import com.google.gson.Gson;
import com.grayditch.classes.Message;
import com.grayditch.classes.SMS;
import com.grayditch.constants.Const;
import com.grayditch.functions.ARC4;
import com.grayditch.multicast.Client;

public class SMSBroadcastReceiver extends BroadcastReceiver {

	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

	private Gson gson;
	private String user;
	private String pass;

	@Override
	public void onReceive(final Context context, Intent intent) {

		if (intent.getAction().equals(SMS_RECEIVED)) {
			Bundle bundle = intent.getExtras();
			SmsMessage[] msgs = null;
			if (bundle != null) {

				gson = new Gson();

				SharedPreferences pref = context.getSharedPreferences(
						Const.PREFERENCE_NAME, Context.MODE_PRIVATE);

				user = pref.getString(Const.PREF_USER, "");
				pass = pref.getString(Const.PREF_PIN, "");

				if (!(user.equals("") && !(pass.equals("")))) {
					System.out.println("SMS");

					WifiManager wifiManager = (WifiManager) context
							.getSystemService(Context.WIFI_SERVICE);

					final MulticastLock multicastLock = wifiManager
							.createMulticastLock("AlertApp");
					multicastLock.acquire();

					Object[] pdus = (Object[]) bundle.get("pdus");
					msgs = new SmsMessage[pdus.length];
					for (int i = 0; i < pdus.length; i++) {
						msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
						final String smsNumber = msgs[i]
								.getOriginatingAddress();
						final String smsBody = msgs[i].getMessageBody()
								.toString();

						Thread t = new Thread(new Runnable() {

							public void run() {
								try {
									try {
										SMS sms = new SMS(
												getContactDisplayNameByNumber(
														context, smsNumber),
												smsNumber, smsBody);
										String smsString = gson.toJson(sms);
										Message m = new Message(user, Const.SMS,
												ARC4.stringToHex(ARC4.RunRC4(
														smsString, pass)));
										String alert = gson.toJson(m);
										System.out.println(alert);
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
			}
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
			}
		} finally {
			if (contactLookup != null) {
				contactLookup.close();
			}
		}

		return name;
	}

}