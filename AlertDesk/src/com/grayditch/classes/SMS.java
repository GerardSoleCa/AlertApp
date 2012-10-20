package com.grayditch.classes;

public class SMS {

	public SMS(String senderName, String senderNumber, String smsBody) {
		super();
		this.senderName = senderName;
		this.senderNumber = senderNumber;
		this.smsBody = smsBody;
	}
	
	public String senderName;
	public String senderNumber;
	public String smsBody;
}
