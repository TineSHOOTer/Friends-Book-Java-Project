package FriendsBook;
import java.util.Scanner;

public class LoginUser {

	private DataStorage data;
	
	public LoginUser(DataStorage d) {
		data=d;
	}
	
	public void loginAccount() {
		Scanner input = new Scanner(System.in);
		
		System.out.println("username : ");
		String username= input.nextLine();
		System.out.println("Password : ");
		String password= input.nextLine();
		
		data.login(username, password);
	}
}
