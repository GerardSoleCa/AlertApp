package com.grayditch.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Enumeration;

import com.google.gson.Gson;
import com.grayditch.classes.Message;
import com.grayditch.functions.Singleton;

public class Server {
	private byte[] buffer;
	private MulticastSocket socket;
	private Gson gson;

	private static final String MULTICAST_ADDRESS = "230.0.0.1";
	private static final int MULTICAST_PORT = 53587;

	public Server() throws IOException {

		socket = new MulticastSocket(MULTICAST_PORT);

		InetSocketAddress socketAddress = new InetSocketAddress(
				MULTICAST_ADDRESS, MULTICAST_PORT);
		Enumeration<NetworkInterface> interfaces = NetworkInterface
				.getNetworkInterfaces();
		for (NetworkInterface interface_ : Collections.list(interfaces)) {
			if (interface_.isLoopback())
				continue;

			if (!interface_.isUp())
				continue;

			Enumeration<InetAddress> addresses = interface_.getInetAddresses();
			for (InetAddress address : Collections.list(addresses)) {
				if (address instanceof Inet6Address)
					continue;

				if (!address.isReachable(3000))
					continue;

				// System.out.format("ni: %s, ia: %s\n", interface_, address);
				socket.joinGroup(socketAddress, interface_);
				break;
			}
		}

		gson = new Gson();
	}

	public void startServer() throws UnknownHostException, IOException {

		try {
			while (true) {
				buffer = new byte[1024];

				DatagramPacket dgp = new DatagramPacket(buffer, buffer.length);
				socket.receive(dgp);
				int numBytes = dgp.getLength();
				byte[] msg = new byte[numBytes];
				msg = dgp.getData();


				try {
					Message message = gson.fromJson(new String(msg).trim(),
							Message.class);
					Singleton.getInstance().treatMessage(message);
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
