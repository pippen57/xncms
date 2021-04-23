package top.pippen.xncms.utils;

import cn.hutool.crypto.SecureUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.Date;


/**
 * @author pippen
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    private static final char SEPARATOR = '_';


    public static String usernameEncoder(String username){
        return SecureUtil.md5(DigestUtils.md5Hex(String.format("top.%s.pippen.xncms.%s", username, "xncms")));
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }

        s = s.toLowerCase();

        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toUnderScoreCase(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }


    /**
     * 获得当天是周几
     */
    public static String getWeekDay() {
        String[] weekDays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    public static String getNameToMosaic(String name) {
        if (StringUtils.isNotBlank(name)) {
            char[] chars = name.toCharArray();
            StringBuilder sb = new StringBuilder();
            sb.append(chars[0]);
            int length = name.length();
            if (length >= 3) {
                length = 3;
            }
            for (int i = 0; i < length - 1; i++) {
                sb.append("*");
            }
            return sb.toString();
        } else {
            return name;
        }
    }

    /**
     * Ensures the string contain suffix.
     *
     * @param string string must not be blank
     * @param suffix suffix must not be blank
     * @return string contain suffix specified
     */
    @NonNull
    public static String ensureSuffix(@NonNull String string, @NonNull String suffix) {
        Assert.hasText(string, "String must not be blank");
        Assert.hasText(suffix, "Suffix must not be blank");

        return org.apache.commons.lang3.StringUtils.removeEnd(string, suffix) + suffix;
    }
}
