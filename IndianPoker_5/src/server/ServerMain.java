package server;
 
import Model.IndianPoker;

public class ServerMain {
	public static IPServer ips;
	public static void main(String[] args) {
		ips = new IPServer(3000);
		try {
			ips.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}
}
