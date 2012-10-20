package com.grayditch.systemtray.core;

import java.util.EventListener;

public interface AlertEventListener extends EventListener {
	public void smsAlert(AlertEvent e, String jsonInfo);

	public void callAlert(AlertEvent e, String jsonInfo);

	public void whatsappAlert(AlertEvent e, String jsonInfo);

	public void testAlert(AlertEvent e, String jsonInfo);

}
