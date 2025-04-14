package FriendsBook;

import java.util.Scanner;

public class RegisterUser {

	private DataStorage data;
    
	public RegisterUser(DataStorage d)
	{
		data = d;
	}
	
	 
	public void createAccount()
	{
		Scanner input = new Scanner(System.in);
		
		System.out.println("Please enter your username including [#?!*]: ");
		String username = null;
		while(true) {
			username = input.nextLine();
			if (username.matches(".*[#?!*].*") && username.matches(".*[A-Z].*") && username.matches(".*[0-9].*") && 
					username.length()>=3 && username.length()<=10) {
	            break;
	        }
			System.out.println("Invalid username: " + username);
			
			}
		System.out.println("Please enter your Gender: ");
		String gender = input.nextLine();
		System.out.println("Please enter your password: ");
		String password= null;
		while(true) {
		password = input.nextLine();
		if (password.matches(".*[#?!*].*") && password.matches(".*[A-Z].*") && password.matches(".*[0-9].*") && 
				password.length()>=3) {
         
            break;
        }
		System.out.println("Invalid password: " + password);
		}
		System.out.println("Please enter your school: ");
		String school = input.nextLine();
		System.out.println("Please enter your age: ");
		int age = input.nextInt();
		
		
		data.register(username, gender,password, school,age);
		
	}
}
