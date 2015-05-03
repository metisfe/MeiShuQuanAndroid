package com.metis.meishuquan.adapter.studio;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.metis.meishuquan.model.commons.User;

/**
 * Created by WJ on 2015/5/3.
 */
public class InfoAdapter extends BaseAdapter {

    private User mUser = null;

    //private

    public InfoAdapter (User user) {
        setUser(user, false);
    }

    public void setUser (User user) {
        setUser(user, true);
    }

    private void setUser (User user, boolean notify) {
        mUser = user;
        if (notify) {
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
