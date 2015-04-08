package com.metis.meishuquan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.util.ChatManager;
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
            this.adapter.setPrivateData(targetId);
            this.titleBar.setText(adapter.getName());
        } else {
            this.leaveGroup.setVisibility(View.VISIBLE);
            this.nameGroup.setVisibility(View.VISIBLE);
            this.adapter.setDiscussionData(targetId);
            this.titleBar.setText("Chat Info(" + adapter.getMemberCount() + ")");
        }

        this.gridView.setAdapter(adapter);
    }

    class FriendGridViewAdapter extends BaseAdapter {
        public RongIMClient.Discussion discussion;
        public RongIMClient.UserInfo userInfo;
        public boolean isPrivate;

        @Override
        public int getCount() {
            if (isPrivate) {
                return 2;
            } else {
                int extra = ChatManager.isDiscussionMine(discussion) ? 2 : 1;
                if (discussion != null && discussion.getMemberIdList() != null) {
                    return discussion.getMemberIdList().size() + extra;
                }
                return 2;
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
                //if this is a private page
                if (position == 0) {
                    icon.setData(userInfo.getPortraitUri(), userInfo.getName(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                } else {
                    icon.setPlusMinus(true, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
            } else {
                //if this is a group chat page
                if (position < discussion.getMemberIdList().size()) {
                    RongIMClient.UserInfo info = ChatManager.getUserInfo(discussion.getMemberIdList().get(position));
                    icon.setData(info.getPortraitUri(), info.getName(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                } else if (position == discussion.getMemberIdList().size()) {
                    icon.setPlusMinus(true, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                } else {
                    icon.setPlusMinus(false, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
            }

            return convertView;
        }

        private void setPrivateData(String targetId) {
            isPrivate = true;
            this.userInfo = ChatManager.getUserInfo(targetId);
        }

        private void setDiscussionData(String targetId) {
            isPrivate = false;
            this.discussion = ChatManager.getDiscussion(targetId);
        }

        private String getName() {
            if (isPrivate) {
                if (userInfo != null) {
                    return userInfo.getName();
                }
            } else {
                if (discussion != null) {
                    return discussion.getName();
                }
            }
            return "";
        }

        private int getMemberCount() {
            if (isPrivate) return 1;
            if (discussion != null && discussion.getMemberIdList() != null)
                return discussion.getMemberIdList().size();
            return 1;
        }
    }
}
