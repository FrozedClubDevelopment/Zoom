package club.frozed.core.utils.time;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TimeUtil {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");

    public static String formatIntoCalendarString(Date date) {
        return (dateFormat.format(date));
    }

    private static final String HOUR_FORMAT = "%02d:%02d:%02d";

    private static final String MINUTE_FORMAT = "%02d:%02d";

    private TimeUtil() {
        throw new RuntimeException("Cannot instantiate a utility class.");
    }

    public static String millisToTimer(long millis) {
        long seconds = millis / 1000L;
        if (seconds > 3600L) {
            return String.format("%02d:%02d:%02d", seconds / 3600L, seconds % 3600L / 60L, seconds % 60L);
        }

        return String.format("%02d:%02d", seconds / 60L, seconds % 60L);
    }

    public static String millisToSeconds(long millis) {
        return new DecimalFormat("#0.0").format(((float) millis / 1000.0F));
    }

    public static String dateToString(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.getTime().toString();
    }

    public static Timestamp addDuration(long duration) {
        return truncateTimestamp(new Timestamp(System.currentTimeMillis() + duration));
    }

    public static Timestamp truncateTimestamp(Timestamp timestamp) {
        if (timestamp.toLocalDateTime().getYear() > 2037) {
            timestamp.setYear(2037);
        }

        return timestamp;
    }

    public static Timestamp addDuration(Timestamp timestamp) {
        return truncateTimestamp(new Timestamp(System.currentTimeMillis() + timestamp.getTime()));
    }

    public static Timestamp fromMillis(long millis) {
        return new Timestamp(millis);
    }

    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static String millisToRoundedTime(long millis) {
        millis++;
        long seconds = millis / 1000L;
        long minutes = seconds / 60L;
        long hours = minutes / 60L;
        long days = hours / 24L;
        long weeks = days / 7L;
        long months = weeks / 4L;
        long years = months / 12L;
        minutes++;
        if (years > 0L) return years + " year" + ((years == 1L) ? "" : "s");
        if (months > 0L) return months + " month" + ((months == 1L) ? "" : "s");
        if (weeks > 0L) return weeks + " week" + ((weeks == 1L) ? "" : "s");
        if (days > 0L) return days + " day" + ((days == 1L) ? "" : "s");
        if (hours > 0L) return hours + " hour" + ((hours == 1L) ? "" : "s");
        if (minutes > 0L) return minutes + " minute" + ((minutes == 1L) ? "" : "s");

        return seconds + " second" + ((seconds == 1L) ? "" : "s");
    }

    public static long parseTime(String time) {
        long totalTime = 0L;
        boolean found = false;
        Matcher matcher = Pattern.compile("\\d+\\D+").matcher(time);
        while (matcher.find()) {
            String s = matcher.group();
            Long value = Long.parseLong(s.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[0]);
            String type = s.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[1];
            switch (type) {
                case "s":
                    totalTime += value.longValue();
                    found = true;
                case "m":
                    totalTime += value.longValue() * 60L;
                    found = true;
                case "h":
                    totalTime += value.longValue() * 60L * 60L;
                    found = true;
                case "d":
                    totalTime += value.longValue() * 60L * 60L * 24L;
                    found = true;
                case "w":
                    totalTime += value.longValue() * 60L * 60L * 24L * 7L;
                    found = true;
                case "M":
                    totalTime += value.longValue() * 60L * 60L * 24L * 30L;
                    found = true;
                case "y":
                    totalTime += value.longValue() * 60L * 60L * 24L * 365L;
                    found = true;
            }
        }
        return !found ? -1L : (totalTime * 1000L);
    }
}

