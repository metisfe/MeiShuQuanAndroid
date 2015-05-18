package com.metis.meishuquan.fragment.commons;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.MyFriendList;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by WJ on 2015/5/18.
 */
public class FriendChooseFragment extends Fragment {

    private static final String TAG = FriendChooseFragment.class.getSimpleName();

    private List<FriendDelegate> mFriendsList = new ArrayList<FriendDelegate>();

    private StickyGridHeadersGridView mGridView = null;

    private FriendAdapter mAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends_list, null, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGridView = (StickyGridHeadersGridView)view.findViewById(R.id.friends_list_view);
        mAdapter = new FriendAdapter(getActivity(), mFriendsList);
        mGridView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        UserInfoOperator.getInstance().getMyFriends(1, new UserInfoOperator.OnGetListener<MyFriendList>() {
            @Override
            public void onGet(boolean succeed, MyFriendList myFriendList) {
                if (succeed) {
                    Log.v(TAG, "myFriendList.size=" + myFriendList.getMyFirends().size());
                    List<User> friends = myFriendList.getMyFirends();
                    List<FriendDelegate> delegates = new ArrayList<FriendDelegate>();
                    for (User u : friends) {
                        FriendDelegate delegate = new FriendDelegate(u);
                        delegates.add(delegate);
                    }
                    Collections.sort(delegates);
                    mFriendsList.addAll(delegates);
                    mAdapter.notifyDataSetChanged();
                    //myFriendList.getMyFirends()
                }
            }
        });
    }

    private class FriendDelegate implements Comparable<FriendDelegate>{
        public User user;
        public boolean isSelected = false;

        private String pinyin;

        public FriendDelegate (User user, boolean selected) {
            this.user = user;
            isSelected = selected;
            String name = user.getName();
            if (TextUtils.isEmpty(name)) {
                pinyin = "#";
            } else {
                String[] array = PinyinHelper.toHanyuPinyinStringArray(name.charAt(0));
                if (array == null || array.length == 0) {
                    pinyin = "#";
                } else {
                    pinyin = array[0];
                }
            }

        }

        public FriendDelegate (User user) {
            this (user, false);
        }


        @Override
        public int compareTo(FriendDelegate friendDelegate) {
            return user.getName().compareTo(friendDelegate.user.getName());
        }
    }

    private class FriendAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter {

        private Context mContext = null;
        private List<FriendDelegate> mDataList = null;

        private FriendAdapter (Context context, List<FriendDelegate> delegates) {
            mContext = context;
            mDataList = delegates;
        }

        @Override
        public long getHeaderId(int position) {
            FriendDelegate delegate = getItem(position);
            String fistLetterUpper = (delegate.pinyin.charAt(0) + "").toUpperCase();
            return fistLetterUpper.hashCode();
        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            FriendDelegate delegate = getItem(position);
            TextView header = (TextView)LayoutInflater.from(mContext).inflate(R.layout.layout_friend_header, null);
            header.setText(delegate.pinyin.toUpperCase().charAt(0) + "");
            return header;
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public FriendDelegate getItem(int i) {
            return mDataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.layout_friend_item, null);
            }
            FriendDelegate delegate = getItem(i);
            CheckBox checkBox = (CheckBox)view.findViewById(R.id.friend_checkbox);
            ImageView provileIv = (ImageView)view.findViewById(R.id.friend_profile);
            TextView nameTv = (TextView)view.findViewById(R.id.friend_name);

            ImageLoaderUtils.getImageLoader(mContext).displayImage(delegate.user.getUserAvatar(), provileIv, ImageLoaderUtils.getRoundDisplayOptions(getResources().getDimensionPixelSize(R.dimen.friend_list_friend_profile_size)));
            nameTv.setText(delegate.user.getName());
            return view;
        }

    }
}
