package com.grayditch.alertapp;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.grayditch.accessibility.AccessibilityServicePostJellyBean;
import com.grayditch.accessibility.AccessibilityServicePreJellyBean;
import com.grayditch.classes.CALL;
import com.grayditch.classes.Message;
import com.grayditch.constants.Const;
import com.grayditch.functions.ARC4;
import com.grayditch.multicast.Client;

public class MainActivity extends Activity {
	private SharedPreferences pref;
	private EditText userEditText, pinEditText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		pref = getApplicationContext().getSharedPreferences(
				Const.PREFERENCE_NAME, Context.MODE_PRIVATE);
		userEditText = (EditText) findViewById(R.id.userEditText);
		pinEditText = (EditText) findViewById(R.id.pinEditText);

		userEditText.setText(pref.getString(Const.PREF_USER, ""));
		pinEditText.setText(pref.getString(Const.PREF_PIN, ""));

		boolean isJellyBean = getResources().getBoolean(R.bool.is_jelly_bean);
		if (isJellyBean) {
			startService(new Intent(this,
					AccessibilityServicePostJellyBean.class));
		} else {
			startService(new Intent(this,
					AccessibilityServicePreJellyBean.class));
		}

	}

	public void saveButton(View v) {
		pref.edit()
				.putString(Const.PREF_USER, userEditText.getText().toString())
				.commit();
		pref.edit().putString(Const.PREF_PIN, pinEditText.getText().toString())
				.commit();
		String toast = getString(R.string.savedToast);
		Toast.makeText(getApplicationContext(), toast, toast.length()).show();
	}

	public void openSettings(View v) {

		Intent intent = new Intent(
				android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
		startActivityForResult(intent, 0);
	}

	public void sendNotification(View v) {

		final String user = pref.getString(Const.PREF_USER, "");
		final String pass = pref.getString(Const.PREF_PIN, "");

		if (!(user.equals("") && !(pass.equals("")))) {
			WifiManager wifiManager = (WifiManager) getApplicationContext()
					.getSystemService(Context.WIFI_SERVICE);

			final MulticastLock multicastLock = wifiManager
					.createMulticastLock("AlertApp");
			multicastLock.acquire();

			Thread t = new Thread(new Runnable() {

				public void run() {
					try {
						Message m = new Message(user, Const.TEST, ARC4.stringToHex(ARC4.RunRC4(
								Const.NULL, pass)));
						String alert = new Gson().toJson(m);
						new Client().sendAlert(alert);
						multicastLock.release();

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
