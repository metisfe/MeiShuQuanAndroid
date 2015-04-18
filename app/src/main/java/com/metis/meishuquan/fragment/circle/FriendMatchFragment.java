package com.metis.meishuquan.fragment.circle;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.circle.CPhoneFriend;
import com.metis.meishuquan.model.circle.UserAdvanceInfo;
import com.metis.meishuquan.model.contract.ReturnGsonInfo;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.ChatManager;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.view.circle.CircleTitleBar;
import com.metis.meishuquan.view.circle.ContactListItemView;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by wudi on 4/17/2015.
 */
public class FriendMatchFragment extends Fragment {
    private CircleTitleBar titleBar;
    private ViewGroup rootView;
    private ExpandableListView listView;
    private SearchView searchView;
    private CircleFriendListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_circle_friendmatch, container, false);
        initView();

        return rootView;
    }

    private void initView() {
        this.titleBar = (CircleTitleBar) rootView.findViewById(R.id.fragment_circle_friendmatch_titlebar);
        titleBar.setText("通讯录联系人");
        titleBar.setLeftButton("back", 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        titleBar.setRightButton("完成", 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        //init listview
        this.listView = (ExpandableListView) rootView.findViewById(R.id.fragment_circle_friendmatch_list);
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
                //TODO: should open personal page

                return true;
            }
        });

        this.searchView = (SearchView) rootView.findViewById(R.id.fragment_circle_friendmatch_search);
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

        //TODO: should use non-UI thread to read DB
        List<String> list = ChatManager.getPhoneNumberList();
        String PATH = "v1.1/Message/GetFriendByPhoneNums";
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();

        ApiDataProvider.getmClient().invokeApi(PATH, list,
                HttpPost.METHOD_NAME, null, (Class<ReturnGsonInfo<List<CPhoneFriend>>>) new ReturnGsonInfo<List<CPhoneFriend>>().getClass(),
                new ApiOperationCallback<ReturnGsonInfo<List<CPhoneFriend>>>() {
                    @Override
                    public void onCompleted(ReturnGsonInfo<List<CPhoneFriend>> result, Exception exception, ServiceFilterResponse response) {
                        if (result == null || result.getData() == null) {
                            List<CPhoneFriend> fakeList = new ArrayList<CPhoneFriend>();

                            CPhoneFriend cPhoneFriend = new CPhoneFriend();
                            cPhoneFriend.IsFriend = 1;
                            cPhoneFriend.PhoneNumber = "158274";
                            cPhoneFriend.UserNickName = "友好大白";
                            cPhoneFriend.UserAvatar = "http://www.google.com.hk/imgres?imgurl=http://img1.wikia.nocookie.net/__cb20140912133822/disney/images/6/6f/Baymax_Disney_INFINITY.png&imgrefurl=http://disney.wikia.com/wiki/Baymax&h=295&w=275&tbnid=AhZNjnTb0zLpcM:&zoom=1&docid=iJRsmavDXZMTXM&hl=zh-CN&ei=6_8wVdHNAcWOmwW6xYHACw&tbm=isch&ved=0CCUQMygKMAo";

                            fakeList.add(cPhoneFriend);

                            cPhoneFriend = new CPhoneFriend();
                            cPhoneFriend.IsFriend = 0;
                            cPhoneFriend.PhoneNumber = "213123";
                            cPhoneFriend.UserNickName = "战斗大白";
                            cPhoneFriend.UserAvatar = "http://www.google.com.hk/imgres?imgurl=http://img2.wikia.nocookie.net/__cb20140728161309/disney/images/3/3b/Baymax_with_a_Rose_in_his_hand.png&imgrefurl=http://disney.wikia.com/wiki/Baymax/Gallery&h=960&w=960&tbnid=houTI6OGLkKZbM:&zoom=1&docid=U2t5iSvGKlMqdM&hl=zh-CN&ei=6_8wVdHNAcWOmwW6xYHACw&tbm=isch&ved=0CBwQMygBMAE";
                            fakeList.add(cPhoneFriend);
                            result = new ReturnGsonInfo<List<CPhoneFriend>>(fakeList);
                        }

                        progressDialog.cancel();
                        adapter.friendList = ChatManager.getGroupedFriendMatchList(result.getData());
                        listView.setAdapter(adapter);
                        expandAll();
                    }
                });
    }

    public void expandAll() {
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            listView.expandGroup(i);
        }
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
            //((ContactListItemView) convertView).setCheckMode(info.getName(), info.getPortraitUri(), excludeSet.contains(info.getUserId()) ? -1 : (selectedSet.contains(info.getUserId()) ? 1 : 0));
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
