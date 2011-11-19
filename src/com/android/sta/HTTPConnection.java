package com.android.sta;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class HTTPConnection {

	private String SERVERIP = null;
	private int SERVERPORT = 45000;
	private Socket socket = null;
	private static HTTPConnection instance = null;
	
	private HTTPConnection(String ip) throws IOException {
		SERVERIP = ip;
		InetAddress serverAddr = InetAddress.getByName(SERVERIP);
		socket = new Socket(serverAddr, SERVERPORT);
	}
	
	public static HTTPConnection getInstance(String ip) {
		if (instance == null) {
			try {
				return new HTTPConnection(ip);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return instance;
	}
	
	public void sendMessage(String message) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
				socket.getOutputStream())), true);
		
		out.println(message);

	}
	
	public String receiveMessage() {
		String ret = null;

		BufferedReader networkReader;
		try {
			networkReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			ret = networkReader.readLine();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ret;
		
	}
	
	public void closeConnection() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
