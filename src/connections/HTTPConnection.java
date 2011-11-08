package connections;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class HTTPConnection {

	private String SERVERIP = "93.175.1.14";
	private int SERVERPORT = 45000;
	private Socket socket = null;
	
	public HTTPConnection() {
		
	}
	
	public void createConnection() throws IOException {
		InetAddress serverAddr = InetAddress.getByName(SERVERIP);
		socket = new Socket(serverAddr, SERVERPORT);
	}
	
	public void sendMessage(String message) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
				socket.getOutputStream())), true);
		
		out.print(message);

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
