package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient implements AutoCloseable {
	private String host;
	private int port;
	private Socket socket;
	private BufferedReader inputBufferedReader; // WE
	private PrintWriter outputPrintWriter; // WY
	private final int PROTOCOL_PREFIX_LENGTH = 3;
	private String userName; // nazwa wybrana przez u¿ytkownika
	private String senderName; // nazwa nadawcy wiadomoœci
	
	public ChatClient(String host, int port, String userName) {
		this.host = host;
		this.port = port;
		this.userName = userName;
	}
	
	@Override
	public void close() throws Exception {
		socket.close();
	}
	
	public void run() throws UnknownHostException, IOException {
		socket = new Socket(host, port);
		inputBufferedReader = new BufferedReader(
		new InputStreamReader(socket.getInputStream()));
		outputPrintWriter = new PrintWriter(socket.getOutputStream(), true);
		outputPrintWriter.println(userName);
		
		Runnable task = new Runnable() {
		@Override 
		public void run() {
			try {
				while (true) {
					String msg = inputBufferedReader.readLine();
					String decodedMsg = decodeUID(msg);
					System.out.println(String.format("%s: %s",senderName,decodedMsg));
				}
			} catch (IOException e) {
				System.out.println(e.getMessage()); }
			}
		};
			new Thread(task).start();
		}
	
	private String decodeUID(String msg) {
		msg = msg.substring(PROTOCOL_PREFIX_LENGTH);
		char sep = (char) 31;
		String[] param = msg.split(String.valueOf(sep));
		senderName = param[0];
		return msg.substring(senderName.length() + 1);
	}
	
	public void sendMessage(String message) {
		outputPrintWriter.println(message);
	}

}
