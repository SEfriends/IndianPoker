package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import DiceUI.DiceMenu;

public class IPClient extends AbstractClient {
	private String clientID;

	public IPClient(String host, int port, String id) throws IOException {
		super(host, port);
		clientID = id;
		openConnection();
		sendToServer(clientID);
		// TODO Auto-generated constructor stub
	}

	// Instance methods ************************************************

	/**
	 * This method handles all data that comes in from the server.
	 * 
	 * @param msg
	 *            The message from the server.
	 */
	public void handleMessageFromServer(Object msg) {
		String s = msg.toString();
		if (s.startsWith("Play")) {
			s = s.substring(4);

		} else if (s.startsWith("Ranking")) {
			
			s = s.substring(9);
			ClientMain.menu.hs = s;
			
			ClientMain.menu.hscomplete = true;
			
		} else if (s.startsWith("Roll")) {
			s = s.substring(4);
			ClientMain.menu.roll = s;
		} else if(s.startsWith("clientID")){
			s = s.substring(8);
			clientID = s;
		} else if(s.startsWith("msg")){
			s = s.substring(3);
		} else if (s.startsWith("Quit")){
			System.out.println(s.substring(4));
			this.quit();
		} else if(s.startsWith("New")){
			System.out.println(s.substring(3));
			try {
				BufferedReader fromConsole = new BufferedReader(
						new InputStreamReader(System.in));
				System.out.print("ID : ");
				String id = fromConsole.readLine();				
				System.out.print("패스워드 : ");
				String pwd = fromConsole.readLine();
				String m = "New"+"2"+"!"+id+"!"+pwd;
				sendToServer(m);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception ex) {
				System.out.println("Unexpected error while reading from console!");
			}
		} else if (s.startsWith("Start")){
			//ClientMain.menu = new DiceMenu();
			//ClientMain.menu.setVisible(true);
		}
	}

	/**
	 * This method handles all data coming from the UI
	 * 
	 * @param message
	 *            The message from the UI.
	 */
	public void handleMessageFromClientUI(String message) {
		try {
			sendToServer(message);
		} catch (IOException e) {
			System.out.println("서버에 메세지를 보낼 수 없습니다. 클라이언트를 종료합니다..");
			quit();
		}
	}

	public void accept() {
		try {
			BufferedReader fromConsole = new BufferedReader(
					new InputStreamReader(System.in));
			String message;

			while (true) {
				message = fromConsole.readLine();
				handleMessageFromClientUI(message);
			}
		} catch (Exception ex) {
			System.out.println("Unexpected error while reading from console!");
		}
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}

	public void setID(String id) {
		clientID = id;
	}

	public String getID() {
		return clientID;
	}
}