package com.metis.meishuquan.model.circle;

import com.google.gson.annotations.SerializedName;
import com.metis.meishuquan.fragment.circle.FriendMatchFragment;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by jiaxh on 4/18/2015.
 */

public class CUserModel extends RealmObject implements Serializable, FriendMatchFragment.UserInfoImpl {

    @SerializedName("userId")
    public int userId;

    @SerializedName("name")
    public String name;//昵称

    @SerializedName("remarkName")
    public String remarkName;//备注名

    @SerializedName("avatar")
    public String avatar;

    @SerializedName("grade")
    public String grade;

    @SerializedName("identity")
    public int identity;

    @SerializedName("relation")
    public int relation;

    @SerializedName("account")
    public String account;

    @SerializedName("rongCloud")
    public String rongCloud;

    @SerializedName("isFakeData")
    public boolean isFakeData;

    @SerializedName("userRole")
    public int userRole;

    public String subName;

    @Override
    public String getUserId() {
        return userId + "";
    }

    @Override
    public String getUserName() {
        return name;
    }

    @Override
    public String getUserTelephone() {
        return "";
    }

    @Override
    public String getPinYin() {
        char[] chars = getUserName().toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            String[] strings = PinyinHelper.toHanyuPinyinStringArray(c);
            if (strings == null || strings.length == 0) {
                sb.append(c);
            } else {
                sb.append(strings[0]);
            }
        }
        return sb.toString();
    }
}
