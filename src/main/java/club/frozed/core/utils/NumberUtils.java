package club.frozed.zoom.utils;

public class NumberUtils {
    public static boolean checkNumber(String s) {
        try {
            int i = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
