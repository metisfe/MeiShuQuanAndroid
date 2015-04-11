package com.metis.meishuquan.util;

import android.text.TextUtils;
import android.util.Log;

import com.metis.meishuquan.MainApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import io.rong.imlib.RongIMClient;

/**
 * Created by wudi on 4/7/2015.
 */
public class ChatManager {
    public static String userId = "diwu";

    public static List<RongIMClient.Conversation> conversations;
    public static HashMap<String, RongIMClient.UserInfo> contactCache = new HashMap<String, RongIMClient.UserInfo>();
    public static HashMap<String, RongIMClient.Discussion> discussionCache = new HashMap<String, RongIMClient.Discussion>();

    public static boolean isDiscussionMine(RongIMClient.Discussion discussion) {
        if (discussion != null && !TextUtils.isEmpty(userId) && discussion != null && !TextUtils.isEmpty(discussion.getCreatorId())) {
            return userId.equals(discussion.getCreatorId());
        }

        return false;
    }

    public static RongIMClient.UserInfo getUserInfo(String targetId) {
        if (contactCache != null && contactCache.containsKey(targetId)) {
            return contactCache.get(targetId);
        }

        RongIMClient.UserInfo info = new RongIMClient.UserInfo(targetId, targetId, "");
        contactCache.put(targetId, info);
        return info;
    }

    public static RongIMClient.Discussion getDiscussion(String targetId) {
        if (discussionCache != null && discussionCache.containsKey(targetId)) {
            return discussionCache.get(targetId);
        }

        //TODO: this is fake data in real data remember to put myself to the front
        List<String> mlist = new ArrayList<>();
        mlist.add(userId);
        mlist.add("diwulechao2");
        RongIMClient.Discussion discussion = new RongIMClient.Discussion(targetId, targetId, userId, true, mlist);
        discussionCache.put(targetId, discussion);
        return discussion;
    }

    public static void onReceive(RongIMClient.Message message) {
        Log.d("im", "onReceive: targetId:" + message.getTargetId() + "sender Id:" + message.getSenderUserId() + "type:" + message.getConversationType().toString());
//        if (message.getConversationType() == RongIMClient.ConversationType.PRIVATE) {
//
//        }
    }

}
