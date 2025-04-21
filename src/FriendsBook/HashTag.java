package FriendsBook;
import java.util.Scanner;
public class HashTag {

	private DataStorage data;
	private String fromuserid;
	
	public HashTag(String s) {
		
		fromuserid=s;
	}
	
	public void hashtagtrends() {
		DataStorage data = new SQL_Database();
		data.seeHashtagTrends(fromuserid);
	}

	
}
