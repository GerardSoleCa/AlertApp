package com.grayditch.systemtray.core;

import java.util.EventObject;

public class AlertEvent extends EventObject {

	public final static int SMS_ALERT = 0;
	public final static int CALL_ALERT = 1;
	public final static int WHATSAPP_ALERT=2;
	public final static int TEST_ALERT=3;

	private int type;

	public AlertEvent(Object arg0, int type) {
		super(arg0);
		this.type = type;
	}

	public int getType() {
		return type;
	}

}
