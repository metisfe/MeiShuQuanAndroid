package com.metis.meishuquan.util;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.model.circle.CDiscussion;
import com.metis.meishuquan.model.circle.CPhoneFriend;
import com.metis.meishuquan.model.circle.CUserModel;
import com.metis.meishuquan.model.circle.MyFriendList;
import com.metis.meishuquan.model.circle.MyGroupList;
import com.metis.meishuquan.model.circle.PhoneFriend;
import com.metis.meishuquan.model.circle.UserAdvanceInfo;
import com.metis.meishuquan.model.circle.UserInfoMulGet;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.message.TextMessage;

/**
 * Created by wudi on 4/7/2015.
 */
public class ChatManager {
    public static String userRongId = "";
    private static HashMap<String, CUserModel> contacts = new HashMap<String, CUserModel>();
    private static HashMap<String, RongIMClient.Discussion> discussions = new HashMap<String, RongIMClient.Discussion>();
    private static List<String> friends = new ArrayList<>();
    private static List<String> myWatchGroup = new ArrayList<>();

    private static OnReceivedListener onReceivedListener;
    private static OnFriendListReceivedListener onFriendListReceivedListener;

    public static List<String> getMyWatchGroup() {
        return myWatchGroup;
    }

    public static void setMyWatchGroup(List<String> group) {
        if (group != null)
            myWatchGroup = group;
    }

    public static void getMyWatchGroupFromApi(final OnGroupListReceivedListener onReceivedListener) {
        StringBuilder PATH = new StringBuilder("v1.1/Message/GetMyDiscussion");
        PATH.append("?&session=" + MainApplication.userInfo.getCookie());
        PATH.append("&userid=");
        PATH.append(MainApplication.userInfo.getUserId());

        ApiDataProvider.getmClient().invokeApi(PATH.toString(), null,
                HttpGet.METHOD_NAME, null, MyGroupList.class,
                new ApiOperationCallback<MyGroupList>() {
                    @Override
                    public void onCompleted(MyGroupList result, Exception exception, ServiceFilterResponse response) {
//                        Log.d("circle","test" + response.getContent());
                        if (result != null && result.option != null && result.option.isSuccess() && result.data != null) {
                            List<String> ids = new ArrayList<String>();
                            for (CDiscussion d : result.data) {
                                ids.add(d.discussionId);
                            }
                            onReceivedListener.onReceive(ids);
                            setMyWatchGroup(ids);
                        } else {
                            onReceivedListener.onReceive(null);
                        }
                    }
                });
    }

    public static boolean containUserInfo(String rongId) {
        return contacts.containsKey(rongId);
    }

    public static CUserModel getUserInfo(String rongId) {
        if (contacts.containsKey(rongId)) {
            return contacts.get(rongId);
        }

        if (rongId.equals(userRongId)) {
            CUserModel my = new CUserModel();
            my.name = MainApplication.userInfo.getName();
            my.rongCloud = MainApplication.userInfo.getRongCloudId();
            my.avatar = MainApplication.userInfo.getUserAvatar();
            putUserInfo(my);
            return my;
        }

        //for now don't get real data, view will try to get real data
        CUserModel info = new CUserModel();
        info.name = info.rongCloud = rongId;
        info.avatar = "";
        info.isFakeData = true;
        return info;
    }

    public static void putUserInfo(CUserModel userModel) {
        contacts.put(userModel.rongCloud, userModel);
        //TODO: save to DB
    }

    public static void putUserInfos(List<CUserModel> userModels) {
        if (userModels == null) return;
        for (CUserModel model : userModels) {
            if (model != null) {
                contacts.put(model.rongCloud, model);
            }
        }
        //TODO: save to DB
    }

    public static void putDiscussion(String targetId, RongIMClient.Discussion discussion) {
        normalizeDiscussion(discussion);
        discussions.put(targetId, discussion);
        //TODO: save to DB
    }

    public static RongIMClient.Discussion getDiscussion(final String targetId) {
        if (discussions.containsKey(targetId)) {
            return discussions.get(targetId);
        }

        //if not put fake data
        List<String> mlist = new ArrayList<>();
        mlist.add(userRongId);
        mlist.add(MainApplication.userInfo.getName());
        RongIMClient.Discussion discussion = new RongIMClient.Discussion(targetId, targetId, userRongId, true, mlist);

        //get real data from Rong
        if (MainApplication.rongClient != null) {
            MainApplication.rongClient.getDiscussion(targetId, new RongIMClient.GetDiscussionCallback() {

                @Override
                public void onSuccess(RongIMClient.Discussion discussion) {
                    ChatManager.putDiscussion(targetId, discussion);
                }

                @Override
                public void onError(ErrorCode errorCode) {
                }
            });
        }

        return discussion;
    }

