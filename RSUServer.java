import java.io.*;
import java.net.*;

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

					if (message.equals("This is a AdClient.")) {
						sendMessage("Welcome to the DSRC Advertisement system!");
						sendMessage("1 Print your current advertisement.");
					} else if (message.equals("2")) {
						sendMessage("You sent 2");
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

	void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("server>" + msg);
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