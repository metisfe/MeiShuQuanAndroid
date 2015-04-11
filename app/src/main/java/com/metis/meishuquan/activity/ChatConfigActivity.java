package com.metis.meishuquan.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ScrollView;
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
    private ScrollView scrollView;
    private EditText editText;
    private boolean onEditTextMode;

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
        this.scrollView = (ScrollView) this.findViewById(R.id.activity_circle_chatconfigactivity_scrollview);
        this.editText = (EditText) this.findViewById(R.id.activity_circle_chatconfigactivity_edittext);
        this.adapter = new FriendGridViewAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.type = getIntent().getStringExtra("type");
        this.targetId = getIntent().getStringExtra("targetId");

        setData();
    }

    @Override
    public void onBackPressed() {
        if (onEditTextMode)
        {
            setData();
            return;
        }

        super.onBackPressed();
    }

    private void setData() {
        onEditTextMode = false;
        this.scrollView.setVisibility(View.VISIBLE);
        this.editText.setVisibility(View.GONE);

        if ("private".equals(type)) {
            this.leaveGroup.setVisibility(View.GONE);
            this.nameGroup.setVisibility(View.GONE);
            this.adapter.setPrivateData(targetId);
            this.titleBar.setText(adapter.getName());
        } else {
            this.leaveGroup.setVisibility(View.VISIBLE);
            this.nameGroup.setVisibility(View.VISIBLE);
            this.nameGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEditTextMode = true;
                    scrollView.setVisibility(View.GONE);
                    editText.setVisibility(View.VISIBLE);
                    String name=adapter.getName();
                    if (!TextUtils.isEmpty(name))
                    {
                        editText.setText(name);
                        editText.setSelection(0,name.length());
                    }

                    editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, final boolean hasFocus) {
                            editText.post(new Runnable() {
                                @Override
                                public void run() {
                                    InputMethodManager imm = (InputMethodManager) ChatConfigActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    if (hasFocus)
                                    {
                                        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                                    }
                                    else
                                    {
                                        imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                                    }

                                }
                            });
                        }
                    });
                    editText.requestFocus();
                    titleBar.setText("edit group name");
                    titleBar.setLeftButton("back",0,new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setData();
                        }
                    });

                    titleBar.setRightButton("confirm",0,new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO: save name changed
                            setData();
                        }
                    });
                }
            });
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
