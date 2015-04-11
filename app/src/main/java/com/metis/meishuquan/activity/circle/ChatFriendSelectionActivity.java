package com.metis.meishuquan.activity.circle;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SearchView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.util.ChatManager;
import com.metis.meishuquan.util.Utils;
import com.metis.meishuquan.view.circle.CircleTitleBar;

import java.util.ArrayList;

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

    private void initView()
    {
        this.titleBar = (CircleTitleBar) this.findViewById(R.id.activity_circle_chatfriendselectionactivity_titlebar);
        titleBar.setText("message");
        onClickListener = new View.OnClickListener(){
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
        this.listView.setTextFilterEnabled(true);
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
        prepareFriendData();
        this.listView.setAdapter(adapter);
    }

    private void prepareFriendData()
    {


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getParams(intent);
    }

    class CircleFriendListAdapter extends BaseExpandableListAdapter{

        @Override
        public int getGroupCount() {
            return 0;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 0;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            return null;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            return null;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}
