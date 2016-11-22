package Client;

import java.io.IOException;

public class ClientMain {	
	public static IPClient ipc;
	public static void main(String[] args) throws IOException {
		String host = "";
		int port = 3000; // The port number
		String clientID = "클라이언트";

		try {
			host = args[0];
			
		} catch (ArrayIndexOutOfBoundsException e) {
			host = "localhost";
			
		}
		ipc = new IPClient(host, port, clientID);
		//chat.accept(); // Wait for console data

	}
}
