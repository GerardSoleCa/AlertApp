package com.grayditch.classes;

public class Message {

	public Message(String user, int type, String content) {
		super();
		this.user = user;
		this.type = type;
		this.content = content;
	}

	public String user;
	public int type;
	public String content;
}
