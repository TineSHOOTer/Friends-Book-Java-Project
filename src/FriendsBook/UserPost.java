package FriendsBook;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class UserPost {
	
	private DataStorage data;
	private String fromuserid;

	public UserPost(String user) {
		fromuserid=user;
	}
	
	public void post() {
		Scanner scanner = new Scanner(System.in);
		DataStorage data = new SQL_Database();

		System.out.print("Write your post: ");
        String post = scanner.nextLine();

        String hashtags = extractHashtags(post);       
        String cleanedPost = removeHashtags(post).trim();

        data.userpost(fromuserid, cleanedPost, hashtags);
        System.out.println("✅ Post submitted!");
	}
	public static String extractHashtags(String post) {
        StringBuilder hashtags = new StringBuilder();
        Pattern pattern = Pattern.compile("#\\w+");
        Matcher matcher = pattern.matcher(post);

        while (matcher.find()) {
            hashtags.append(matcher.group()).append(" ");
        }

        return hashtags.toString().trim();
    }

    
    public static String removeHashtags(String post) {
        return post.replaceAll("#\\w+", "");
    }
	
	

}