    public static void putFriendList(List<CUserModel> models) {
        List<String> ids = new ArrayList<>();
        for (CUserModel model : models) {
//            Log.d("circle", "frind id:" + model.userId + "name:" + model.name + "rid:" + model.rongCloud);
            if (model != null && !TextUtils.isEmpty(model.rongCloud))
                ids.add(model.rongCloud);
        }

        friends = ids;
    }

    public static void clear() {
        friends.clear();
        myWatchGroup.clear();
        contacts.clear();
    }

    public static List<CUserModel> getFriendList() {
        List<CUserModel> flist = new ArrayList<>();
        for (String rid : friends) {
            if (contacts.containsKey(rid)) {
                flist.add(contacts.get(rid));
            }
        }

        return flist;
    }

    public static boolean isDiscussionMine(RongIMClient.Discussion discussion) {
        if (discussion != null && !TextUtils.isEmpty(userRongId) && discussion != null && !TextUtils.isEmpty(discussion.getCreatorId())) {
            return userRongId.equals(discussion.getCreatorId());
        }
        return false;
    }

    public static void onReceive(final RongIMClient.Message message) {
        Log.d("im", "onReceive: targetId:" + message.getTargetId() + "sender Id:" + message.getSenderUserId() + "type:" + message.getConversationType().toString());
        if (onReceivedListener != null) {
            switch (message.getConversationType()) {
                case PRIVATE:
                    final String uid = message.getSenderUserId();
                    if (!ChatManager.contacts.containsKey(uid)) {
                        List<String> list = new ArrayList<>();
                        list.add(uid);
                        getUserInfoFromApi(list, new OnUserInfoDataReceived() {
                            @Override
                            public void onReceive(List<CUserModel> models) {
                                putUserInfos(models);
                            }
                        });
                    } else {
                        onReceivedListener.onReceive(message);
                    }

                    break;
                case DISCUSSION:
                    final String targetId = message.getTargetId();
                    if (!discussions.containsKey(targetId) && MainApplication.rongClient != null) {
                        MainApplication.rongClient.getDiscussion(targetId, new RongIMClient.GetDiscussionCallback() {

                            @Override
                            public void onSuccess(RongIMClient.Discussion discussion) {
                                putDiscussion(targetId, discussion);
                                onReceivedListener.onReceive(message);
                            }

                            @Override
                            public void onError(ErrorCode errorCode) {
                                onReceivedListener.onReceive(message);
                            }
                        });
                    } else {
                        onReceivedListener.onReceive(message);
                    }
                    break;
            }
        }
    }

    public static void refreshFriendData() {
        StringBuilder PATH = new StringBuilder("v1.1/Message/MyFriendList");
        PATH.append("?type=1&session=");
        PATH.append(MainApplication.getSession());

        ApiDataProvider.getmClient().invokeApi(PATH.toString(), null,
                HttpGet.METHOD_NAME, null, MyFriendList.class,
                new ApiOperationCallback<MyFriendList>() {
                    @Override
                    public void onCompleted(MyFriendList result, Exception exception, ServiceFilterResponse response) {
                        if (result != null && result.option != null && result.option.isSuccess() && result.data.myFirends != null) {
                            Log.d("circle", "get flist size:" + result.data.myFirends.size());
                            ChatManager.clear();
                            ChatManager.putFriendList(result.data.myFirends);
                            ChatManager.putUserInfos(result.data.myFirends);
                            if (onFriendListReceivedListener != null) {
                                onFriendListReceivedListener.onReceive();
                            }
                        }
                    }
                });
    }

    public static List<List<UserAdvanceInfo>> getGroupedFriendList() {
        List<UserAdvanceInfo> fList = new ArrayList<>();
        List<List<UserAdvanceInfo>> ret = new ArrayList<>();
        for (String id : friends) {
            CUserModel user = getUserInfo(id);
            UserAdvanceInfo ainfo = new UserAdvanceInfo(user.rongCloud, user.name, user.avatar);
            fList.add(ainfo);
        }

        Collections.sort(fList, new Comparator<UserAdvanceInfo>() {
            @Override
            public int compare(UserAdvanceInfo user1, UserAdvanceInfo user2) {

                return user1.getPinYin().compareTo(user2.getPinYin());
            }
        });

        char lastChar = 0;
        for (UserAdvanceInfo info : fList) {
            if (lastChar != info.getPinYin().charAt(0)) {
                //new group found
                List<UserAdvanceInfo> tl = new ArrayList<UserAdvanceInfo>();
                tl.add(info);
                ret.add(tl);
                lastChar = info.getPinYin().charAt(0);
            } else {
                //old group
                ret.get(ret.size() - 1).add(info);
            }
        }

        return ret;
    }

