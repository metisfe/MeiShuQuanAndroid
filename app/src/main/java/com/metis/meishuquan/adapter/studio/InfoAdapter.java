package com.metis.meishuquan.adapter.studio;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.manager.common.UserManager;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.view.shared.MyInfoBtn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJ on 2015/5/3.
 */
public class InfoAdapter extends BaseAdapter {

    private Context mContext = null;

    private User mUser = null;

    private OnInfoItemClickListener mInfoListener = null;

    private List<Item> mDataList = new ArrayList<Item>();

    private boolean canEdit = false;

    //private

    public InfoAdapter (Context context, User user) {
        mContext = context;
        setUser(user, false);
    }

    public void setUser (User user) {
        setUser(user, true);
    }

    private void setUser (User user, boolean notify) {
        mUser = user;
        canEdit = UserManager.isMySelf(user);
        mDataList.clear();
        mDataList.addAll(makeItemListFromUser(user));
        if (notify) {
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Item getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyInfoBtn btn = null;
        if (convertView == null) {
            btn = new MyInfoBtn(mContext);
        } else {
            btn = (MyInfoBtn)convertView;
        }
        final Item item = getItem(position);
        btn.setText(item.title);
        btn.setSecondaryText(item.content);
        btn.setImageVisible(false);
        btn.setArrowVisible(item.editable ? View.VISIBLE : View.GONE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInfoListener != null && item.editable) {
                    mInfoListener.onClick(v, item, mUser);
                }
            }
        });
        convertView = btn;
        return convertView;
    }

    public static class Item {
        public String title;
        public String content;

        public boolean editable = true;

        public Item (String title, String content) {
            this (title, content, false);
        }

        public Item (String title, String content, boolean editable) {
            this.title = title;
            this.content = content;
            this.editable = editable;
        }
    }

    private List<Item> makeItemListFromUser (User user) {
        List<Item> itemList = new ArrayList<Item>();
        boolean isStudent = false;//TODO

        if (isStudent) {
            itemList.add(new Item(
                    mContext.getString(R.string.info_nick),
                    user.getName(), canEdit
            ));
            itemList.add(new Item(
                    mContext.getString(R.string.info_meishuquan_id),
                    user.getAccout(), canEdit && TextUtils.isEmpty(user.getAccout())
            ));
            itemList.add(new Item(
                    mContext.getString(R.string.info_recents),
                    user.getSelfSignature(), canEdit
            ));
            itemList.add(new Item(
                    mContext.getString(R.string.info_gender),
                    user.getGender(), canEdit
            ));
            itemList.add(new Item(
                    mContext.getString(R.string.info_ages),
                    UserManager.caculateAgeByBirthday(user.getBirthday()) + "", canEdit
            ));
            itemList.add(new Item(
                    mContext.getString(R.string.info_constellation),
                    user.getHoroscope(), canEdit
            ));
            itemList.add(new Item(
                    mContext.getString(R.string.info_level),
                    user.getGrade(), false
            ));
            itemList.add(new Item(
                    mContext.getString(R.string.info_provience),
                    user.getRegion(), canEdit
            ));
            itemList.add(new Item(
                    mContext.getString(R.string.info_school),
                    "", canEdit
            ));
            //TODO
            /*itemList.add(new Item(
                    mContext.getString(R.string.info_school),
                    user.get
            ));*/
        } else {
            itemList.add(new Item(
                    mContext.getString(R.string.info_nick),
                    user.getName(), canEdit
            ));
            itemList.add(new Item(
                    mContext.getString(R.string.info_meishuquan_id),
                    user.getAccout(), canEdit && TextUtils.isEmpty(user.getAccout())
            ));
            itemList.add(new Item(
                    mContext.getString(R.string.info_gender),
                    user.getGender(), canEdit
            ));
            itemList.add(new Item(
                    mContext.getString(R.string.info_ages),
                    UserManager.caculateAgeByBirthday(user.getBirthday()) + "", canEdit
            ));
            itemList.add(new Item(
                    mContext.getString(R.string.info_constellation),
                    user.getHoroscope(), canEdit
            ));
            itemList.add(new Item(
                    mContext.getString(R.string.info_level),
                    user.getGrade(), false
            ));
            itemList.add(new Item(
                    mContext.getString(R.string.info_provience),
                    user.getRegion(), canEdit
            ));
            itemList.add(new Item(
                    mContext.getString(R.string.info_department),
                    "", canEdit //TODO
            ));
            itemList.add(new Item(
                    mContext.getString(R.string.info_department_address),
                    user.getLocationAddress(), canEdit
            ));
        }
        return itemList;
    }

    public void setOnInfoItemClickListener (OnInfoItemClickListener listener) {
        mInfoListener = listener;
    }

    public static interface OnInfoItemClickListener {
        public void onClick (View view, Item item, User user);
    }

    public void setNickName (String name) {
        updateInfo(R.string.info_nick, name);
    }

    public void setMeishuquanId (String meishuquanId) {
        updateInfo(R.string.info_meishuquan_id, meishuquanId);
    }

    public void setSelfSignature (String content) {
        updateInfo(R.string.info_recents, content);
    }

    public void setGender (String gender) {
        updateInfo(R.string.info_gender, gender);
    }

    public void setConstellation (String constellation) {
        updateInfo(R.string.info_constellation, constellation);
    }

    public void setBirthday (int year, int month, int day) {
        updateInfo(R.string.info_ages, UserManager.caculateAgeByBirthday(year, month, day) + "");
    }

    public void setDepartmentAddress (String address) {
        updateInfo(R.string.info_department_address, address);
    }

    public void setProvince (String province) {
        updateInfo(R.string.info_provience, province);
    }

    private void updateInfo (@StringRes int res, String content) {
        updateInfo(mContext.getString(res), content);
    }

    private void updateInfo (String key, String content) {
        final int length = mDataList.size();
        for (int i = 0; i < length; i++) {
            Item item = mDataList.get(i);
            if (item.title.equals(key)) {
                item.content = content;
                notifyDataSetChanged();
                return;
            }
        }
    }
}
