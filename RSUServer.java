import java.io.*;
import java.net.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class RSUServer {
	ServerSocket providerSocket;
	Socket connection = null;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;

	RSUServer() {
	}

	void run() {
		try {
			// 1. creating a server socket
			providerSocket = new ServerSocket(2004, 10);
			// 2. Wait for connection
			System.out.println("Waiting for connection");
			connection = providerSocket.accept();
			System.out.println("Connection received from "
					+ connection.getInetAddress().getHostName());
			// 3. get Input and Output streams
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
			sendMessage("Connection successful");
			// 4. The two parts communicate via the input and output streams
			do {
				try {
					message = (String) in.readObject();
					System.out.println("client>" + message);

					// TODO: receive identity

					if (message.equals("This is a AdClient.")) {
						sendMessage("Welcome to the DSRC Advertisement system! Please choose:");
					} else if (message.equals("This is a car!")) {
						String currentAd = readFromDB();
						sendMessage(currentAd);
						sendUnseenMessage("bye");
					} else if (message.equals("userinput1")) {
						String currentAd = readFromDB();
						sendMessage("Your current advertisement is: "
								+ currentAd);
						sendMessage("bye");
					} else if (message.equals("userinput2")) {
						sendMessage("Please input the new advertisement: ");
						message = (String) in.readObject();
						writeToDB(message);
						sendMessage("Your advertisement has changed to "
								+ message);
						sendMessage("bye");
					} else if (message.equals("userinput3")) {
						sendMessage("bye");
					}
				} catch (ClassNotFoundException classnot) {
					System.err.println("Data received in unknown format");
				}
			} while (!message.equals("bye"));
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			// 4: Closing connection
			try {
				in.close();
				out.close();
				providerSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	void writeToDB(String advertisement) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(
					"someCompany.txt"));
			out.write(advertisement + "\n");
			out.close();
		} catch (IOException e) {
		}
	}

	String readFromDB() {
		String currentAdvertisement = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"someCompany.txt"));
			currentAdvertisement = br.readLine();
		} catch (IOException e) {
			System.err.println("Error: " + e);
		}
		return currentAdvertisement;
	}

	void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("server>" + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	void sendUnseenMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public static void main(String args[]) {
		RSUServer server = new RSUServer();
		while (true) {
			server.run();
		}
	}
}
