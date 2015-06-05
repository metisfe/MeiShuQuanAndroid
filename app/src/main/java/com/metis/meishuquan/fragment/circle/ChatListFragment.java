package com.metis.meishuquan.fragment.circle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.metis.meishuquan.MainActivity;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.circle.ChatActivity;
import com.metis.meishuquan.activity.circle.SearchUserInfoActivity;
import com.metis.meishuquan.activity.info.QrScanActivity;
import com.metis.meishuquan.activity.login.LoginActivity;
import com.metis.meishuquan.model.BLL.CommonOperator;
import com.metis.meishuquan.model.circle.UserSearch;
import com.metis.meishuquan.util.ChatManager;
import com.metis.meishuquan.util.ViewUtils;
import com.metis.meishuquan.view.circle.CircleChatListItemView;
import com.metis.meishuquan.view.circle.PopupAddWindow;
import com.metis.meishuquan.view.common.BadgeView;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by wudi on 4/4/2015.
 */
public class ChatListFragment extends CircleBaseFragment {
    public static final String CLASS_NAME = ChatListFragment.class.getSimpleName();
    public static int REQUEST_QC = 11;

    private ViewGroup rootView;
    private ListView listView;
    private List<RongIMClient.Conversation> clist = null;
    private ChatListAdapter adapter;

    private FragmentManager fm;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ChatManager.SetOnReceivedListener(new ChatManager.OnReceivedListener() {
            @Override
            public void onReceive(RongIMClient.Message message) {
                refreshList();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ChatManager.RemoveOnReceivedListener();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_QC && resultCode == getActivity().RESULT_OK) {
            final String userPhone = (String) data.getExtras().get(QrScanActivity.KEY_RESULT);
            Log.i("QR_RESULT", userPhone);
            CommonOperator.getInstance().searchUser(userPhone, new ApiOperationCallback<UserSearch>() {
                @Override
                public void onCompleted(UserSearch result, Exception exception, ServiceFilterResponse response) {
                    if (result != null && result.option.isSuccess()) {
                        //显示好友信息
                        Intent intent = new Intent(getActivity(), SearchUserInfoActivity.class);
                        intent.putExtra(SearchUserInfoActivity.KEY_USER_INFO, (java.io.Serializable) result.data);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public void timeToSetTitleBar() {
        getTitleBar().setText(MainApplication.userInfo.getName().equals("") ? "消息" : MainApplication.userInfo.getName());
        getTitleBar().setRightButton("", R.drawable.icon_circle_add_, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupAddWindow addWindow = new PopupAddWindow(getActivity(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity) getActivity()).removeAllAttachedView();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //select friend
                        StartFriendPickFragment startFriendPickFragment = new StartFriendPickFragment();
                        Bundle args = new Bundle();
                        args.putString("fromtype", "friendpickfragment");
                        args.putString("title", "消息");
                        startFriendPickFragment.setArguments(args);

                        fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                        ft.add(R.id.content_container, startFriendPickFragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddFriendFragment addFriendFragment = new AddFriendFragment();
                        fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                        ft.add(R.id.content_container, addFriendFragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(MainApplication.UIContext, QrScanActivity.class), REQUEST_QC);
                    }
                });

                ((MainActivity) getActivity()).addAttachView(addWindow);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
        MobclickAgent.onPageStart(CLASS_NAME);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(CLASS_NAME);
    }

    private void refreshList() {
        clist.clear();
        //添加@我的
        RongIMClient.Conversation atMe = new RongIMClient.Conversation();
        atMe.setObjectName("@我的");
        atMe.setConversationType(RongIMClient.ConversationType.SYSTEM);
        //添加评论我的
        RongIMClient.Conversation commentMe = new RongIMClient.Conversation();
        commentMe.setObjectName("评论我的");
        commentMe.setConversationType(RongIMClient.ConversationType.SYSTEM);
        clist.add(atMe);
        clist.add(commentMe);
        if (this.listView != null && adapter != null && MainApplication.rongClient != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    clist.addAll(MainApplication.rongClient.getConversationList());
                }
            }).start();
            if (clist != null) adapter.data = clist;
            ViewUtils.delayExecute(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            }, 50);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_circle_chatlistfragment, container, false);
        listView = (ListView) rootView.findViewById(R.id.fragment_circle_chatlistfragment_listview);
        fm = getActivity().getSupportFragmentManager();
        clist = new ArrayList<RongIMClient.Conversation>();
        adapter = new ChatListAdapter();

        //列表点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //@我的
                if (position == 0) {
                    AtMeFragment atMeFragment = new AtMeFragment();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add(R.id.content_container, atMeFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }

                //评论
                else if (position == 1) {
                    CircleCommentMeFragment commentMeFragment = new CircleCommentMeFragment();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add(R.id.content_container, commentMeFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }

                //聊天
                else {
                    MainApplication.refreshRong();
                    RongIMClient.Conversation conversation = adapter.data.get(position);
//                    RongIM.getInstance().startPrivateChat(getActivity(),conversation.getTargetId(),ChatManager.getConversationTitle(conversation));

//                    RongIMClient.Conversation newConverstion=null;
//                    RongIM.setGetUserInfoProvider(new RongIM.GetUserInfoProvider() {
//                        @Override
//                        public RongIMClient.UserInfo getUserInfo(String s) {
//
//                            return null;
//                        }
//                    },false);

                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("title", ChatManager.getConversationTitle(conversation));
                    intent.putExtra("targetId", conversation.getTargetId());
                    intent.putExtra("type", conversation.getConversationType().toString());
                    getActivity().startActivity(intent);
                }
            }
        });
//        if (MainApplication.rongClient != null) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    clist.addAll(MainApplication.rongClient.getConversationList());
//                    adapter.data = clist;
//                    adapter.notifyDataSetChanged();
//                }
//            }).start();
//        }

        listView.setAdapter(adapter);
        return rootView;
    }

    class ChatListAdapter extends BaseAdapter {
        public List<RongIMClient.Conversation> data = new ArrayList<RongIMClient.Conversation>();

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
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new CircleChatListItemView(getActivity(), data.get(position));
            } else {
                ((CircleChatListItemView) convertView).setData(data.get(position));
            }
            return convertView;
        }
    }
}
