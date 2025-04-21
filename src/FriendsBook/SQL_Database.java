package FriendsBook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
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
	@Override
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
	@Override
	public void getAllFriends(String userId) {
	    PreparedStatement statement = null;
	    ResultSet rs = null;
	    Scanner scanner = new Scanner(System.in);

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
	            return;
	        }

	        // Display friends with indexes
	        System.out.println("👥 Select a friend to view profile:");
	        for (int i = 0; i < friends.size(); i++) {
	            System.out.println((i + 1) + ". " + friends.get(i));
	        }

	        System.out.print("Enter number (0 to cancel): ");
	        int choice = Integer.parseInt(scanner.nextLine());

	        if (choice < 1 || choice > friends.size()) {
	            System.out.println("❌ Invalid choice or cancelled.");
	            return;
	        }

	        String selectedFriendId = friends.get(choice - 1);
	        showUserProfile(selectedFriendId);

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

	private void showUserProfile(String userId) {
	    PreparedStatement stmt = null;
	    ResultSet rs = null;

	    try {
	        connection = DriverManager.getConnection(DATABASE_URL, db_id, db_psw);

	        String query = "SELECT * FROM User_Profile WHERE user_id = ?";
	        stmt = connection.prepareStatement(query);
	        stmt.setString(1, userId);
	        rs = stmt.executeQuery();

	        if (rs.next()) {
	            System.out.println("\n📄 Friend Profile:");
	            System.out.println("User ID  : " + rs.getString("user_id"));
	            System.out.println("Gender   : " + rs.getString("gender"));
	            System.out.println("Age      : " + rs.getString("age"));
	            System.out.println("School   : " + rs.getString("school"));
	        } else {
	            System.out.println("⚠ No profile found.");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (stmt != null) stmt.close();
	            if (connection != null) connection.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}

	@Override
	public void userpost(String userid, String post, String hashtag) {
		PreparedStatement statement = null;
		try {
			connection = DriverManager.getConnection(DATABASE_URL, db_id, db_psw);
            String s = DateAndTime.DateTime();
            connection.setAutoCommit(false);

			String query="insert into post(user_id, content, Hashtag, created_at) values(?,?,?,?)";
			statement = connection.prepareStatement(query);
			statement.setString(1, userid);
			statement.setString(2, post);
			statement.setString(3, hashtag);
			statement.setString(4, s);

			int rowsInserted = statement.executeUpdate();
	        connection.commit();

	        if (rowsInserted > 0) {
	            System.out.println("✔ Post created successfully.");
	        }

	        // Show all posts of the user
	        String fetchQuery = "SELECT * FROM post WHERE user_id = ? ORDER BY created_at DESC";
	        statement = connection.prepareStatement(fetchQuery);
	        statement.setString(1, userid);
	        resultSet = statement.executeQuery();

	        System.out.println("\n📜 Your Posts:");
	        while (resultSet.next()) {
	            String content = resultSet.getString("content");
	            String tag = resultSet.getString("Hashtag");
	            String time = resultSet.getString("created_at");

	            System.out.println("[" + time + "] " + content + " #" + tag +"");
            
	        }
		}
		catch (Exception e) {
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
	@Override
	public void showFriendsPosts(String currentUserId) {
	    PreparedStatement stmt = null ,commentStmt = null, insertCommentStmt = null;
	    ResultSet rs = null, commentRs= null;
	    Scanner scanner= new Scanner(System.in);

	    try {
	        connection = DriverManager.getConnection(DATABASE_URL, db_id, db_psw);

	        // SQL to fetch posts from friends (bidirectional)
	        String sql = """
	            SELECT p.post_id,p.user_id, p.content, p.Hashtag, p.created_at
	            FROM post p
	            JOIN (
	                SELECT user_id_1 AS friend_id FROM friends WHERE user_id_2 = ?
	                UNION
	                SELECT user_id_2 AS friend_id FROM friends WHERE user_id_1 = ?
	            ) f ON p.user_id = f.friend_id
	            ORDER BY p.created_at DESC
	        """;

	        stmt = connection.prepareStatement(sql);
	        stmt.setString(1, currentUserId);
	        stmt.setString(2, currentUserId);
	        rs = stmt.executeQuery();

	        System.out.println("📢 Posts from Your Friends:");
	        boolean hasPosts = false;

	        while (rs.next()) {
	            hasPosts = true;
	            int postId = rs.getInt("post_id");
	            String userId = rs.getString("user_id");
	            String content = rs.getString("content");
	            String tag = rs.getString("Hashtag");
	            String time = rs.getString("created_at");

	            System.out.println("[" + time + "] " + userId + ": " + content+ "  " + tag+ "");
	            String commentQuery = "SELECT user_id, comments, created_at FROM comments WHERE post_id = ? ORDER BY created_at";
	            commentStmt = connection.prepareStatement(commentQuery);
	            commentStmt.setInt(1, postId);
	            commentRs = commentStmt.executeQuery();
	            
	            while (commentRs.next()) {
	                String commenter = commentRs.getString("user_id");
	                String comment = commentRs.getString("comments");
	                String commentTime = commentRs.getString("created_at");

	                System.out.println("   💬 [" + commentTime + "] " + commenter + ": " + comment);
	            }

	            System.out.print("→ Do you want to comment on this post? (yes/no): ");
	            String answer = scanner.nextLine().trim().toLowerCase();
	            if (answer.equals("yes")) {
	                System.out.print("Write your comment: ");
	                String commentText = scanner.nextLine();
	                String s = DateAndTime.DateTime();

	                String insertComment = "INSERT INTO comments (post_id, user_id, comments, created_at) VALUES (?, ?, ?, ?)";
	                insertCommentStmt = connection.prepareStatement(insertComment);
	                insertCommentStmt.setInt(1, postId);
	                insertCommentStmt.setString(2, currentUserId);
	                insertCommentStmt.setString(3, commentText);
	                insertCommentStmt.setString(4, s);
	                insertCommentStmt.executeUpdate();

	                System.out.println("✅ Comment added!");
	            }
	            System.out.println(); // space between posts
	        }
	        

	        if (!hasPosts) {
	            System.out.println("No posts from friends yet.");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (stmt != null) stmt.close();
	            if (connection != null) connection.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}

	public void show2post(String userid) {
	    PreparedStatement stmt = null, commentStmt = null;
	    ResultSet rs = null;
	    Scanner scanner = new Scanner(System.in);

	    try {
	        connection = DriverManager.getConnection(DATABASE_URL, db_id, db_psw);

	        String sql = """
	            SELECT p.post_id, p.user_id, p.content, p.Hashtag, p.created_at
	            FROM post p
	            JOIN (
	                SELECT user_id_1 AS friend_id FROM friends WHERE user_id_2 = ?
	                UNION
	                SELECT user_id_2 AS friend_id FROM friends WHERE user_id_1 = ?
	            ) f ON p.user_id = f.friend_id
	        """;

	        stmt = connection.prepareStatement(sql);
	        stmt.setString(1, userid);
	        stmt.setString(2, userid);
	        rs = stmt.executeQuery();

	        // Store posts in a list
	        List<Map<String, String>> posts = new ArrayList<>();

	        while (rs.next()) {
	            Map<String, String> post = new HashMap<>();
	            post.put("post_id", rs.getString("post_id"));
	            post.put("user_id", rs.getString("user_id"));
	            post.put("content", rs.getString("content"));
	            post.put("Hashtag", rs.getString("Hashtag"));
	            post.put("created_at", rs.getString("created_at"));
	            posts.add(post);
	        }

	        if (posts.isEmpty()) {
	            System.out.println("No posts from friends yet.");
	            return;
	        }

	        // Shuffle and pick up to 2 posts
	        Collections.shuffle(posts);
	        int limit = Math.min(2, posts.size());

	        for (int i = 0; i < limit; i++) {
	            Map<String, String> post = posts.get(i);
	            System.out.println("[" + post.get("created_at") + "] " + post.get("user_id") + ": " + post.get("content") + " " + post.get("Hashtag"));

	            // Comment prompt
	            System.out.print("💬 Do you want to comment on this post? (yes/no): ");
	            String choice = scanner.nextLine().trim().toLowerCase();
	            if (choice.equals("yes")) {
	                System.out.print("✍ Enter your comment: ");
	                String comment = scanner.nextLine();
	                String s = DateAndTime.DateTime();

	                String insertComment = "INSERT INTO comments (post_id, user_id, comments, created_at) VALUES (?, ?, ?, ?)";
	                commentStmt = connection.prepareStatement(insertComment);
	                commentStmt.setInt(1, Integer.parseInt(post.get("post_id")));
	                commentStmt.setString(2, userid);
	                commentStmt.setString(3, comment);
	                commentStmt.setString(4, s);
	                commentStmt.executeUpdate();

	                System.out.println("✅ Comment added!");
	            }
	            System.out.println(); // spacing between posts
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (stmt != null) stmt.close();
	            if (commentStmt != null) commentStmt.close();
	            if (connection != null) connection.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	
	    public void seeHashtagTrends(String currentUserId) {
	        Scanner scanner = new Scanner(System.in);
	        PreparedStatement stmt = null;
	        ResultSet rs = null;

	        try {
	            connection = DriverManager.getConnection(DATABASE_URL, db_id, db_psw);

	            // Step 1: Get top 2 trending hashtags
	            String trendQuery = """
	                SELECT Hashtag, COUNT(*) AS count
	                FROM post
	                WHERE Hashtag IS NOT NULL AND Hashtag <> ''
	                GROUP BY Hashtag
	                ORDER BY count DESC
	            """;

	            stmt = connection.prepareStatement(trendQuery);
	            rs = stmt.executeQuery();

	            List<String> trending = new ArrayList<>();
	            System.out.println("Top Trending Hashtags:");
	            int count = 0;
	            while (rs.next() && count < 2) {
	                String hashtag = rs.getString("Hashtag");
	                System.out.println((count + 1) + ". " + hashtag);
	                trending.add(hashtag);
	                count++;
	            }

	            if (trending.isEmpty()) {
	                System.out.println("No trending hashtags available.");
	                return;
	            }

	            // Step 2: Let user select or enter custom hashtag
	            System.out.print("Select a hashtag by number or type a new one: ");
	            String input = scanner.nextLine().trim();

	            String selectedHashtag;
	            
	            if (input.matches("\\d") && Integer.parseInt(input) <= trending.size()) {
	                selectedHashtag = trending.get(Integer.parseInt(input) - 1);
	            } else {
	                selectedHashtag = input;
	            }

	            // Step 3: Fetch and show friend posts with that hashtag
	            String postQuery = """
	                SELECT p.user_id, p.content, p.created_at
	                FROM post p
	                JOIN (
	                    SELECT user_id_1 AS friend_id FROM friends WHERE user_id_2 = ?
	                    UNION
	                    SELECT user_id_2 AS friend_id FROM friends WHERE user_id_1 = ?
	                ) f ON p.user_id = f.friend_id
	                WHERE p.Hashtag = ?
	                ORDER BY p.created_at DESC
	            """;

	            stmt = connection.prepareStatement(postQuery);
	            stmt.setString(1, currentUserId);
	            stmt.setString(2, currentUserId);
	            stmt.setString(3, selectedHashtag);
	            rs = stmt.executeQuery();

	            System.out.println("📢 Posts from friends using #" + selectedHashtag + ":");
	            boolean found = false;
	            while (rs.next()) {
	                found = true;
	                String uid = rs.getString("user_id");
	                String content = rs.getString("content");
	                String time = rs.getString("created_at");

	                System.out.println("[" + time + "] " + uid + ": " + content);
	            }

	            if (!found) {
	                System.out.println("No posts found with that hashtag.");
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                if (rs != null) rs.close();
	                if (stmt != null) stmt.close();
	                if (connection != null) connection.close();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    

	}

	

}
	
