package com.metis.meishuquan.util;

import java.util.regex.Pattern;

/**
 * Created by WJ on 2015/4/29.
 */
public class PatternUtils {
    public static final Pattern PATTERN_MEISHUQUAN_ID = Pattern.compile("[a-zA-Z]\\w{5,11}");
    public static final Pattern PATTERN_PASSWORD = Pattern.compile("[a-zA-Z0-9]{6,12}");
    public static final Pattern PATTERN_NICK_NAME = Pattern.compile(".{1,6}");
    public static final Pattern PATTERN_BIRTHDAY = Pattern.compile("\\d\\d\\d\\d-\\d{1,2}-\\d{1,2}");

    public static String formatToDateStr (int year, int month, int day) {
        return year + "-" + month + "-" + day;
    }

}
