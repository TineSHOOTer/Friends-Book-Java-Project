package FriendsBook;
import java.util.*;

public class FriendsBookSystem {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner input = new Scanner(System.in);
		String selection = "";
		DataStorage data = new SQL_Database();
		
		while(!selection.equals("x"))  //while not x, keep displaying the menu
		{
			//display the menu
			System.out.println();
			System.out.println("Please make your selection: ");
			System.out.println("1: Register");
			System.out.println("2: Login");
			System.out.println("x: Finish");
			
			//get the selection from the user
			selection = input.nextLine();
			System.out.println();
			
			//based on the input, go to different function
			if(selection.equals("1"))
			{
				//open a new checking 
				new RegisterUser(data).createAccount();
			}
			else if(selection.equals("2"))
			{
				new LoginUser(data).loginAccount();
			}
			 	 
		}

	}

}
