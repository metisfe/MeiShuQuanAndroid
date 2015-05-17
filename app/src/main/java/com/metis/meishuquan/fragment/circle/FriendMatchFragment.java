package com.metis.meishuquan.fragment.circle;

import android.app.ProgressDialog;
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
import android.widget.SearchView;
import android.widget.TextView;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.circle.CPhoneFriend;
import com.metis.meishuquan.model.circle.UserAdvanceInfo;
import com.metis.meishuquan.model.circle.PhoneFriend;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.ChatManager;
import com.metis.meishuquan.view.circle.CircleTitleBar;
import com.metis.meishuquan.view.circle.ContactListItemView;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import org.apache.http.client.methods.HttpPost;

import java.util.ArrayList;
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
        StringBuilder PATH = new StringBuilder("v1.1/Message/GetFriendByPhoneNums");
        PATH.append("?&session=" + MainApplication.userInfo.getCookie());

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();

        ApiDataProvider.getmClient().invokeApi(PATH.toString(), list,
                HttpPost.METHOD_NAME, null, PhoneFriend.class,
                new ApiOperationCallback<PhoneFriend>() {
                    @Override
                    public void onCompleted(PhoneFriend result, Exception exception, ServiceFilterResponse response) {
                        if (result == null || result.data == null) {
                            result = new PhoneFriend();
                            result.data = new ArrayList<CPhoneFriend>();
                        }

                        progressDialog.cancel();
                        adapter.friendList = ChatManager.getGroupedFriendMatchList(result.data);
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
        public List<List<UserAdvanceInfo>> friendList;//匹配的好友

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

            final UserAdvanceInfo info = friendList.get(groupPosition).get(childPosition);
            ((ContactListItemView) convertView).setRequestMode(info.getName(), info.getPortraitUri(), info.mode ? null : new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestMessageFragment requestMessageFragment = new RequestMessageFragment();

                    Bundle args = new Bundle();
                    args.putString("targetid", info.getUserId());
                    requestMessageFragment.setArguments(args);

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                    ft.add(R.id.content_container, requestMessageFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
