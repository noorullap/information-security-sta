package com.android.sta;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class HTTPConnection {

	private String SERVERIP = null;
	private int SERVERPORT = 45000;
	private static Socket socket = null;
	private static HTTPConnection instance = null;
	
	private HTTPConnection(String ip, String port) throws IOException {
		SERVERIP = ip;
		SERVERPORT = Integer.valueOf(port).intValue();
		InetAddress serverAddr = InetAddress.getByName(SERVERIP);
		socket = new Socket(serverAddr, SERVERPORT);
	}
	
	public static HTTPConnection getInstance() {
		if (instance == null) {
			try {
				Context context = MainManager.getInstance().getContext();
				
				SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
				String ip_server = settings.getString( context.getString(R.string.server_host), "127.0.0.1");
				String port_server = settings.getString(context.getString(R.string.server_port), "45000");
				Log.d( StartScreen.TAG, "connecting to "+ip_server+":"+port_server);
				
				instance = new HTTPConnection(ip_server, port_server);
				
				return instance;
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
		out.flush();

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
	
	public static void closeConnection() {
		try {
			if (instance != null) {
				socket.close();
				instance = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
