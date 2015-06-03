package com.metis.meishuquan.push;

/**
 * Created by WJ on 2015/6/2.
 */
public enum PushType {
    /*活动状态通知
    ActivityNotification =1,
    私信通知
    DirectMessageNotification =2,
    关注通知
    PayAttentionNotification =3,
    @我的
    ReferMeNotification = 4,
    评论我的
    CommentMeNotification=5,
    FriendNotification = 6,
         /// <summary>
         /// 申请好友推送
         /// </summary>

         ApplyFriendNotification=7,
         /// <summary>
         /// 新闻推送
         /// </summary>
         NewsNotification=8,
         /// <summary>
         ///  课程推送
         /// </summary>
         CourseNotifcation=9,
    */
    DEFAULT (0, "Default"),
    ACTIVITY (1, "ActivityNotification"),
    DIRECT_MESSAGE (2, "DirectMessageNotification"),
    PAY_ATTENTION (3, "PayAttentionNotification"),
    REFER_ME (4, "ReferMeNotification"),
    COMMENT_ME (5, "CommentMeNotification"),
    FRIEND (6, "FriendNotification"),
    APPLY_FRIEND (7, "ApplyFriendNotification"),
    NEWS (8, "NewsNotification"),
    COURSE (9, "CourseNotification");

    private int type;
    private String tag;

    PushType (int type, String tag) {
        this.type = type;
        this.tag = tag;
    }

    public static PushType getPushType (int type) {
        for (PushType t : values()) {
            if (t.type == type) {
                return t;
            }
        }
        /*throw new IllegalArgumentException ("no PushType found for type " + type + " in method getPushType");*/
        return DEFAULT;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
