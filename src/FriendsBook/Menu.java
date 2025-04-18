package FriendsBook;

import java.util.Scanner;

public class Menu {
	
	private DataStorage data;
	private String fromuserid;
	
	public Menu(String u) {
		fromuserid=u;
		
	}

	public void menucard() {
		Scanner input = new Scanner(System.in);
		String selection = "";
		DataStorage data = new SQL_Database();
		data.show2post(fromuserid);
		
		while(!selection.equals("x"))  //while not x, keep displaying the menu
		{
		System.out.println("Select a Service. ");
		System.out.println("1. Select a post ");
		System.out.println("2. Check notification ");
		System.out.println("3. Create new post ");
		System.out.println("4. Friends ");
		System.out.println("5. Send message ");
		System.out.println("6. Send friend Request ");
		System.out.println("7. Hashtag ");
		System.out.println("x. Logout  ");

		selection= input.nextLine();
		
		if(selection.equals("1")) {
			data.showFriendsPosts(fromuserid);

		}
		else if(selection.equals("2"))
		{

			getnotification();
		}
		else if(selection.equals("3")) {
			new UserPost(fromuserid).post();
		}
		else if(selection.equals("4")) {
			friends();
		}
		else if(selection.equals("5")) {
			new Messages(fromuserid).sendmessage();
		}
		else if(selection.equals("6")) {
			friendrequest();
		}
		else if(selection.equals("7")) {
//			new CreatePost(data).
		}
		}
		
	}
	public void friendrequest() {
		Scanner input = new Scanner(System.in);
		DataStorage data = new SQL_Database();

		System.out.println("Enter a friend username: ");
		String touserid=input.nextLine();
		
		data.SendFriendRequest(fromuserid, touserid);
	}
	public void getnotification() {
		DataStorage data= new SQL_Database();
		data.getPendingNotifications(fromuserid);
		System.out.println(" ");
		
	}
	public void friends() {
		DataStorage data= new SQL_Database();
		data.getAllFriends(fromuserid);
	}
}
