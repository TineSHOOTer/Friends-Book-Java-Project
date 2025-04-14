package FriendsBook;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateAndTime {
	
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

    //get the date and time now as a String
    public static String DateTime()
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
        }

}