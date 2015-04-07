package com.metis.meishuquan.util;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import io.rong.imlib.RongIMClient;

/**
 * Created by wudi on 4/7/2015.
 */
public class ContactManager {
    public static String userId = "diwulechao";

    public static List<RongIMClient.Conversation> conversations;
    public static HashMap<String, List<RongIMClient.UserInfo>> contactCache;
    public static HashMap<String, List<RongIMClient.Discussion>> discussionCache;

    public static String getUserNameFromCache(String targetId) {
        RongIMClient.UserInfo info;
        if (contactCache != null && contactCache.containsKey(targetId)) {
            info = (RongIMClient.UserInfo) contactCache.get(targetId);
            if (info != null && !TextUtils.isEmpty(info.getName())) {
                return info.getName();
            }
        }

        return "";
    }

    public static int getDiscussionUserCountFromCache(String targetId) {
        RongIMClient.Discussion discussion;
        if (discussionCache != null && discussionCache.containsKey(targetId)) {
            discussion = (RongIMClient.Discussion) discussionCache.get(targetId);
            if (discussion != null && discussion.getMemberIdList() != null && discussion.getMemberIdList().size() > 0) {
                return discussion.getMemberIdList().size();
            }
        }

        return 1;
    }

    public static boolean getDiscussionIsMineFromCache(String targetId) {
        RongIMClient.Discussion discussion;
        if (!TextUtils.isEmpty(userId) && discussionCache != null && discussionCache.containsKey(targetId)) {
            discussion = (RongIMClient.Discussion) discussionCache.get(targetId);
            if (discussion != null && !TextUtils.isEmpty(discussion.getCreatorId())) {
                return userId.equals(discussion.getCreatorId());
            }
        }

        return false;
    }

//    public static String getUserNameFromCache(String targetId) {
//        RongIMClient.UserInfo info;
//        if (contactCache != null && contactCache.containsKey(targetId)) {
//            info = (RongIMClient.UserInfo) contactCache.get(targetId);
//            if (info != null && !TextUtils.isEmpty(info.getName())) {
//                return info.getName();
//            }
//        }
//
//        return "";
//    }
}
