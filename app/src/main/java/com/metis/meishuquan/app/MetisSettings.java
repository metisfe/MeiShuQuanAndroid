package com.metis.meishuquan.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.metis.meishuquan.model.commons.User;


/**
 * Created by Tata on 2015/6/2.
 */
public class MetisSettings {

    private Context mContext;
    private SharedPreferences mGlobalPreferences;
    public static final String SHARED_PREFERENCE_NAME = "com.metis.meishuquan";


    public MetisSettings(Context mContext) {
        this.mContext = mContext;
        this.mGlobalPreferences = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public final StringPreference USER_ID = new StringPreference("USER_id", null);
    public final StringPreference NAME = new StringPreference("NAME", null);
    public final StringPreference MAILBOX = new StringPreference("MAILBOX", null);
    public final StringPreference GENDER = new StringPreference("GENDER", null);
    public final StringPreference PHONE = new StringPreference("PHONE", null);
    public final StringPreference SELFINTRODUCE = new StringPreference("SELFINTRODUCE", null);
    public final StringPreference GRADE = new StringPreference("GRADE", null);
    public final StringPreference REGION = new StringPreference("REGION", null);
    public final StringPreference USERAVATAR = new StringPreference("USERAVATAR", null);
    public final StringPreference AVATAR = new StringPreference("AVATAR", null);
    public final StringPreference USERROLE = new StringPreference("USERROLE", null);
    public final StringPreference BIRTHDAY = new StringPreference("BIRTHDAY", null);
    public final StringPreference ACCOUT = new StringPreference("ACCOUT", null);
    public final StringPreference RELATIONTYPE = new StringPreference("RELATIONTYPE", null);
    public final StringPreference REGISTRATIONDATE = new StringPreference("REGISTRATIONDATE", null);
    public final StringPreference COOKIE = new StringPreference("COOKIE", null);
    public final StringPreference RONGCLOUDID = new StringPreference("RONGCLOUDID", null);

    public final StringPreference TOKEN = new StringPreference("TOKEN", null);
    public final StringPreference GOODSUBJECTS = new StringPreference("GOODSUBJECTS", null);
    public final StringPreference SELFSIGNATURE = new StringPreference("SELFSIGNATURE", null);
    public final StringPreference LOCATIONSTUDIO = new StringPreference("LOCATIONSTUDIO", null);
    public final StringPreference LOCATIONSCHOOL = new StringPreference("LOCATIONSCHOOL", null);
    public final StringPreference BACKGROUNDIMG = new StringPreference("BACKGROUNDIMG", null);
    public final StringPreference USERRESUME = new StringPreference("USERRESUME", null);
    public final StringPreference STUDIO = new StringPreference("STUDIO", null);
    public final StringPreference FANSNUM = new StringPreference("FANSNUM", null);
    public final StringPreference FOCUSNUM = new StringPreference("FOCUSNUM", null);
    public final StringPreference APPLOGINSTATE = new StringPreference("APPLOGINSTATE", null);
    public final StringPreference RONGLOGINSTATE = new StringPreference("RONGLOGINSTATE", null);




    public void persistentUser(User user) {
        if (user==null)
            return;
      USER_ID.setValue(String.valueOf(user.getUserId()));
        NAME.setValue(user.getName());
        MAILBOX.setValue(user.getMailbox());
        GENDER.setValue(user.getGender());
        PHONE.setValue(user.getPhoneNum());
        SELFINTRODUCE.setValue(user.getSelfIntroduce());
        GRADE.setValue(user.getGrade());
        REGION.setValue(String.valueOf(user.getRegion()));
        USERAVATAR.setValue(user.getUserAvatar());
        AVATAR.setValue(user.getAvatar());
        USERROLE.setValue(String.valueOf(user.getUserRole()));
        BIRTHDAY.setValue(user.getBirthday() );
        ACCOUT.setValue(user.getAccout());
        RELATIONTYPE.setValue(String.valueOf(user.getRelationType()));
        REGISTRATIONDATE.setValue(user.getRegistrationDate());
        COOKIE.setValue(user.getCookie() );
        RONGCLOUDID.setValue(user.getRongCloudId() );
        TOKEN.setValue(user.getToken());
        GOODSUBJECTS.setValue(user.getGoodSubjects());
        SELFSIGNATURE.setValue(user.getSelfSignature());
        LOCATIONSTUDIO.setValue(String.valueOf(user.getLocationStudio()));
        LOCATIONSCHOOL.setValue(user.getLocationSchool());
        BACKGROUNDIMG.setValue(user.getBackGroundImg());
        USERRESUME.setValue(user.getUserResume());
        STUDIO.setValue(String.valueOf(user.getStudio()));
        FANSNUM.setValue(String.valueOf(user.getFansNum()));
        FOCUSNUM.setValue(String.valueOf(user.getFocusNum()));
        APPLOGINSTATE.setValue(String.valueOf(user.getAppLoginState().ordinal()));
        RONGLOGINSTATE.setValue(String.valueOf(user.getRongLoginState().ordinal()));
    }


    /**
     * String参数保存
     */
    public class StringPreference extends CommonPreference<String> {

        public StringPreference(String id, String defaultValue) {
            super(id, defaultValue);
        }

        @Override
        public String getValue() {
            return mGlobalPreferences.getString(getId(), getDefaultValue());
        }

        @Override
        public boolean setValue(String val) {
            return mGlobalPreferences.edit().putString(getId(), val).commit();
        }
    }

    public abstract class CommonPreference<T> {
        private final String id;
        private T defaultValue;

        /**
         * @param id           数据保存的Key
         * @param defaultValue 初始值
         */
        public CommonPreference(String id, T defaultValue) {
            this.id = id;
            this.defaultValue = defaultValue;
        }

        protected T getDefaultValue() {
            return defaultValue;
        }

        public abstract T getValue();

        public abstract boolean setValue(T val);

        public String getId() {
            return id;
        }

        /**
         * 重置为初始值
         */
        public void resetToDefault() {
            setValue(getDefaultValue());
        }
    }
}
