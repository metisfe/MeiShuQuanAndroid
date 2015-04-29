package com.metis.meishuquan.util;

import java.util.regex.Pattern;

/**
 * Created by WJ on 2015/4/29.
 */
public class PatternUtils {
    public static final Pattern MEISHUQUAN_ID_PATTERN = Pattern.compile("[a-zA-Z]\\w{5,11}");
}
