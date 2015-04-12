package com.metis.meishuquan.fragment.circle;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.model.circle.UserAdvanceInfo;
import com.metis.meishuquan.util.ChatManager;
import com.metis.meishuquan.view.circle.ContactListItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wudi on 4/2/2015.
 */
public class ContactListFragment extends CircleBaseFragment {
    private ViewGroup rootView;
    private ExpandableListView listView;
    private CircleFriendListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_circle_contactlistfragment, container, false);
        this.listView = (ExpandableListView) rootView.findViewById(R.id.fragment_circle_contactlistfragment_list);
        this.listView.setGroupIndicator(null);
        this.listView.setBackgroundColor(Color.rgb(255, 255, 255));
        this.listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        this.listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (groupPosition == 0) {
                    if (childPosition == 0) {
                        //TODO: friend request page
                    } else {
                        //TODO: group list page
                    }
                    return true;
                }

                String uid = ((UserAdvanceInfo) adapter.getChild(groupPosition - 1, childPosition)).getUserId();
                //TODO: personal page
                return true;
            }
        });

        this.adapter = new CircleFriendListAdapter();
        this.adapter.friendList = ChatManager.getGroupedFriendList();
        this.adapter.fakeItems.add(new UserAdvanceInfo("新的朋友", R.drawable.view_circle_newfriend));
        this.adapter.fakeItems.add(new UserAdvanceInfo("群聊", R.drawable.view_circle_groupchat));
        this.listView.setAdapter(adapter);
        expandAll();

        return rootView;
    }

    @Override
    public void timeToSetTitleBar() {
        getTitleBar().setText("选择联系人");
        getTitleBar().setRightButton("Add", 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFriendFragment addFriendFragment = new AddFriendFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                ft.add(R.id.content_container, addFriendFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    public void expandAll() {
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            listView.expandGroup(i);
        }
    }

    class CircleFriendListAdapter extends BaseExpandableListAdapter {
        public List<UserAdvanceInfo> fakeItems = new ArrayList<>();
        public List<List<UserAdvanceInfo>> friendList;
        public List<UserAdvanceInfo> queryResult = new ArrayList<>();

        @Override
        public int getGroupCount() {
            return 1 + friendList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if (groupPosition == 0) return fakeItems.size();
            return friendList.get(groupPosition - 1).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            if (groupPosition == 0) {
                return fakeItems;
            }

            return friendList.get(groupPosition - 1);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            if (groupPosition == 0) {
                return fakeItems.get(childPosition);
            }
            return friendList.get(groupPosition - 1).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (groupPosition == 0) {
                return null;
            }

            String name = "";
            name += friendList.get(groupPosition - 1).get(0).getPinYin().charAt(0);
            if (name.equals("~")) name = "#";

            if (convertView == null ) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_topline_comment_list_item_tag, null);
            }

            convertView.setBackgroundColor(Color.rgb(230, 232, 237));
            TextView mTag = (TextView) convertView.findViewById(R.id.id_tv_listview_tag);
            mTag.setTextColor(Color.rgb(255, 83, 99));
            mTag.setText(name.toUpperCase());
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null || !(convertView instanceof ContactListItemView)) {
                convertView = new ContactListItemView(getActivity());
            }

            UserAdvanceInfo info;
            if (groupPosition == 0) {
                info = fakeItems.get(childPosition);
                ((ContactListItemView) convertView).setNormalMode(info.getName(), "", "", info.getResourceId(), true);
            } else {
                info = friendList.get(groupPosition - 1).get(childPosition);
                ((ContactListItemView) convertView).setNormalMode(info.getName(), info.getPortraitUri(), "", 0, false);
            }

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

}
