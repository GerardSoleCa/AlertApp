package com.grayditch.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.NoSuchAlgorithmException;

import com.google.gson.Gson;
import com.grayditch.classes.CALL;
import com.grayditch.classes.Message;
import com.grayditch.functions.ARC4;

public class Client {

	/**
	 * @param args
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public static void main(String[] args) throws IOException,
			NoSuchAlgorithmException {

//		String a = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut mollis commodo urna, et consequat leo placerat eu. Aenean a enim in urna pellent";
//		String b = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut mollis commodo urna, et consequat leo placerat eu.";
//		String c = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
//		SMS sms = new SMS("erer", "23", a);
//		String smsString = new Gson().toJson(sms);
//		Message m = new Message("rere", Const.SMS, ARC4.stringToHex(ARC4
//				.RunRC4(smsString, "1234")));
//		String message = new Gson().toJson(m);

		CALL call = new CALL("787878", "7878");
		String smsString = new Gson().toJson(call);
		Message m = new Message("erer", 1, ARC4.stringToHex(ARC4.RunRC4(
				smsString, "1234")));
		String message = new Gson().toJson(m);

//		 Message m = new Message("sdsd", Const.WHATSAPP,
//		 ARC4.stringToHex(ARC4
//		 .RunRC4(Const.NULL, "1234")));
//		
//		 String message = new Gson().toJson(m);

		MulticastSocket enviador = new MulticastSocket();

		DatagramPacket dgp = new DatagramPacket(message.getBytes(),
				message.length(), InetAddress.getByName("230.0.0.1"), 53587);

		enviador.send(dgp);
	}
}