package FriendsBook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class SQL_Database implements DataStorage {
   
	final String DATABASE_URL = 
            "jdbc:mysql://cobmysql.uhcl.edu/prajapatim5423?useSSL=false";
	 final String db_id = "prajapatim5423";
	 final String db_psw = "2345423";
    
	 Connection connection = null;
	 Statement statement = null;
	 ResultSet resultSet = null;
	 
	@Override
	public void register(String user_id, String gender, String password, String school, int age)
	 {       
       try
       {
            //connect to the database
            connection = DriverManager.getConnection(DATABASE_URL, 
                    db_id, db_psw);
            //create a statement
            statement = connection.createStatement();
           
            //to get the account number
            resultSet = statement.executeQuery("Select * "
                    + "from user_profile");
            
            //rolled back to here if anything wrong
            connection.setAutoCommit(false);
            
            int r = statement.executeUpdate("insert into user_profile values "
                    + "('" + user_id + "', '" + gender + "', '" 
                    + password + "', '" + school + "', '" + age + "')");
            
            connection.commit();
            connection.setAutoCommit(true);
            
             //display msg
           System.out.println("***The user is created!***");
           System.out.println("***The user is " +  user_id 
                   + "!***");
           System.out.println();
            
        }
        catch(SQLException e)
        {
            //handle the exceptions
            System.out.println("user creation failed");
            e.printStackTrace();
        }
        finally
        {
            //close the database
            try
            {
                resultSet.close();
                statement.close();
                connection.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
	 }
	@Override
	public void login(String username, String password) {
		try {
			connection = DriverManager.getConnection(DATABASE_URL, db_id, db_psw);
			statement = connection.createStatement();
			resultSet = statement.executeQuery("Select * from user_profile where user_id='"+username+"'");
			connection.setAutoCommit(false);
			if(resultSet.next())
            {
                //the id is found, check the password
                if(username.equals(resultSet.getString(1)) && password.equals(resultSet.getString(3))) {
                	System.out.println("------Login Successfull-----");
                	new Menu(resultSet.getString(1)).menucard();
                }
                else System.out.println("------Login Failed-----");
            }
			else System.out.println("------Login Fail-----");
			
		}
		catch(Exception e) {
			System.out.println("user login failed");
            e.printStackTrace();
		}
		finally
        {
            //close the database
            try
            {
                resultSet.close();
                statement.close();
                connection.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
		 
		 
	}
	public void SendFriendRequest(String fromuserid, String toUserid) {
		try {
			connection = DriverManager.getConnection(DATABASE_URL, db_id, db_psw);
			statement = connection.createStatement();
			resultSet= statement.executeQuery("Select * from notifications");
            connection.setAutoCommit(false);
            String s = DateAndTime.DateTime();
            System.out.println(s);
            int r = statement.executeUpdate("insert into notifications(type,content, DateTime, status, user_id_1, user_id_2) values "
            		+ "('" + "Friend Request " + "','" + "null"+"','" + s + "','" + "Pending" + "','" + fromuserid + "','"+toUserid+"')");
            
            connection.commit();
            connection.setAutoCommit(true);
            
             //display msg
           System.out.println("----Friend request sent successfully----");
           System.out.println();
		}catch(Exception e) {
			System.out.println("----User not found----");
			e.printStackTrace();
		}
		finally
        {
            //close the database
            try
            {
                resultSet.close();
                statement.close();
                connection.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
	}
	

	public void getPendingNotifications(String toUserId) {
	    PreparedStatement statement = null;
	    Scanner scanner = new Scanner(System.in);

//	    try {
//	        connection = DriverManager.getConnection(DATABASE_URL, db_id, db_psw);
//
//	        String query = "SELECT * FROM notifications WHERE user_id_2 = ? AND status = 'Pending'";
//	        statement = connection.prepareStatement(query);
//	        statement.setString(1, toUserId);
//	        resultSet = statement.executeQuery();
//
//	        Map<Integer, String> notificationMap = new HashMap<>();
//	        int index = 1;
//
//	        while (resultSet.next()) {
//	            String fromUser = resultSet.getString("user_id_1");
//	            int notificationId = resultSet.getInt("notification_id"); // assuming primary key column
//	            notificationMap.put(index, fromUser);
//
//	            System.out.println(index + ". Friend Request from: " + fromUser);
//	            index++;
//	        }
//
//	        if (notificationMap.isEmpty()) {
//	            System.out.println("-----No new notifications-----");
//	            return;
//	        }
//
//	        // Prompt to accept or reject each one
//	        for (Map.Entry<Integer, String> entry : notificationMap.entrySet()) {
//	            int option = entry.getKey();
//	            String fromUser = entry.getValue();
//
//	            System.out.print("[" + option + "] Accept or Reject friend request from " + fromUser + "? (yes/no): ");
//	            String choice = scanner.nextLine().trim().toLowerCase();
//
//	            // Get notification ID again
//	            String getIdQuery = "SELECT notification_id FROM notifications WHERE user_id_1 = ? AND user_id_2 = ? AND status = 'Pending'";
//	            PreparedStatement idStmt = connection.prepareStatement(getIdQuery);
//	            idStmt.setString(1, fromUser);
//	            idStmt.setString(2, toUserId);
//	            ResultSet idResult = idStmt.executeQuery();
//
//	            if (idResult.next()) {
//	                int notificationId = idResult.getInt("notification_id");
//
//	                if (choice.equals("yes")) {
//	                    // Accept: insert into friends
//	                    PreparedStatement insertStmt = connection.prepareStatement(
//	                        "INSERT INTO friends(user_id_1, user_id_2) VALUES (?, ?)"
//	                    );
//	                    insertStmt.setString(1, fromUser);
//	                    insertStmt.setString(2, toUserId);
//	                    insertStmt.executeUpdate();
//
//	                    // Update status to Accepted
//	                    PreparedStatement updateStmt = connection.prepareStatement(
//	                        "UPDATE notifications SET status = 'Accepted' WHERE notification_id = ?"
//	                    );
//	                    updateStmt.setInt(1, notificationId);
//	                    updateStmt.executeUpdate();
//
//	                    System.out.println("-----Accepted friend request-----");
//	                } else {
//	                    // Update status to Declined
//	                    PreparedStatement updateStmt = connection.prepareStatement(
//	                        "UPDATE notifications SET status = 'Declined' WHERE notification_id = ?"
//	                    );
//	                    updateStmt.setInt(1, notificationId);
//	                    updateStmt.executeUpdate();
//
//	                    System.out.println("-----Declined friend request------");
//	                }
//	            }
//	            idResult.close();
//	        }
//
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    } finally {
//	        try {
//	            if (resultSet != null) resultSet.close();
//	            if (statement != null) statement.close();
//	            if (connection != null) connection.close();
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	        }
//	    }
	    
	    try {
	        connection = DriverManager.getConnection(DATABASE_URL, db_id, db_psw);

	        String query = "SELECT * FROM notifications WHERE user_id_2 = ? AND status = 'Pending'";
	        statement = connection.prepareStatement(query);
	        statement.setString(1, toUserId);
	        resultSet = statement.executeQuery();

	        Map<Integer, Integer> notificationIdMap = new HashMap<>();
	        Map<Integer, String> typeMap = new HashMap<>();
	        Map<Integer, String> fromUserMap = new HashMap<>();
	        Map<Integer, String> contentMap = new HashMap<>();
	        int index = 1;

	        System.out.println("------ New Notifications -----");
	        while (resultSet.next()) {
	            int notificationId = resultSet.getInt("notification_id");
	            String type = resultSet.getString("type");
	            String fromUser = resultSet.getString("user_id_1");
	            String content = resultSet.getString("content");

	            System.out.println(index + ". [" + type + "] from " + fromUser);

	            notificationIdMap.put(index, notificationId);
	            typeMap.put(index, type);
	            fromUserMap.put(index, fromUser);
	            contentMap.put(index, content);

	            index++;
	        }

	        if (notificationIdMap.isEmpty()) {
	            System.out.println("----- No new notifications -----");
	            return;
	        }

	        System.out.print("\nSelect notification to act on (Enter number or 0 to skip): ");
	        int choice = Integer.parseInt(scanner.nextLine().trim());

	        if (choice == 0 || !notificationIdMap.containsKey(choice)) {
	            System.out.println("No action taken.");
	            return;
	        }

	        int notificationId = notificationIdMap.get(choice);
	        String type = typeMap.get(choice);
//	        System.out.println(type);
	        String fromUser = fromUserMap.get(choice);
	        String content = contentMap.get(choice);

	        if (type.equalsIgnoreCase("Friend Request ")) {
	            System.out.print("Accept friend request from " + fromUser + "? (yes/no): ");
	            String decision = scanner.nextLine().trim().toLowerCase();

	            if (decision.equals("yes")) {
	                PreparedStatement insertFriend = connection.prepareStatement(
	                        "INSERT INTO friends(user_id_1, user_id_2) VALUES (?, ?)"
	                );
	                insertFriend.setString(1, fromUser);
	                insertFriend.setString(2, toUserId);
	                insertFriend.executeUpdate();
	                insertFriend.close();

	                System.out.println("✔ Friend request accepted.");
	                updateNotificationStatus(notificationId, "Accepted");
	            } else {
	                System.out.println("✘ Friend request declined.");
	                updateNotificationStatus(notificationId, "Declined");
	            }

	        } else if (type.equalsIgnoreCase("Message")) {
	            System.out.println("\n📩 Message from " + fromUser + ":");
	            System.out.println("\"" + content + "\"");

	            // Mark as Read
	            updateNotificationStatus(notificationId, "Read");

	            // Ask to reply
	            System.out.print("Would you like to reply to this message? (yes/no): ");
	            String replyChoice = scanner.nextLine().trim().toLowerCase();

	            if (replyChoice.equals("yes")) {
	                System.out.print("Enter your reply: ");
	                String replyContent = scanner.nextLine();

	                // Insert reply as a new message
	                PreparedStatement replyStmt = connection.prepareStatement(
	                    "INSERT INTO notifications(type, content, DateTime, status, user_id_1, user_id_2) VALUES (?, ?, ?, ?, ?, ?)"
	                );
	                replyStmt.setString(1, "Message");
	                replyStmt.setString(2, replyContent);
	                replyStmt.setString(3, DateAndTime.DateTime()); // Assuming you have this method
	                replyStmt.setString(4, "Pending");
	                replyStmt.setString(5, toUserId);  // current user replying
	                replyStmt.setString(6, fromUser);  // original sender

	                replyStmt.executeUpdate();
	                replyStmt.close();

	                System.out.println("📤 Reply sent!");
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        
	    } finally {
	        try {
	            if (resultSet != null) resultSet.close();
	            if (statement != null) statement.close();
	            if (connection != null) connection.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	private void updateNotificationStatus(int notificationId, String newStatus) throws SQLException {
		
	    PreparedStatement updateStmt = connection.prepareStatement(
	            "UPDATE notifications SET status = ? WHERE notification_id = ?"
	    );
	    
	    updateStmt.setString(1, newStatus);
	    updateStmt.setInt(2, notificationId);
	    updateStmt.executeUpdate();
	    updateStmt.close();
	}
	
	public void SendUserMessage(String fromuserid, String touserid, String message) {
		
		try {
			connection = DriverManager.getConnection(DATABASE_URL, db_id, db_psw);
			statement = connection.createStatement();
			resultSet= statement.executeQuery("Select * from notifications");
            connection.setAutoCommit(false);
            String s = DateAndTime.DateTime();
            System.out.println(s);
            int r = statement.executeUpdate("insert into notifications(type,content, DateTime, status, user_id_1, user_id_2) values "
            		+ "('" + "Message" + "','" + message +"','" + s + "','" + "Pending" + "','" + fromuserid + "','"+touserid+"')");
            
            connection.commit();
            connection.setAutoCommit(true);
            
             //display msg
           System.out.println("----Message Sent----");
           System.out.println();
		}catch(Exception e) {
			System.out.println("----Please write valid user_id----");
			e.printStackTrace();
		}
		finally
        {
            //close the database
            try
            {
                resultSet.close();
                statement.close();
                connection.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
	}
	public void getAllFriends(String userId) {
	    PreparedStatement statement = null;
	    ResultSet rs = null;

	    try {
	        connection = DriverManager.getConnection(DATABASE_URL, db_id, db_psw);

	        String query = "SELECT * FROM friends WHERE user_id_1 = ? OR user_id_2 = ?";
	        statement = connection.prepareStatement(query);
	        statement.setString(1, userId);
	        statement.setString(2, userId);
	        rs = statement.executeQuery();

	        List<String> friends = new ArrayList<>();

	        while (rs.next()) {
	            String user1 = rs.getString("user_id_1");
	            String user2 = rs.getString("user_id_2");

	            String friend = user1.equals(userId) ? user2 : user1;
	            friends.add(friend);
	        }

	        if (friends.isEmpty()) {
	            System.out.println("❌ You have no friends yet.");
	        } else {
	            System.out.println("👥 Your Friends:");
	            for (String friend : friends) {
	                System.out.println("- " + friend);
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (statement != null) statement.close();
	            if (connection != null) connection.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}

}
	
