package com.metis.meishuquan.fragment.circle;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.circle.ChatActivity;
import com.metis.meishuquan.model.circle.CDiscussion;
import com.metis.meishuquan.model.circle.CPhoneFriend;
import com.metis.meishuquan.model.circle.MyGroupList;
import com.metis.meishuquan.model.circle.PhoneFriend;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.ChatManager;
import com.metis.meishuquan.util.Utils;
import com.metis.meishuquan.view.circle.CircleGroupListItemView;
import com.metis.meishuquan.view.circle.CircleTitleBar;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.RongIMClient;

/**
 * Created by wudi on 4/12/2015.
 */
public class GroupListFragment extends Fragment {
    private ViewGroup rootView;
    private CircleTitleBar titleBar;
    private TextView footerView;
    private ListView list;
    private CircleFriendListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_circle_grouplistfragment, container, false);
        this.titleBar = (CircleTitleBar) rootView.findViewById(R.id.fragment_circle_grouplistfragment_titlebar);
        titleBar.setText("群聊");
        titleBar.setLeftButton("返回", 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        this.footerView = new TextView(getActivity());
        footerView.setGravity(Gravity.CENTER);
        footerView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dip2px(getActivity(), 20)));
        this.list = (ListView) rootView.findViewById(R.id.fragment_circle_grouplistfragment_list);
        this.list.addFooterView(footerView);
        list.setFooterDividersEnabled(false);
        list.setDivider(null);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                CDiscussion dis = (CDiscussion) adapter.getItem(position);
                intent.putExtra("title", dis.name);
                intent.putExtra("targetId", dis.discussionId);
                intent.putExtra("type", RongIMClient.ConversationType.DISCUSSION.toString());
                getActivity().startActivity(intent);
            }
        });

        adapter = new CircleFriendListAdapter();
        List<String> ids = ChatManager.getMyWatchGroup();
        if (ids != null) {
            List<CDiscussion> discussions = new ArrayList<CDiscussion>();
            for (String id : ids) {
                RongIMClient.Discussion tp = ChatManager.getDiscussion(id);
                CDiscussion d = new CDiscussion();
                d.name = tp.getName();
                d.discussionId = tp.getId();
                d.count = tp.getMemberIdList().size();
                discussions.add(d);
            }

            adapter.discussions = discussions;
            if (discussions.size() > 0) {
                footerView.setText(String.valueOf(adapter.getCount()) + "个群聊");
            }
        }
        list.setAdapter(adapter);

        ChatManager.getMyWatchGroupFromApi(new ChatManager.OnGroupListReceivedListener() {
            @Override
            public void onReceive(List<String> ids) {
                if (ids != null) {
                    List<CDiscussion> discussions = new ArrayList<CDiscussion>();
                    for (String id : ids) {
                        RongIMClient.Discussion tp = ChatManager.getDiscussion(id);
                        CDiscussion d = new CDiscussion();
                        d.name = tp.getName();
                        d.discussionId = tp.getId();
                        d.count = tp.getMemberIdList().size();
                        discussions.add(d);
                    }

                    adapter.discussions = discussions;
                    if (adapter.getCount() > 0) {
                        footerView.setText(String.valueOf(adapter.getCount()) + "个群聊");
                    }

                    adapter.notifyDataSetChanged();
                }
            }
        });

        return rootView;
    }

    class CircleFriendListAdapter extends BaseAdapter {
        public List<CDiscussion> discussions = new ArrayList<>();

        @Override
        public int getCount() {
            return discussions.size();
        }

        @Override
        public Object getItem(int position) {
            return discussions.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new CircleGroupListItemView(getActivity());
            }

            ((CircleGroupListItemView) convertView).setData(discussions.get(position).name, discussions.get(position).count, "");
            return convertView;
        }
    }

}
