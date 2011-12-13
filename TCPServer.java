
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TCPServer {
	public static void main(String argv[]) throws Exception {
		String clientSentence;
		String capitalizedSentence;
		ServerSocket welcomeSocket = new ServerSocket(45000);
		Socket connectionSocket = welcomeSocket.accept();
		System.out.println("welcomeSocket.accept success");
		
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(Pattern.compile("(0)\\s([0-9]+)\\s([0-9]+)\\s"));
		patterns.add(Pattern.compile("(1)\\s([0-9]+)\\s"));
		patterns.add(Pattern.compile("(2)\\s([0-9]+)\\s"));
		patterns.add(Pattern.compile("(3)\\s([0-9]+)\\s([0-9]+)\\s([0-9]+)\\s"));
		

		while (true) {

			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(
					connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			System.out.println("start reading");

			clientSentence = inFromClient.readLine();
			System.out.println("end reading:" + clientSentence);



			for (Pattern pat : patterns) {
				Matcher mat = pat.matcher(clientSentence);

				if (mat.matches()) {
					// outToClient.flush();

					switch (Integer.valueOf(mat.group(1)).intValue()) {

					case 0:
						System.out.println("Matches 0");
						if (mat.group(2).equals("1") && mat.group(3).equals("1234")) {
							System.out.println("success");
							outToClient.writeBytes("0 true\n");
						} else {
							System.out.println("fault");
							outToClient.writeBytes("0 false\n");
						}
						break;
					case 1:
						System.out.println("Matches 1");
						outToClient.writeBytes("1 SECRETSECRETSESESEESE123\n");
						break;
						
					case 2:
						System.out.println("Matches 2");
						outToClient.writeBytes("2 987656789\n");
						break;
						
					case 3:
						System.out.println("Matches 3");
						System.out.println(mat.group(0));
						outToClient.writeBytes("3 true\n");
						break;
						
					}
	
					
					break;

				} else {
					// System.out.println("Doesn't match");
					outToClient.flush();
				}

			}

			//
			// System.out.println("Received: " + clientSentence);
			// capitalizedSentence = clientSentence.toUpperCase() + '\n';
			// outToClient.writeBytes(capitalizedSentence);
		}
	}
}