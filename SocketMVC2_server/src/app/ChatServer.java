package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
	private ConcurrentHashMap<Integer, PrintWriter> clients = 
			new ConcurrentHashMap<Integer, PrintWriter>();
	private ServerSocket serverSocket;
	private int port;
	
	public ChatServer(int port) {this.port = port; }
	
	public void start() throws IOException{
		serverSocket = new ServerSocket(port);
		System.out.println("The server is running.");
		while (true) {
			Socket socket = serverSocket.accept();
			new UserHandler(socket).start();
		}
	}
	
	private class UserHandler extends Thread {
		private Socket userSocket;
		private int userID;
		private String userName;
		private BufferedReader inputBufferedReader;
		private PrintWriter outputPrintWriter;
		
		public UserHandler (Socket socket) throws IOException{
			userSocket = socket;
			registerUser();
		}
		
		private void registerUser() throws IOException {
			inputBufferedReader = new BufferedReader(
					new InputStreamReader(userSocket.getInputStream()));
			outputPrintWriter = new PrintWriter(userSocket.getOutputStream(), true);
			
			userID = new Random().nextInt(Integer.MAX_VALUE);
			userName = inputBufferedReader.readLine();
			clients.putIfAbsent(userID, outputPrintWriter);
			System.out.println("Name: " + userName);
			System.out.println("UID: " + String.valueOf(userID));
			
		}
		
		@Override public void run() {
			try {
				while (true) {
					String userMessage = inputBufferedReader.readLine();
					if (userMessage == null) throw new IOException();
					if (!userMessage.isEmpty()) {
						clients.entrySet().stream().filter(entry -> entry.getKey() != userID)
						.forEach(entry -> sendMassage(entry.getValue(), userMessage));}
				}
			} catch (IOException e) {System.out.println("User reste connection.");
			} finally { 
				clients.remove(userID);
				try {
					userSocket.close();
					System.out.println("User removed.");
				} catch (IOException e) {e.printStackTrace();}		
			}
			}
		private void sendMassage(PrintWriter output, String userMassage) {
			final char SEP = (char) 31;
			String serverMsg = "MSG" + userName +SEP + userMassage;
			System.out.println(serverMsg);
			output.println(serverMsg);
		}
		}
		
		
}

