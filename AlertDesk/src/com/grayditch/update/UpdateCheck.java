package com.grayditch.update;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import com.grayditch.constants.Const;
import com.grayditch.systemtray.notification.notifier.NotifierDialog;

public class UpdateCheck {

	public static boolean updateCheck() {
		try {
			String urlString = Const.SERVER_UPDATE;
			URL url;

			url = new URL(urlString);

			URLConnection conn = url.openConnection();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			int result = bis.read();
			while (result != -1) {
				byte b = (byte) result;
				buf.write(b);
				result = bis.read();
			}
			Double d = new Double(buf.toString());
			if (d.compareTo(Const.CURRENT_VERSION) > 0) {
				return true;
			}
		} catch (ConnectException e) {
		} catch (UnknownHostException e) {
		} catch (FileNotFoundException e) {
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return false;
	}
}
