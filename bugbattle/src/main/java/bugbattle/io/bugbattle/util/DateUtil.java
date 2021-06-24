package bugbattle.io.bugbattle.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

    public static String formatDate(String time, String date) {
        String result = "";
        date += "-" + Calendar.getInstance().get(Calendar.YEAR);

        String[] splittedDate = date.split("-");
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS", Locale.getDefault());
        try {
            Calendar cal = Calendar.getInstance();
            Date parsedDate = sdf.parse(date + " " + time);
            if (parsedDate != null) {
                cal.setTime(parsedDate);
            }
            TimeZone tz = TimeZone.getTimeZone("UTC");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()); // Quoted "Z" to indicate UTC, no timezone offset
            df.setTimeZone(tz);
            result += df.format(cal.getTime());
        } catch (ParseException err) {
            result += Calendar.getInstance().get(Calendar.YEAR) + "-" + splittedDate[1] + "-" + splittedDate[0] + " " + time;
        }
        return result;
    }

    public static Date stringToDate(String date) throws ParseException {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        return df.parse(date);
    }

    public static String dateToString(Date date){
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        return df.format(date);
    }
}
