package com.metis.meishuquan.activity.circle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.meishuquan.MainActivity;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.framework.util.ThreadPool;
import com.metis.meishuquan.model.circle.CUserModel;
import com.metis.meishuquan.model.circle.MyFriendList;
import com.metis.meishuquan.model.circle.ReturnOnlyInfo;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.ChatManager;
import com.metis.meishuquan.util.Utils;
import com.metis.meishuquan.util.ViewUtils;
import com.metis.meishuquan.view.circle.CircleGridIcon;
import com.metis.meishuquan.view.circle.CircleTitleBar;
import com.metis.meishuquan.view.shared.SwitchButton;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import org.apache.http.client.methods.HttpGet;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.RongIMClient;

/**
 * Created by wudi on 4/7/2015.
 */
public class ChatConfigActivity extends FragmentActivity {
    private CircleTitleBar titleBar;
    private ViewGroup nameGroup, clearGroup, watchGroup;
    private GridView gridView;
    private SwitchButton switchButton, watchButton;
    private TextView leaveGroup;
    private FriendGridViewAdapter adapter;
    private ScrollView scrollView;
    private EditText editText;
    private boolean onEditTextMode;

    private String targetId;
    private RongIMClient.ConversationType type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_chatconfigactivity);
        this.gridView = (GridView) this.findViewById(R.id.activity_circle_chatconfigactivity_gridview);
        this.nameGroup = (ViewGroup) this.findViewById(R.id.activity_circle_chatconfigactivity_namegroup);
        this.switchButton = (SwitchButton) this.findViewById(R.id.activity_circle_chatconfigactivity_nodisturbswitch);
        this.watchButton = (SwitchButton) this.findViewById(R.id.activity_circle_chatconfigactivity_watchswitch);

        this.watchGroup = (ViewGroup) this.findViewById(R.id.activity_circle_chatconfigactivity_watchgroup);
        this.clearGroup = (ViewGroup) this.findViewById(R.id.activity_circle_chatconfigactivity_clearhistorygroup);
        clearGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainApplication.rongIM != null)
                    MainApplication.rongIM.clearMessages(ChatConfigActivity.this, type, targetId);
            }
        });

        this.leaveGroup = (TextView) this.findViewById(R.id.activity_circle_chatconfigactivity_leavegroup);
        this.titleBar = (CircleTitleBar) this.findViewById(R.id.activity_circle_chatconfigactivity_titlebar);
        this.scrollView = (ScrollView) this.findViewById(R.id.activity_circle_chatconfigactivity_scrollview);
        this.editText = (EditText) this.findViewById(R.id.activity_circle_chatconfigactivity_edittext);
        this.adapter = new FriendGridViewAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.type = RongIMClient.ConversationType.valueOf(getIntent().getStringExtra("type"));
        this.targetId = getIntent().getStringExtra("targetId");

        setData();
        refreshDataFromRong();
    }

    private void refreshDataFromRong() {
        if (type == RongIMClient.ConversationType.DISCUSSION && MainApplication.rongClient != null) {
            MainApplication.rongClient.getDiscussion(targetId, new RongIMClient.GetDiscussionCallback() {
                @Override
                public void onSuccess(final RongIMClient.Discussion discussion) {
                    ChatManager.putDiscussion(discussion.getId(), discussion);
                    ChatManager.getUserInfoFromApi(discussion.getMemberIdList(), new ChatManager.OnUserInfoDataReceived() {
                        @Override
                        public void onReceive(List<CUserModel> models) {
                            ChatManager.putUserInfos(models);
                            if (adapter != null) {
                                adapter.discussion = discussion;
                                setGridViewHeight(adapter.getCount());
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }

                @Override
                public void onError(ErrorCode errorCode) {
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (onEditTextMode) {
            setData();
            return;
        }

        super.onBackPressed();
    }

    private void setGridViewHeight(int itemNumber) {
        ViewGroup.LayoutParams params = this.gridView.getLayoutParams();
        params.height = ((itemNumber - 1) / 4 + 1) * Utils.dip2px(this, 90) - Utils.dip2px(this, 20);
        gridView.setLayoutParams(params);
    }

    private void setData() {
        onEditTextMode = false;
        this.scrollView.setVisibility(View.VISIBLE);
        this.editText.setVisibility(View.GONE);
        this.titleBar.setLeftButton("back", 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.titleBar.setRightButton("", 0, null);

        if (type == RongIMClient.ConversationType.PRIVATE) {
            this.leaveGroup.setVisibility(View.GONE);
            this.nameGroup.setVisibility(View.GONE);
            this.adapter.setPrivateData(targetId);
            this.titleBar.setText(adapter.getName());
        } else {
            this.leaveGroup.setVisibility(View.VISIBLE);
            this.leaveGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MainApplication.rongClient != null) {
                        final ProgressDialog dialog = new ProgressDialog(ChatConfigActivity.this);
                        dialog.show();
                        MainApplication.rongClient.quitDiscussion(targetId, new RongIMClient.OperationCallback() {
                            @Override
                            public void onSuccess() {
                                MainApplication.Handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.cancel();
                                        Toast.makeText(ChatConfigActivity.this, "success quite the discussion", Toast.LENGTH_LONG).show();

                                        // this is also dup code
                                        if (ChatManager.getMyWatchGroup().contains(targetId)) {
                                            ChatManager.getMyWatchGroup().remove(targetId);
                                            StringBuilder PATH = new StringBuilder("v1.1/Message/AddDiscussion");
                                            PATH.append("?session=");
                                            PATH.append(MainApplication.userInfo.getCookie());
                                            PATH.append("&userId=");
                                            PATH.append(MainApplication.userInfo.getUserId());
                                            PATH.append("&discussionId=");
                                            PATH.append(targetId);
                                            PATH.append("&type=1");

                                            ApiDataProvider.getmClient().invokeApi(PATH.toString(), null,
                                                    HttpGet.METHOD_NAME, null, ReturnOnlyInfo.class,
                                                    new ApiOperationCallback<ReturnOnlyInfo>() {
                                                        @Override
                                                        public void onCompleted(ReturnOnlyInfo result, Exception exception, ServiceFilterResponse response) {
                                                        }
                                                    });
                                        }

                                        Intent intent = new Intent(ChatConfigActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                });
                            }

                            @Override
                            public void onError(ErrorCode errorCode) {
                                MainApplication.Handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.cancel();
                                        Toast.makeText(ChatConfigActivity.this, "fail quite the discussion", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
                    }

                }
            });

            this.nameGroup.setVisibility(View.VISIBLE);
            this.nameGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEditTextMode = true;
                    scrollView.setVisibility(View.GONE);
                    editText.setVisibility(View.VISIBLE);
                    String name = adapter.getName();
                    if (!TextUtils.isEmpty(name)) {
                        editText.setText(name);
                        editText.setSelection(0, name.length());
                    }

                    editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, final boolean hasFocus) {
                            editText.post(new Runnable() {
                                @Override
                                public void run() {
                                    InputMethodManager imm = (InputMethodManager) ChatConfigActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    if (hasFocus) {
                                        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                                    } else {
                                        imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                                    }
                                }
                            });
                        }
                    });

                    editText.requestFocus();

                    titleBar.setText("edit group name");
                    titleBar.setLeftButton("返回", 0, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setData();
                        }
                    });

                    titleBar.setRightButton("confirm", 0, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(editText.getText().toString()) && MainApplication.rongClient != null) {
                                final ProgressDialog progressDialog = new ProgressDialog(ChatConfigActivity.this);
                                progressDialog.show();
                                MainApplication.rongClient.setDiscussionName(targetId, editText.getText().toString(), new RongIMClient.OperationCallback() {
                                    @Override
                                    public void onSuccess() {
                                        progressDialog.cancel();
                                        ChatManager.getDiscussion(targetId).setName(editText.getText().toString());
                                        ViewUtils.delayExecute(new Runnable() {
                                            @Override
                                            public void run() {
                                                setData();
                                            }
                                        }, 50);
                                    }

                                    @Override
                                    public void onError(ErrorCode errorCode) {
                                        Toast.makeText(ChatConfigActivity.this, "error: " + errorCode.getMessage(), Toast.LENGTH_LONG).show();
                                        progressDialog.cancel();
                                    }
                                });
                            }

                        }
                    });
                }
            });
            this.adapter.setDiscussionData(targetId);
            this.titleBar.setText("Chat Info(" + adapter.getMemberCount() + ")");
            this.watchGroup.setVisibility(View.VISIBLE);
            this.watchButton.setStatus(ChatManager.getMyWatchGroup().contains(targetId));
            this.watchButton.setOnChangeListener(new SwitchButton.OnChangeListener() {
                @Override
                public void onChange(SwitchButton sb, final boolean state) {
                    final ProgressDialog dialog = new ProgressDialog(ChatConfigActivity.this);
                    dialog.show();

                    //below code is dup code
                    StringBuilder PATH = new StringBuilder("v1.1/Message/AddDiscussion");
                    PATH.append("?session=");
                    PATH.append(MainApplication.userInfo.getCookie());
                    PATH.append("&userId=");
                    PATH.append(MainApplication.userInfo.getUserId());
                    PATH.append("&discussionId=");
                    PATH.append(targetId);
                    PATH.append("&type=");
                    PATH.append(state ? "0" : "1");

                    ApiDataProvider.getmClient().invokeApi(PATH.toString(), null,
                            HttpGet.METHOD_NAME, null, ReturnOnlyInfo.class,
                            new ApiOperationCallback<ReturnOnlyInfo>() {
                                @Override
                                public void onCompleted(ReturnOnlyInfo result, Exception exception, ServiceFilterResponse response) {
                                    dialog.cancel();

                                    if (result != null && result.option != null && result.option.isSuccess()) {
                                        Toast.makeText(ChatConfigActivity.this, "success", Toast.LENGTH_SHORT).show();
                                        Log.d("circle", "success watch group id:" + targetId + "Status:" + state);
                                        if (state && !ChatManager.getMyWatchGroup().contains(targetId))
                                            ChatManager.getMyWatchGroup().add(targetId);
                                        else if (!state && ChatManager.getMyWatchGroup().contains(targetId))
                                            ChatManager.getMyWatchGroup().remove(targetId);
                                    } else {
                                        watchButton.setStatus(!state);
                                    }
                                }
                            });
                }
            });
        }

        this.gridView.setAdapter(adapter);
        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CircleGridIcon icon = (CircleGridIcon) view;
                switch (icon.type) {
                    case 0:
                        if (adapter.isEditMode && position > 0 && MainApplication.rongClient != null) {
                            //remove item
                            final ProgressDialog progressDialog = new ProgressDialog(ChatConfigActivity.this);
                            progressDialog.show();
                            final int pos = position;
                            MainApplication.rongClient.removeMemberFromDiscussion(targetId, adapter.discussion.getMemberIdList().get(pos), new RongIMClient.OperationCallback() {
                                @Override
                                public void onSuccess() {
                                    progressDialog.cancel();
                                    final List<String> ulist = new ArrayList<String>(adapter.discussion.getMemberIdList());
                                    ulist.remove(pos);
                                    ViewUtils.delayExecute(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.discussion.setMemberIdList(ulist);
                                            setGridViewHeight(adapter.getCount());
                                            adapter.notifyDataSetChanged();
                                        }
                                    }, 50);
                                }

                                @Override
                                public void onError(ErrorCode errorCode) {
                                    Toast.makeText(ChatConfigActivity.this, "error: " + errorCode.getMessage(), Toast.LENGTH_LONG).show();
                                    progressDialog.cancel();
                                }
                            });
                        } else {
                            //TODO: this should open person's detail page
                        }

                        break;
                    case 1:
                        //add another people to become discussion or add more people into the discussion
                        Intent intent = new Intent(ChatConfigActivity.this, ChatFriendSelectionActivity.class);
                        ArrayList<String> excludeList = new ArrayList<String>();
                        if (adapter.isPrivate) {
                            excludeList.add(adapter.userInfo.rongCloud);
                            intent.putExtra("fromtype", "privateconfig");
                            intent.putExtra("targetid", targetId);
                        } else {
                            excludeList.addAll(adapter.discussion.getMemberIdList());
                            intent.putExtra("fromtype", "discussionconfig");
                            intent.putExtra("targetid", targetId);
                        }

                        intent.putStringArrayListExtra("excludelist", excludeList);
                        startActivity(intent);
                        break;
                    case 2:
                        //click the minus icon
                        if (ChatManager.isDiscussionMine(adapter.discussion)) {
                            adapter.isEditMode = true;
                        }

                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        });

        this.gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (ChatManager.isDiscussionMine(adapter.discussion)) {
                    adapter.isEditMode = true;
                    adapter.notifyDataSetChanged();
                    return true;
                }

                return false;
            }
        });
    }

    class FriendGridViewAdapter extends BaseAdapter {
        public RongIMClient.Discussion discussion;
        public CUserModel userInfo;
        public boolean isPrivate;
        public boolean isEditMode;

        @Override
        public int getCount() {
            if (isPrivate) {
                return 2;
            } else {
                int extra = ChatManager.isDiscussionMine(discussion) ? 2 : 1;
                if (discussion != null && discussion.getMemberIdList() != null) {
                    return discussion.getMemberIdList().size() + (isEditMode ? 0 : extra);
                }

                return 0;
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
            Log.d("circle", "get view: " + position);
            if (convertView == null)
                convertView = new CircleGridIcon(ChatConfigActivity.this);

            CircleGridIcon icon = (CircleGridIcon) convertView;
            if (isPrivate) {
                //if this is a private page
                if (position == 0) {
                    icon.setData(userInfo.avatar, userInfo.name, userInfo.rongCloud);
                    icon.setEditMode(false);
                } else {
                    icon.setPlusMinus(true);
                    icon.setEditMode(false);
                }
            } else {
                //if this is a group chat page
                if (position < discussion.getMemberIdList().size()) {
                    CUserModel info = ChatManager.getUserInfo(discussion.getMemberIdList().get(position));
                    icon.setData(info.avatar, info.name, info.rongCloud);
                    icon.setEditMode(isEditMode && position > 0);
                } else if (position == discussion.getMemberIdList().size()) {
                    icon.setPlusMinus(true);
                    icon.setEditMode(false);
                } else {
                    icon.setPlusMinus(false);
                    icon.setEditMode(false);
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
            setGridViewHeight(adapter.getCount());
            Log.d("circle", "set Discussion Data with cid:" + discussion.getCreatorId());
        }

        private String getName() {
            if (isPrivate) {
                if (userInfo != null) {
                    return userInfo.name;
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
