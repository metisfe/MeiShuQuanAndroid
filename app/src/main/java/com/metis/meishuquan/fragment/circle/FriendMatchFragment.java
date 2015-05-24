package com.metis.meishuquan.fragment.circle;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.common.base.CharMatcher;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.circle.CPhoneFriend;
import com.metis.meishuquan.model.circle.CUserModel;
import com.metis.meishuquan.model.circle.MyFriendList;
import com.metis.meishuquan.model.circle.UserAdvanceInfo;
import com.metis.meishuquan.model.circle.PhoneFriend;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.ActivityUtils;
import com.metis.meishuquan.util.ChatManager;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.view.circle.CircleTitleBar;
import com.metis.meishuquan.view.circle.ContactListItemView;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        titleBar.setLeftButton("", R.drawable.bg_btn_arrow_left, new View.OnClickListener() {
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
        PATH.append("?session=" + MainApplication.userInfo.getCookie());

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();

        ApiDataProvider.getmClient().invokeApi(PATH.toString(), list,
                HttpPost.METHOD_NAME, null, PhoneFriend.class,
                new ApiOperationCallback<PhoneFriend>() {
                    @Override
                    public void onCompleted(PhoneFriend result, Exception exception, ServiceFilterResponse response) {
                        if (!result.option.isSuccess()) {
                            return;
                        }
                        if (result == null || result.data == null) {
                            result = new PhoneFriend();
                            result.data = new ArrayList<CPhoneFriend>();
                        }

                        progressDialog.cancel();

                        List<UserAdvanceInfo> waitToAddList = ChatManager.getGroupedFriendMatchList(result.data);
                        if (!waitToAddList.isEmpty()) {
                            Group group = new Group(getString(R.string.friends_wait_to_add, waitToAddList.size()), waitToAddList);
                            adapter.friendList.add(group);
                        }

                        Map<String, String> map = ChatManager.getPhoneNumberNameMap();
                        if (map.size() > 0) {
                            Set<String> set = map.keySet();
                            List<SimpleUser> userList = new ArrayList<SimpleUser>();
                            for (String phoneNumber : set) {
                                String name = map.get(phoneNumber);
                                SimpleUser user = new SimpleUser(name, phoneNumber);
                                userList.add(user);
                            }
                            if (userList.size() > 0) {
                                Group group = new Group(getString(R.string.friends_wait_to_ask, userList.size()), userList);
                                adapter.friendList.add(group);
                            }
                        }
                        StringBuilder PATH = new StringBuilder("v1.1/Message/MyFriendList");
                        PATH.append("?type=2&session=");//type:0全部，1好友列表，2历史好友列表
                        PATH.append(MainApplication.getSession());

                        final ProgressDialog progressDialog = new ProgressDialog(getActivity());

                        ApiDataProvider.getmClient().invokeApi(PATH.toString(), null,
                                HttpGet.METHOD_NAME, null, MyFriendList.class,
                                new ApiOperationCallback<MyFriendList>() {
                                    @Override
                                    public void onCompleted(MyFriendList result, Exception exception, ServiceFilterResponse response) {
                                        if (result.option.isSuccess()) {
                                            List<CUserModel> userModelList = result.data.myFirends;
                                            if (userModelList == null) {
                                                return;
                                            }
                                            final int size = userModelList.size();
                                            if (size > 0) {
                                                Group group = new Group(getString(R.string.friends_had_add, size), userModelList);
                                                adapter.friendList.add(group);
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                });
                        /*List<UserAdvanceInfo> userInfos = */
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
        public List<Group> friendList = new ArrayList<Group>();//匹配的好友

        //public List<UserAdvanceInfo> queryResult = new ArrayList<>();

        @Override
        public int getGroupCount() {
            return friendList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return friendList.get(groupPosition).mChildList.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return friendList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return friendList.get(groupPosition).mChildList.get(childPosition);
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
            Group group = friendList.get(groupPosition);
            UserInfoImpl userInfo = group.mChildList.get(0);
            /*name += .getPinYin().charAt(0);
            if (name.equals("~")) name = "#";*/

            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_topline_comment_list_item_tag, null);
            }

            convertView.setBackgroundColor(Color.rgb(230, 232, 237));
            TextView mTag = (TextView) convertView.findViewById(R.id.id_tv_listview_tag);
            mTag.setTextColor(Color.rgb(255, 83, 99));
            mTag.setText(group.getGroupName());
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            /*if (convertView == null || !(convertView instanceof ContactListItemView)) {
                convertView = new ContactListItemView(getActivity());
            }*/
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_contact_item, null);
            }
            Group group = friendList.get(groupPosition);
            final UserInfoImpl userInfo = group.mChildList.get(childPosition);
            ImageView profileIv = (ImageView)convertView.findViewById(R.id.contact_item_image);
            TextView nameTv = (TextView)convertView.findViewById(R.id.contact_item_name);
            Button btn = (Button)convertView.findViewById(R.id.contact_item_btn);
            //ContactListItemView contactListItemView = (ContactListItemView)convertView;
            if (userInfo instanceof UserAdvanceInfo) {
                final UserAdvanceInfo info = (UserAdvanceInfo)userInfo;
                nameTv.setText(info.getUserName());
                profileIv.setVisibility(View.GONE);
                btn.setText("添加");
                btn.setBackgroundResource(R.drawable.bg_contact_shape);
                btn.setOnClickListener(new View.OnClickListener() {
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
                /*contactListItemView.setRequestMode(info.getName(), info.getPortraitUri(), info.mode ? null : new View.OnClickListener() {
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
                });*/
            } else if (userInfo instanceof SimpleUser) {
                profileIv.setVisibility(View.GONE);
                nameTv.setText(userInfo.getUserName());
                btn.setText("邀请");
                btn.setBackgroundResource(R.drawable.bg_contact_outline);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityUtils.sendSms(getActivity(), userInfo.getUserTelephone(), "Hi, 我在美术圈，你可以通过我的手机号加我为好友#美术圈是你最好的朋友圈#下载地址:http://www.meishuquan.net/Down.aspx");
                    }
                });
            } else if (userInfo instanceof CUserModel) {
                CUserModel model = (CUserModel)userInfo;
                profileIv.setVisibility(View.VISIBLE);
                btn.setVisibility(View.GONE);
                ImageLoaderUtils.getImageLoader(getActivity()).displayImage(model.avatar, profileIv, ImageLoaderUtils.getNormalDisplayOptions(R.drawable.default_portrait_fang));
                nameTv.setText(userInfo.getUserName());
            }

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    public static interface UserInfoImpl {
        public String getUserId ();
        public String getUserName ();
        public String getUserTelephone ();
    }

    public class Group {
        private String groupName;
        private List<? extends UserInfoImpl> mChildList = null;

        public Group (String name, List<? extends UserInfoImpl> userInfos) {
            groupName = name;
            mChildList = userInfos;
        }

        public String getGroupName() {
            return groupName;
        }

        public List<? extends UserInfoImpl> getmChildList() {
            return mChildList;
        }
    }

    public class SimpleUser implements UserInfoImpl {

        private String mName = null;
        private String mTelephone = null;

        public SimpleUser (String name, String telephone) {
            mName = name;
            mTelephone = telephone;
        }

        @Override
        public String getUserId() {
            return mTelephone;
        }

        @Override
        public String getUserName() {
            return mName;
        }

        @Override
        public String getUserTelephone() {
            return mTelephone;
        }
    }
}
