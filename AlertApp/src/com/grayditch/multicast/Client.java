package com.grayditch.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import android.net.wifi.WifiManager.MulticastLock;

import com.google.gson.Gson;

public class Client {
	private MulticastSocket socket;
	private Gson gson;

	public Client() throws IOException {
		socket = new MulticastSocket();
		gson = new Gson();
	}

	public void sendAlert(String alert) throws UnknownHostException,
			IOException {
		
		
		DatagramPacket dgp = new DatagramPacket(alert.getBytes(),
				alert.length(), InetAddress.getByName("230.0.0.1"), 53587);

		socket.send(dgp);

		System.out.println("Envia");
	}

}
