package FriendsBook;

public interface DataStorage {

	void register(String user_id, String gender, String password, String school, int age);
	void login(String username, String password);
	void SendFriendRequest(String fromuserid,String toUserid);
	void getPendingNotifications(String toUserid);
	void SendUserMessage(String fromuserid, String touserid, String message);
	void getAllFriends(String userId);
//	void acceptFriendRequest(int notificationId);
	
}
