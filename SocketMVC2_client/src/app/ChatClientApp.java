package app;

import java.util.Scanner;

public class ChatClientApp {
	public static void main(String[] args) throws Exception {
		if (args.length < 3) { return; }
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		String userName = args[2];
		
		ChatClient chat = new ChatClient(host, port, userName);
		chat.run();
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter message or press Ctrl+z :");
		while (scanner.hasNextLine()) {
			chat.sendMessage(scanner.nextLine());
		}
		scanner.close();
		chat.close();
	}
}
