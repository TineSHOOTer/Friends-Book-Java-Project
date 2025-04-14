package FriendsBook;
import java.util.Scanner;

public class Notifications {

	private DataStorage data;
	private String toUserid;
	
	public Notifications(String u) {
		
		toUserid=u;
		
	}
	
	public void getnotifications() {
		data.getPendingNotifications(toUserid);
	}
	
}
