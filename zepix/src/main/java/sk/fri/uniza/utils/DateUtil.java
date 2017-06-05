package sk.fri.uniza.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtil {
    private static final String datePattern = "yyyy-MM-dd";
    private static final String timeStampPattern = datePattern + " HH:mm:ss";
    private static DateFormat timeStampFormat = new SimpleDateFormat(timeStampPattern);
    private static DateFormat dateFormat = new SimpleDateFormat(datePattern);

    private DateUtil(){}

    public static Date parseTimeStamp(String timestamp) {
        try {
            return timeStampFormat.parse(timestamp);
        } catch (ParseException e) {
            throw new WrongTimpeStampExcpetion();
        }
    }

    public static Date parseDate(String date) {
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            throw new WrongTimpeStampExcpetion();
        }
    }


    static class WrongTimpeStampExcpetion extends RuntimeException{
    }
}
