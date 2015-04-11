package com.metis.meishuquan.activity.circle;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.model.circle.UserAdvanceInfo;
import com.metis.meishuquan.util.ChatManager;
import com.metis.meishuquan.util.Utils;
import com.metis.meishuquan.view.circle.CircleTitleBar;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import io.rong.imlib.RongIMClient;

/**
 * Created by wudi on 4/11/2015.
 */
public class ChatFriendSelectionActivity extends Activity {
    private CircleTitleBar titleBar;
    private View.OnClickListener onClickListener;
    private ExpandableListView listView;
    private SearchView searchView;
    private CircleFriendListAdapter adapter;
    private String[] excludeList;

    private void getParams(Intent intent) {
        excludeList = intent.getStringArrayExtra("excludelist");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParams(getIntent());
        setContentView(R.layout.activity_circle_chatfriendselectionactivity);
        initView();
    }

    private void initView() {
        this.titleBar = (CircleTitleBar) this.findViewById(R.id.activity_circle_chatfriendselectionactivity_titlebar);
        titleBar.setText("message");
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: save changes
                finish();
            }
        };

        titleBar.setLeftButton("back", 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //init listview
        this.listView = (ExpandableListView) this.findViewById(R.id.activity_circle_chatfriendselectionactivity_list);
        this.listView.setGroupIndicator(null);
        this.listView.setBackgroundColor(Color.rgb(255, 255, 255));
        this.listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        this.searchView = (SearchView) this.findViewById(R.id.activity_circle_chatfriendselectionactivity_search);
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

    public void expandAll()
    {
        for ( int i = 0; i < adapter.getGroupCount(); i++ ) {
            listView.expandGroup(i);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getParams(intent);
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
                convertView = LayoutInflater.from(ChatFriendSelectionActivity.this).inflate(R.layout.fragment_topline_comment_list_item_tag, null);
            }

            convertView.setBackgroundColor(Color.rgb(230, 232, 237));
            TextView mTag = (TextView) convertView.findViewById(R.id.id_tv_listview_tag);
            mTag.setTextColor(Color.rgb(255, 83, 99));
            mTag.setText(name.toUpperCase());
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            TextView tv = new TextView(ChatFriendSelectionActivity.this);
            tv.setText(friendList.get(groupPosition).get(childPosition).getName());
            return tv;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
