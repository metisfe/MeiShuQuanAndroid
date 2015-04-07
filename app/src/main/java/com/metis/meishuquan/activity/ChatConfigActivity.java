package com.metis.meishuquan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.util.ContactManager;
import com.metis.meishuquan.view.circle.CircleGridIcon;
import com.metis.meishuquan.view.circle.CircleTitleBar;
import com.metis.meishuquan.view.shared.SwitchButton;

import io.rong.imlib.RongIMClient;

/**
 * Created by wudi on 4/7/2015.
 */
public class ChatConfigActivity extends Activity {
    private CircleTitleBar titleBar;
    private ViewGroup nameGroup, clearGroup;
    private GridView gridView;
    private SwitchButton switchButton;
    private TextView leaveGroup;
    private FriendGridViewAdapter adapter;

    private String type, targetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_chatconfigactivity);
        this.gridView = (GridView) this.findViewById(R.id.activity_circle_chatconfigactivity_gridview);
        this.nameGroup = (ViewGroup) this.findViewById(R.id.activity_circle_chatconfigactivity_namegroup);
        this.switchButton = (SwitchButton) this.findViewById(R.id.activity_circle_chatconfigactivity_nodisturbswitch);
        this.clearGroup = (ViewGroup) this.findViewById(R.id.activity_circle_chatconfigactivity_clearhistorygroup);
        this.leaveGroup = (TextView) this.findViewById(R.id.activity_circle_chatconfigactivity_leavegroup);
        this.titleBar = (CircleTitleBar) this.findViewById(R.id.activity_circle_chatconfigactivity_titlebar);
        this.adapter = new FriendGridViewAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.type = getIntent().getStringExtra("type");
        this.targetId = getIntent().getStringExtra("targetId");

        setData();
    }

    private void setData() {
        if ("private".equals(type)) {
            this.leaveGroup.setVisibility(View.GONE);
            this.nameGroup.setVisibility(View.GONE);
            this.titleBar.setText(ContactManager.getUserNameFromCache(targetId));
            this.adapter.setPrivateData(targetId);
        } else {
            this.leaveGroup.setVisibility(View.VISIBLE);
            this.nameGroup.setVisibility(View.VISIBLE);
            this.titleBar.setText("Chat Info(" + ContactManager.getDiscussionUserCountFromCache(targetId) + ")");
            this.adapter.setPrivateData(targetId);
        }
    }

    class FriendGridViewAdapter extends BaseAdapter {
        public RongIMClient.Conversation conversation;
        public RongIMClient.UserInfo userInfo;
        public boolean isPrivate;

        @Override
        public int getCount() {
            if (isPrivate) {
                return 2;
            } else {
                int extra = ContactManager.getDiscussionIsMineFromCache(targetId) ? 2 : 1;
                return ContactManager.getDiscussionUserCountFromCache(targetId) + extra;
            }
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = new CircleGridIcon(ChatConfigActivity.this);

            CircleGridIcon icon = (CircleGridIcon) convertView;
            if (isPrivate) {
                if (position == 0) {
                    //icon.setData();
                } else {
                    icon.setPlusMinus(true, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
            } else {

            }

            return null;
        }

        private void setPrivateData(String targetId) {
            isPrivate = true;
        }

        private void setDiscussionData(String targetId) {
            isPrivate = false;
        }
    }
}
