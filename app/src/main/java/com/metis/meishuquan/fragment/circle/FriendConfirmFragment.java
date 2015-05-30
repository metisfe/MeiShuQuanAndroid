package com.metis.meishuquan.fragment.circle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.circle.CMyFriendListModel;
import com.metis.meishuquan.model.circle.CPhoneFriend;
import com.metis.meishuquan.model.circle.CUserModel;
import com.metis.meishuquan.model.circle.MyFriendList;
import com.metis.meishuquan.model.circle.PhoneFriend;
import com.metis.meishuquan.model.circle.ReturnOnlyInfo;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.ChatManager;
import com.metis.meishuquan.view.circle.CircleTitleBar;
import com.metis.meishuquan.view.circle.ContactListItemView;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wudi on 4/12/2015.
 */
public class FriendConfirmFragment extends Fragment {
    public static final String CLASS_NAME=FriendConfirmFragment.class.getSimpleName();

    private ViewGroup rootView;
    private CircleTitleBar titleBar;
    private ListView listView;
    private View searchView;
    private CircleFriendListAdapter adapter;

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
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_circle_friendconfirmfragment, container, false);
        this.titleBar = (CircleTitleBar) rootView.findViewById(R.id.fragment_circle_friendconfirmfragment_titlebar);
        titleBar.setText("新的朋友");
        titleBar.setLeftButton("", R.drawable.bg_btn_arrow_left, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        //init listview
        this.listView = (ListView) rootView.findViewById(R.id.fragment_circle_friendconfirmfragment_list);

        this.searchView = rootView.findViewById(R.id.fragment_circle_friendconfirmfragment_search);
        this.searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnlineFriendSearchFragment onlineFriendSearchFragment = new OnlineFriendSearchFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                ft.add(R.id.content_container, onlineFriendSearchFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        this.adapter = new CircleFriendListAdapter();
        //TODO: cache
        listView.setAdapter(adapter);

        StringBuilder PATH = new StringBuilder("v1.1/Message/MyFriendList");
        PATH.append("?type=2&session=");//type:0全部，1好友列表，2历史好友列表
        PATH.append(MainApplication.userInfo.getCookie());

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();

        ApiDataProvider.getmClient().invokeApi(PATH.toString(), null,
                HttpGet.METHOD_NAME, null, MyFriendList.class,
                new ApiOperationCallback<MyFriendList>() {
                    @Override
                    public void onCompleted(MyFriendList result, Exception exception, ServiceFilterResponse response) {
                        progressDialog.cancel();
                        if (result != null && result.data != null && result.data.historyFirends != null) {
                            adapter.data = result.data.historyFirends;
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

        return rootView;
    }

    class CircleFriendListAdapter extends BaseAdapter {
        public List<CUserModel> data = new ArrayList<>();

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null || !(convertView instanceof ContactListItemView)) {
                convertView = new ContactListItemView(getActivity());
            }

            ((ContactListItemView) convertView).setAcceptMode(data.get(position).name, data.get(position).avatar, "申请理由，backend required", data.get(position).relation, data.get(position).relation == 5 ? new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringBuilder PATH = new StringBuilder("v1.1/Message/AddFriend");
                    PATH.append("?session=");
                    PATH.append(MainApplication.userInfo.getCookie());
                    PATH.append("&userId=");
                    PATH.append(data.get(position).userId);
                    PATH.append("&type=2");

                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.show();
                    ApiDataProvider.getmClient().invokeApi(PATH.toString(), null,
                            HttpGet.METHOD_NAME, null, ReturnOnlyInfo.class,
                            new ApiOperationCallback<ReturnOnlyInfo>() {
                                @Override
                                public void onCompleted(ReturnOnlyInfo result, Exception exception, ServiceFilterResponse response) {
                                    progressDialog.cancel();
                                    if (result != null && result.option != null && result.option.isSuccess()) {
                                        Toast.makeText(getActivity(), "已添加", Toast.LENGTH_LONG).show();
                                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent("refresh_friend_list"));
                                        getActivity().getSupportFragmentManager().popBackStack();
                                        data.get(position).relation = 1;
                                        notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(getActivity(), "添加失败", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            } : null);

            return convertView;
        }
    }
}