package com.metis.meishuquan.fragment.circle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.circle.ChatActivity;
import com.metis.meishuquan.model.circle.UserAdvanceInfo;
import com.metis.meishuquan.push.PushType;
import com.metis.meishuquan.push.UnReadManager;
import com.metis.meishuquan.util.ChatManager;
import com.metis.meishuquan.view.circle.ContactListItemView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wudi on 4/2/2015.
 */
public class ContactListFragment extends CircleBaseFragment {
    public static final String CLASS_NAME = ContactListFragment.class.getSimpleName();
    private ViewGroup rootView;
    private ExpandableListView listView;
    private CircleFriendListAdapter adapter;

    private UserAdvanceInfo newFriendInfo = null, crowdChatInfo = null;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ChatManager.SetOnFriendListReceivedListener(new ChatManager.OnFriendListReceivedListener() {
                @Override
                public void onReceive() {
                    Log.d("circle", "refresh contact list");
                    if (adapter != null) {
                        adapter.friendList = ChatManager.getGroupedFriendList();
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
    };

    private UnReadManager.Observable mObservable = new UnReadManager.Observable() {
        @Override
        public void onChanged(String tag, int count, int delta) {
            if (newFriendInfo != null) {
                newFriendInfo.showRed = count > 0;
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UnReadManager.getInstance(getActivity()).registerObservable(PushType.FRIEND.getTag(), mObservable);
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(CLASS_NAME); //统计页面
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(CLASS_NAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_circle_contactlistfragment, container, false);
        this.listView = (ExpandableListView) rootView.findViewById(R.id.fragment_circle_contactlistfragment_list);
        this.listView.setGroupIndicator(null);
        this.listView.setBackgroundColor(getResources().getColor(R.color.bg_listview));
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
                        FriendConfirmFragment friendConfirmFragment = new FriendConfirmFragment();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                        ft.add(R.id.content_container, friendConfirmFragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    } else {
                        GroupListFragment groupListFragment = new GroupListFragment();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                        ft.add(R.id.content_container, groupListFragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                    return true;
                }

                String uid = ((UserAdvanceInfo) adapter.getChild(groupPosition, childPosition)).getUserId();
                //TODO: personal page
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("title", ((UserAdvanceInfo) adapter.getChild(groupPosition, childPosition)).getName());
                intent.putExtra("targetId", uid);
                intent.putExtra("type", "PRIVATE");
                getActivity().startActivity(intent);

//                Toast.makeText(getActivity(), "Enter personal page id: " + uid, Toast.LENGTH_LONG).show();

                LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter("refresh_friend_list"));
                return true;
            }
        });

        this.adapter = new CircleFriendListAdapter();
        this.adapter.friendList = ChatManager.getGroupedFriendList();
        newFriendInfo = new UserAdvanceInfo("新的朋友", R.drawable.icon_add_friend);
        int count = UnReadManager.getInstance(getActivity()).getCountByTag(PushType.FRIEND.getTag());
        newFriendInfo.showRed = count > 0;
        crowdChatInfo = new UserAdvanceInfo("群聊", R.drawable.icon_chat_group);
        this.adapter.fakeItems.add(newFriendInfo);
        this.adapter.fakeItems.add(crowdChatInfo);
        this.listView.setAdapter(adapter);
        expandAll();

        return rootView;
    }

//    @Override
//    public void onAttach(Activity activity) {
//        Log.d("circle", "on attach contact list");
//        super.onAttach(activity);
//        ChatManager.SetOnFriendListReceivedListener(new ChatManager.OnFriendListReceivedListener() {
//            @Override
//            public void onReceive() {
//                Log.d("circle", "refresh contact list");
//                if (adapter != null) {
//                    adapter.friendList = ChatManager.getGroupedFriendList();
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        });
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        Log.d("circle", "on detach contact list");
//        ChatManager.RemoveOnFriendListReceivedListener();
//        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
//    }


    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
        UnReadManager.getInstance(getActivity()).registerObservable(PushType.FRIEND.getTag(), mObservable);
        super.onDestroy();
    }

    @Override
    public void timeToSetTitleBar() {
        getTitleBar().setText(MainApplication.userInfo.getName().equals("") ? "联系人" : MainApplication.userInfo.getName());
        getTitleBar().setRightButton("", R.drawable.icon_circle_add_, new View.OnClickListener() {
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
                View v = new View(getActivity());
                v.setTag(1);
                return v;
            }

            String name = "";
            name += friendList.get(groupPosition - 1).get(0).getPinYin().charAt(0);
            if (name.equals("~")) name = "#";

            if (convertView == null || convertView.getTag() == null || ((Integer) convertView.getTag()) == 1) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_topline_comment_list_item_tag, null);
            }

            convertView.setBackgroundColor(getResources().getColor(R.color.common_color_e2e2e2));
            TextView mTag = (TextView) convertView.findViewById(R.id.id_tv_listview_tag);
            mTag.setTextColor(getResources().getColor(R.color.common_color_7e7e7e));
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
                ((ContactListItemView) convertView).setNormalMode(info.getUserId(), info.getName(), "", "", info.getResourceId(), true);
            } else {
                info = friendList.get(groupPosition - 1).get(childPosition);
                ((ContactListItemView) convertView).setNormalMode(info.getUserId(), info.getName(), "", info.getPortraitUri(), 0, false);
            }
            ((ContactListItemView) convertView).setRedDotVisiblity(info.showRed ? View.VISIBLE : View.GONE);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

}
