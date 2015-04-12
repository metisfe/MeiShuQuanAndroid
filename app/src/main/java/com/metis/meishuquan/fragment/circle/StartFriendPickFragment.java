package com.metis.meishuquan.fragment.circle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.circle.ChatActivity;
import com.metis.meishuquan.model.circle.UserAdvanceInfo;
import com.metis.meishuquan.util.ChatManager;
import com.metis.meishuquan.view.circle.CircleTitleBar;
import com.metis.meishuquan.view.circle.ContactListItemView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import io.rong.imlib.RongIMClient;

/**
 * Created by wudi on 4/12/2015.
 */
public class StartFriendPickFragment extends Fragment {
    private CircleTitleBar titleBar;
    private ViewGroup rootView;
    private View.OnClickListener onClickListener;
    private ExpandableListView listView;
    private SearchView searchView;
    private CircleFriendListAdapter adapter;
    private ArrayList<String> excludeList;
    private HashSet<String> excludeSet = new HashSet<>();
    private HashSet<String> selectedSet = new HashSet<>();
    private String fromType, targetId, title;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_circle_startfriendpick, container, false);
        getParams();
        initView();

        return rootView;
    }

    private void getParams() {
        excludeList = getArguments().getStringArrayList("excludelist");
        fromType = getArguments().getString("fromtype");
        targetId = getArguments().getString("targetid");
        title = getArguments().getString("title");
        if (excludeList != null) {
            for (String id : excludeList) {
                excludeSet.add(id);
            }
        }
    }

    private void initView() {
        this.titleBar = (CircleTitleBar) rootView.findViewById(R.id.fragment_circle_startfriendpick_titlebar);
        titleBar.setText(title);
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: save changes
                if ("privateconfig".equals(fromType)) {
                    createDiscussion();
                } else if ("discussionconfig".equals(fromType)) {
                    addMemberToDiscussion();
                } else {
                    finish();
                }
            }
        };

        titleBar.setLeftButton("back", 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //init listview
        this.listView = (ExpandableListView) rootView.findViewById(R.id.fragment_circle_startfriendpick_list);
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
                String uid = ((UserAdvanceInfo) adapter.getChild(groupPosition, childPosition)).getUserId();
                if (excludeSet!=null && excludeSet.contains(uid))
                    return true;
                if (selectedSet.contains(uid)) selectedSet.remove(uid);
                else selectedSet.add(uid);
                adapter.notifyDataSetChanged();

                if (selectedSet.size() == 0) {
                    titleBar.setRightButton("", 0, null);
                } else {
                    titleBar.setRightButton("确认（" + selectedSet.size() + "）", 0, onClickListener);
                }

                return true;
            }
        });

        this.searchView = (SearchView) rootView.findViewById(R.id.fragment_circle_startfriendpick_search);
        this.searchView.setSubmitButtonEnabled(false);
        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //TODO:
                return false;
            }
        });

        this.adapter = new CircleFriendListAdapter();
        this.adapter.friendList = ChatManager.getGroupedFriendList();
        this.listView.setAdapter(adapter);
        expandAll();
    }

    public void expandAll() {
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            listView.expandGroup(i);
        }
    }

    private void createDiscussion() {
        final List<String> ulist = new ArrayList<String>(selectedSet);
        if (!selectedSet.contains(ChatManager.userId)) {
            ulist.add(ChatManager.userId);
        }

        if (!selectedSet.contains(targetId) && !TextUtils.isEmpty(targetId)) {
            ulist.add(targetId);
        }

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        MainApplication.rongClient.createDiscussion("Not Set", ulist, new RongIMClient.CreateDiscussionCallback() {
            @Override
            public void onSuccess(String s) {
                Log.d("circle", "discussion created, id: " + s);
                progressDialog.cancel();
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("title", "Group Chat (" + (selectedSet.size() + 1) + ")");
                intent.putExtra("targetId", s);
                intent.putExtra("type", RongIMClient.ConversationType.DISCUSSION.toString());
                startActivity(intent);

                ChatManager.discussionCache.put(s, new RongIMClient.Discussion(s, "Not Set", ChatManager.userId, true, ulist));
                //TODO: should also save to DB
                finish();
            }

            @Override
            public void onError(ErrorCode errorCode) {
                progressDialog.cancel();
                Toast.makeText(getActivity(), "error: " + errorCode.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void finish()
    {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void addMemberToDiscussion() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        //first we need to check the existing member list to prevent crash
        MainApplication.rongClient.getDiscussion(targetId, new RongIMClient.GetDiscussionCallback() {
            @Override
            public void onSuccess(final RongIMClient.Discussion discussion) {
                //now we can add
                final List<String> ulist = new ArrayList<String>();
                for (String id : selectedSet) {
                    if (!discussion.getMemberIdList().contains(id)) ulist.add(id);
                }

                final int totalCount = discussion.getMemberIdList().size() + ulist.size();

                if (ulist.size() > 0) {
                    MainApplication.rongClient.addMemberToDiscussion(targetId, ulist, new RongIMClient.OperationCallback() {
                        @Override
                        public void onSuccess() {
                            List<String> mlist = new ArrayList<String>(discussion.getMemberIdList());
                            for (String id : ulist) {
                                mlist.add(id);
                            }

                            discussion.setMemberIdList(mlist);
                            ChatManager.discussionCache.put(targetId, discussion);
                            //TODO: should also save to DB

                            progressDialog.cancel();
                            Intent intent = new Intent(getActivity(), ChatActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            intent.putExtra("title", "Group Chat (" + totalCount + ")");
                            intent.putExtra("targetId", targetId);
                            intent.putExtra("type", RongIMClient.ConversationType.DISCUSSION.toString());
                            startActivity(intent);
                        }

                        @Override
                        public void onError(ErrorCode errorCode) {
                            progressDialog.cancel();
                            Toast.makeText(getActivity(), "error: " + errorCode.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    progressDialog.cancel();
                    Toast.makeText(getActivity(), "no new member", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onError(ErrorCode errorCode) {
                progressDialog.cancel();
                Toast.makeText(getActivity(), "discussion not exist", Toast.LENGTH_LONG).show();
                finish();
            }
        });


    }

    class CircleFriendListAdapter extends BaseExpandableListAdapter {
        public List<List<UserAdvanceInfo>> friendList;
        public List<UserAdvanceInfo> queryResult = new ArrayList<>();

        @Override
        public int getGroupCount() {
            return friendList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return friendList.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return friendList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return friendList.get(groupPosition).get(childPosition);
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
            String name = "";
            name += friendList.get(groupPosition).get(0).getPinYin().charAt(0);
            if (name.equals("~")) name = "#";

            if (convertView == null) {
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

            UserAdvanceInfo info = friendList.get(groupPosition).get(childPosition);
            ((ContactListItemView) convertView).setCheckMode(info.getName(), info.getPortraitUri(), excludeSet.contains(info.getUserId()) ? -1 : (selectedSet.contains(info.getUserId()) ? 1 : 0));
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
