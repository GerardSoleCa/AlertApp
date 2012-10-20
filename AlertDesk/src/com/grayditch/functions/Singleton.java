package com.grayditch.functions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import com.grayditch.classes.Message;
import com.grayditch.classes.User;
import com.grayditch.constants.Const;
import com.grayditch.systemtray.core.AlertEvent;
import com.grayditch.systemtray.core.AlertEventListener;

public class Singleton {
	private Properties properties = null;
	static private Singleton INSTANCE = null;
	private User user;
	private List<AlertEventListener> listeners = null;
	private String propertiesFileDir = null;
	private String systemName = null;

	private Singleton() {
		user = new User();
		listeners = new LinkedList<AlertEventListener>();

		propertiesFileDir = System.getProperty("user.home")
				+ System.getProperty("file.separator") + "user.properties";
		properties = new Properties();
		try {
			try {
				properties.load(new FileInputStream(propertiesFileDir));
			} catch (FileNotFoundException e) {
				properties.setProperty("user", "");
				properties.setProperty("pass", "");
				properties.store(new FileOutputStream(propertiesFileDir), null);
				properties.load(new FileInputStream(propertiesFileDir));
			}
			this.user.user = properties.getProperty("user");
			this.user.pass = properties.getProperty("pass");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private synchronized static void createInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Singleton();
		}
	}

	public static Singleton getInstance() {
		if (INSTANCE == null) {
			createInstance();
		}
		return INSTANCE;
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	/* EVENT MANAGERS */

	public void addAlertEventListener(AlertEventListener listener) {
		listeners.add(listener);
	}

	public void removeAlertEventListener(AlertEventListener listener) {
		listeners.remove(listener);
	}

	private void fireEvent(AlertEvent e, String jsonInfo) {
		Iterator<AlertEventListener> it = listeners.iterator();
		while (it.hasNext()) {

			switch (e.getType()) {
			case AlertEvent.SMS_ALERT:
				it.next().smsAlert(e, jsonInfo);
				break;
			case AlertEvent.CALL_ALERT:
				it.next().callAlert(e, jsonInfo);
				break;
			case AlertEvent.WHATSAPP_ALERT:
				it.next().whatsappAlert(e, jsonInfo);
				break;
			case AlertEvent.TEST_ALERT:
				it.next().testAlert(e, jsonInfo);
				break;
			}
		}
	}

	/* END EVENT MANAGERS */

	public void setUser(String user, String pass) {

		this.user.user = user;
		this.user.pass = pass;
		try {
			properties.setProperty("user", user);
			properties.setProperty("pass", pass);
			FileOutputStream fo = new FileOutputStream(propertiesFileDir);
			properties.store(fo, null);
			fo.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

	public User getUser() {
		return user;
	}

	public void treatMessage(Message message) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		if (message.user.equals(this.user.user)) {
			String content;
			content = ARC4.RunRC4(ARC4.convertHexToString(message.content),
					this.user.pass);

			// System.out.println(content);

			switch (message.type) {
			case Const.SMS:
				fireEvent(new AlertEvent(this, AlertEvent.SMS_ALERT), content);
				break;

			case Const.CALL:
				fireEvent(new AlertEvent(this, AlertEvent.CALL_ALERT), content);
				break;

			case Const.WHATSAPP:
				fireEvent(new AlertEvent(this, AlertEvent.WHATSAPP_ALERT),
						content);
				break;

			case Const.TEST:
				fireEvent(new AlertEvent(this, AlertEvent.TEST_ALERT), content);
				break;

			default:
				break;
			}

		}
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

}
