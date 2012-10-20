package com.grayditch;

import java.io.IOException;

import com.grayditch.functions.Singleton;
import com.grayditch.systemtray.AlertDeskMainUI;

public class AlertDesk {
	public static void main(String[] args) throws IOException {
		Singleton.getInstance().setSystemName(
				System.getProperty("os.name").toLowerCase());
		new AlertDeskMainUI().execute();
	}
}
