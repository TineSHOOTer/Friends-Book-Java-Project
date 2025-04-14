package FriendsBook;

import java.util.Scanner;

public class Messages {

	private DataStorage data;
	private String fromuserid;
	
	public Messages(String u) {
		fromuserid=u;
		
	}
	
	public void sendmessage() {
		System.out.println(fromuserid);
		Scanner input = new Scanner(System.in);
		DataStorage data = new SQL_Database();

		System.out.println("Enter a friend username : ");
		String touserid=input.nextLine();
		System.out.println("Type your message :");
		String message=input.nextLine();
		
		data.SendUserMessage(fromuserid, touserid, message);
	}
}
