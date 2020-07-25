package HoneyBeeBot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class checkHoney {
    public static boolean check(String line) {
        Pattern pattern = Pattern.compile("(http|ftp|https)://(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])/?");
        Matcher honey_match = pattern.matcher(line);
        if (honey_match.find())
            return true;
        return false;
    }
}