    public static List<List<UserAdvanceInfo>> getGroupedFriendMatchList(List<CPhoneFriend> friends) {
        List<UserAdvanceInfo> fList = new ArrayList<>();
        List<List<UserAdvanceInfo>> ret = new ArrayList<>();
        for (CPhoneFriend friend : friends) {
            RongIMClient.UserInfo info = new RongIMClient.UserInfo(String.valueOf(friend.userid), friend.userNickName, friend.userAvatar);
            if (TextUtils.isEmpty(info.getName())) info.setName(String.valueOf(friend.userid));
            UserAdvanceInfo ainfo = new UserAdvanceInfo(info);
            ainfo.mode = friend.isFriend == 0 ? false : true;
            fList.add(ainfo);
        }

        Collections.sort(fList, new Comparator<UserAdvanceInfo>() {
            @Override
            public int compare(UserAdvanceInfo user1, UserAdvanceInfo user2) {
                return user1.getPinYin().compareTo(user2.getPinYin());
            }
        });

        char lastChar = 0;
        for (UserAdvanceInfo info : fList) {
            if (lastChar != info.getPinYin().charAt(0)) {
                //new group found
                List<UserAdvanceInfo> tl = new ArrayList<UserAdvanceInfo>();
                tl.add(info);
                ret.add(tl);
                lastChar = info.getPinYin().charAt(0);
            } else {
                //old group
                ret.get(ret.size() - 1).add(info);
            }
        }

        return ret;
    }

    private static void normalizeDiscussion(RongIMClient.Discussion discussion) {
        if (discussion == null) return;
        List<String> mlist = discussion.getMemberIdList();
        if (mlist == null) return;
        int pos = 0;
        String firstString = "";
        for (String id : mlist) {
            if (pos == 0) firstString = id;
            if (id.equals(ChatManager.userRongId) && pos > 0) {
                mlist.set(pos, firstString);
                mlist.set(0, id);
            }
            pos++;
        }
    }

    public static void SetOnReceivedListener(OnReceivedListener listener) {
        onReceivedListener = listener;
    }

    public static void SetOnFriendListReceivedListener(OnFriendListReceivedListener listener) {
        onFriendListReceivedListener = listener;
    }

    public static void RemoveOnReceivedListener() {
        onReceivedListener = null;
    }

    public static void RemoveOnFriendListReceivedListener() {
        onFriendListReceivedListener = null;
    }

    public static String getConversationTitle(RongIMClient.Conversation conversation) {
        if (conversation == null) {
            return "null";
        }
        if (!TextUtils.isEmpty(conversation.getConversationTitle())) {
            return conversation.getConversationTitle();
        }
        switch (conversation.getConversationType()) {
            case PRIVATE:
                String title = ChatManager.getUserInfo(conversation.getTargetId()).name;
                if (TextUtils.isEmpty(title)) {
                    title = "Some one";
                }
                return title;
            case DISCUSSION:
                title = ChatManager.getDiscussion(conversation.getTargetId()).getName();
                if (TextUtils.isEmpty(title)) {
                    title = "Some discussion";
                }
                return title;
            default:
                return "some other type";
        }
    }

    public static String getLastString(RongIMClient.Conversation conversation) {
        if (conversation != null && conversation.getLatestMessage() != null) {
            RongIMClient.MessageContent content = conversation.getLatestMessage();
            if (content instanceof TextMessage) {
                return ((TextMessage) content).getContent();
            }
        }

        return "";
    }

    public static List<String> getPhoneNumberList() {
        List<String> ret = new ArrayList<>();
        Cursor phones = MainApplication.UIContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phoneNumber = normalizePhoneNumber(phoneNumber);
            if (!TextUtils.isEmpty(phoneNumber)) ret.add(phoneNumber);
        }
        phones.close();
        return ret;
    }

    private static String normalizePhoneNumber(String s) {
        if (s == null || s.length() < 11) return null;
        s = s.substring(s.length() - 11);
        if (s.charAt(0) != '1') return null;
        return s;
    }

    public static void getUserInfoFromApi(List<String> rids, final OnUserInfoDataReceived onUserInfoDataReceived) {
        StringBuilder PATH = new StringBuilder("v1.1/Message/GetUserInfoByRongCouldId");
        PATH.append("?&session=" + MainApplication.userInfo.getCookie());

        ApiDataProvider.getmClient().invokeApi(PATH.toString(), rids,
                HttpPost.METHOD_NAME, null, UserInfoMulGet.class,
                new ApiOperationCallback<UserInfoMulGet>() {
                    @Override
                    public void onCompleted(UserInfoMulGet result, Exception exception, ServiceFilterResponse response) {
                        if (onUserInfoDataReceived != null) {
                            if (result == null || result.data == null || result.data.size() == 0) {
                                onUserInfoDataReceived.onReceive(null);
                            } else {
                                onUserInfoDataReceived.onReceive(result.data);
                            }
                        }
                    }
                });
    }

    public interface OnReceivedListener {
        public void onReceive(RongIMClient.Message message);
    }

    public interface OnUserInfoDataReceived {
        public void onReceive(List<CUserModel> models);
    }

    public interface OnFriendListReceivedListener {
        public void onReceive();
    }

    public interface OnGroupListReceivedListener {
        public void onReceive(List<String> ids);
    }
}
