package com.metis.meishuquan.manager.common;

import android.text.TextUtils;
import android.util.Log;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.util.PatternUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WJ on 2015/5/4.
 */
public class UserManager {

    private static final String TAG = UserManager.class.getSimpleName();

    public static void updateMyInfo (String key, String value) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(key, value);
        UserInfoOperator.getInstance().updateUserInfo(MainApplication.userInfo.getUserId(), map);
    }

    public static int caculateAgeByBirthday (String birthdayStr) {
        if (TextUtils.isEmpty(birthdayStr)) {
            return 0;
        }
        int tIndex = birthdayStr.indexOf('T');
        if (tIndex < 0) {
            tIndex = birthdayStr.length();
        }
        String birthday = birthdayStr.substring(0, tIndex);
        if (!PatternUtils.PATTERN_BIRTHDAY.matcher(birthday).matches()) {
            throw new NumberFormatException("be sure birthday yyyy-MM-dd and in face is " + birthdayStr);
        }
        Calendar cal = Calendar.getInstance();
        int nowYear = cal.get(Calendar.YEAR);
        int nowMonth = cal.get(Calendar.MONTH) + 1;
        int nowDay = cal.get(Calendar.DAY_OF_MONTH);
        int index1 = birthday.indexOf('-');
        int index2 = birthday.lastIndexOf('-');
        int year = Integer.valueOf(birthday.substring(0, index1));
        int month = Integer.valueOf(birthday.substring(index1 + 1, index2));
        int day = Integer.valueOf(birthday.substring(index2 + 1));
        return caculateAgeByBirthday(year, month, day);
    }

    public static int caculateAgeByBirthday (int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        int nowYear = cal.get(Calendar.YEAR);
        int nowMonth = cal.get(Calendar.MONTH) + 1;
        int nowDay = cal.get(Calendar.DAY_OF_MONTH);

        int ageYear = nowYear - year;
        int result = 0;
        if (month > nowMonth) {
            result = ageYear - 1;
        } else if (month < nowMonth) {
            result = ageYear;
        } else {
            if (day > nowDay) {
                result = ageYear - 1;
            } else {
                result = ageYear;
            }
        }
        return Math.max(ageYear, 0);
    }

    public static boolean isMySelf (User user) {
        if (user == null) {
            return false;
        }
        if (MainApplication.userInfo == null) {
            return false;
        }
        return user.getUserId() == MainApplication.userInfo.getUserId();
    }
}
