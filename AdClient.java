import java.io.*;
import java.net.*;
import java.util.*;

public class AdClient {
	Socket requestSocket;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;

	AdClient() {
	}

	void run() {
		try {
			// 1. creating a socket to connect to the server
			requestSocket = new Socket("localhost", 2004);
			System.out.println("Connected to localhost in port 2004");
			// 2. get Input and Output streams
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			// 3: Communicating with the server
			do {
				try {
					message = (String) in.readObject();
					System.out.println("server>" + message);

					sendMessage("This is a AdClient.");
					message = (String) in.readObject();
					System.out.println("server>" + message);

					System.out
							.println("1 Print out your current advertisement.");
					System.out.println("2 Change your advertisement.");
					System.out.println("3 Quit");
					Scanner sc = new Scanner(System.in);
					String choice = sc.nextLine();
					while (!(choice.equals("1") || choice.equals("2") || choice
							.equals("3"))) {
						System.out.println("please enter 1 or 2 or 3");
						choice = sc.nextLine();
					}
					if (choice.equals("1")) {
						sendUnseenMessage("userinput1");
					}
					else if (choice.equals("2")) {
						sendUnseenMessage("userinput2");
					}
					else if (choice.equals("3")) {
						sendUnseenMessage("bye");
					}
					

				} catch (ClassNotFoundException classNot) {
					System.err.println("data received in unknown format");
				}
			} while (!message.equals("bye"));
		} catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			// 4: Closing connection
			try {
				in.close();
				out.close();
				requestSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("client>" + msg);
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
		AdClient client = new AdClient();
		client.run();
	}
}
