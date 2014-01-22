import java.io.*;
import java.net.*;
import java.util.*;

public class CarClient {
	Socket requestSocket;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;

	CarClient() {
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
			try {
				message = (String) in.readObject();
				System.out.println("server>" + message);
				sendUnseenMessage("Thisisacar!");
				message = (String) in.readObject();
				System.out.println(message);
			} catch (ClassNotFoundException classNot) {
				System.err.println("data received in unknown format");
			}
		} catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} catch (IOException ioException) {
			System.err.println("Ooops, server is down.");
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
		CarClient client = new CarClient();
		client.run();
	}
}
